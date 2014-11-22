package ufba.mypersonaltrainner;

/**
 * Created by junior on 29/10/14.
 */
public class ItemListMeuTreino {
    private String nome;
    private int repeticoes;
    private int carga;

    public ItemListMeuTreino(String nome, int repeticoes, int carga) {
        this.nome = nome;
        this.repeticoes = repeticoes;
        this.carga = carga;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public String getNome() { return nome; }
}
