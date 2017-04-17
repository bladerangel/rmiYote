package modulos.main.init;

import javafx.application.Application;
import javafx.stage.Stage;
import utilitarios.CarregarView;

//classe aplicacao main do layout
public class Main extends Application {

    private CarregarView carregarView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        carregarView = new CarregarView("MainVisao"); //carrega o arquivo MainVisao.fxml
        carregarView.setEstagio(primaryStage);
        carregarView.setCena();
        carregarView.sairPartida(); //carrega acao quando o jogador sair do jogo
        carregarView.show();

    }

    //executa a aplicacao
    public static void main(String[] args) {
        launch(args);
    }
}
