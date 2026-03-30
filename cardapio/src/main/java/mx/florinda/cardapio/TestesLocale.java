package mx.florinda.cardapio;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class TestesLocale {

    static void main(String[] args) {
        //Locale.availableLocales().forEach(System.out::println);

        System.out.println("Locale default "+ Locale.getDefault());

        Locale localeUs = Locale.US;
        Locale localePtBr = Locale.of("pt","BR");

        DateTimeFormatter formatterDataHora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        System.out.println(formatterDataHora.format(ZonedDateTime.now()));
        System.out.println(formatterDataHora.withLocale(localeUs).format(ZonedDateTime.now()));
        System.out.println(formatterDataHora.withLocale(localePtBr).format(ZonedDateTime.now()));

        DateTimeFormatter formatterMesAno = DateTimeFormatter.ofPattern("MMMM/yyyy");
        System.out.println(formatterMesAno.withLocale(localeUs).format(ZonedDateTime.now()));
        System.out.println(formatterMesAno.withLocale(localeUs).format(ZonedDateTime.now()));
        System.out.println(formatterMesAno.withLocale(localePtBr).format(ZonedDateTime.now()));

        System.out.println(NumberFormat.getCurrencyInstance().format(2.99));
        System.out.println(NumberFormat.getCurrencyInstance(localeUs).format(2.99));
        System.out.println(NumberFormat.getCurrencyInstance(localePtBr).format(2.99));

        ResourceBundle mensagens = ResourceBundle.getBundle("mensagens");
        ResourceBundle mensagensUS = ResourceBundle.getBundle("mensagens", localeUs);
        ResourceBundle mensagensPtBR = ResourceBundle.getBundle("mensagens", localePtBr);
        System.out.println(mensagens.getString("categoria.cardapio.entradas"));
        System.out.println(mensagensUS.getString("categoria.cardapio.entradas"));
        System.out.println(mensagensPtBR.getString("categoria.cardapio.entradas"));


    }

}
