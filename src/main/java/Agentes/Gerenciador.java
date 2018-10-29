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

    protected void setup() {
        System.out.println("Gerenciador de dialogo incializado");
        frame.init();
        resposta = null;
        pares = null;
        pilha.init();
        GerenciadorCasa gc = new GerenciadorCasa();
        try {
            gc.init();
        } catch (IOException ex) {
            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
        }

        FSMBehaviour compFSM = new FSMBehaviour(this) {

        };
// r e g i s t r am o s o p r im e i r o comportamento − X
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                pares = null;
                if (msgr != null) {
                    try {
                        pares = (Vector<Pares>) msgr.getContentObject();
                        StringBuilder output = new StringBuilder();
                        System.out.println("PARES RECEBIDOS");

                    } catch (UnreadableException ex) {
                        Logger.getLogger(Gerenciador.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                    //System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());
                }
                // interrompe este comportamento ate que chegue uma nova mensagem
                block();
            }
        });
// registramos estado inicial − AnalisandoAtos
        compFSM.registerFirstState(new OneShotBehaviour(this) {
            public void action() {
                resposta = null;
                if (pilha.vazia()) {
                    if (pares != null) {
                        if (pares.size() == 1) {
                            pilha.setIntencaoatual(pares.elementAt(0).getIntencao());
                            pilha.setArgsatual(pares.elementAt(0).getArgs());
                        }
                        if (pares.size() > 1) {
                            for (int i = 0; i <= pares.size(); i++) {
                                if (pares.elementAt(i).getIntencao().equals("Confirmar")) {
                                    pilha.insere(pilha.getIntencaoatual(), pilha.getArgsatual());
                                    pilha.setIntencaoatual(pares.elementAt(i).getIntencao());
                                    pilha.setArgsatual(pares.elementAt(i).getArgs());
                                } else {
                                    pilha.insere(pares.elementAt(i).getIntencao(),
                                            pares.elementAt(i).getArgs());
                                }
                            }
                        }
                    }
                } else {

                }
            }

            public int onEnd() {
                if (pares != null) {
                    return 1;
                }
                return 0;
            }
        },
                 "AnalisandoAtos");
// registramos outro estado − ProcessandoAtoAtual
        compFSM.registerState(new OneShotBehaviour(this) {
            int flag = 0;

            public void action() {
                if (pilha.getIntencaoatual().equals("") || pilha.getArgsatual().equals("")) {
                    resposta = TratadorErro.tratarerro(pilha.getIntencaoatual(), pilha.getArgsatual());
                    flag = 2;
                } else {
                    tratarato(pilha.getIntencaoatual(), pilha.getArgsatual());
                    flag = 1;
                }
            }

            public int onEnd() {

                return flag;
            }

        }, "ProcessandoAtoAtual");

        // registramos outro estado − RespostaNula
        compFSM.registerState(new OneShotBehaviour(this) {
            int flag = 0;

            public void action() {
                if (pilha.vazia() && frame.getAcao() != null && frame.getDispositivo() != null && frame.getLocal() != null) {
                    if (frame.getTarefa().equalsIgnoreCase("controlardispositivos")) {
                        resposta = ExecutadorTarefa.executar(frame, gc);
                        flag = 1;
                    }
                } else {
                    pilha.remove();
                    flag = 0;
                }
            }

            public int onEnd() {

                return flag;
            }

        }, "PilhaVazia");

// registramos o ultimo estado − ComunicaGLN
        compFSM.registerLastState(new OneShotBehaviour(this) {

            public void action() {

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
            }
        }, "ComunicaGLN");
        // definimos as transicoes
        compFSM.registerTransition("AnalisandoAtos", "ProcessandoAtoAtual", 1);
        compFSM.registerTransition("AnalisandoAtos", "AnalisandoAtos", 0);
        compFSM.registerTransition("ProcessandoAtoAtual", "ComunicaGLN", 2);
        compFSM.registerTransition("ProcessandoAtoAtual", "PilhaVazia", 1);
        compFSM.registerTransition("PilhaVazia", "ProcessandoAtoAtual", 0);
        compFSM.registerTransition("PilhaVazia", "ComunicaGLN", 1);
        // acionamos o comportamento
        addBehaviour(compFSM);

    }

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
