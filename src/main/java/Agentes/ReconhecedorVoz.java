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
import java.util.Scanner;
/**
 *
 * @author Lidera Consultoria
 */
public class ReconhecedorVoz  extends Agent{

    /**
     * @param args the command line arguments
     */
        protected void setup(){
               System.out.println("Reconhecedor de voz incializado");
        addBehaviour (new CyclicBehaviour(this) {
            public void action() {
                String msgr;
                msgr = "";
                Scanner ler = new Scanner(System.in);
                msgr = ler.nextLine();
                
                ACLMessage msge = new ACLMessage(ACLMessage.INFORM);
                msge.setLanguage ("Portugues");
                msge.addReceiver(new AID("Semantizador", AID.ISLOCALNAME));
                msge.setContent(msgr);
                send(msge) ;
 
        // interrompe este comportamento ate que chegue uma nova mensagem
            }
            });
    }
        
    
}
