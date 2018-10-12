/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

/**
 *
 * @author Luiz
 */
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import static jade.lang.acl.ACLMessage.INFORM;

public class ControladorDispositivos extends Agent {
    protected void setup() {

        System.out.println("Controlador de Dispositivos incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                if (msgr != null) {
                    System.out.println(" - " + myAgent.getLocalName() + "<- " + msgr.getContent());

                    try {

                       
                        ACLMessage msge = new ACLMessage(INFORM);
                        msge.setLanguage("Portugues");
                        msge.addReceiver(new AID("ControladorDispositivos", AID.ISLOCALNAME));
                        msge.setContent(msgr.getContent());

                        send(msge);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // interrompe este comportamento ate que chegue uma nova mensagem
                }
                block();

            }
        });
    }
    
}
