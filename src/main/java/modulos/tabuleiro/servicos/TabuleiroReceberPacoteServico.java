package modulos.tabuleiro.servicos;

import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.interfaces.ServidorInterface;
import modulos.comunicacao.servicos.ComunicacaoServico;
import utilitarios.JanelaAlerta;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//classe servico recebe pacote tabuleiro usado no controlador e implemanta a interface servidor
public class TabuleiroReceberPacoteServico extends UnicastRemoteObject implements ServidorInterface {


    private JanelaAlerta janelaAlerta;
    private TabuleiroServico tabuleiroServico;
    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;

    public TabuleiroReceberPacoteServico(JanelaAlerta janelaAlerta, TabuleiroServico tabuleiroServico, ComunicacaoServico comunicacaoServico, ChatServico chatServico) throws RemoteException {
        super();
        this.janelaAlerta = janelaAlerta;
        this.tabuleiroServico = tabuleiroServico;
        this.comunicacaoServico = comunicacaoServico;
        this.chatServico = chatServico;
    }

    //registra o jogador no servidor de nomes
    public void iniciarJogador() throws IOException, NotBoundException {
        comunicacaoServico.getComunicacao().registrarServidor(this, tabuleiroServico.getTabuleiro().getJogador());
    }

    //recebe mensagem quando o segundo jogador se conectar
    @Override
    public void receberPacoteIniciarPartida() throws RemoteException, NotBoundException, MalformedURLException {
        janelaAlerta.janelaAlertaRunLater("Iniciar Partida", null, "O jogador 2 conectou-se");
        tabuleiroServico.iniciarPartida();

    }

    //recebe mensagem do chat
    @Override
    public void receberPacoteMensagemChat(int jogador, String mensagem) {
        tabuleiroServico.adicionarMensagemChat(jogador, mensagem);
    }

    //recebe mensagem de pegar peça fora do tabuleiro
    @Override
    public void recebePacotePegarPeca() {
        tabuleiroServico.pegarPeca();
    }

    //recebe mensagem de pasasr turno
    @Override
    public void receberPacotePassarTurno() {
        tabuleiroServico.passarTurno();
        if (tabuleiroServico.getTabuleiro().getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0)
            tabuleiroServico.desabilitarBotaoPegarPeca(false);
    }

    //recebe mensagem de adição de peça no tabuleiro
    @Override
    public void receberPacoteAdicionarPeca(int posicao) {
        tabuleiroServico.adicionarPeca(posicao);
        tabuleiroServico.setTextNumeroPecasAdversarias();
    }

    //recebe mensagem de movimentação de peça
    @Override
    public void receberPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        tabuleiroServico.andarPecar(posicaoInicial, posicaoFinal);
    }

    //recebe mensagem de captura de peça
    @Override
    public void receberPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        tabuleiroServico.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
        tabuleiroServico.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroServico.getTabuleiro().getJogador());
        tabuleiroServico.setTextNumeroPecas();
    }

    //recebe mensagem de remoção de outra peça do adversario caso tenha capturado
    @Override
    public void receberPacoteRemoverOutraPeca(int posicao) {
        tabuleiroServico.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroServico.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroServico.getTabuleiro().getJogador());
        tabuleiroServico.removerOutraPeca(posicao);
        tabuleiroServico.setTextNumeroPecas();
    }

    //recebe mensagem de vitoria de partida
    @Override
    public void receberPacoteVitoria() {
        tabuleiroServico.vitoria();
        janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Você perdeu a partida!");
    }

    //recebe mensagem de empate de partida
    @Override
    public void receberEmpatePartida() {
        tabuleiroServico.empate();
    }

    //recebe mensagem para reinicio de partida
    @Override
    public void receberPacoteReiniciarPartida() {
        tabuleiroServico.reiniciarPartida();
    }


    //recebe mensagem para desistencia de partida
    @Override
    public void recebePacoteDesistirPartida(int jogador) {
        chatServico.adicionarMensagemChat("O jogador " + jogador + " desistiu da partida!");
        janelaAlerta.janelaAlertaRunLater("Resultado partida", null, "O jogador " + jogador + " desistiu da partida!");
    }

    //recebe mensagem para saida de partida
    @Override
    public void receberPacoteSairPartida(int jogador) {
        janelaAlerta.janelaAlertaRunLater("Sair partida", null, "O jogador " + jogador + " saiu da partida!");
        chatServico.adicionarMensagemChat("O jogador " + jogador + " saiu da partida!");
    }
}
