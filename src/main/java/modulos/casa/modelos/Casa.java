package modulos.casa.modelos;

import modulos.peca.modelos.Peca;

//classe casa modelo
public class Casa {

    private Peca peca;
    private int posicao;
    public static final int CASA_VAZIA = 0;

    public Casa() {
        peca = new Peca();
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }
}
