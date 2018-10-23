/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.io.Serializable;

/**
 *
 * @author felip
 */
public class FrameControle implements Serializable{

    private String protocolo;
    private String endereco;
    private String dispositivo;
    private String estado;
    private String local;

    public void init() {
        protocolo = null;
        endereco = null;
        dispositivo = null;
        estado = null;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public String getEstado() {
        return estado;
    }
    
    public String getLocal() {
        return local;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public void setLocal(String local){
        this.local = local;
    }

}
