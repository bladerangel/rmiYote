package modulos.tabuleiro.servicos;

import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.interfaces.ServidorInterface;
import modulos.comunicacao.servicos.ComunicacaoServico;
import utilitarios.JanelaAlerta;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//classe servico recebe pacote tabuleiro usado no controlador
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

    //recebe mensagem do chat
    @Override
    public void receberPacoteMensagemChat(int jogador, String mensagem) throws RemoteException {
        tabuleiroServico.adicionarMensagemChat(jogador, mensagem);
    }

    //recebe mensagem de pegar peça fora do tabuleiro
    @Override
    public void recebePacotePegarPeca() throws RemoteException {
        tabuleiroServico.pegarPeca();
    }

    //recebe mensagem de pasasr turno
    @Override
    public void receberPacotePassarTurno() throws RemoteException {
        tabuleiroServico.passarTurno();
        if (tabuleiroServico.getTabuleiro().getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0)
            tabuleiroServico.desabilitarBotaoPegarPeca(false);
    }

    //recebe mensagem de adição de peça no tabuleiro
    @Override
    public void receberPacoteAdicionarPeca(int posicao) throws RemoteException {
        tabuleiroServico.adicionarPeca(posicao);
        tabuleiroServico.setTextNumeroPecasAdversarias();
    }

    //recebe mensagem de movimentação de peça
    @Override
    public void receberPacoteAndarPeca(int posicaoInicial, int posicaoFinal) throws RemoteException {
        tabuleiroServico.andarPecar(posicaoInicial, posicaoFinal);
    }

    //recebe mensagem de captura de peça
    @Override
    public void receberPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) throws RemoteException {
        tabuleiroServico.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
        tabuleiroServico.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroServico.getTabuleiro().getJogador());
        tabuleiroServico.setTextNumeroPecas();
    }

    //recebe mensagem de remoção de outra peça do adversario caso tenha capturado
    @Override
    public void receberPacoteRemoverOutraPeca(int posicao) throws RemoteException {
        tabuleiroServico.getTabuleiro().getJogador().removerPecasDentroTabuleiro();
        tabuleiroServico.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroServico.getTabuleiro().getJogador());
        tabuleiroServico.removerOutraPeca(posicao);
        tabuleiroServico.setTextNumeroPecas();
    }

    //recebe mensagem de vitoria de partida
    @Override
    public void receberPacoteVitoria() throws RemoteException {
        tabuleiroServico.vitoria();
        janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Você perdeu a partida!");
    }

    //recebe mensagem de empate de partida
    @Override
    public void receberEmpatePartida() throws RemoteException {
        tabuleiroServico.empate();
    }

    //recebe mensagem para reinicio de partida
    @Override
    public void receberPacoteReiniciarPartida() throws RemoteException {
        tabuleiroServico.reiniciarPartida();
    }


    //recebe mensagem para desistencia de partida
    @Override
    public void recebePacoteDesistirPartida(int jogador) throws RemoteException {
        chatServico.adicionarMensagemChat("O jogador " + jogador + " desistiu da partida!");
        janelaAlerta.janelaAlertaRunLater("Resultado partida", null, "O jogador " + jogador + " desistiu da partida!");
    }

    //recebe mensagem para saida de partida
    @Override
    public void receberPacoteSairPartida(int jogador) throws RemoteException {
        janelaAlerta.janelaAlertaRunLater("Sair partida", null, "O jogador " + jogador + " saiu da partida!");
        chatServico.adicionarMensagemChat("O jogador " + jogador + " saiu da partida!");
        tabuleiroServico.sairPartida();
    }

}
