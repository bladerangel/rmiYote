package modulos.peca.modelos;

import modulos.casa.modelos.Casa;

//classe peca modelo
public class Peca {

    private int tipo;

    public Peca() {
        tipo = Casa.CASA_VAZIA;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }
}
