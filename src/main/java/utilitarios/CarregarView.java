package utilitarios;

import modulos.main.controladores.MainControlador;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//classe utilitario para o carregamento de arquivos .fxml
public class CarregarView {

    private FXMLLoader fxmlLoader;
    private Stage estagio;
    private Scene cena;

    public CarregarView(String view) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/" + view + ".fxml"));
    }

    public void setEstagio(Stage estagio) {
        this.estagio = estagio;
    }

    public void setCena() {
        try {
            cena = new Scene(this.fxmlLoader.load());
            this.estagio.setScene(cena);
            this.estagio.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //executa a açao quando o jogador fecha a janela
    public void sairPartida() {
        MainControlador mainControlador = (MainControlador) fxmlLoader.getController();
        estagio.setOnCloseRequest(event -> mainControlador.sairPartida());
    }

    //mostra a aplicação layout
    public void show() {
        this.estagio.show();
    }
}
