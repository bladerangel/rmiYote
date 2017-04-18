package modulos.main.servicos;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import modulos.tabuleiro.servicos.TabuleiroEnviarPacoteServico;
import modulos.tabuleiro.servicos.TabuleiroReceberPacoteServico;
import modulos.tabuleiro.servicos.TabuleiroServico;
import utilitarios.JanelaAlerta;

import java.io.IOException;
import java.rmi.NotBoundException;

//classe servico main usado no controlador
public class MainServico {

    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;
    private TabuleiroServico tabuleiroServico;
    private TabuleiroEnviarPacoteServico tabuleiroEnviarPacoteServico;
    private TabuleiroReceberPacoteServico tabuleiroReceberPacoteServico;
    private JanelaAlerta janelaAlerta;

    public MainServico(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, TextField escreverMensagem, TextArea chat, Button pegarPeca, Button passarTurno) throws IOException, NotBoundException {

        janelaAlerta = new JanelaAlerta();
        comunicacaoServico = new ComunicacaoServico(janelaAlerta);
        comunicacaoServico.iniciarComunicacao(); //inicia a comunicacao
        chatServico = new ChatServico(escreverMensagem, chat);
        tabuleiroServico = new TabuleiroServico(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, pegarPeca, passarTurno, janelaAlerta, chatServico, comunicacaoServico);
        tabuleiroServico.criarTabuleiro(); //inicia o layout do tabuleiro
        tabuleiroEnviarPacoteServico = new TabuleiroEnviarPacoteServico(janelaAlerta, tabuleiroServico, comunicacaoServico, chatServico);
        tabuleiroReceberPacoteServico = new TabuleiroReceberPacoteServico(janelaAlerta, tabuleiroServico, comunicacaoServico, chatServico);
        tabuleiroReceberPacoteServico.iniciarJogador();
        tabuleiroEnviarPacoteServico.enviarPacoteIniciarPartida();
    }

    public ChatServico getChatServico() {
        return chatServico;
    }

    public TabuleiroEnviarPacoteServico getTabuleiroEnviarPacoteServico() {
        return tabuleiroEnviarPacoteServico;
    }
}
