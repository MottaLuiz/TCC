/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.*;

/**
 *
 * @author Luiz
 */
public class HistoricoTarefas {

    private List<FrameTarefa> historico = new LinkedList<FrameTarefa>();

    public void init() {
        historico = null;
    }

    public void insere(FrameTarefa objeto) {
        this.historico.add(objeto);
    }

    public FrameTarefa remove() {
        return this.historico.remove(0);
    }

    public boolean vazia() {
        return this.historico.size() == 0;
    }
}
