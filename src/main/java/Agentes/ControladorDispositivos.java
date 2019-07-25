//CONECTAR DISPOSITIVOS REAIS OU SIMULADOR

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
import java.util.Vector;

public class ControladorDispositivos extends Agent {

    static boolean executa(Vector<String> comm, String acao) {
       switch (comm.get(1)){
           
       
               case "Protocolo1":{
                 return SimuladorCasa.simulaComm(comm.get(0), acao); 
               }
               case "Protocolo2":{
                   return SimuladorCasa.simulaComm(comm.get(0), acao);
               }
                case "Protocolo3":{
                   return SimuladorCasa.simulaComm(comm.get(0), acao);
               }
    }
       return false;
    }
    protected void setup() {

        System.out.println("Controlador de Dispositivos incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                
                //adicionar rotina de monitoramento de dispisitivos

            }
        });
    }
    
}
