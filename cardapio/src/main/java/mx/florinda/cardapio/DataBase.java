package mx.florinda.cardapio;

import java.math.BigDecimal;
import java.util.List;

public interface DataBase {

    List<ItemCardapio> listaDeItemCardapio();

    ItemCardapio findById(Long id);

    boolean removerItemCardapio(Long idParaRemover);

    boolean alterarPrecoItemCardapio(long id, BigDecimal novoPreco);

    void addItemCardapio(ItemCardapio itemCardapio);

    int totalItemCardapio();

}
