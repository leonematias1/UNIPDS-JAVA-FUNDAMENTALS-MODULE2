package mx.florinda.cardapio;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorItensCardapioSocket {

    private static final DataBase database = new DataBase();

    static void main(String[] args) throws Exception{

        try(ExecutorService executorService = Executors.newFixedThreadPool(50)){
            try(ServerSocket serverSocket = new ServerSocket(8000)){
                System.out.println("Subiu o Servidor");
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
            System.out.println(request);

            System.out.println("---------------------------------");
            System.out.println("\n\n\n Chegou um novoRequest");
            Thread.sleep(250);

            String[] requestChunks = request.split("\r\n\r\n");
            String requestLineAndHeader = requestChunks[0];
            String[] requestLineAndHeaderChunks = requestLineAndHeader.split("\r\n");

            String methodAndURI = requestLineAndHeaderChunks[0];

            System.out.println("---------------------------------");
            OutputStream clientOS = clientSocket.getOutputStream();
            PrintStream clientOut = new PrintStream(clientOS);

            if(methodAndURI.contains("GET /itensCardapio.json")){
                System.out.println("Chamou itensCardapio.json");
                Path path = Path.of("itensCardapio.json");
                String json = Files.readString(path);
                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-type: application/json; charset=UTF-8");
                clientOut.println();
                clientOut.println(json);
            }else if(methodAndURI.contains("GET /itens-cardapio")){
                System.out.println("Chamou itens-cardapio");
                List<ItemCardapio> listItens = database.listaDeItemCardapio();
                Gson gson = new Gson();
                String json = gson.toJson(listItens);
                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-type: application/json; charset=UTF-8");
                clientOut.println();
                clientOut.println(json);
            }else if(methodAndURI.contains("GET /total-itens-cardapio")){
                System.out.println("Chamou itens-cardapio/total");
                List<ItemCardapio> listItens = database.listaDeItemCardapio();
                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-type: application/json; charset=UTF-8");
                clientOut.println();
                clientOut.println(listItens.size());
            } else if(methodAndURI.contains("POST /add-itens-cardapio")){
                System.out.println("Chamou itens-cardapio/total");

                if (requestChunks.length == 1) {
                    clientOut.println("HTTP/1.1 400 Bad Request");
                    return;
                }else{
                    String body = requestChunks[1];
                    Gson gson = new Gson();
                    ItemCardapio itemCardapio = gson.fromJson(body, ItemCardapio.class);
                    database.addItemCardapio(itemCardapio);
                }

                clientOut.println("HTTP/1.1 201 CREATED");
            } else {
                System.out.println("URI Não encontrada "+methodAndURI);
                clientOut.println("HTTP/1.1 404 Not Found");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        //curl -v -X POST -d '{"id":240,"nome":"ITEM 240","descricao":"ITEM 240.\n","categoria":"BEBIDAS","preco":2.99}' -H 'Content-Type: application/json' http://localhost:8000/add-itens-cardapio

    }

}
