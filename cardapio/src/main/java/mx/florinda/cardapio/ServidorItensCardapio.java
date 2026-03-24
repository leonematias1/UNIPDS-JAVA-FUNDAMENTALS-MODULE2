package mx.florinda.cardapio;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServidorItensCardapio {

    static void main(String[] args) throws IOException {

        //Não é recomendado usar dependencias fora do .java
        // pois pode ser legado ou até mesmo não term em
        // outras versões do java de outros locais

        // o InetSocketAddress é basicamente um ip e uma porta, ou seja nesse caso o localhsot:8000
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
        //Classes funcionais tem apenas 1 metodo
        httpServer.createContext("/itensCardapio.json",
                exchange -> {
                    Path path  = Path.of("itensCardapio.json");
                    String json = Files.readString(path);
                    byte[] bytes = json.getBytes();
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.add("Content-type","application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, bytes.length);
                    try (OutputStream responseBody = exchange.getResponseBody()) {
                        responseBody.write(bytes);
                    }
                });
        System.out.println("Subiu o Servidor HTTP");
        httpServer.start();


    }

}
