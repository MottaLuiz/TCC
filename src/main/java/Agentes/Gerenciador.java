/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import utils.GerenciadorCasa;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import static jade.lang.acl.ACLMessage.INFORM;
import jade.lang.acl.UnreadableException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Lidera Consultoria
 */
public class Gerenciador extends Agent {

    protected void setup() {
        System.out.println("Gerenciador de dialogo incializado");
        GerenciadorCasa.consultar();
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr =receive() ;
                String pares[][][];
                
                if (msgr!=null ){
                    try {
                    pares = (String[][][]) msgr.getContentObject();
                    StringBuilder output =new StringBuilder();
                    System.out.println("MATRIZ RECEBIDA");
                    for (int i=0; i<pares.length ; i++){
                            output.append("\n");
                            for (int j=0; j<pares[i].length;j++){
                                output.append(" ");
                                for (int k=0; k<pares[i][j].length;k++){
                                   output.append(pares[i][j][k]);
                                    //System.out.println(pares[i][j][k]);
                                    output.append(" ");
                                }
                            }
                        }
                        System.out.println(output);
                } catch (UnreadableException ex) {
                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
            //System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());
                
                  ACLMessage msge = new ACLMessage(INFORM);
                msge.setLanguage ("Portugues");
                msge.addReceiver(new AID("GeradorLN", AID.ISLOCALNAME));

                msge.setContent(msgr.getContent());
                send(msge) ;}
            // interrompe este comportamento ate que chegue uma nova mensagem
            block();
            }
        });
    }

}