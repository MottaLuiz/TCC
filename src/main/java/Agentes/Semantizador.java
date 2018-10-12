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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.Chunk;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.SyntacticChunk;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Lidera Consultoria
 */
public class Semantizador extends Agent {
    private static final boolean TRACE_MODE = false;
    static String botName = "conhecimentodialogo";

    private Analyzer cogroo;


    protected void setup() {
        System.out.println("Semantizador incializado");
        String resourcesPath = getResourcesPath();
        //System.out.println(resourcesPath);
        MagicBooleans.trace_mode = TRACE_MODE;
        Bot bot = new Bot("conhecimentodialogo", resourcesPath);
        bot.writeAIMLFiles();
        bot.writeQuit();
        
        
        
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                if (msgr != null) {
                    
                        //System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());
                        
                        String mensagem = msgr.getContent();
                        String[] textoseparado = mensagem.split(":");
                        String frase=textoseparado[4].substring(2, textoseparado[4].length() - 26);
                        System.out.println("frase:" + frase);
                        //System.out.println("confiancafrase:" + textoseparado[5].substring(1, 6));
                        String[] palavras = textoseparado[4].substring(2, textoseparado[4].length() - 26).split(" ");
                        for (int i = 0; i <= palavras.length - 1; i++) {
                            int auxiliar = textoseparado[6].indexOf(palavras[i]) + 17 + palavras[i].length();
                            int auxiliar2 = textoseparado[6].indexOf(palavras[i]) + 25 + palavras[i].length();
                            System.out.println("palavra " + i + ":" + palavras[i]);
                            System.out.println("confiança palavra " + i + ":" + textoseparado[6].substring(auxiliar, auxiliar2));
                            

                        }
                        Set<String> possiveisacoes = new HashSet<>(Arrays.asList(new String[]{"ligar", "desligar", "aumentar", "diminuir"}));
                        Set<String> possiveislocais = new HashSet<>(Arrays.asList(new String[]{"quarto", "sala", "cozinha", "varanda"}));
                        Set<String> possiveisdispositivos = new HashSet<>(Arrays.asList(new String[]{"lampada", "televisao", "som"}));
                        Set<String> possiveisconfirmacoes = new HashSet<>(Arrays.asList(new String[]{"sim", "nao"}));
                        //Cogroo
                        
                        //analise sintatica
                        ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
                        cogroo = factory.createPipe();
                        Document document = new DocumentImpl();
                        document.setText(frase);
                        cogroo.analyze(document);
                        System.out.println(document);
                        System.out.println("frase2="+frase);
                        
                        
                        //StringBuilder output = new StringBuilder();
                        int nacc = 0;
                        String[][][] pares = new String[3][5][3];
                        String confirmacao="";
                        // and now we navigate the document to print its data
                        for (Sentence sentence : document.getSentences()) {
                            
                            for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
                                System.out.println("estreutura="+structure);
                                System.out.println("acc?="+structure.getTag());
                                
                                if ("ACC".equals(structure.getTag())) {
                                    nacc = nacc + 1;
                                }
                            }
                            System.out.println("nacc="+nacc);
                            if (nacc >= 2) {
                                String[][] locais = new String[3][3];
                                String[][] dispositivos = new String[3][3];
                                String[] acao = new String[3];
                                int cont = 0;
                                int contdisp[] = new int[3];
                                int auxdisp = 0;
                                int contlocais[] = new int[3];
                                int auxlocais = 0;
                                String palavra;
                                for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
                                    for (Token token : structure.getTokens()) {
                                         palavra = StringUtils.removeAll(StringUtils.removeAll(Arrays.toString(token.getLemmas()), "\\["), "\\]");
                                        palavra = getEq(palavra, resourcesPath);
                                        System.out.println("tokenlematizado=" + Arrays.toString(token.getLemmas()));
                                        if ("P".equals(structure.getTag())) {
                                            System.out.println("acao:"+ Arrays.toString(token.getLemmas()));
                                            acao[cont] = palavra;
                                            cont=cont+1;
                                            auxdisp = 0;
                                            auxlocais = 0;
                                        } else if ("ACC".equals(structure.getTag())) {
                                            if ("n".equals(token.getPOSTag())) {
                                                if (possiveisdispositivos.contains(StringUtils.stripAccents(palavra))) {
                                                    dispositivos[cont-1][auxdisp] = palavra;
                                                    contdisp[cont-1] = auxdisp + 1;
                                                } else {
                                                    if (possiveislocais.contains(StringUtils.stripAccents(palavra))) {
                                                        locais[cont-1][auxdisp] = palavra;
                                                        contlocais[cont-1] = auxlocais + 1;
                                                    }
                                                }
                                            }
                                        }
                                        if("adv".equals(token.getPOSTag())){
                                            confirmacao=StringUtils.stripAccents(palavra);
                                        }
                                    }

                                }
                                for (int j = 0; j <= cont - 1; j++) {
                                    
                                    pares[j][0][0]="Gerenciar Dispositivos";
                                    if (possiveisacoes.contains(StringUtils.stripAccents(acao[j]))){
                                        pares[j][1][0]=acao[j];
                                        System.out.println("acao: " + acao[j]);
                                    }
                                    for (int i = 0; i <= contdisp[j] - 1; i++) {
                                        pares[j][2][i]=dispositivos[j][i];
                                        System.out.println("dispositivos: " + dispositivos[j][i]);
                                    }
                                    for (int i = 0; i <= contlocais[j] - 1; i++) {
                                        pares[j][3][i]=locais[j][i];
                                        System.out.println("locais: " + locais[j][i]);
                                    }
                                }
                            } else {
                                
                                String[] locais = new String[3];
                                String[] dispositivos = new String[3];
                                String acao = new String();
                                int contdisp = 0;
                                int contlocais = 0;
                                String palavra;
                                for (Token token : sentence.getTokens()) {
                                    palavra=StringUtils.removeAll(StringUtils.removeAll(Arrays.toString(token.getLemmas()), "\\["), "\\]");
                                    palavra=getEq(palavra,resourcesPath);

                                    if ("v-fin".equals(token.getPOSTag())) {
                                        acao = palavra;
                                    }

                                    if (("n".equals(token.getPOSTag()))||("adj".equals(token.getPOSTag()))) {
                                        
                                        if (possiveisdispositivos.contains(StringUtils.stripAccents(palavra))) {
                                            dispositivos[contdisp] = palavra;
                                            contdisp = contdisp + 1;
                                            
                                            
                                        } else if (possiveislocais.contains(StringUtils.stripAccents(palavra))) {
                                            locais[contlocais] = palavra;
                                            contlocais = contlocais + 1;
                                        }
                                    }
                                    if("adv".equals(token.getPOSTag())){
                                        confirmacao=palavra;
                                    }
                                    
                                }
                                pares[0][0][0]="Gerenciar Dispositivos";
                                if (possiveisacoes.contains(StringUtils.stripAccents(acao))){
                                    pares[0][1][0]=acao;
                                    System.out.println("acao: " + acao);
                                }
                                for (int i = 0; i <= contdisp - 1; i++) {
                                    pares[0][2][i]=dispositivos[i];
                                    System.out.println("dispositivos: " + dispositivos[i]);
                                }
                                for (int i = 0; i <= contlocais - 1; i++) {
                                    pares[0][3][i]=locais[i];
                                    System.out.println("locais: " + locais[i]);
                                }
                                
                            }
                        }
                        if (possiveisconfirmacoes.contains(confirmacao)){
                            pares[0][4][0]=confirmacao;
                        }
                        
                        
                        ACLMessage msge = new ACLMessage(INFORM);
                        msge.setLanguage("Portugues");
                        msge.addReceiver(new AID("Gerenciador", AID.ISLOCALNAME));
                        System.out.println("pares: ");
                        StringBuilder output = new StringBuilder();
                        for (int i=0; i<pares.length ; i++){
                            output.append("\n");
                            for (int j=0; j<pares[i].length;j++){
                                output.append(" ");
                                for (int k=0; k<pares[i][j].length;k++){
                                   output.append(pares[i][j][k]);
                                    //System.out.println(pares[i][j][k]);
                                    output.append(" ");
                                }
                            }
                        }
                        System.out.println(output);
                        
                        
                    try {
                        //System.out.println(Arrays.toString(pares));
                        msge.setContentObject(pares);
                    } catch (IOException ex) {
                        Logger.getLogger(Semantizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        send(msge);

                        //msge.setContent(Arrays.toString(pares));
                        //send(msge) ;

                }
                // interrompe este comportamento ate que chegue uma nova mensagem
                block();
            }
        });
    }
    
    
    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        //System.out.println(path);
        String resourcesPath = path + "\\src\\main";
        return resourcesPath;
    }
    
    public String getEq(String requisicao, String resourcesPath) {
        String request = requisicao;
        Bot bota = new Bot("conhecimentodialogo", resourcesPath);
        Chat chatSession = new Chat(bota);
        String response = chatSession.multisentenceRespond(request);
        if (!"I have no answer for that.".equals(response)) {
            requisicao = requisicao.replaceAll(request, response);
        }
        return requisicao;

    }


}

