package mx.florinda.cardapio;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class ClienteItensCardapio {

    static void main(String[] args) throws IOException {

        URI uri = URI.create("http://localhost:8000/itens-cardapio");
        try(HttpClient httpClient = HttpClient.newHttpClient()){
            HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                    .header("Accept", "application/x-java-serialized-object")
                    .build();
            HttpResponse<byte[]> response = httpClient.send(httpRequest,
                                            HttpResponse.BodyHandlers.ofByteArray());
            int statusCode = response.statusCode();
            byte[] body = response.body();
            System.out.println(statusCode);
            System.out.println(body);

            var bis = new ByteArrayInputStream(body);
            var ois = new ObjectInputStream(bis);
            List<ItemCardapio> itens = (List<ItemCardapio>) ois.readObject();
            itens.forEach(System.out::println);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

}
