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
        System.out.println(resourcesPath);
        MagicBooleans.trace_mode = TRACE_MODE;
        Bot bot = new Bot("conhecimentodialogo", resourcesPath);
        System.out.println("Gerador de linguagem natural incializado");
        bot.writeAIMLFiles();
        bot.writeQuit();
        System.out.println("Gerador de linguagem natural incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                if (msgr != null) {
                    System.out.println(" - " + myAgent.getLocalName() + "<- " + msgr.getContent());

                    try {

                        String request = "RESPOSTA PADRAO";//
                        //String request=msgr.toString();
                        Bot bota = new Bot("conhecimentodialogo", resourcesPath);
                        Chat chatSession = new Chat(bota);

                        String response = chatSession.multisentenceRespond(request);
                        System.out.println(response);
                        ACLMessage msge = new ACLMessage(INFORM);
                        msge.setLanguage("Portugues");
                        msge.addReceiver(new AID("GeradorVoz", AID.ISLOCALNAME));
                        msge.setContent(response);

                        send(msge);

                    } catch (Exception e) {
                        e.printStackTrace();
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
        System.out.println(path);
        String resourcesPath = path + "\\src\\main";
        return resourcesPath;
    }
}
