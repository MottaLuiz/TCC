//FUNCIONANDO + TIRAR OS TESTES 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import jade.core.behaviours.CyclicBehaviour;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import utils.FrameControle;

/**
 *
 * @author Lidera Consultoria
 */
public class ReconhecedorVoz extends Agent {
    //teste

    //private FrameControle teste = new FrameControle();
    /**
     * @param args the command line arguments
     */
    protected void setup() {

        System.out.println("Reconhecedor de voz incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //System.out.println(speechResults);
                String msgr;
                msgr = "";
                //msgr = speechResults.toString();

                msgr = "{\n"
                        + "  \"results\": [\n"
                        + "    {\n"
                        + "      \"final\": true,\n"
                        + "      \"alternatives\": [\n"
                        + "        {\n"
                        + "          \"transcript\": \"ligue luz da sala \",\n"
                        + "          \"confidence\": 0.873,\n"
                        + "          \"word_confidence\": [\n"
                        + "            [\n"
                        + "              \"ligue\",\n"
                        + "              0.157\n"
                        + "            ],\n"
                        + "            [\n"
                        + "              \"luz\",\n"
                        + "              0.975\n"
                        + "            ],\n"
                        + "            [\n"
                        + "              \"da\",\n"
                        + "              0.998\n"
                        + "            ],\n"
                        + "            [\n"
                        + "              \"sala\",\n"
                        + "              1.0\n"
                        + "            ],\n"
                        + "            ]\n"
                        + "        }\n"
                        + "      ]\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"result_index\": 0\n"
                        + "}";

                ACLMessage msge = new ACLMessage(ACLMessage.INFORM);
                msge.setLanguage("Portugues");
                msge.addReceiver(new AID("Semantizador", AID.ISLOCALNAME));
                msge.setContent(msgr);
                send(msge);
                block();

            }
        });


    }
    
}
