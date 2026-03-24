package mx.florinda.cardapio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ClienteItensCardapio {

    static void main(String[] args) throws IOException {

        URI uri = URI.create("http://localhost:8000/itensCardapio.json");
        try(HttpClient httpClient = HttpClient.newHttpClient()){
            HttpRequest httpRequest = HttpRequest.newBuilder(uri).build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String body = response.body();
            System.out.println(statusCode);
            System.out.println(body);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
