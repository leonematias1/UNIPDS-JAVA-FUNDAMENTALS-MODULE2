package mx.florinda.cardapio;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
                Gson gson = new Gson();
                String json = gson.toJson(listItens);
                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-type: application/json; charset=UTF-8");
                clientOut.println();
                clientOut.println(json);
                //curl http://localhost:8000/itens-cardapio
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
            }
            else {
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
