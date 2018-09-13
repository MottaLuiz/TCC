/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
/**
 *
 * @author Lidera Consultoria
 */
public class GeradorVoz extends Agent{
            protected void setup(){
        System.out.println("Gerador de voz incializado");
         addBehaviour (new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg =receive() ;
                if (msg!=null )
            System.out.println(" - " + myAgent.getLocalName( )+"<- " + msg.getContent());
            // interrompe este comportamento ate que chegue uma nova mensagem
            block();
            }
        });
    }
    
}