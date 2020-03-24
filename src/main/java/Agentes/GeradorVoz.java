//FALTA CONSEGUIR TOCAR DIRETO O ROLE

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Lidera Consultoria
 */
public class GeradorVoz extends Agent {
    // to store current position 

    Long currentFrame;
    Clip clip;
    AudioInputStream audioInputStream;
    static String filePath;

    protected void setup() {
        System.out.println("Gerador de voz incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(" - " + myAgent.getLocalName() + "<- " + msg.getContent());
                    
                    
                    // interrompe este comportamento ate que chegue uma nova mensagem

                    try {
                        File currDir = new File(".");
                        String path = currDir.getAbsolutePath();
                        path = path.substring(0, path.length() - 2);
                        //System.out.println(path);
                        String resourcesPath = path + "\\hello_world.wav";
                        filePath = resourcesPath;
                        // create AudioInputStream object 
                        audioInputStream
                                = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

                        // create clip reference 
                        clip = AudioSystem.getClip();

                        // open audioInputStream to the clip 
                        clip.open(audioInputStream);

                        clip.start();
                    } catch (Exception ex) {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                }

                block();
            }
        });
    }

    public void play() {
        //start the clip 
        clip.start();

    }

}
