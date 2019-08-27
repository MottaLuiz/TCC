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

    private String resposta = new String();

    protected void setup() {
        FrameTarefa frametarefa = new FrameTarefa();
        Vector<FrameTarefa> vetorframestarefa = new Vector<>();

        System.out.println("Gerenciador incializado");

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                String estado = new String();
                estado = "VerificandoErro";
                ACLMessage msgr = receive();
                if (msgr != null) {
                    try {
                        Vector<Pares> pares = (Vector<Pares>) msgr.getContentObject();
                        //transforma vetor de pares em frame
                        for (int i = 0; i < pares.size(); i++) {
                            System.out.println("teste gerenciador argumento: " + pares.get(i).getArgs() + "--- intencao: " + pares.get(i).getIntencao());
                            System.out.println(pares.get(i).getIntencao());
                            if ("Informarcomando".equals(pares.get(i).getIntencao())) {
                                frametarefa.setTarefa(pares.get(i).getArgs());
                            } else if ("Informaracao".equals(pares.get(i).getIntencao())) {
                                frametarefa.setAcao(pares.get(i).getArgs());
                            } else if ("Informardispositivo".equals(pares.get(i).getIntencao())) {
                                frametarefa.setDispositivo(pares.get(i).getArgs());
                            } else if ("Informarlocal".equals(pares.get(i).getIntencao())) {
                                frametarefa.setLocal(pares.get(i).getArgs());
                            }
                        }
                        pares = null;

                    } catch (UnreadableException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //imprime frame
                    System.out.println("teste frame tarefa" + frametarefa.getTarefa());
                    System.out.println("teste frame Acao" + frametarefa.getAcao());
                    System.out.println("teste frame dispositivo " + frametarefa.getDispositivo());
                    System.out.println("teste frame local" + frametarefa.getLocal());
                    //adiciona frame ao vetor de frames
                    vetorframestarefa.add(frametarefa);
                    ACLMessage msge = new ACLMessage(INFORM);
                    msge.setLanguage("Portugues");
                    msge.addReceiver(new AID("GeradorLN", AID.ISLOCALNAME));

                    switch (estado) {
                        case "VerificandoErro": {
                            if (vetorframestarefa.get(vetorframestarefa.size() - 1).getTarefa() == null) {
                                //se nao tem a tarefa a ser realizada
                                System.out.println("erro Tarefa");
                            } else if (vetorframestarefa.get(vetorframestarefa.size() - 1).getDispositivo() == null) {
                                //se nao tem dispositivo alvo
                                System.out.println("erro Dispositivo");
                                estado = "EsperandoDispositivo";
                                //verifica quais dispositivos existem se existir local
                                if (vetorframestarefa.get(vetorframestarefa.size() - 1).getLocal() == null){
                                    //fazer MOTTA ------------------------------------------------ verifica qual dispositivos existem naquele local e sugere pro GeradorLN
                                    resposta = "possiveis dispositivos para o local especificado";
                                }
                                else {
                                    resposta = "PERGUNTAR DISPOSITIVO";
                                }
                                
                            } else if (vetorframestarefa.get(vetorframestarefa.size() - 1).getLocal() == null) {
                                //se nao tem local alvo
                                System.out.println("erro Local");
                                if (vetorframestarefa.get(vetorframestarefa.size() - 1).getAcao() == null){
                                    //fazer MOTTA ------------------------------------------------ verifica qual acao pode ser realizada naquele local e dispositivo
                                    resposta = "acao a ser realizada pelo dispositivo naquele local";
                                }
                                
                            } else if (vetorframestarefa.get(vetorframestarefa.size() - 1).getAcao() == null) {
                                //se nao tem a acao a ser realizada
                                System.out.println("erro Acao");
                            } else {
                                //se possui todos os dados -- ok
                                System.out.println("deu certo");

                                resposta = "DISPOSITIVO " + vetorframestarefa.get(vetorframestarefa.size() - 1).getDispositivo() + " LOCAL " + vetorframestarefa.get(vetorframestarefa.size() - 1).getLocal() + " ACAO " + vetorframestarefa.get(vetorframestarefa.size() - 1).getAcao();

                                System.out.println("Resposta é " + resposta);

                            }
                            msge.setContent(resposta);
                            send(msge);

                        }
                        
                        case "EsperandoDispositivo":{
                            System.out.println("entrou esperando dispositivo");
                            if (vetorframestarefa.get(vetorframestarefa.size() - 1).getDispositivo() == null){
                                estado = "EsperandoDispositivo";
                            }
                            else{
                                vetorframestarefa.get(vetorframestarefa.size() - 1).setDispositivo(frametarefa.getDispositivo());
                                estado = "VerificandoErro";
                            }
                        }
                        
                        
                        
                    }

                } else {
                    block();
                }
            }
        });
    }
}
