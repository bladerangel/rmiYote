package modulos.comunicacao.interfaces;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

//interface do servidor
public interface ServidorInterface extends Remote {

    public void receberPacoteIniciarPartida() throws RemoteException, NotBoundException, MalformedURLException;

    public void receberPacoteMensagemChat(int jogador, String mensagem) throws RemoteException;

    public void recebePacotePegarPeca() throws RemoteException;

    public void receberPacotePassarTurno() throws RemoteException;

    public void receberPacoteAdicionarPeca(int posicao) throws RemoteException;

    public void receberPacoteAndarPeca(int posicaoInicial, int posicaoFinal) throws RemoteException;

    public void receberPacoteCapturarPeca(int posicaoInicial, int posicaoFinal, int posicaoVerificar) throws RemoteException;

    public void receberPacoteRemoverOutraPeca(int posicao) throws RemoteException;

    public void receberPacoteVitoria() throws RemoteException;

    public void receberEmpatePartida() throws RemoteException;

    public void receberPacoteReiniciarPartida() throws RemoteException;

    public void recebePacoteDesistirPartida(int jogador) throws RemoteException;

    public void receberPacoteSairPartida(int jogador) throws RemoteException;
}
