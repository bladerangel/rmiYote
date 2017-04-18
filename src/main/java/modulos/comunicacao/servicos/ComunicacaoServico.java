package modulos.comunicacao.servicos;

import modulos.comunicacao.modelos.Comunicacao;
import utilitarios.JanelaAlerta;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;

//classe servico de comunicacao usado no controlador
public class ComunicacaoServico {

    private JanelaAlerta janelaAlerta;
    private boolean servidor;

    public ComunicacaoServico(JanelaAlerta janelaAlerta) {
        this.janelaAlerta = janelaAlerta;
    }

    public void iniciarComunicacao() {
        try {
            LocateRegistry.createRegistry(1099);
            janelaAlerta.janelaAlerta("Iniciar Partida", null, "Aguarde o jogador 2 conectar-se ....");
            janelaAlerta.janelaAlerta("Iniciar Partida", null, "O jogador 2 conectou-se");
            servidor = true;
        } catch (IOException e) { //caso o servidor esteja conectado é iniciado o cliente
            try {
                //comunicacao.iniciarCliente();
                servidor = false;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    //verificar se o jogador conectado é o cliente
    public boolean isServidor() {
        return servidor;
    }
}
