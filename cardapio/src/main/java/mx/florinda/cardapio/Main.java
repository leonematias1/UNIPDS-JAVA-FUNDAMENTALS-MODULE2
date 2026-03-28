package mx.florinda.cardapio;

import java.math.BigDecimal;
import java.util.*;

public class Main {

    static void main(String[] args) throws InterruptedException {


        SQLDataBase sqlDataBase = new SQLDataBase();
        System.out.println(" -------- Listagem de  itens -------- ");
        List<ItemCardapio> listItens = sqlDataBase.listaDeItemCardapio();
        listItens.forEach(System.out::println);

        System.out.println(" -------- Total de  itens -------- ");
        int total = sqlDataBase.totalItemCardapio();
        System.out.println("Existem o total de: "+total+ " de itens cadastrados");

//        System.out.println(" -------- inserção de  itens -------- ");
        ItemCardapio itemInsert = new ItemCardapio(
                null, "Leite de burra",
                "O chaves acreditava ser leite mas no final era só gesso branco",
                ItemCardapio.CategoriaCardapio.BEBIDAS,
                new BigDecimal("10.99"), new BigDecimal("5.99"));
//        sqlDataBase.addItemCardapio(itemInsert);
//        sqlDataBase.listaDeItemCardapio().forEach(System.out::println);

        System.out.println(" -------- Listagem de item unico -------- ");
        System.out.println(sqlDataBase.findById(2L));


//        InMemoryDataBase database = new InMemoryDataBase();
//        List<ItemCardapio> itens = database.listaDeItemCardapio();
//        database.insertDataAtMap();
        //System.out.println("TreeSet-----------------------------");
//        Comparator<ItemCardapio.CategoriaCardapio> comparadorPorNome = Comparator.comparing(ItemCardapio.CategoriaCardapio::name);
//        Set<ItemCardapio.CategoriaCardapio> categorias = new TreeSet<>(comparadorPorNome);
//        for(ItemCardapio item : itens){
//            categorias.add(item.categoria());
//        }
        //System.out.println(categorias);
        //System.out.println("LinkedHash----------------------------");
//        itens.stream()
//                .map(ItemCardapio::categoria)
//                .collect(Collectors.toCollection(LinkedHashSet::new))
//                .forEach(System.out::println);
        //System.out.println("HashSet----------------------------");
//        itens.stream()
//                .map(ItemCardapio::categoria)
//                .collect(Collectors.toCollection(HashSet::new))
//                .forEach(System.out::println);
        //System.out.println("----------------------------");
        //Quantos itens por categoria
        //System.out.println("-Quantos itens por categoria-");
//        HashMap<ItemCardapio.CategoriaCardapio, Integer> itensPorCategoria = new LinkedHashMap<>();
//        for (ItemCardapio item : itens){
//            ItemCardapio.CategoriaCardapio categoria = item.categoria();
//           if(!itensPorCategoria.containsKey(categoria) ){
//               itensPorCategoria.put(categoria, 1);
//           }else{
//               Integer quantidadeAnterior = itensPorCategoria.get(categoria);
//               itensPorCategoria.put(categoria, quantidadeAnterior+1);
//           }
//        }
        //System.out.println(itensPorCategoria);
        //System.out.println("--------------------------");

//        itens.stream()
//                .collect(Collectors.groupingBy(
//                        ItemCardapio::categoria,
//                                TreeMap::new,
//                                Collectors.counting()
//                ))
//                .forEach((chave, valor ) -> System.out.println(chave + " => "+valor));
//        System.out.println("--------find by id---------");
//        ItemCardapio item = database.findById(6L);
//        System.out.println(item);
//        System.out.println("--------------------------");
//        HistoricoVisualizacao historicoVisualizacao = new HistoricoVisualizacao(database);
//        historicoVisualizacao.registrarVisualizacao(1L);
//        historicoVisualizacao.registrarVisualizacao(2L);
//        historicoVisualizacao.registrarVisualizacao(3L);
//        historicoVisualizacao.registrarVisualizacao(11L);
//
//        historicoVisualizacao.listarVisualizacoes();
//        System.out.println("------Remoção de item --------");
//        Long idParaRemover = 1L;
//        boolean removal = database.removerItemCardapio(idParaRemover);
//        if(removal){
//            System.out.println("Item Removido: "+idParaRemover);
//        }else{
//            System.out.println("Item não encontrado: "+idParaRemover);
//        }
//        database.listItemCardapioMap();
//        System.out.println("Chamando garbage collector");
//        System.gc();
//        Thread.sleep(500);
//        historicoVisualizacao.mostrarTotalItensVisualizados();
//        System.out.println("--------Mudança de preço-----------");
//        ItemCardapio itemCardapioAtual = database.findByIdMap(1L);
//        System.out.printf("\n Item atual %s com o preço %s",itemCardapioAtual.nome(), itemCardapioAtual.preco());
//        database.alterarPrecoItemCardapio(1L, new BigDecimal("3.99"));
//        ItemCardapio itemCardapioAlterado = database.findByIdMap(1L);
//        database.alterarPrecoItemCardapio(1L, new BigDecimal("2.99"));
//        database.alterarPrecoItemCardapio(1L, new BigDecimal("4.99"));
//        System.out.printf("\n Item atual %s com o preço %s",itemCardapioAlterado.nome(), itemCardapioAlterado.preco());
//        System.out.println("--------------------------");
//        database.rastroAuditoriaPrecos();

    }

}
