package br.com.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ClienteViaCEP {

    static void main(String[] args) throws IOException {


        System.out.println("-------------Java 1.0----------");
        System.out.println("-------------Convencional----------");
        URL url = new URL("https://viacep.com.br/ws/01001000/json/");
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();
        Scanner scanner = new Scanner(is);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            System.out.println(line);
        }
        scanner.close();
        System.out.println("--------Abrindo stream direto----------");
        Scanner scanner1 = new Scanner(url.openStream());
        while(scanner1.hasNextLine()){
            String line = scanner1.nextLine();
            System.out.println(line);
        }
        scanner.close();
        System.out.println("--------Abrindo stream direto com close automatico----------");
        try(Scanner scanner2 = new Scanner(url.openStream())){
            while(scanner2.hasNextLine()){
                String line = scanner2.nextLine();
                System.out.println(line);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        System.out.println("-------------Java 1.4----------");
        URI uri = URI.create("https://viacep.com.br/ws/01001000/json/");
        //o httpClient é do java 11
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
