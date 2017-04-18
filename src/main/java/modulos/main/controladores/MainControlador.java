package modulos.main.controladores;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import modulos.main.servicos.MainServico;

//classe controladora main do layout
public class MainControlador implements Initializable {

    @FXML
    private Pane tabuleiroPane;

    @FXML
    private Text numeroPecas;

    @FXML
    private Text numeroPecasAdversarias;

    @FXML
    private Text tipoJogador;

    @FXML
    private Text turnoAtual;

    @FXML
    private TextField escreverMensagem;

    @FXML
    private TextArea chat;

    @FXML
    private Button pegarPeca;

    @FXML
    private Button passarTurno;

    private MainServico mainServico;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            mainServico = new MainServico(tabuleiroPane, numeroPecas, numeroPecasAdversarias, tipoJogador, turnoAtual, escreverMensagem, chat, pegarPeca, passarTurno);
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    //o jogador clica no botao pegar peça
    @FXML
    public void pegarPeca() throws RemoteException {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacotePegarPeca();
    }

    //o jogador clica no botao passar turno
    @FXML
    public void passarTurno() throws RemoteException {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacotePassarTurno();
    }

    //o jogador clica no botao desistir da partida
    @FXML
    private void desistirPartida() throws RemoteException {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacoteDesistirPartida();
    }

    //o jogador clica no botao limpar mensagem
    @FXML
    public void limparMensagem() {
        mainServico.getChatServico().limparMensagem();
    }

    //o jogador clica no botao para enviar uma mensagem ao chat
    @FXML
    public void enviarMensagem() throws RemoteException {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacoteMensagemChat();
    }

    //o jogador clica no para sair do jogo
    public void sairPartida() throws RemoteException, MalformedURLException, NotBoundException {
        mainServico.getTabuleiroEnviarPacoteServico().enviarPacoteSairPartida();
    }
}
