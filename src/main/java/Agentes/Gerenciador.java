//MUDAR A LOGICA

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

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
                        /*
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
                        } else { */
                            estado = "EsperandoMSG";
                        }
                     
                    //}
                    case "AtualizandoPilha": {
                        /*
                        boolean flag = false;
                        resposta = null;
                        System.out.println("estado processando analisando atos");
                        if (pilha.vazia()) {
                            if (pares != null) {
                                if (pares.size() == 1) {
                                    pilha.setIntencaoatual(pares.elementAt(0).getIntencao());
                                    pilha.setArgsatual(pares.elementAt(0).getArgs());
                                }
                                if (pares.size() > 1) {
                                    for (int i = 0; i < pares.size(); i++) {
                                        if (pares.elementAt(i).getIntencao().equals("Informarconfirmacao")) {
                                            pilha.insere(pilha.getIntencaoatual(), pilha.getArgsatual());
                                            pilha.setIntencaoatual(pares.elementAt(i).getIntencao());
                                            pilha.setArgsatual(pares.elementAt(i).getArgs());
                                            flag = true;
                                        } else {
                                            pilha.insere(pares.elementAt(i).getIntencao(), pares.elementAt(i).getArgs());
                                            System.out.println("inseriu " + i + " ");
                                        }
                                    }
                                }
                                if (flag) {
                                    flag = false;
                                } else {
                                    pilha.remove();
                                    System.out.println(pilha.getArgsatual() + "  " + pilha.getIntencaoatual());
                                    pares = null;
                                }
                            }
                        } else {
                            System.out.println("atualiza a pilha");
                            pilha.remove();

                        }
*/
                        estado = "ProcessandoAtoAtual";
                    }
                    case "ProcessandoAtoAtual": {
                    /*
                        System.out.println("estado processando atos");
                        System.out.println(pilha.getArgsatual() + "  " + pilha.getIntencaoatual());
                        if (pilha.getIntencaoatual().equals("") || pilha.getArgsatual().equals("")) {
                            resposta = TratadorErro.tratarerro(pilha.getIntencaoatual(), pilha.getArgsatual());
                            estado = "ComunicaGLN";
                        } else {
                            tratarato(pilha.getIntencaoatual(), pilha.getArgsatual());
                            estado = "PilhaVazia";
                        }

                    }
                    case "PilhaVazia": {
                        System.out.println("estado Pilha vazia");
                        if (pilha.vazia() && frame.getAcao() != null && frame.getDispositivo() != null && frame.getLocal() != null) {
                            if (frame.getTarefa().equalsIgnoreCase("controlardispositivos")) {
                                resposta = ExecutadorTarefa.executar(frame, gc);
                                estado = "ComunicaGLN";
                            }
                        } else {
                            pilha.remove();
                            estado = "AtualizaPilha";
                        }

                    } */
                    estado = "AtualizaPilha";}


                    case "ComunicaGLN": {

                        ACLMessage msge = new ACLMessage(INFORM);
                        msge.setLanguage("Portugues");
                        msge.addReceiver(new AID("GeradorLN", AID.ISLOCALNAME));

                        resposta=null;
                        resposta = "RESPOSTA PADRAO";
                        msge.setContent(resposta);
                        send(msge);
                        estado = "EsperandoMSG";
                    }

                }

            }
        });
    }
/*
    private void tratarato(String intencaoatualaux, String argsatualaux) {

        switch (intencaoatualaux) {

            case "Informarlocal": {
                frame.setLocal(argsatualaux);
                System.out.println(frame.getLocal());
            }
            case "Informardispositivo": {
                frame.setDispositivo(argsatualaux);
                System.out.println(frame.getDispositivo());
            }
            case "Informaracao": {
                if (argsatualaux.equals("ligar") || argsatualaux.equals("desligar") || argsatualaux.equals("aumentar") || argsatualaux.equals("diminuir")) {
                    frame.setTarefa("Controlardispositivos");
                    System.out.println(frame.getTarefa());
                }
                frame.setAcao(argsatualaux);
                System.out.println(frame.getAcao());
            }
            case "Informarconfirmacao": {
                if (argsatualaux.equalsIgnoreCase("SIM")) {
                    TratadorErro.tratarrepostaerro(pilha);
                }
                if (argsatualaux.equalsIgnoreCase("NAO")) {
                    TratadorErro.tratarnaoentendido(pilha);
                }
            }
            case "Informarnumeral":

            case "Informarnome":

            case "Informarcomando":
                frame.setTarefa(argsatualaux);
        }
    } 
*/
}

