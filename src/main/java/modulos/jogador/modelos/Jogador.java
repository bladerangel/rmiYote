package modulos.jogador.modelos;

//classe casa modelo
public class Jogador {

    private int tipo;
    private int quantidadePecasForaTabuleiro;
    private int quantidadePecasDentroTabuleiro;
    private boolean removerOutraPeca; //utilizado quando o jogador captura uma peça
    public static final int TIPO_JOGADOR_SERVIDOR = 1;
    public static final int TIPO_JOGADOR_CLIENTE = 2;
    public static final int QUANTIDADE_PECAS = 12;

    public Jogador(int tipo, int quantidadePecasForaTabuleiro, int quantidadePecasDentroTabuleiro) {
        this.tipo = tipo;
        this.quantidadePecasForaTabuleiro = quantidadePecasForaTabuleiro;
        this.quantidadePecasDentroTabuleiro = quantidadePecasDentroTabuleiro;
        removerOutraPeca = false;
    }

    public int getTipo() {
        return tipo;
    }

    //jogador pega peça de fora e coloca no tabuleiro
    public void removerPecasForaTabuleiro() {
        if (quantidadePecasForaTabuleiro > 0) {
            quantidadePecasForaTabuleiro--;
            quantidadePecasDentroTabuleiro++;
        }
    }

    //jogador captura peças do adversario
    public void removerPecasDentroTabuleiro() {
        quantidadePecasDentroTabuleiro--;
    }

    //calcula o total de peças do jogador no jogo
    public int totalPecas() {
        return quantidadePecasForaTabuleiro + quantidadePecasDentroTabuleiro;
    }

    public int getQuantidadePecasForaTabuleiro() {
        return quantidadePecasForaTabuleiro;
    }

    public void setRemoverOutraPeca(boolean removerOutraPeca) {
        this.removerOutraPeca = removerOutraPeca;
    }

    //verifica se o jogador pode retirar outra peça do adversario ao realizar uma captura
    public boolean isRemoverOutraPeca() {
        return removerOutraPeca;
    }

}
