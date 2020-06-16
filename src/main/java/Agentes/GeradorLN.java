// PRONTO - POVOAR AIML
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
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alicebot.ab.MagicBooleans;

/**
 *
 * @author Lidera Consultoria
 */
public class GeradorLN extends Agent {

    private static final boolean TRACE_MODE = false;
    static String botName = "conhecimentodialogo";

    protected void setup() {

        String resourcesPath = getResourcesPath();
        MagicBooleans.trace_mode = TRACE_MODE;
        Bot bot = new Bot("conhecimentodialogo", resourcesPath);

        bot.writeAIMLFiles();
        bot.writeQuit();

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                if ((msgr != null) && (msgr.getContent().toString() != "")) {

                    try {

                        String request = msgr.getContent();//
                        //String request=msgr.toString();
                        Bot bota = new Bot("conhecimentodialogo", resourcesPath);
                        Chat chatSession = new Chat(bota);

                        String response = chatSession.multisentenceRespond(request);
                        ACLMessage msge = new ACLMessage(INFORM);
                        msge.setLanguage("Portugues");
                        msge.addReceiver(new AID("GeradorVoz", AID.ISLOCALNAME));
                        msge.setContent(response);

                        send(msge);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // interrompe este comportamento ate que chegue uma nova mensagem
                }
                block();

            }
        });
    }

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
//        System.out.println(path);
        String resourcesPath = path + "\\src\\main";
        return resourcesPath;
    }
}
