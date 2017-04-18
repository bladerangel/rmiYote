package modulos.comunicacao.modelos;

import modulos.comunicacao.interfaces.ServidorInterface;
import modulos.jogador.modelos.Jogador;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

//classe comunicacao modelo
public class Comunicacao {

    private ServidorInterface servidorInterface;

    public void iniciarServidorNomes(int porta) throws RemoteException {
        LocateRegistry.createRegistry(porta);
    }

    //registrar servidor no servidor de nomes
    public void registrarServidor(ServidorInterface servidorInterface, Jogador jogador) throws IOException {
        Naming.rebind("//localhost/jogador" + jogador.getTipo(), servidorInterface);
    }

    //localizar servidor no servidor de nomes
    public void localizarServidor(Jogador jogador) throws RemoteException, NotBoundException, MalformedURLException {
        servidorInterface = (ServidorInterface) Naming.lookup("//localhost/jogador" + jogador.getTipo());

    }

    //remover servidor  no servidor de nomes
    public void removerRegistroServidor(Jogador jogador) throws RemoteException, NotBoundException, MalformedURLException {
        Naming.unbind("//localhost/jogador" + jogador.getTipo());
    }

    public ServidorInterface getServidorInterface() {
        return servidorInterface;
    }
}
