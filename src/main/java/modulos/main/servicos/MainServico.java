package modulos.main.servicos;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import modulos.chat.servicos.ChatServico;
import modulos.comunicacao.servicos.ComunicacaoServico;
import modulos.tabuleiro.servicos.TabuleiroEnviarPacoteServico;
import modulos.tabuleiro.servicos.TabuleiroReceberPacoteServicos;
import modulos.tabuleiro.servicos.TabuleiroServico;
import utilitarios.JanelaAlerta;

//classe servico main usado no controlador
public class MainServico {

    private ComunicacaoServico comunicacaoServico;
    private ChatServico chatServico;
    private TabuleiroServico tabuleiroServico;
    private TabuleiroEnviarPacoteServico tabuleiroEnviarPacoteServico;
    private TabuleiroReceberPacoteServicos tabuleiroReceberPacoteServicos;
    private JanelaAlerta janelaAlerta;

    public MainServico(Pane tabuleiroPane, Text numeroPecas, Text numeroPecasAdversarias, Text tipoJogador, Text turnoAtual, TextField escreverMensagem, TextArea chat, Button pegarPeca, Button passarTurno) {

        janelaAlerta = new JanelaAlerta();
        comunicacaoServico = new ComunicacaoServico(janelaAlerta);
        comunicacaoServico.iniciarComunicacao(); //inicia a comunicacao
        chatServico = new ChatServico(escreverMensagem, chat);
        tabuleiroServico = new TabuleiroServico(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, pegarPeca, passarTurno, janelaAlerta, chatServico, comunicacaoServico);
        tabuleiroServico.iniciarPartida(); //inicia a partida
        tabuleiroEnviarPacoteServico = new TabuleiroEnviarPacoteServico(janelaAlerta, tabuleiroServico, comunicacaoServico, chatServico);
        tabuleiroReceberPacoteServicos = new TabuleiroReceberPacoteServicos(janelaAlerta, tabuleiroServico, comunicacaoServico, chatServico);
        tabuleiroReceberPacoteServicos.iniciarThreadRecebePacotes(); //inicia thread responsavel por receber as mensagens
    }

    public ChatServico getChatServico() {
        return chatServico;
    }

    public TabuleiroEnviarPacoteServico getTabuleiroEnviarPacoteServico() {
        return tabuleiroEnviarPacoteServico;
    }
}
