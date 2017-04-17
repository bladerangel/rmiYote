package modulos.tabuleiro.servicos;

import modulos.casa.componentes.CasaBotao;
import modulos.casa.modelos.Casa;
import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import modulos.tabuleiro.modelos.Tabuleiro;
import utilitarios.JanelaAlerta;

//classe servico envia pacote tabuleiro usado no controlador
public class TabuleiroEnviarPacoteServico {

    private JanelaAlerta janelaAlerta;
    private TabuleiroServico tabuleiroServico;
    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;


    public TabuleiroEnviarPacoteServico(JanelaAlerta janelaAlerta, TabuleiroServico tabuleiroServico, ComunicacaoServico comunicacaoServico, ChatServico chatServico) {
        this.janelaAlerta = janelaAlerta;
        this.tabuleiroServico = tabuleiroServico;
        this.comunicacaoServico = comunicacaoServico;
        this.chatServico = chatServico;
        tabuleiroServico.getCasasTabuleiro().forEach(casa -> casa.setOnMouseClicked(event -> movimentarPeca((CasaBotao) event.getSource())));
    }

    //envia mensagem no chat
    public void enviarPacoteMensagemChat() {
        if (!chatServico.getEscreverMensagem().getText().equals("")) {
            comunicacaoServico.getComunicacao().enviarPacote("jogadorDigitou:" + tabuleiroServico.getTabuleiro().getJogador().getTipo() + ":" + chatServico.getEscreverMensagem().getText());
            tabuleiroServico.adicionarMensagemChat(tabuleiroServico.getTabuleiro().getJogador().getTipo(), chatServico.getEscreverMensagem().getText());
            chatServico.limparMensagem();
        }
    }

    //envia mensagem de pegar peça fora do tabuleiro
    public void enviarPacotePegarPeca() {
        tabuleiroServico.pegarPeca();
        tabuleiroServico.desabilitarBotaoPegarPeca(true);
        comunicacaoServico.getComunicacao().enviarPacote("pegarPeca");
    }

    //envia mensagem de passar turno
    public void enviarPacotePassarTurno() {
        tabuleiroServico.passarTurno();
        comunicacaoServico.getComunicacao().enviarPacote("passarTurno");
        tabuleiroServico.desabilitarBotaoPegarPeca(true);
        tabuleiroServico.desabilitarBotaoPassarTurno(true);
    }

    //envia mensagem do jogador que adicionou uma peça no tabuleiro
    public void enviarPacoteAdicionarPeca(int posicao) {
        tabuleiroServico.adicionarPeca(posicao);
        tabuleiroServico.setTextNumeroPecas();
        comunicacaoServico.getComunicacao().enviarPacote("adicionarPeca:" + posicao);
        enviarPacotePassarTurno();
    }

    //envia mensagem do jogador que andou uma posicao no tabuleiro
    public void enviarPacoteAndarPeca(int posicaoInicial, int posicaoFinal) {
        tabuleiroServico.andarPecar(posicaoInicial, posicaoFinal);
        comunicacaoServico.getComunicacao().enviarPacote("andarPeca:" + posicaoInicial + ":" + posicaoFinal);
        enviarPacotePassarTurno();
    }

    //jogador movimenta uma peça no tabuleiro
    public void movimentarPeca(CasaBotao casa) {
        if (tabuleiroServico.getTabuleiro().getTurnoJogador() == tabuleiroServico.getTabuleiro().getJogador()) {
            verificarMovimento(tabuleiroServico.getTabuleiro().getPosicaoInicial(), casa.getCasa().getPosicao());
        }
    }

    //verifica qual tipo de movimento é realizado pelo jogador
    public void verificarMovimento(int posicaoInicial, int posicaoFinal) {
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
    public void verificaCaptura(int posicaoInicial, int posicaoFinal) {
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
    public void verificarMovimentoAndar(int posicaoInicial, int posicaoFinal) {
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
    public void enviarPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        if (tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getJogadorAdversario().getTipo()) {
            tabuleiroServico.capturarPeca(posicaoInicial, posicaoFinal, posicaoVerificar);
            tabuleiroServico.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroServico.getCasasTabuleiro().get(posicaoVerificar).removerPeca(tabuleiroServico.getTabuleiro().getJogadorAdversario());
            tabuleiroServico.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(true);
            tabuleiroServico.selecionarPeca(posicaoFinal);
            tabuleiroServico.setTextNumeroPecasAdversarias();
            tabuleiroServico.desabilitarBotaoPegarPeca(true);
            tabuleiroServico.desabilitarBotaoPassarTurno(false);
            comunicacaoServico.getComunicacao().enviarPacote("capturarPeca:" + posicaoInicial + ":" + posicaoFinal + ":" + posicaoVerificar);
            verificarVitoria();
            verificarEmpate();
        }
    }

    //envia mensagem do jogador que removou outra peça do adversario caso tenha capturado
    public void enviarPacoteRemoverOutraPeca(int posicao) {
        if (tabuleiroServico.getCasasTabuleiro().get(posicao).getCasa().getPeca().getTipo() == tabuleiroServico.getTabuleiro().getJogadorAdversario().getTipo()) {
            tabuleiroServico.getTabuleiro().getJogadorAdversario().removerPecasDentroTabuleiro();
            tabuleiroServico.getCasasTabuleiro().get(posicao).removerPeca(tabuleiroServico.getTabuleiro().getJogadorAdversario());
            tabuleiroServico.removerOutraPeca(posicao);
            tabuleiroServico.getTabuleiro().getTurnoJogador().setRemoverOutraPeca(false);
            tabuleiroServico.setTextNumeroPecasAdversarias();
            comunicacaoServico.getComunicacao().enviarPacote("removerOutraPeca:" + posicao);
            verificarVitoria();
            verificarEmpate();

        }
    }

    //verifica e envia mensagem se há um jogador vitorioso
    public void verificarVitoria() {
        if (tabuleiroServico.getTabuleiro().getJogadorAdversario().totalPecas() == 0) {
            janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Você ganhou a partida!");
            tabuleiroServico.vitoria();
            comunicacaoServico.getComunicacao().enviarPacote("vitoriaPartida");
            enviarPacoteReiniciarPartida();
        }
    }

    //verifica e envia mensagem se há empate
    public void verificarEmpate() {
        if (tabuleiroServico.getTabuleiro().getTurnoJogador().totalPecas() <= 3 && tabuleiroServico.getTabuleiro().getJogadorAdversario().totalPecas() <= 3) {
            tabuleiroServico.empate();
            comunicacaoServico.getComunicacao().enviarPacote("empatePartida");
            enviarPacoteReiniciarPartida();
        }
    }

    //envia mensagem para reinicio de partida
    public void enviarPacoteReiniciarPartida() {
        tabuleiroServico.reiniciarPartida();
        comunicacaoServico.getComunicacao().enviarPacote("reiniciarPartida");
    }

    //envia mensagem para desistencia de partida
    public void enviarPacoteDesistirPartida() {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiroServico.getTabuleiro().getJogador().getTipo() + " desistiu da partida!");
        comunicacaoServico.getComunicacao().enviarPacote("desistirPartida:" + tabuleiroServico.getTabuleiro().getJogador().getTipo());
        enviarPacoteReiniciarPartida();
    }

    //envia mensagem para saida de partida
    public void enviarPacoteSairPartida() {
        comunicacaoServico.getComunicacao().enviarPacote("sairPartida:" + tabuleiroServico.getTabuleiro().getJogador().getTipo());
        tabuleiroServico.sairPartida();
    }

}
