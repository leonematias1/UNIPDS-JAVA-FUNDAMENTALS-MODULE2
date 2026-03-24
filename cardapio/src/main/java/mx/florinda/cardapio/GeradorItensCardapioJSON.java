package mx.florinda.cardapio;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GeradorItensCardapioJSON {

    static void main(String[] args) throws IOException {
        DataBase dataBase = new DataBase();
        List<ItemCardapio> listaIntensCardapio = dataBase.listaDeItemCardapio();

        Gson gson = new Gson();
        String json = gson.toJson(listaIntensCardapio);

        Path path = Path.of("itensCardapio.json");
        Files.writeString(path, json);

        //jwebserver abre um servidor de arquivos nativo
        //jwebserver  é do java 18
    }

}
