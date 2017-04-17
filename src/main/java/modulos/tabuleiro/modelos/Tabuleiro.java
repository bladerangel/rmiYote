package modulos.tabuleiro.modelos;

import modulos.casa.modelos.Casa;
import modulos.jogador.modelos.Jogador;

//classe tabuleiro modelo
public class Tabuleiro {

    private Jogador jogador;
    private Jogador jogadorAdversario;
    private Jogador turnoJogador;
    public static final int QUANTIDADE_LINHAS = 5;
    public static final int QUANTIDADE_COLUNAS = 6;
    public static final int POSICAO_INICIAL_VAZIA = -1;
    private int posicaoInicial;

    //inicia o tabuleiro de acordo com o tipo de jogador conectado
    public Tabuleiro(boolean servidor) {
        if (servidor) {
            jogador = new Jogador(Jogador.TIPO_JOGADOR_SERVIDOR, Jogador.QUANTIDADE_PECAS, Casa.CASA_VAZIA);
            jogadorAdversario = new Jogador(Jogador.TIPO_JOGADOR_CLIENTE, Jogador.QUANTIDADE_PECAS, Casa.CASA_VAZIA);
            turnoJogador = jogador;
        } else {
            jogador = new Jogador(Jogador.TIPO_JOGADOR_CLIENTE, Jogador.QUANTIDADE_PECAS, Casa.CASA_VAZIA);
            jogadorAdversario = new Jogador(Jogador.TIPO_JOGADOR_SERVIDOR, Jogador.QUANTIDADE_PECAS, Casa.CASA_VAZIA);
            turnoJogador = jogadorAdversario;
        }
        posicaoInicial = POSICAO_INICIAL_VAZIA;
    }

    //muda o turno da partida
    public void mudarTurnoJogador() {
        if (turnoJogador == jogador)
            turnoJogador = jogadorAdversario;
        else
            turnoJogador = jogador;
        turnoJogador.setRemoverOutraPeca(false);
        posicaoInicial = POSICAO_INICIAL_VAZIA;
    }

    public Jogador getTurnoJogador() {
        return turnoJogador;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Jogador getJogadorAdversario() {
        return jogadorAdversario;
    }

    public void setPosicaoInicial(int posicaoInicial) {
        this.posicaoInicial = posicaoInicial;
    }

    public int getPosicaoInicial() {
        return posicaoInicial;
    }

}
