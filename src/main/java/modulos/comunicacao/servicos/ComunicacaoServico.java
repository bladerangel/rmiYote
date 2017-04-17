package modulos.comunicacao.servicos;

import modulos.comunicacao.modelos.Comunicacao;
import utilitarios.JanelaAlerta;

import java.io.IOException;

//classe servico de comunicacao usado no controlador
public class ComunicacaoServico {

    private Comunicacao comunicacao;
    private JanelaAlerta janelaAlerta;
    private boolean servidor;

    public ComunicacaoServico(JanelaAlerta janelaAlerta) {
        this.janelaAlerta = janelaAlerta;
    }

    public void iniciarComunicacao() {
        try {
            comunicacao = new Comunicacao(); //inicia a conexao
            comunicacao.iniciarServidor(9999);
            janelaAlerta.janelaAlerta("Iniciar Partida", null, "Aguarde o jogador 2 conectar-se ....");
            comunicacao.esperandoConexao();
            janelaAlerta.janelaAlerta("Iniciar Partida", null, "O jogador 2 conectou-se");
            servidor = true;
        } catch (IOException e) { //caso o servidor esteja conectado é iniciado o cliente
            try {
                comunicacao.iniciarCliente(9999);
                servidor = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public Comunicacao getComunicacao() {
        return comunicacao;
    }

    //verificar se o jogador conectado é o cliente
    public boolean isServidor() {
        return servidor;
    }
}
