//PENSAR SE VAI SIMULAR OU COMPRAR UMA LAMPADA
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
import jade.lang.acl.UnreadableException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import java.awt.event.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import utils.FrameControle;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import utils.Dispositivo;
import Agentes.GerenciadorCasa;
import java.util.Vector;

public class SimuladorCasa extends Agent {

    static boolean simulaComm(String endereco, String acao) {
        System.out.println("Menssagem enviada para dispisitivo: " + endereco + " ação: " + acao);
        return true;
    }

    private String disp;
    private String estado;
    private String local;
    static String filePath;
    List<Dispositivo> listadispositivos = new ArrayList<Dispositivo>();

    protected void setup() {

        System.out.println("Simulador da Casa incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();

                if (msgr != null) {
                System.out.println(" - " + myAgent.getLocalName() + "<- " + msgr.getContent());
                //simula casa toda vez que recebe uma mensagem
                JFrame frame = new JFrame();
                PaintPane pane = new PaintPane();
                pane.setBackground(Color.white);

                frame.setBounds(0, 0, 1920, 1080);
                frame.setVisible(true);
                frame.add(pane);

                //mostra por 10s       
                try {
                    Thread.sleep(10000);

                } catch (InterruptedException ex) {
                    Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.setVisible(false);
                frame = null;
                // interrompe este comportamento ate que chegue uma nova mensagem
                }
                block();

            }
        });
    }

    protected class PaintPane extends JPanel {
        BufferedImage img = null;

        @Override
        protected void paintComponent(Graphics g) {

            GerenciadorCasa gc = new GerenciadorCasa();
            try {
                gc.init();
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }

            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            

            //ILUMINACAO
            //1 - Cozinha
            //1.1 - lampada
            String bool;
            try {
                bool = gc.EstadoDispositivo("lampada","cozinha");
                if (bool.equals("ligado")) {
                    g.setColor(Color.yellow);
                    g.fillRect(0, 0, 960, 540);
                }
                System.out.println("estado é igual a : " + bool);
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //2 - Quarto
            //2.1 - lampada
            try {
                bool = gc.EstadoDispositivo("lampada","quarto");
                if (bool.equals("ligado")) {
                    g.setColor(Color.yellow);
                    g.fillRect(960, 540, 960, 540);
                }
                System.out.println("estado é igual a : " + bool);
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            //3 - Sala
            //3.1 - lampada
            try {
                bool = gc.EstadoDispositivo("lampada","sala");
                if (bool.equals("ligado")) {
                    g.setColor(Color.yellow);
                    g.fillRect(0, 540, 960, 540);
                }
                System.out.println("estado é igual a : " + bool);
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //4 - Varanda
            //4.1 - lampada
            try {
                bool = gc.EstadoDispositivo("lampada","varanda");
                if (bool.equals("ligado")) {
                    g.setColor(Color.yellow);
                    g.fillRect(960, 0, 960, 540);
                }
                System.out.println("estado é igual a : " + bool);
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          
            
            
            
            
            
            
            
            //DESENHO DA CASA
            g.setColor(Color.black);
            //1 - Cozinha

            g.drawRect(0, 0, 960, 540);
            Font font = new Font("Arial", Font.BOLD, 20);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds("Cozinha", g);
            g.drawString("Cozinha", (int) (0 + rect.getWidth() / 2), (int) (0 + rect.getHeight()));
            
            //2 - Quarto
            g.drawRect(960, 540, 960, 540);
            rect = fm.getStringBounds("Quarto", g);
            g.drawString("Quarto", (int) (960 + rect.getWidth() / 2), (int) (540 + rect.getHeight()));
            
            //3 - Sala
            g.drawRect(0, 540, 960, 540);

            rect = fm.getStringBounds("Sala", g);
            g.drawString("Sala", (int) (0 + rect.getWidth() / 2), (int) (540 + rect.getHeight()));
            
            
            //4 - Varanda
            g.drawRect(960, 0, 960, 540);
            rect = fm.getStringBounds("Varanda", g);
            g.drawString("Varanda", (int) (960 + rect.getWidth() / 2), (int) (0 + rect.getHeight()));
            
            
            
            //DISPOSITIVOS
            //2 - Quarto
            //2.2 - tv
            try {
                bool = gc.EstadoDispositivo("televisao","quarto");
                if (bool.equals("ligado")) {
                    img = ImageIO.read(new File("televisaoligada.png"));
                }
                else {
                    img = ImageIO.read(new File("televisaodesligada.png"));
                }
                
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            float prop = (float) 0.5;
            float w = img.getWidth(null) * prop;
            float h = img.getHeight(null) * prop;

            BufferedImage bi = new BufferedImage(Math.round(w), Math.round(h), BufferedImage.TYPE_INT_ARGB);
            g.drawImage(img, 1000, 600, Math.round(w), Math.round(h), null);
            
            //2.3 - som
            try {
                bool = gc.EstadoDispositivo("som","quarto");
                if (bool.equals("ligado")) {
                    img = ImageIO.read(new File("somligado.png"));
                }
                else {
                    img = ImageIO.read(new File("somdesligado.png"));
                }
                
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            prop = (float) 0.1;
            w = img.getWidth(null) * prop;
             h = img.getHeight(null) * prop;

             bi = new BufferedImage(Math.round(w), Math.round(h), BufferedImage.TYPE_INT_ARGB);
            g.drawImage(img, 1500, 600, Math.round(w), Math.round(h), null);
            
            
            //3 - Sala
            //3.2 - tv
            try {
                bool = gc.EstadoDispositivo("televisao","sala");
                if (bool.equals("ligado")) {
                    img = ImageIO.read(new File("televisaoligada.png"));
                }
                else {
                    img = ImageIO.read(new File("televisaodesligada.png"));
                }
                
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            prop = (float) 0.5;
            w = img.getWidth(null) * prop;
            h = img.getHeight(null) * prop;

            bi = new BufferedImage(Math.round(w), Math.round(h), BufferedImage.TYPE_INT_ARGB);
            g.drawImage(img, 0, 600, Math.round(w), Math.round(h), null);
            
            
            //3.3 - som
            try {
                bool = gc.EstadoDispositivo("som","sala");
                if (bool.equals("ligado")) {
                    img = ImageIO.read(new File("somligado.png"));
                }
                else {
                    img = ImageIO.read(new File("somdesligado.png"));
                }
                
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            prop = (float) 0.1;
            w = img.getWidth(null) * prop;
             h = img.getHeight(null) * prop;

             bi = new BufferedImage(Math.round(w), Math.round(h), BufferedImage.TYPE_INT_ARGB);
            g.drawImage(img, 500, 600, Math.round(w), Math.round(h), null);
            
            
            //4 - Varanda
            //4.2 - som
            try {
                bool = gc.EstadoDispositivo("som","varanda");
                if (bool.equals("ligado")) {
                    img = ImageIO.read(new File("somligado.png"));
                }
                else {
                    img = ImageIO.read(new File("somdesligado.png"));
                }
                
            } catch (IOException ex) {
                Logger.getLogger(SimuladorCasa.class.getName()).log(Level.SEVERE, null, ex);
            }
            prop = (float) 0.1;
            w = img.getWidth(null) * prop;
             h = img.getHeight(null) * prop;

             bi = new BufferedImage(Math.round(w), Math.round(h), BufferedImage.TYPE_INT_ARGB);
            g.drawImage(img, 1000, 100, Math.round(w), Math.round(h), null);
            
        }

    }

}
