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
            comunicacao.iniciarServidorNomes(1099);
            janelaAlerta.janelaAlerta("Iniciar Partida", null, "Aguarde o jogador 2 conectar-se ....");
            servidor = true;
        } catch (IOException e) { //caso o servidor de nomes esteja iniciado
            servidor = false;
        }

    }

    public Comunicacao getComunicacao() {
        return comunicacao;
    }

    //verificar se o jogador conectado é o servidor
    public boolean isServidor() {
        return servidor;
    }
}
