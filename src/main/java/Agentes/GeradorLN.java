/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import static jade.lang.acl.ACLMessage.INFORM;
/**
 *
 * @author Lidera Consultoria
 */
public class GeradorLN extends Agent{
    
            protected void setup(){
        System.out.println("Gerador de linguagem natural incializado");
         addBehaviour (new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr =receive() ;
                if (msgr!=null ){
            System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());
                  ACLMessage msge = new ACLMessage(INFORM);
                msge.setLanguage ("Portugues");
                msge.addReceiver(new AID("GeradorVoz", AID.ISLOCALNAME));
                msge.setContent(msgr.getContent());
                System.out.print("olá motta");
                
                send(msge) ;}
            // interrompe este comportamento ate que chegue uma nova mensagem
            block();
            }
        });
    }
}
