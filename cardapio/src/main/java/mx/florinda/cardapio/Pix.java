package mx.florinda.cardapio;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Pix implements Serializable {

    @Serial
    private static final long serialVerisionUID = 2L;

    private Long id;

    private BigDecimal valor;

    private String chaveDestino;

    private Instant dataHora;

    private String message;

    public Pix(Long id, BigDecimal valor, String chaveDestino, Instant dataHora, String message) {
        this.id = id;
        this.valor = valor;
        this.chaveDestino = chaveDestino;
        this.dataHora = dataHora;
        this.message = message;
        System.out.println("Chamou construtor");
    }

    public Pix(){
        System.out.println("Chamou construtor normal");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getChaveDestino() {
        return chaveDestino;
    }

    public void setChaveDestino(String chaveDestino) {
        this.chaveDestino = chaveDestino;
    }

    @Override
    public String toString() {
        return "Pix{" +
                "id=" + id +
                ", valor=" + valor +
                ", chaveDestino='" + chaveDestino + '\'' +
                '}';
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pix pix = (Pix) o;
        return Objects.equals(id, pix.id) && Objects.equals(valor, pix.valor) && Objects.equals(chaveDestino, pix.chaveDestino) && Objects.equals(dataHora, pix.dataHora) && Objects.equals(message, pix.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, chaveDestino, dataHora, message);
    }

}
