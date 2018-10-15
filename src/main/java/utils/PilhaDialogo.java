/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Stack;

/**
 *
 * @author Luiz
 */
public class PilhaDialogo {

    private String intencaoatual;
    private String argsatual;
    private Stack<String> intencoes = new Stack<String>();
    private Stack<String> args = new Stack<String>();

    public String getIntencaoatual() {
        return intencaoatual;
    }

    public void setIntencaoatual(String intencaoatual) {
        this.intencaoatual = intencaoatual;
    }

    public String getArgsatual() {
        return argsatual;
    }

    public void setArgsatual(String argsatual) {
        this.argsatual = argsatual;
    }

    public void init() {
        intencaoatual = null;
        intencoes = null;
        argsatual = null;
        args = null;
    }

    public void insere(String atosaux, String argsaux) {
        int i;
        i = 0;
        while (atosaux != null && argsaux != null) {
            this.intencoes.push(atosaux);
            this.args.push(argsaux);
        }

    }

    public void remove() {
        this.intencaoatual = this.intencoes.pop();
        this.argsatual = this.args.pop();
    }

    public boolean vazia() {
        if (this.intencoes.isEmpty() && this.args.isEmpty()) {
            return true;
        }
        return false;
    }

}
