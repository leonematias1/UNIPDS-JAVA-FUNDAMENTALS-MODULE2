package mx.florinda.cardapio;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorItensCardapioSocket {

    private static final DataBase database = new SQLDataBase();

    private static final Logger logger = Logger.getLogger(ServidorItensCardapioSocket.class.getName());

    static void main(String[] args) throws Exception{

        try(ExecutorService executorService = Executors.newFixedThreadPool(50)){
            try(ServerSocket serverSocket = new ServerSocket(8000)){
                logger.info("Subiu o Servidor");
                while (true){
                    Socket clientSocket = serverSocket.accept();
                    executorService.execute(() -> trataRequisicao(clientSocket));
                }
            }
        }

    }

    private static void trataRequisicao(Socket clientSocket) {
        try (clientSocket){
            InputStream clientIS = clientSocket.getInputStream();

            StringBuilder requestBuilder = new StringBuilder();
            int data;
            do {
                data = clientIS.read();
                requestBuilder.append((char) data);
            }while (clientIS.available() >0);

            String request = requestBuilder.toString();
            logger.finest(request);

            logger.fine("---------------------------------");
            logger.fine("\n\n\n Chegou um novoRequest");
            Thread.sleep(250);

            String[] requestChunks = request.split("\r\n\r\n");
            String requestLineAndHeader = requestChunks[0];
            String[] requestLineAndHeaderChunks = requestLineAndHeader.split("\r\n");

            String methodAndURI = requestLineAndHeaderChunks[0];

            logger.fine("---------------------------------");
            OutputStream clientOS = clientSocket.getOutputStream();
            PrintStream clientOut = new PrintStream(clientOS);

            if(methodAndURI.contains("GET /itensCardapio.json")){
                logger.fine("Chamou itensCardapio.json");
                Path path = Path.of("itensCardapio.json");
                String json = Files.readString(path);
                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-type: application/json; charset=UTF-8");
                clientOut.println();
                clientOut.println(json);
                //curl http://localhost:8000/itensCardapio.json
            }else if(methodAndURI.contains("GET /itens-cardapio")){
                logger.fine("Chamou itens-cardapio");
                List<ItemCardapio> listItens = database.listaDeItemCardapio();

                String mediaType = "";
                for(int i = 1 ; i < requestLineAndHeaderChunks.length ; i++){
                    String header = requestLineAndHeaderChunks[i];
                    logger.fine(header);
                    if(header.contains("Accept")){
                        mediaType = header.replace("Accept: ", "");
                        logger.info(mediaType);
                    }
                }


                byte[] body;
                if("application/x-java-serialized-object".equals(mediaType)){
                    mediaType = "application/x-java-serialized-object";
                    var bos = new ByteArrayOutputStream();
                    var oos = new ObjectOutputStream(bos);
                    oos.writeObject(listItens);
                    body = bos.toByteArray();
                }else{
                    mediaType = "application/json";
                    Gson gson = new Gson();
                    String json = gson.toJson(listItens);
                    body = json.getBytes(StandardCharsets.UTF_8);
                }

                clientOS.write("HTTP/1.1 200 OK \r\n".getBytes(StandardCharsets.UTF_8));
                clientOS.write(("Content-type: "+mediaType+"; charset=UTF-8 \r\n\r\n").getBytes(StandardCharsets.UTF_8));
                clientOS.write(body);

                //curl http://localhost:8000/itens-cardapio
                //curl.exe -v -H "Accept: application/x-java-serialized-object" http://localhost:8000/itens-cardapio
                //  curl.exe -v -H "Accept: application/x-java-serialized-object" http://localhost:8000/itens-cardapio --output itens.ser
            }else if(methodAndURI.contains("GET /total-itens-cardapio")){
                logger.fine("Chamou itens-cardapio/total");
                List<ItemCardapio> listItens = database.listaDeItemCardapio();
                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-type: application/json; charset=UTF-8");
                clientOut.println();
                clientOut.println(listItens.size());
                //curl http://localhost:8000/total-itens-cardapio
            } else if(methodAndURI.contains("POST /add-itens-cardapio")){
                logger.fine("Chamou /add-itens-cardapio");

                if (requestChunks.length == 1) {
                    logger.warning("HTTP/1.1 400 Bad Request");
                    clientOut.println("HTTP/1.1 400 Bad Request");
                    return;
                }else{
                    String body = requestChunks[1];
                    Gson gson = new Gson();
                    ItemCardapio itemCardapio = gson.fromJson(body, ItemCardapio.class);
                    database.addItemCardapio(itemCardapio);
                }
                clientOut.println("HTTP/1.1 201 CREATED");
                //curl -v -X POST -d '{"id":240,"nome":"ITEM 240","descricao":"ITEM 240.\n","categoria":"BEBIDAS","preco":2.99}' -H 'Content-Type: application/json' http://localhost:8000/add-itens-cardapio

            }else if(methodAndURI.contains("POST /find-itens-cardapio")){
                logger.fine("Chamou find-itens-cardapio");
                if (requestChunks.length == 1) {
                    logger.warning("HTTP/1.1 400 Bad Request");
                    clientOut.println("HTTP/1.1 400 Bad Request");
                    return;
                }else{
                    String body = requestChunks[1];
                    try{
                        JSONObject payLoad = new JSONObject(body);
                        if(!payLoad.has("id")){
                            logger.warning("HTTP/1.1 400 Bad Request");
                            clientOut.println("HTTP/1.1 400 Bad Request");
                            return;
                        }else{
                            Gson gson = new Gson();
                            long id = payLoad.getLong("id");
                            ItemCardapio itemC = database.findById(id);
                            if(itemC == null){
                                clientOut.println("Item de ID: "+id+" Não foi encontrado.");
                                return;
                            }
                            String json = gson.toJson(itemC);
                            clientOut.println(json);
                        }
                    }catch (JSONException ex){
                        clientOut.println("HTTP/1.1 400 Bad Request");
                        return;
                    }
                }
                clientOut.println("HTTP/1.1 200 OK");
                //curl -v -X POST -d '{"id":240}' -H 'Content-Type: application/json' http://localhost:8000/find-itens-cardapio
            }else if(methodAndURI.contains("POST /remove-itens-cardapio")){
                logger.fine("Chamou remove-itens-cardapio");
                if (requestChunks.length == 1) {
                    logger.warning("HTTP/1.1 400 Bad Request");
                    clientOut.println("HTTP/1.1 400 Bad Request");
                    return;
                }else{
                    String body = requestChunks[1];
                    try{
                        JSONObject payLoad = new JSONObject(body);
                        if(!payLoad.has("id")){
                            clientOut.println("HTTP/1.1 400 Bad Request");
                            return;
                        }else{
                            Long id = payLoad.getLong("id");
                            boolean wasRemoved = database.removerItemCardapio(id);
                            if(wasRemoved){
                                clientOut.println("Item de ID: "+id+" Foi removido com sucesso.");
                            }else{
                                clientOut.println("Item de ID: "+id+" Não foi encontrado.");
                            }
                        }
                    }catch (JSONException ex){
                        clientOut.println("HTTP/1.1 400 Bad Request");
                        return;
                    }
                }
                clientOut.println("HTTP/1.1 200 OK");
                //curl -v -X POST -d '{"id":11}' -H 'Content-Type: application/json' http://localhost:8000/remove-itens-cardapio
            }else if(methodAndURI.contains("POST /alter-preco-itens-cardapio")){
                logger.fine("Chamou alter-preco-itens-cardapio");
                if (requestChunks.length == 1) {
                    logger.warning("HTTP/1.1 400 Bad Request");
                    clientOut.println("HTTP/1.1 400 Bad Request");
                    return;
                }else{
                    String body = requestChunks[1];
                    try{
                        JSONObject payLoad = new JSONObject(body);
                        if(!payLoad.has("id") && !payLoad.has("newpreco")){
                            clientOut.println("HTTP/1.1 400 Bad Request");
                            return;
                        }else{
                            long id = payLoad.getLong("id");
                            BigDecimal newpreco = new BigDecimal(payLoad.getString("newpreco"));
                            boolean wasUpdated = database.alterarPrecoItemCardapio(id,newpreco);
                            if(wasUpdated){
                                clientOut.println("O preço do Item de ID: "+id+" Foi alterado com sucesso.");
                            }else{
                                clientOut.println("Item de ID: "+id+" Não foi encontrado.");
                            }
                        }
                    }catch (JSONException ex){
                        logger.warning("HTTP/1.1 400 Bad Request");
                        clientOut.println("HTTP/1.1 400 Bad Request");
                        return;
                    }
                }
                clientOut.println("HTTP/1.1 200 OK");
                //curl -v -X POST -d '{"id":10,"newpreco":"299.99"}' -H 'Content-Type: application/json' http://localhost:8000/alter-preco-itens-cardapio
            }else if(methodAndURI.contains("GET /home") || methodAndURI.contains("GET /home-en")){
                List<ItemCardapio> itens = database.listaDeItemCardapio();
                StringBuilder htmlTodosOsItens = new StringBuilder();
                Locale locale = methodAndURI.contains("-en") ? Locale.US : Locale.of("pt","BR");
                NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(locale);
                ResourceBundle mensagens = ResourceBundle.getBundle("mensagens");

                DateTimeFormatter formatterMesAno = DateTimeFormatter.ofPattern("MMMM/yyyy");

                for (ItemCardapio item : itens) {
                    String precoHtml;
                    if (item.precoComDesconto() != null) {
                            precoHtml = """
                                            <p class="preco">
                                              <span class="preco-antigo">%s</span>
                                              <span class="preco-promo">%s</span>
                                            </p>
                                        """.formatted(
                                                formatadorMoeda.format(item.preco()),
                                                formatadorMoeda.format(item.precoComDesconto()));
                    } else {
                        precoHtml = """
                                        <p class="preco">%s</p>
                                    """.formatted(formatadorMoeda.format(item.preco()));
                    }

                   String itemHtml = """
                                        <div class="item">
                                            <h2>%s</h2>
                                            <p>%s</p>
                                            <h3>%s</h3>
                                            %s
                                        </div>
                                    """.formatted(item.nome(), item.descricao(),
                                    mensagens.getString("categoria.cardapio."+item.categoria().toString().toLowerCase()), precoHtml );

                    htmlTodosOsItens.append(itemHtml);
                }

                String html = """
                                <!DOCTYPE html>
                                <html lang="pt-BR">
                                <head>
                                  <meta charset="UTF-8">
                                  <title>Cardápio</title>
                                  <style>
                                    body {
                                      font-family: Arial, sans-serif;
                                      background: #f5f5f5;
                                      margin: 20px;
                                    }
                                    
                                    h1 {
                                      text-align: center;
                                    }
                                
                                    .cardapio {
                                      display: grid;
                                      grid-template-columns: repeat(5, 1fr);
                                      gap: 15px;
                                    }
                                
                                    .item {
                                      background: white;
                                      padding: 15px;
                                      border-radius: 10px;
                                      box-shadow: 0 0 8px rgba(0,0,0,0.1);
                                      border: 1px solid #ddd; /* borda fina */
                                    }
                                
                                    .item h2 {
                                      margin: 0;
                                      font-size: 18px;
                                    }
                                
                                    .item h3 {
                                      margin: 5px 0;
                                      font-size: 14px;
                                      color: #777;
                                    }
                                
                                    .item p {
                                      margin: 5px 0;
                                      color: #555;
                                    }
                                
                                    .preco {
                                      margin-top: 10px;
                                      font-weight: bold;
                                    }
                                
                                    .preco-antigo {
                                      text-decoration: line-through;
                                      color: #999;
                                      margin-right: 8px;
                                    }
                                
                                    .preco-promo {
                                      background: #fff9c4;
                                      padding: 2px 6px;
                                      border-radius: 4px;
                                      color: #000;
                                    }
                                  </style>
                                </head>
                                <body>
                                
                                  <h1>Cardápio Dona Florinda</h1>
                                
                                  <div class="cardapio">
                                    %s
                                  </div>
                                  <div style="text-align:center">
                                    <h5>%s</h5>
                                  </div>
                                </body>
                                </html>
                            """.formatted(htmlTodosOsItens.toString(), formatterMesAno.format(ZonedDateTime.now()));

                clientOut.print("HTTP/1.1 200 OK\r\n");
                clientOut.print("Content-type: text/html; charset=UTF-8\r\n\r\n");
                clientOut.println(html);
                clientOut.println();

            }else {
                logger.warning("HTTP/1.1 404 Not Found");
                clientOut.println("HTTP/1.1 404 Not Found");
            }
        }catch (Exception e){
            //logger.severe("Erro no Servidor");
            logger.log(Level.SEVERE, "Erro no servidor",e);
            throw new RuntimeException(e);
        }

    }

}
