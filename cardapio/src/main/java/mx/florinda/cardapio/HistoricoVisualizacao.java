package mx.florinda.cardapio;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class HistoricoVisualizacao {

    private final DataBase database;

    private final Map<ItemCardapio, LocalDateTime> visualizacoes = new WeakHashMap<>();

    public HistoricoVisualizacao(DataBase database){
        this.database = database;
    }

    public void registrarVisualizacao(Long itemId){
        ItemCardapio item = database.findByIdMap(itemId);
        if(item == null){
            System.out.println("Item não encontrado: "+itemId);
            return;
        }
        LocalDateTime agora = LocalDateTime.now();
        visualizacoes.put(item, agora);
        System.out.printf("'%s' visualizado em '%s'\n",item.nome(), agora);
    }

    public void mostrarTotalItensVisualizados(){
        System.out.println("Total de itens visualizados: "+visualizacoes.size() );
    }

    public void listarVisualizacoes(){
        if(visualizacoes.isEmpty()){
            System.out.println("Nenhum item foi visualizado ainda.");
            return;
        }
        mostrarTotalItensVisualizados();
        System.out.println("\nHistórico de visualizações: ");
        visualizacoes.forEach((item, hora) -> System.out.printf("\n- %s em %s", item.nome(), hora));
        System.out.println();
    }
}
