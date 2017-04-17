package modulos.tabuleiro.servicos;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import modulos.casa.componentes.CasaBotao;

import modulos.tabuleiro.modelos.Tabuleiro;
import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import utilitarios.JanelaAlerta;

import java.util.ArrayList;

//classe servico tabuleiro usado no controlador
public class TabuleiroServico {

    private Pane tabuleiroPane;
    private Text numeroPecas;
    private Text numeroPecasAdversarias;
    private Text tipoJogador;
    private Text turnoAtual;
    private Button pegarPeca;
    private Button passarTurno;

    private JanelaAlerta janelaAlerta;
    private Tabuleiro tabuleiro;
    private ArrayList<CasaBotao> casasTabuleiro;
    private ChatServico chatServico;
    private ComunicacaoServico comunicacaoServico;

    public TabuleiroServico(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, Button pegarPeca, Button passarTurno, JanelaAlerta janelaAlerta, ChatServico chatServico, ComunicacaoServico comunicacaoServico) {
        this.tabuleiroPane = tabuleiroPane;
        this.numeroPecas = numeroPecas;
        this.numeroPecasAdversarias = numeroPecasAdversarias;
        this.tipoJogador = tipoJogador;
        this.turnoAtual = turnoAtual;
        this.pegarPeca = pegarPeca;
        this.passarTurno = passarTurno;
        this.chatServico = chatServico;
        this.comunicacaoServico = comunicacaoServico;
        this.janelaAlerta = janelaAlerta;
        casasTabuleiro = new ArrayList<CasaBotao>();
    }

    //inicia a partida
    public void iniciarPartida() {
        criarTabuleiro();

    }

    //cria o layout do tabuleiro
    public void criarTabuleiro() {
        VBox linhasTabuleiro = new VBox();
        tabuleiroPane.getChildren().add(linhasTabuleiro);
        for (int i = 0; i < Tabuleiro.QUANTIDADE_LINHAS; i++) {
            HBox colunasTabuleiro = new HBox();
            tabuleiroPane.getChildren().add(colunasTabuleiro);
            linhasTabuleiro.getChildren().add(colunasTabuleiro);
            for (int j = 0; j < Tabuleiro.QUANTIDADE_COLUNAS; j++) {
                CasaBotao casa = new CasaBotao();
                casasTabuleiro.add(casa);
                colunasTabuleiro.getChildren().add(casa);
                casa.getCasa().setPosicao(casasTabuleiro.size() - 1);
            }
        }
        iniciarTabuleiro();
    }

    //inicia o tabuleiro
    public void iniciarTabuleiro() {
        casasTabuleiro.forEach(CasaBotao::resetarCasa);
        tabuleiro = new Tabuleiro(comunicacaoServico.isServidor());
        if (tabuleiro.getTurnoJogador() == tabuleiro.getJogador()) {
            desabilitarBotaoPegarPeca(false);
        } else {
            desabilitarBotaoPegarPeca(true);
        }

        desabilitarBotaoPassarTurno(true);
        setTextTipoJogador();
        setTextNumeroPecas();
        setTextNumeroPecasAdversarias();
        setTextTurnoAtual();
    }


    public void setTextTipoJogador() {
        tipoJogador.setText("Você: Jogador " + tabuleiro.getJogador().getTipo());
    }

    public void setTextNumeroPecas() {
        numeroPecas.setText(tabuleiro.getJogador().getQuantidadePecasForaTabuleiro() + " peças restantes");
    }

    public void setTextNumeroPecasAdversarias() {
        numeroPecasAdversarias.setText(tabuleiro.getJogadorAdversario().getQuantidadePecasForaTabuleiro() + " peças adversarias restantes");
    }

    public void setTextTurnoAtual() {
        turnoAtual.setText("Turno Atual: Jogador " + tabuleiro.getTurnoJogador().getTipo());
    }

    //desabilita o botao pegar peça
    public void desabilitarBotaoPegarPeca(boolean desabilitar) {
        pegarPeca.setDisable(desabilitar);
    }

    //desabilita o botao passar turnp
    public void desabilitarBotaoPassarTurno(boolean desabilitar) {
        passarTurno.setDisable(desabilitar);
    }

    //jogador escreve uma mensagem no chat
    public void adicionarMensagemChat(int jogador, String mensagem) {
        chatServico.adicionarMensagemChat("O jogador " + jogador + " digitou: " + mensagem);
    }

    //exibe a mensagem quando o jogador pega uma peça fora do tabuleiro
    public void pegarPeca() {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " tirou 1 peça");
    }

    //jogador passa o turno e exibe a mensagem do jogador que irá jogar
    public void passarTurno() {
        tabuleiro.mudarTurnoJogador();
        chatServico.adicionarMensagemChat("O turno é do jogador " + tabuleiro.getTurnoJogador().getTipo());
        setTextTurnoAtual();
    }

    //jogador adiciona uma peça no tabuleiro e exibe a mensagem da posicao da casa aonde o jogador colocou a peça
    public void adicionarPeca(int posicao) {
        tabuleiro.getTurnoJogador().removerPecasForaTabuleiro();
        casasTabuleiro.get(posicao).colocarPeca(tabuleiro.getTurnoJogador());
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " colocou uma peça na posicao " + posicao);
    }

    //jogador seleciona uma peça do tabuleiro
    public void selecionarPeca(int posicaoFinal) {
        tabuleiro.setPosicaoInicial(posicaoFinal);
    }

    //jogador movimenta uma peça no tabuleiro e exibe mensagem do jogador que moveu a peça
    public void andarPecar(int posicaoInicial, int posicaoFinal) {
        casasTabuleiro.get(posicaoInicial).removerPeca(tabuleiro.getTurnoJogador());
        casasTabuleiro.get(posicaoFinal).colocarPeca(tabuleiro.getTurnoJogador());
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " moveu a peça da casa " + posicaoInicial + " para " + posicaoFinal);
    }

    //jogador captura uma peça e exibe a mensagem do jogador que capturou a peça do adversario
    public void capturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) {
        casasTabuleiro.get(posicaoInicial).removerPeca(tabuleiro.getTurnoJogador());
        casasTabuleiro.get(posicaoFinal).colocarPeca(tabuleiro.getTurnoJogador());
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " capturou a peça da casa " + posicaoVerificar);
    }

    //exibe a mensagem do jogador que removeu a peça
    public void removerOutraPeca(int posicao) {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " removeu a peça da casa " + posicao);
    }

    //exibe a mensagem do jogador vitorioso
    public void vitoria() {
        chatServico.adicionarMensagemChat("O jogador " + tabuleiro.getTurnoJogador().getTipo() + " ganhou a partida!");
    }

    //exibe a mensagem de empate
    public void empate() {
        chatServico.adicionarMensagemChat("A partida terminou em empate");
        janelaAlerta.janelaAlertaRunLater("Resultado da partida", null, "Deu empate!");
    }

    //exibe a mensagem de reinicio de partida
    public void reiniciarPartida() {
        chatServico.adicionarMensagemChat("O jogo foi reiniciado!");
        iniciarTabuleiro();
    }

    //jogador sair da partida
    public void sairPartida() {
        comunicacaoServico.getComunicacao().fecharConexao();
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Button getPegarPeca() {
        return pegarPeca;
    }

    public Button getPassarTurno() {
        return passarTurno;
    }

    public ArrayList<CasaBotao> getCasasTabuleiro() {
        return casasTabuleiro;
    }
}
