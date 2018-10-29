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
import utils.FrameControle;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import utils.Dispositivo;

public class SimuladorCasa extends Agent {

    static boolean simulaComm(String endereco, String acao) {
        System.out.println("Menssagem enviada para dispisitivo: " + endereco + " ação: " + acao);
        return true;
    }

    private FrameControle controle = new FrameControle();
    private String disp;
    private String estado;
    private String local;
    static String filePath;
    List<Dispositivo> listadispositivos = new ArrayList<Dispositivo>();

    protected void setup() {
        controle.init();

        System.out.println("Simulador da Casa incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                if (msgr != null) {
                    System.out.println(" - " + myAgent.getLocalName() + "<- " + msgr.getContent());
                    try {//pegar a acao que sera realizada
                        controle = (FrameControle) msgr.getContentObject();
                        disp = controle.getDispositivo();
                        estado = controle.getEstado();
                        local = controle.getLocal();
                    } catch (UnreadableException ex) {
                        Logger.getLogger(Gerenciador.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                    try {//criar lista de dispositivos
                        File currDir = new File(".");
                        String path = currDir.getAbsolutePath();
                        path = path.substring(0, path.length() - 1);
                        String resourcesPath = path + "listadedispositivos.txt";
                        filePath = resourcesPath;
                        String nome = filePath;
                        FileReader arq = new FileReader(nome);
                        BufferedReader lerArq = new BufferedReader(arq);
                        String linha = lerArq.readLine();
                        while (linha != null) {
                            System.out.printf("%s\n", linha);
                            linha = lerArq.readLine(); // lê da segunda até a última linha
                            String[] disploc = linha.split(";");

                            Dispositivo dispositivo = new Dispositivo();
                            for (int i = 0; i <= disploc.length; i++) {
                                String[] disp = disploc[i].split(" ");
                                dispositivo.setNome(disp[0]);
                                dispositivo.setLocal(disp[1]);
                                dispositivo.setEstado(disp[2]);
                                listadispositivos.add(dispositivo);
                            }
                        }

                        arq.close();

                    } catch (IOException e) {
                        System.err.printf("Erro na abertura do arquivo");
                    }

                    for (int i = 0; i <= listadispositivos.size(); i++) {
                        if (listadispositivos.get(i).getNome() == null ? disp == null : listadispositivos.get(i).getNome().equals(disp)) {
                            if (listadispositivos.get(i).getLocal() == null ? local == null : listadispositivos.get(i).getLocal().equals(local)) {
                                listadispositivos.get(i).setEstado(estado);
                            }
                        }
                    }

                    for (int i = 0; i <= listadispositivos.size(); i++) {
                        System.out.println("dispositivo : " + listadispositivos.get(i).getNome());
                        System.out.println("local : " + listadispositivos.get(i).getLocal());
                        System.out.println("estado: " + listadispositivos.get(i).getEstado());
                        System.out.println(" ");
                    }

                    // interrompe este comportamento ate que chegue uma nova mensagem
                }
                block();

            }
        });
    }

}
