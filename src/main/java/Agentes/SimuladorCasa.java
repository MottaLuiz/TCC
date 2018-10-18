/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

/**
 *
 * @author felip
 */
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import static jade.lang.acl.ACLMessage.INFORM;

import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class SimuladorCasa extends Agent {
    protected void setup() {

        System.out.println("Simulador da Casa incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
               // if (msgr != null) {
                    System.out.println(" - " + myAgent.getLocalName() + "<- " + msgr.getContent());
                    
                    JFrame f = new JFrame("Teste");
                    f.setSize(1000, 1000);
                    f.setVisible(true);
                    
                    
                    
                    
/*
                    try {

                       
                        ACLMessage msge = new ACLMessage(INFORM);
                        msge.setLanguage("Portugues");
                        msge.addReceiver(new AID("ExecutadorTerefas", AID.ISLOCALNAME));
                        msge.setContent(msgr.getContent());

                        send(msge);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    // interrompe este comportamento ate que chegue uma nova mensagem
               // }
               // block();

            }
        });
    }
    
    public void paint(Graphics g){
        g.setColor(Color.red);
        g.drawRect(50, 50, 200, 200);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.blue);
        g2d.drawRect(75, 75, 300, 200);

        Graphics2D g1 = (Graphics2D) g2d;/*
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("televisaoligada.png"));
        } catch (IOException ex) {
            Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
        }
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        g1.drawImage(img, 50, 50, null);  // TODO code application logic here*/
    }
}
