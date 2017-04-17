package modulos.casa.componentes;

import javafx.scene.control.Button;
import modulos.jogador.modelos.Jogador;
import modulos.casa.modelos.Casa;

//classe componente botao do layout
public class CasaBotao extends Button {

    private Casa casa;

    public CasaBotao() {
        casa = new Casa();
        getStyleClass().add("casa"); //adicionando estilo inicial do botao
    }

    //adicionando estilo do botao de acordo com o tipo de jogador
    public void colocarPeca(Jogador jogador) {
        casa.getPeca().setTipo(jogador.getTipo());
        if (jogador.getTipo() == Jogador.TIPO_JOGADOR_SERVIDOR)
            getStyleClass().add("imagemPeca");
        else
            getStyleClass().add("imagemPecaAdversaria");
    }

    //removendo estilo do botao de acordo com o tipo de jogador
    public void removerPeca(Jogador jogador) {
        casa.getPeca().setTipo(Casa.CASA_VAZIA);
        if (jogador.getTipo() == Jogador.TIPO_JOGADOR_SERVIDOR)
            getStyleClass().remove("imagemPeca");
        else
            getStyleClass().remove("imagemPecaAdversaria");
    }

    //removendo todos os estilo no botao
    public void resetarCasa() {
        casa.getPeca().setTipo(Casa.CASA_VAZIA);
        getStyleClass().remove("imagemPeca");
        getStyleClass().remove("imagemPecaAdversaria");
    }

    public Casa getCasa() {
        return casa;
    }
}
