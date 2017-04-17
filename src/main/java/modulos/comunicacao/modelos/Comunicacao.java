package modulos.comunicacao.modelos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//classe comunicacao modelo
public class Comunicacao {

    private Socket cliente;
    private ServerSocket servidor;
    private DataInputStream fluxoEntradaDados;
    private DataOutputStream fluxoSaidaDados;
    private String mensagemRecebida;

    public void iniciarServidor(int porta) throws IOException {
        servidor = new ServerSocket(porta);
    }

    public void iniciarCliente(int porta) throws IOException {
        cliente = new Socket(InetAddress.getLocalHost(), porta);
        iniciarFluxoDados();
    }

    public void iniciarFluxoDados() {
        try {
            fluxoSaidaDados = new DataOutputStream(cliente.getOutputStream());
            fluxoEntradaDados = new DataInputStream(cliente.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //verificar se o cliente está conectado
    public boolean isConectado() {
        return !cliente.isOutputShutdown();
    }

    //servidor esperando a conexao
    public void esperandoConexao() throws IOException {
        cliente = servidor.accept();
        iniciarFluxoDados();
    }

    //enviando mensagem
    public void enviarPacote(String mensagem) {
        try {
            fluxoSaidaDados.writeUTF(mensagem);
            fluxoSaidaDados.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //recebendo mensagem
    public String receberPacote() {
        try {
            mensagemRecebida = fluxoEntradaDados.readUTF();
            System.out.println("Mensagem Recebida: " + mensagemRecebida);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mensagemRecebida;

    }

    //fechar conexao com cliente
    public void fecharConexao() {
        try {
            cliente.shutdownOutput();
            cliente.shutdownInput();
            cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
