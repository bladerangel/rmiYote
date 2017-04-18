package modulos.comunicacao.modelos;

import modulos.tabuleiro.servicos.TabuleiroEnviarPacoteServico;
import modulos.tabuleiro.servicos.TabuleiroReceberPacoteServico;

import java.rmi.Naming;

//classe comunicacao modelo
public class Comunicacao {

    //ServidorInterface servidorInterface; //envia

    public void iniciarServidor(TabuleiroEnviarPacoteServico tabuleiroEnviarPacoteServico) throws Exception {
        //servidorInterface = tabuleiroEnviarPacoteServico;
        //Naming.rebind("//localhost/servidor", servidorInterface);
        System.out.println("Servidor Criado!");
    }

    public void iniciarCliente(TabuleiroReceberPacoteServico tabuleiroReceberPacoteServico) throws Exception {
       //servidorInterface = (ServidorInterface) Naming.lookup("//localhost/servidor");
        //servidorInterface.setCliente(tabuleiroReceberPacoteServico);
        System.out.println("Cliente Criado!");

    }

    //public ServidorInterface getServidorInterface() {
       // return servidorInterface;
    //}
}
