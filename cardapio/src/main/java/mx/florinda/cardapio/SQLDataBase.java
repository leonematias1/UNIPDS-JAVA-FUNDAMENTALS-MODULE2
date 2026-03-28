package mx.florinda.cardapio;



import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLDataBase implements DataBase {

    private static Connection con;

    SQLDataBase(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cardapio", "root", "senha123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ItemCardapio> listaDeItemCardapio() {
        ArrayList<ItemCardapio> listItens = new ArrayList<>();
        String query = "SELECT id, nome, descricao, categoria, preco, preco_promocional from item_cardapio";
        try(
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){
                listItens.add(new ItemCardapio(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    ItemCardapio.CategoriaCardapio.valueOf(rs.getString("categoria")),
                    rs.getBigDecimal("preco"),
                    rs.getBigDecimal("preco_promocional")
                ));
            }
            return listItens;
        }catch (SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public int totalItemCardapio() {
        int total = 0;
        String query = "SELECT count(*) as total from item_cardapio";
        try(
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
        ){
            if(rs.next()){
                total = rs.getInt("total");
            }
            return total;
        }catch (SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void addItemCardapio(ItemCardapio itemCardapio) {
        ArrayList<ItemCardapio> listItens = new ArrayList<>();
        String query =
                " INSERT INTO item_cardapio (nome, descricao, categoria, preco, preco_promocional)" +
                " VALUES (?, ?, ?, ?, ? )";
        try(
            PreparedStatement ps = con.prepareStatement(query);
        ){
            ps.setString(1,itemCardapio.nome());
            ps.setString(2,itemCardapio.descricao());
            ps.setString(3,itemCardapio.categoria().toString());
            ps.setBigDecimal(4,itemCardapio.preco());
            ps.setBigDecimal(5,itemCardapio.precoComDesconto());
            ps.execute();
        }catch (SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public ItemCardapio findById(Long id) {
        String query = "SELECT id, nome, descricao, categoria, preco, preco_promocional from item_cardapio where id = ? ";
        try(
            PreparedStatement ps = con.prepareStatement(query);
        ){
            ps.setLong(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new ItemCardapio(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            ItemCardapio.CategoriaCardapio.valueOf(rs.getString("categoria")),
                            rs.getBigDecimal("preco"),
                            rs.getBigDecimal("preco_promocional")
                    );
                }
            }finally {
                ps.close();
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean removerItemCardapio(Long idParaRemover) {
        String query = "DELETE from item_cardapio where id = ? ";
        try(
            PreparedStatement ps = con.prepareStatement(query);
        ){
            ps.setLong(1,idParaRemover);
            if(ps.executeUpdate() >= 1)
                return true;
        }catch (SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean alterarPrecoItemCardapio(long id, BigDecimal novoPreco) {
        String query = "UPDATE item_cardapio set preco = ? where id = ? ";
        try(
            PreparedStatement ps = con.prepareStatement(query);
        ){
            ps.setBigDecimal(1,novoPreco);
            ps.setLong(2,id);
            if(ps.executeUpdate() >= 1)
                return true;
        }catch (SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }
        return false;
    }


}
