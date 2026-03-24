package mx.florinda.cardapio;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static mx.florinda.cardapio.ItemCardapio.CategoriaCardapio.*;

public class DataBase {

    private final Map<Long, ItemCardapio> itensPorId = new HashMap<>();

    private final Map<ItemCardapio, BigDecimal> auditoria = new IdentityHashMap<>();

    public void insertDataAtMap(){

        ItemCardapio refrescoDoChaves = new ItemCardapio(1L, "Refresco do Chaves", """
                Suco de limão, que parece tamarindo mas tem gosto de groselha.
                """, BEBIDAS, new BigDecimal("2.99"), null);
        itensPorId.put(1L,refrescoDoChaves);

        ItemCardapio sanduicheDoChaves = new ItemCardapio(2L, "Sanduíche de presunto do Chaves", """
                Sanduíche de presunto simples, mas feito com muito amor.
                """, PRATOS_PRINCIPAIS, new BigDecimal("3.50"), new BigDecimal("2.99"));
         itensPorId.put(2L,sanduicheDoChaves);

        ItemCardapio tortaDaDonaFlorinda = new ItemCardapio(5L, "Torta da Dona Florinda", """
                Torta de frango com recheio cremoso e massa crocante.
                """, PRATOS_PRINCIPAIS, new BigDecimal("12.99"), new BigDecimal("10.00"));
         itensPorId.put(5L,tortaDaDonaFlorinda);

        ItemCardapio pipocaDoQuico = new ItemCardapio(6L, "Pipoca do Quico", """
                Balde de pipoca preparado com carinho pelo Quico.
                """, PRATOS_PRINCIPAIS, new BigDecimal("4.99"), new BigDecimal("3.99"));
         itensPorId.put(6L,pipocaDoQuico);

        ItemCardapio aguaDeJamaica = new ItemCardapio(7L, "Água de jamaica", """
                Água aromatizada com hibisco e toque de açúcar.
                """, BEBIDAS, new BigDecimal("2.50"), new BigDecimal("2.00"));
         itensPorId.put(7L,aguaDeJamaica);

        ItemCardapio cafeDaDonaFlorinda = new ItemCardapio(8L, "Café da Dona Florinda", """
                Café forte para começar o dia com energia.
                """, BEBIDAS, new BigDecimal("1.99"), new BigDecimal("1.50"));
         itensPorId.put(8L,cafeDaDonaFlorinda);

        ItemCardapio churrosDoChaves = new ItemCardapio(9L, "Churros do Chaves", """
                Churros recheados com doce de leite, clássico e irresistível.
                """, SOBREMESA, new BigDecimal("4.99"), new BigDecimal("3.99"));
         itensPorId.put(9L,churrosDoChaves);

        ItemCardapio GelatinaDoNhonho = new ItemCardapio(10L, "Gelatina do Nhonho", """
                Gelatina de várias cores, a sobremesa favorita do Nhonho.
                """, SOBREMESA, new BigDecimal("3.50"), new BigDecimal("2.99"));
         itensPorId.put(10L,GelatinaDoNhonho);

        ItemCardapio boloDaDonaClotilde = new ItemCardapio(11L, "Bolo de Chocolate da Dona Clotilde", """
                Bolo de Chocolate com cobertura de brigadeiro.
                """, SOBREMESA, new BigDecimal("5.99"), new BigDecimal("4.99"));
        itensPorId.put(11L,boloDaDonaClotilde);

    }

    public List<ItemCardapio> listaDeItemCardapio(){

        List<ItemCardapio> items = new ArrayList<>();
        ItemCardapio refrescoDoChaves = new ItemCardapio(1L, "Refresco do Chaves", """
                Suco de limão, que parece tamarindo mas tem gosto de groselha.
                """, BEBIDAS, new BigDecimal("2.99"), null);
        items.add(refrescoDoChaves);

        ItemCardapio sanduicheDoChaves = new ItemCardapio(2L, "Sanduíche de presunto do Chaves", """
                Sanduíche de presunto simples, mas feito com muito amor.
                """, PRATOS_PRINCIPAIS, new BigDecimal("3.50"), new BigDecimal("2.99"));
        items.add(sanduicheDoChaves);

        ItemCardapio tortaDaDonaFlorinda = new ItemCardapio(5L, "Torta da Dona Florinda", """
                Torta de frango com recheio cremoso e massa crocante.
                """, PRATOS_PRINCIPAIS, new BigDecimal("12.99"), new BigDecimal("10.00"));
        items.add(tortaDaDonaFlorinda);

        ItemCardapio pipocaDoQuico = new ItemCardapio(6L, "Pipoca do Quico", """
                Balde de pipoca preparado com carinho pelo Quico.
                """, PRATOS_PRINCIPAIS, new BigDecimal("4.99"), new BigDecimal("3.99"));
        items.add(pipocaDoQuico);

        ItemCardapio aguaDeJamaica = new ItemCardapio(7L, "Água de jamaica", """
                Água aromatizada com hibisco e toque de açúcar.
                """, BEBIDAS, new BigDecimal("2.50"), new BigDecimal("2.00"));
        items.add(aguaDeJamaica);

        ItemCardapio cafeDaDonaFlorinda = new ItemCardapio(8L, "Café da Dona Florinda", """
                Café forte para começar o dia com energia.
                """, BEBIDAS, new BigDecimal("1.99"), new BigDecimal("1.50"));
        items.add(cafeDaDonaFlorinda);

        ItemCardapio churrosDoChaves = new ItemCardapio(9L, "Churros do Chaves", """
                Churros recheados com doce de leite, clássico e irresistível.
                """, SOBREMESA, new BigDecimal("4.99"), new BigDecimal("3.99"));
        items.add(churrosDoChaves);

        ItemCardapio GelatinaDoNhonho = new ItemCardapio(10L, "Gelatina do Nhonho", """
                Gelatina de várias cores, a sobremesa favorita do Nhonho.
                """, SOBREMESA, new BigDecimal("3.50"), new BigDecimal("2.99"));
        items.add(GelatinaDoNhonho);

        ItemCardapio boloDaDonaClotilde = new ItemCardapio(11L, "Bolo de Chocolate da Dona Clotilde", """
                Bolo de Chocolate com cobertura de brigadeiro.
                """, SOBREMESA, new BigDecimal("5.99"), new BigDecimal("4.99"));
        items.add(boloDaDonaClotilde);

        return items;
    }

    public ItemCardapio findById(Long id){
        return listaDeItemCardapio().stream()
                .filter(
                        itemCardapio -> itemCardapio.id().equals(id)).findFirst().orElse(null);
    }

    public ItemCardapio findByIdMap(Long id){
        return itensPorId.get(id);
    }

    public void listItemCardapioMap(){
        itensPorId.forEach((aLong, itemCardapio) -> System.out.println(itemCardapio));
    }
    public boolean removerItemCardapio(Long idParaRemover) {
        ItemCardapio itemCardapioRemovido = itensPorId.remove(idParaRemover);
        return itemCardapioRemovido != null;
    }

    public boolean alterarPrecoItemCardapio(long id, BigDecimal novoPreco) {
        ItemCardapio itemAntigo = findByIdMap(id);
        if(itemAntigo == null){
            return false;
        }
        ItemCardapio itemAlterado = itemAntigo.alterarPreco(novoPreco);
        itensPorId.put(id,itemAlterado);
        auditoria.put(itemAntigo, novoPreco);
        return true;
    }

    public void rastroAuditoriaPrecos(){
        System.out.println("\n Auditoria de preços:");
        auditoria.forEach(
                (itemAntigo, novoPreco) ->
                        System.out.printf("\n - %s: %s => %s\n", itemAntigo.nome(), itemAntigo.preco(), novoPreco));
        System.out.println();
    }

}
