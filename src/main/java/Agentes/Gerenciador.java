/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import utils.GerenciadorCasa;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import static jade.lang.acl.ACLMessage.INFORM;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.*;
import java.util.*;
import utils.PilhaDialogo;

/**
 *
 * @author Lidera Consultoria
 */
public class Gerenciador extends Agent {

    private Vector<Pares> pares = new Vector<>();
    private Pares paux = new Pares();
    private PilhaDialogo pilha = new PilhaDialogo();
    private String resposta = new String();
    private FrameTarefa frame = new FrameTarefa();
    String estado = new String();

    protected void setup() {
        System.out.println("Gerenciador de dialogo incializado");
        frame.init();
        resposta = null;
        pares = null;
        pilha.init();
        estado = "EsperandoMSG";

        GerenciadorCasa gc = new GerenciadorCasa();
        try {
            gc.init();
        } catch (IOException ex) {
            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
        }

        addBehaviour(new CyclicBehaviour(this) {

            public void action() {

                switch (estado) {

                    case "EsperandoMSG": {
                        ACLMessage msgr = receive();
                        pares = null;
                        if (msgr != null) {
                            try {
                                pares = (Vector<Pares>) msgr.getContentObject();
                                StringBuilder output = new StringBuilder();
                                System.out.println("PARES RECEBIDOS");
                                estado = "AtualizandoPilha";

                            } catch (UnreadableException ex) {
                                Logger.getLogger(Gerenciador.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }

                            //System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());
                        } else {
                            estado = "EsperandoMSG";
                        }
                    }
                    case "AtualizandoPilha": {

                        resposta = null;
                        if (pilha.vazia()) {
                            if (pares != null) {
                                if (pares.size() == 1) {
                                    pilha.setIntencaoatual(pares.elementAt(0).getIntencao());
                                    pilha.setArgsatual(pares.elementAt(0).getArgs());
                                }
                                if (pares.size() > 1) {
                                    for (int i = 0; i <= pares.size(); i++) {
                                        if (pares.elementAt(i).getIntencao().equals("Informarconfirmacao")) {
                                            pilha.insere(pilha.getIntencaoatual(), pilha.getArgsatual());
                                            pilha.setIntencaoatual(pares.elementAt(i).getIntencao());
                                            pilha.setArgsatual(pares.elementAt(i).getArgs());
                                        } else {
                                            pilha.insere(pares.elementAt(i).getIntencao(),
                                                    pares.elementAt(i).getArgs());
                                        }
                                    }
                                }
                                pares = null;
                            }
                        } else {//caso pilha nao vazia mais de um comando por frase

                        }

                        estado = "ProcessandoAtoAtual";
                    }
                    case "ProcessandoAtoAtual": {

                        if (pilha.getArgsatual().equals("") || pilha.getIntencaoatual().equals("")) {

                            resposta = TratadorErro.tratarerro(pilha.getIntencaoatual(), pilha.getArgsatual());
                            estado = "ComunicaGLN";
                        } else {
                            tratarato(pilha.getIntencaoatual(), pilha.getArgsatual());
                            estado = "PilhaVazia";
                        }

                    }
                    case "PilhaVazia": {

                        if (pilha.vazia() && frame.getAcao() != null && frame.getDispositivo() != null && frame.getLocal() != null) {
                            if (frame.getTarefa().equalsIgnoreCase("controlardispositivos")) {
                                resposta = ExecutadorTarefa.executar(frame, gc);
                                estado = "ComunicaGLN";
                            }
                        } else {
                            pilha.remove();
                            estado = "AtualizaPilha";
                        }

                    }

                    case "ComunicaGLN":{


                            ACLMessage msge = new ACLMessage(INFORM);
                            msge.setLanguage("Portugues");
                            msge.addReceiver(new AID("GeradorLN", AID.ISLOCALNAME));

                            try {
                                msge.setContentObject(resposta);

                            } catch (IOException ex) {
                                Logger.getLogger(Gerenciador.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            send(msge);
                            estado = "EsperandoMSG";
                        }

                }

    }});}

    private void tratarato(String intencaoatualaux, String argsatualaux) {

        switch (intencaoatualaux) {

            case "Informarlocal":
                frame.setLocal(argsatualaux);

            case "Informardispositivo":
                frame.setDispositivo(argsatualaux);

            case "Informaracao":
                if (argsatualaux.equals("ligar") || argsatualaux.equals("desligar") || argsatualaux.equals("aumentar") || argsatualaux.equals("diminuir")) {
                    frame.setTarefa("ControlarDisp");
                }
                frame.setAcao(argsatualaux);

            case "Informarconfirmacao":
                if (argsatualaux.equalsIgnoreCase("SIM")) {
                    TratadorErro.tratarrepostaerro(pilha);
                }
                if (argsatualaux.equalsIgnoreCase("NAO")) {
                    TratadorErro.tratarnaoentendido(pilha);
                }
            case "Informarnumeral":

            case "Informarnome":

            case "Informarcomando":
                frame.setTarefa(argsatualaux);
        }
    }
}
