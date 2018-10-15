/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Luiz
 */
public class FrameTarefa {

    private String dominio;
    private String tarefa;
    private String acao;
    private String dispositivo;
    private String local;

    public void init() {
        dominio = "AutomacaoResidencial";
        tarefa = null;
        acao = null;
        dispositivo = null;
        local = null;
    }

    public String getDominio() {
        return dominio;
    }

    public String getTarefa() {
        return tarefa;
    }

    public String getAcao() {
        return acao;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public String getLocal() {
        return local;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public void setLocal(String local) {
        this.local = local;
    }

}
