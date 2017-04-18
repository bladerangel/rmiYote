package modulos.tabuleiro.servicos;

import modulos.casa.componentes.CasaBotao;
import modulos.casa.modelos.Casa;
import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.interfaces.ServidorInterface;
import modulos.comunicacao.servicos.ComunicacaoServico;
import modulos.tabuleiro.modelos.Tabuleiro;
import utilitarios.JanelaAlerta;

import java.rmi.Naming;
import java.rmi.RemoteException;

//classe servico envia pacote tabuleiro usado no controlador
public class TabuleiroEnviarPacoteServico {

    private JanelaAlerta janelaAlerta;
    private TabuleiroServico tabuleiroServico;
    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;
    private ServidorInterface servidorInterface;

    public TabuleiroEnviarPacoteServico(JanelaAlerta janelaAlerta, TabuleiroServico tabuleiroServico, ComunicacaoServico comunicacaoServico, ChatServico chatServico) throws RemoteException {
        super();
        this.janelaAlerta = janelaAlerta;
        this.tabuleiroServico = tabuleiroServico;
        this.comunicacaoServico = comunicacaoServico;
        this.chatServico = chatServico;
        tabuleiroServico.getCasasTabuleiro().forEach(casa -> casa.setOnMouseClicked(event -> {
            try {
                movimentarPeca((CasaBotao) event.getSource());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }));
    }

    public void procurarServidorNomes() {
        try {
            servidorInterface = (ServidorInterface) Naming.lookup("//localhost/cliente" + tabuleiroServico.getTabuleiro().getJogadorAdversario().getTipo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //envia mensagem no chat
   
    public void enviarPacoteMensagemChat() throws RemoteException {
        if (!chatServico.getEscreverMensagem().getText().equals("")) {
            procurarServidorNomes();
            servidorInterface.receberPacoteMensagemChat(tabuleiroServico.getTabuleiro().getJogador().getTipo(), chatServico.getEscreverMensagem().getText());
            tabuleiroServico.adicionarMensagemChat(tabuleiroServico.getTabuleiro().getJogador().getTipo(), chatServico.getEscreverMensagem().getText());
            chatServico.limparMensagem();
        }
    }

    //envia mensagem de pegar peça fora do tabuleiro
   
    public void enviarPacotePegarPeca() throws RemoteException {
        procurarServidorNomes();
        tabuleiroServico.pegarPeca();
        tabuleiroServico.desabilitarBotaoPegarPeca(true);
        servidorInterface.recebePacotePegarPeca();
    }

    //envia mensagem de passar turno
   
    public void enviarPacotePassarTurno() throws RemoteException {
        procurarServidorNomes();
        tabuleiroServico.passarTurno();
        servidorInterface.receberPacotePassarTurno();
        tabuleiroServico.desabilitarBotaoPegarPeca(true);
        tabuleiroServico.desabilitarBotaoPassarTurno(true);
    }

    //envia mensagem do jogador que adicionou uma peça no tabuleiro
   
    public void enviarPacoteAdicionarPeca(int posicao) throws RemoteException {
        procurarServidorNomes();
        tabuleiroServico.adicionarPeca(posicao);
        tabuleiroServico.setTextNumeroPecas();
        servidorInterface.receberPacoteAdicionarPeca(posicao);
        enviarPacotePassarTurno();
    }

    //envia mensagem do jogador que andou uma posicao no tabuleiro
   
    public void enviarPacoteAndarPeca(int posicaoInicial, int posicaoFinal) throws RemoteException {
        procurarServidorNomes();
        tabuleiroServico.andarPecar(posicaoInicial, posicaoFinal);
        servidorInterface.receberPacoteAndarPeca(posicaoInicial, posicaoFinal);
        enviarPacotePassarTurno();
    }

    //jogador movimenta uma peça no tabuleiro
    public void movimentarPeca(CasaBotao casa) throws RemoteException {
        if (tabuleiroServico.getTabuleiro().getTurnoJogador() == tabuleiroServico.getTabuleiro().getJogador()) {
            verificarMovimento(tabuleiroServico.getTabuleiro().getPosicaoInicial(), casa.getCasa().getPosicao());
        }
    }

    //verifica qual tipo de movimento é realizado pelo jogador
    public void verificarMovimento(int posicaoInicial, int posicaoFinal) throws RemoteException {
        if (tabuleiroServico.getPegarPeca().isDisable() && tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getTabuleiro().getTurnoJogador().getQuantidadePecasForaTabuleiro() > 0 && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == Casa.CASA_VAZIA) { //adicionar Peça tabuleiro
            enviarPacoteAdicionarPeca(posicaoFinal);
        } else if (tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getTurnoJogador().getTipo()) { //escolher peça
            tabuleiroServico.selecionarPeca(posicaoFinal);
        } else if (tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == Casa.CASA_VAZIA && posicaoInicial != Tabuleiro.POSICAO_INICIAL_VAZIA && posicaoInicial != posicaoFinal) {//andar ou capturar uma peça
            verificarMovimentoAndar(posicaoInicial, posicaoFinal);
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!tabuleiroServico.getPassarTurno().isDisable() && !tabuleiroServico.getTabuleiro().getTurnoJogador().isRemoverOutraPeca() && tabuleiroServico.getCasasTabuleiro().get(posicaoFinal).getCasa().getPeca().getTipo() == Casa.CASA_VAZIA && posicaoInicial != Tabuleiro.POSICAO_INICIAL_VAZIA && posicaoInicial != posicaoFinal) {//capturar multipla peças
            verificaCaptura(posicaoInicial, posicaoFinal);
        } else if (!tabuleiroServico.getPassarTurno().isDisable() && tabuleiroServico.getTabuleiro().getTurnoJogador().isRemoverOutraPeca()) {//remover peça ao realizar a captura multipla
            enviarPacoteRemoverOutraPeca(posicaoFinal);
        }
    }

    //verifica se a captura de uma peça é possivel
    public void verificaCaptura(int posicaoInicial, int posicaoFinal) throws RemoteException {
        switch (posicaoFinal - posicaoInicial) {
            case 2: //captura a peça para direita
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial + 1);
                break;
            case -2: //captura a peça para esquerda
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial - 1);
                break;
            case 12: //captura a peça para baixo
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial + 6);
                break;
            case -12: //captura a peça para cima
                enviarPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoInicial - 6);
                break;
            default:
                break;
        }
    }

    //verifica se a movimentação de uma peça é possivel
    public void verificarMovimentoAndar(int posicaoInicial, int posicaoFinal) throws RemoteException {
        switch (posicaoFinal - posicaoInicial) {
            case 1: //anda a peça para direita
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            case -1: //anda a peça para esquerda
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            case 6: //anda a peça para baixo
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            case -6: //anda a peça para cima
                enviarPacoteAndarPeca(posicaoInicial, posicaoFinal);
                break;
            default:
                break;
        }
    }

    //envia mensagem do jogador que capturou uma peça do adversario
   
    public void enviarPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) throws RemoteException {
        if (tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getJogadorAdversario().getTipo()) {
            procurarServidorNomes();
            tabuleiroServico.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
            tabuleiroServico.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroServico.getTabuleiro().getJogadorAdversario());
            tabuleiroServico.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(true);
            tabuleiroServico.selecionarPeca(posicaoFinal);
            tabuleiroServico.setTextNumeroPecasAdversarias();
            tabuleiroServico.desabilitarBotaoPegarPeca(true);
            tabuleiroServico.desabilitarBotaoPassarTurno(false);
            servidorInterface.receberPacoteCapturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
            verificarVitoria();
            verificarEmpate();
        }
    }

    //envia mensagem do jogador que removou outra peça do adversario caso tenha capturado
   
    public void enviarPacoteRemoverOutraPeca(int posicao) throws RemoteException {
        if (tabuleiroServico.getCasasTabuleiro().get(posicao).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getJogadorAdversario().getTipo()) {
            procurarServidorNomes();
            tabuleiroServico.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroServico.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroServico.getTabuleiro().getJogadorAdversario());
            tabuleiroServico.removerOutraPeca(posicao);
            tabuleiroServico.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(false);
            tabuleiroServico.setTextNumeroPecasAdversarias();
            servidorInterface.receberPacoteRemoverOutraPeca(posicao);
            verificarVitoria();
            verificarEmpate();

        }
    }

    //verifica e envia mensagem se há um jogador vitorioso,
   
    public void verificarVitoria() throws RemoteException {
        if (tabuleiroServico.getTabuleiro().getJogadorAdversario().totalPecas() == 0) {
            procurarServidorNomes();
            janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Você ganhou a partida!");
            tabuleiroServico.vitoria();
            servidorInterface.receberPacoteVitoria();
            enviarPacoteReiniciarPartida();
        }
    }

    //verifica e envia mensagem se há empate
   
    public void verificarEmpate() throws RemoteException {
        if (tabuleiroServico.getTabuleiro().getTurnoJogador().totalPecas() <= 3 && tabuleiroServico.getTabuleiro().getJogadorAdversario().totalPecas() <= 3) {
            procurarServidorNomes();
            tabuleiroServico.empate();
            servidorInterface.receberEmpatePartida();
            enviarPacoteReiniciarPartida();
        }
    }

    //envia mensagem para reinicio de partida
   
    public void enviarPacoteReiniciarPartida() throws RemoteException {
        procurarServidorNomes();
        tabuleiroServico.reiniciarPartida();
        servidorInterface.receberPacoteReiniciarPartida();
    }

    //envia mensagem para desistencia de partida
   
    public void enviarPacoteDesistirPartida() throws RemoteException {
        procurarServidorNomes();
        chatServico.adicionarMensagemChat("O jogador " + tabuleiroServico.getTabuleiro().getJogador().getTipo() + " desistiu da partida!");
        servidorInterface.recebePacoteDesistirPartida(tabuleiroServico.getTabuleiro().getJogador().getTipo());
        enviarPacoteReiniciarPartida();
    }

    //envia mensagem para saida de partida
   
    public void enviarPacoteSairPartida() throws RemoteException {
        procurarServidorNomes();
        servidorInterface.receberPacoteSairPartida(tabuleiroServico.getTabuleiro().getJogador().getTipo());
        tabuleiroServico.sairPartida();
    }

}
