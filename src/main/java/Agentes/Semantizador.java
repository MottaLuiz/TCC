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
import java.util.Arrays;
/**
 *
 * @author Lidera Consultoria
 */
public class Semantizador extends Agent{
            protected void setup(){
        System.out.println("Semantizador incializado");
        addBehaviour (new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr =receive() ;
                if (msgr!=null ){
            //System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());
                
            String mensagem = msgr.getContent();
            String[] textoseparado = mensagem.split(":");
            System.out.println("frase:"+textoseparado[4].substring(2,textoseparado[4].length()-26));
            System.out.println("confiancafrase:"+textoseparado[5].substring(1,6));
            String[] palavras = textoseparado[4].substring(2,textoseparado[4].length()-26).split(" ");
            for (int i=0; i<=palavras.length-1;i++){
                int auxiliar = textoseparado[6].indexOf(palavras[i])+17+palavras[i].length();
                int auxiliar2 = textoseparado[6].indexOf(palavras[i])+25+palavras[i].length();
                System.out.println("palavra"+ i+":" + palavras[i]);
                System.out.println("confianca palavra "+i+":" +textoseparado[6].substring(auxiliar,auxiliar2));
            }
            

            
            
  
            
            
                ACLMessage msge = new ACLMessage(INFORM);
                msge.setLanguage ("Portugues");
                msge.addReceiver(new AID("Gerenciador", AID.ISLOCALNAME));
                msge.setContent(msgr.getContent());
                send(msge) ;
                }
        // interrompe este comportamento ate que chegue uma nova mensagem
            block();
            }
        });
    }
}

