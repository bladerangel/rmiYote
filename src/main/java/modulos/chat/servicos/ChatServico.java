package modulos.chat.servicos;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//classe servico de chat do layout usado no controlador
public class ChatServico {

    private TextField escreverMensagem;
    private TextArea chat;

    public ChatServico(TextField escreverMensagem, TextArea chat) {
        this.escreverMensagem = escreverMensagem;
        this.chat = chat;
    }

    public void adicionarMensagemChat(String mensagem) {
        chat.appendText(mensagem + "\n");
    }

    public void limparMensagem() {
        escreverMensagem.clear();
    }

    public TextField getEscreverMensagem() {
        return escreverMensagem;
    }
}
