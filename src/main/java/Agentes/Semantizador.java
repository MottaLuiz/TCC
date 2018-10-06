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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.Chunk;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.SyntacticChunk;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;

/**
 *
 * @author Lidera Consultoria
 */
public class Semantizador extends Agent {

    private Analyzer cogroo;

    protected void setup() {
        System.out.println("Semantizador incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                if (msgr != null) {
                    //System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());

                    String mensagem = msgr.getContent();
                    String[] textoseparado = mensagem.split(":");
                    System.out.println("frase:" + textoseparado[4].substring(2, textoseparado[4].length() - 26));
                    System.out.println("confiancafrase:" + textoseparado[5].substring(1, 6));
                    String[] palavras = textoseparado[4].substring(2, textoseparado[4].length() - 26).split(" ");
                    for (int i = 0; i <= palavras.length - 1; i++) {
                        int auxiliar = textoseparado[6].indexOf(palavras[i]) + 17 + palavras[i].length();
                        int auxiliar2 = textoseparado[6].indexOf(palavras[i]) + 25 + palavras[i].length();
                        System.out.println("palavra" + i + ":" + palavras[i]);
                        System.out.println("confiança palavra " + i + ":" + textoseparado[6].substring(auxiliar, auxiliar2));
                    }
                    Set<String> possiveislocais = new HashSet<String>(Arrays.asList(new String[]{"[quarto]", "[sala]", "[cozinha]", "[varanda]"}));
                    Set<String> possiveisdispositivos = new HashSet<String>(Arrays.asList(new String[]{"[lâmpada]", "[televisão]", "[luz]", "[som]"}));
                    //Cogroo
                    ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
                    cogroo = factory.createPipe();
                    Document document = new DocumentImpl();
                    document.setText(textoseparado[4].substring(2, textoseparado[4].length() - 26));
                    cogroo.analyze(document);
                    StringBuilder output = new StringBuilder();

                    // and now we navigate the document to print its data
                    for (Sentence sentence : document.getSentences()) {
                        int nacc = 0;
                        for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
                            if ("ACC".equals(structure.getTag())) {
                                nacc = nacc + 1;
                            }
                        }
                        if (nacc >= 2) {
                            String[][] locais = new String[10][10];
                            String[][] dispositivos = new String[10][10];
                            String[] acao = new String[10];
                            int cont = 0;
                            int contdisp[] = new int[10];
                            int auxdisp = 0;
                            int contlocais[] = new int[10];
                            int auxlocais = 0;
                            for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
                                for (Token token : structure.getTokens()) {
                                    if ("P".equals(structure.getTag())) {
                                        acao[cont] = Arrays.toString(token.getLemmas());
                                        cont=cont+1;
                                        auxdisp = 0;
                                        auxlocais = 0;
                                    } else if ("ACC".equals(structure.getTag())) {
                                        if ("n".equals(token.getPOSTag())) {
                                            if (possiveisdispositivos.contains(Arrays.toString(token.getLemmas()))) {
                                                dispositivos[cont-1][auxdisp] = Arrays.toString(token.getLemmas());
                                                contdisp[cont-1] = auxdisp + 1;
                                            } else {
                                                if (possiveislocais.contains(Arrays.toString(token.getLemmas()))) {
                                                    locais[cont-1][auxdisp] = Arrays.toString(token.getLemmas());
                                                    contlocais[cont-1] = auxlocais + 1;
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                            for (int j = 0; j <= cont - 1; j++) {
                                System.out.println("acao: " + acao[j]);
                                for (int i = 0; i <= contdisp[j] - 1; i++) {
                                    System.out.println("dispositivos: " + dispositivos[j][i]);
                                }
                                for (int i = 0; i <= contlocais[j] - 1; i++) {
                                    System.out.println("locais: " + locais[j][i]);
                                }
                            }
                        } else {
                            String[] locais = new String[10];
                            String[] dispositivos = new String[10];
                            String acao = new String();
                            int contdisp = 0;
                            int contlocais = 0;
                            for (Token token : sentence.getTokens()) {
                                if ("v-fin".equals(token.getPOSTag())) {
                                    acao = Arrays.toString(token.getLemmas());
                                }

                                if ("n".equals(token.getPOSTag())) {

                                    if (possiveisdispositivos.contains(Arrays.toString(token.getLemmas()))) {
                                        dispositivos[contdisp] = Arrays.toString(token.getLemmas());
                                        contdisp = contdisp + 1;
                                    } else if (possiveislocais.contains(Arrays.toString(token.getLemmas()))) {
                                        locais[contlocais] = Arrays.toString(token.getLemmas());
                                        contlocais = contlocais + 1;
                                    }
                                }
                            }
                            System.out.println("acao: " + acao);
                            for (int i = 0; i <= contdisp - 1; i++) {
                                System.out.println("dispositivos: " + dispositivos[i]);
                            }
                            for (int i = 0; i <= contlocais - 1; i++) {
                                System.out.println("locais: " + locais[i]);
                            }

                        }
                    }

                    // System.out.println(output.toString());
                    //ver por ACC quais são nomes e verbos - verificar nas listas o que é o que de cada nome
                    //AIML
                    //Criar os pares
                    //fim do teste
                    ACLMessage msge = new ACLMessage(INFORM);
                    msge.setLanguage("Portugues");
                    msge.addReceiver(new AID("Gerenciador", AID.ISLOCALNAME));
                    msge.setContent(msgr.getContent());
                    //send(msge) ;
                }
                // interrompe este comportamento ate que chegue uma nova mensagem
                block();
            }
        });
    }
}
