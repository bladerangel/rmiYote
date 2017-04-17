package utilitarios;

import javafx.application.Platform;
import javafx.scene.control.Alert;

//classe utilitario de exibição de alerta
public class JanelaAlerta {

    private Alert alert;

    //exibe uma mensagem de alerta
    public void janelaAlerta(String titulo, String cabecalho, String conteudo) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    //exibe uma mensagem de alerta usando runLater
    public void janelaAlertaRunLater(String titulo, String cabecalho, String conteudo) {
        Platform.runLater(() -> janelaAlerta(titulo, cabecalho, conteudo));
    }
}
