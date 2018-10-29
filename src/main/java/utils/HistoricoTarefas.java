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

    private List<FrameTarefa> historico = new LinkedList<>();

    public void init() {
        historico = null;
    }

    public void insere(FrameTarefa objeto) {

        this.historico.add(0, objeto);
        if (this.historico.size() > 20) {
            this.historico.remove(20);
        }
    }

    public FrameTarefa get(int i) {
        return this.historico.get(i);
    }

    public boolean vazia() {
        return this.historico.isEmpty();
    }
}
