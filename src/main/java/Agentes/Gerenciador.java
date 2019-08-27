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
    

    protected void setup() {
        FrameTarefa frametarefa = new FrameTarefa();
        
        System.out.println("Gerenciador incializado");
        
        
        
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
               ACLMessage msgr = receive();
                if (msgr != null){
                    try {
                       Vector<Pares>  pares = (Vector<Pares>) msgr.getContentObject();
                    for (int i = 0; i < pares.size(); i++){
            System.out.println("teste gerenciador argumento: " + pares.get(i).getArgs() + "--- intencao: " + pares.get(i).getIntencao());
            if (pares.get(i).getIntencao() == "Informarcomando") {
                System.out.println("entrou comando");
                frametarefa.setTarefa(pares.get(i).getArgs()); 
            }
            else if (pares.get(i).getIntencao() == "InformarAcao") {
                frametarefa.setAcao(pares.get(i).getArgs()); 
            }
            else if (pares.get(i).getIntencao() == "Informardispositivo") {
                frametarefa.setDispositivo(pares.get(i).getArgs()); 
            }
            else if (pares.get(i).getIntencao() == "Informarlocal") {
                frametarefa.setLocal(pares.get(i).getArgs());
            }
        }
                    
                    } catch (UnreadableException ex) {
                       Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                   }
                    
              System.out.println("teste frame tarefa"+frametarefa.getTarefa())      ;
              System.out.println("teste frame Acao"+frametarefa.getAcao())      ;
              System.out.println("teste frame dispositivo "+frametarefa.getDispositivo())      ;
              System.out.println("teste frame local"+frametarefa.getLocal())      ;
              
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                }
                else{
                    block();
                }
            }
        });
    }
}