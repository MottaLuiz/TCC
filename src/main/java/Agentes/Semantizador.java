//MUDAR ESTRUTURA PARA MANDAR FRAME + POVOAR AIML

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
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
import utils.Pares;
import utils.EscreverAIML;

/**
 *
 * @author Lidera Consultoria
 */
public class Semantizador extends Agent {

    private static final boolean TRACE_MODE = false;
    static String botName = "conhecimentodialogo";
    private int modocriacao = 0;
    private int flag_numeral = 0;

    private Analyzer cogroo;

    protected void setup() {
        String resourcesPath = getResourcesPath();
        MagicBooleans.trace_mode = TRACE_MODE;
        Bot bot = new Bot("conhecimentodialogo", resourcesPath);
        bot.writeAIMLFiles();
        bot.writeQuit();

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr = receive();
                if (msgr != null) {

                    String mensagem = msgr.getContent();
                    String[] palavras = mensagem.split(" ");

                    Set<String> possiveisacoes = new HashSet<String>(Arrays.asList(new String[]{"ligar", "desligar", "aumentar", "diminuir"}));
                    Set<String> possiveislocais = new HashSet<String>(Arrays.asList(new String[]{"quarto", "sala", "cozinha", "varanda"}));
                    Set<String> possiveisdispositivos = new HashSet<String>(Arrays.asList(new String[]{"lampada", "televisao", "som"}));
                    Set<String> possiveisconfirmacoes = new HashSet<String>(Arrays.asList(new String[]{"sim", "não"}));
                    //Cogroo

                    //analise sintatica
                    //ComponentFactory factory;
                    Locale local = new Locale("pt", "BR");
                    ComponentFactory factory = ComponentFactory.create(local);
                    cogroo = factory.createPipe();
                    Document document = new DocumentImpl();
                    document.setText(mensagem);
                    cogroo.analyze(document);
                    mensagem = getEq(mensagem, resourcesPath);
                    mensagem = StringUtils.lowerCase(mensagem);

                    if (modocriacao == 2) {
//                        if (possiveisnumerais.contains(mensagem)) {
                        Vector<Pares> pares = new Vector<>();
                        Pares p = new Pares();
                        p.setIntencao("Informarnumeral");
                        p.setArgs(mensagem);
                        modocriacao = 0;
                        pares.add(p);
                        enviarmsg(pares);
                        flag_numeral = 1;
//                        }
                    }
                    if (modocriacao == 1) {
                        Vector<Pares> pares = new Vector<>();
                        Pares p = new Pares();
                        p.setIntencao("Informarnome");
                        p.setArgs(mensagem);
                        modocriacao = 2;
                        pares.add(p);
                        enviarmsg(pares);
                    }
                    if ("criar rotina".equals(mensagem)) {
                        modocriacao = 1;
                        Vector<Pares> pares = new Vector<>();
                        Pares p = new Pares();
                        p.setIntencao("informarintencao");
                        p.setArgs("Criar rotina");
                        pares.add(p);
                        enviarmsg(pares);
                    }

                    if ((modocriacao == 0) && (flag_numeral == 0)) {
                        mensagem = mensagem.replace("lampada", "lâmpada");
                        mensagem = mensagem.replace("nao", "não");
                        document.setText(mensagem);
                        cogroo.analyze(document);

                        //StringBuilder output = new StringBuilder();
                        int nacc = 0;

                        String confirmacao = "";
                        // and now we navigate the document to print its data
                        for (Sentence sentence : document.getSentences()) {

                            for (SyntacticChunk structure : sentence.getSyntacticChunks()) {

                                if ("ACC".equals(structure.getTag())) {
                                    nacc = nacc + 1;
                                }
                            }
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
                                        if ("[]".equals(Arrays.toString(token.getLemmas()))) {
                                            palavra = token.getLexeme();
                                        }
                                        if ("P".equals(structure.getTag())) {

                                            if (("ligar".equals(palavra)) && (token.getLexeme().contains("des"))) {
                                                palavra = "desligar";
                                            }
                                            if (possiveisacoes.contains(palavra)) {
                                                acao[cont] = palavra;
                                                cont = cont + 1;
                                            }

                                            auxdisp = 0;
                                            auxlocais = 0;
                                        } else if ("ACC".equals(structure.getTag())) {
                                            if (("n".equals(token.getPOSTag())) || ("adj".equals(token.getPOSTag()))) {
                                                if (possiveisdispositivos.contains(StringUtils.stripAccents(palavra))) {
                                                    dispositivos[cont - 1][auxdisp] = palavra;
                                                    contdisp[cont - 1] = auxdisp + 1;
                                                } else {
                                                    if (possiveislocais.contains(StringUtils.stripAccents(palavra))) {
                                                        locais[cont - 1][auxlocais] = palavra;
                                                        contlocais[cont - 1] = auxlocais + 1;
                                                    }
                                                }
                                            }
                                        }
                                        if ("adv".equals(token.getPOSTag())) {
                                            confirmacao = StringUtils.stripAccents(palavra);
                                        }
                                    }

                                }
                                for (int a = 0; a <= cont - 1; a++) {
                                    if (contdisp[a] == 0) {
                                        contdisp[a] = 1;
                                    }
                                    if (contlocais[a] == 0) {
                                        contlocais[a] = 1;
                                    }

                                    for (int j = 0; j <= contdisp[a] - 1; j++) {
                                        for (int k = 0; k <= contlocais[a] - 1; k++) {
                                            Vector<Pares> pares = new Vector<>();
                                            Pares pcomando = new Pares();
                                            Pares pacao = new Pares();
                                            Pares plocal = new Pares();
                                            Pares pdispositivo = new Pares();
                                            pcomando.setIntencao("Informarcomando");
                                            pcomando.setArgs("Controlardispositivos");
                                            pares.add(pcomando);
                                            if (possiveisacoes.contains(StringUtils.stripAccents(acao[a]))) {
                                                pacao.setIntencao("Informaracao");
                                                pacao.setArgs(StringUtils.stripAccents(acao[a]));

                                                pares.add(pacao);
                                            }
                                            plocal.setIntencao("Informarlocal");
                                            plocal.setArgs(StringUtils.stripAccents(locais[a][k]));
                                            pares.add(plocal);
                                            pdispositivo.setIntencao("Informardispositivo");
                                            pdispositivo.setArgs(StringUtils.stripAccents(dispositivos[a][j]));
                                            pares.add(pdispositivo);
                                            enviarmsg(pares);
                                        }
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
                                    palavra = StringUtils.removeAll(StringUtils.removeAll(Arrays.toString(token.getLemmas()), "\\["), "\\]");
                                    if ("[]".equals(Arrays.toString(token.getLemmas()))) {
                                        palavra = token.getLexeme();
                                    }
                                    palavra = getEq(palavra, resourcesPath);


                                    if (("v-fin".equals(token.getPOSTag())) || ("v-inf".equals(token.getPOSTag()))) {
                                        if (("ligar".equals(palavra)) && (token.getLexeme().contains("des"))) {
                                            palavra = "desligar";
                                        }
                                        acao = palavra;
                                    }

                                    if (("n".equals(token.getPOSTag())) || ("adj".equals(token.getPOSTag()))) {

                                        if (possiveisdispositivos.contains(StringUtils.stripAccents(palavra))) {
                                            dispositivos[contdisp] = palavra;
                                            contdisp = contdisp + 1;

                                        } else if (possiveislocais.contains(StringUtils.stripAccents(palavra))) {
                                            locais[contlocais] = palavra;
                                            contlocais = contlocais + 1;
                                        }
                                    }
                                    if ("adv".equals(token.getPOSTag())) {
                                        confirmacao = palavra;
                                    }

//                                if ("num".equals(token.getPOSTag()) && (modocriacao == 2)) {
//                                    Vector<Pares> pares = new Vector<>();
//                                    Pares p = new Pares();
//                                    p.setIntencao("Informarnumeral");
//                                    p.setArgs(StringUtils.stripAccents(palavra));
//                                    pares.add(p);
//                                    enviarmsg(pares);
//                                    modocriacao = 0;
//                                }
                                }
                                if (contdisp == 0) {
                                    contdisp = 1;
                                }
                                if (contlocais == 0) {
                                    contlocais = 1;
                                }
                                for (int j = 0; j <= contdisp - 1; j++) {
                                    for (int k = 0; k <= contlocais - 1; k++) {
                                        Vector<Pares> pares = new Vector<>();
                                        Pares pcomando = new Pares();
                                        Pares pdisp = new Pares();
                                        Pares pacao = new Pares();
                                        Pares plocal = new Pares();

                                        pcomando.setIntencao("Informarcomando");
                                        pcomando.setArgs("Controlardispositivos");
                                        pares.add(pcomando);


                                        if (possiveisacoes.contains(StringUtils.stripAccents(acao))) {
                                            pacao.setIntencao("Informaracao");
                                            pacao.setArgs(StringUtils.stripAccents(acao));

                                            pares.add(pacao);
                                        }

                                        pdisp.setIntencao("Informardispositivo");
                                        pdisp.setArgs(StringUtils.stripAccents(dispositivos[j]));
                                        pares.add(pdisp);

                                        plocal.setIntencao("Informarlocal");
                                        plocal.setArgs(StringUtils.stripAccents(locais[k]));
                                        pares.add(plocal);

                                        enviarmsg(pares);
                                    }
                                }
                            }
                        }
                        Vector<Pares> pares = new Vector<>();
                        Pares p = new Pares();
                        if (possiveisconfirmacoes.contains(confirmacao)) {
                            p.setIntencao("Informarconfirmacao");
                            p.setArgs(confirmacao);
                            pares.add(p);
                            enviarmsg(pares);/*
                        falta passar para frente se foi sim ou nao o resultado da confirmacao dada pelo usuário
                             */
                        }
                    }
                    flag_numeral = 0;

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
        String resourcesPath = path + "\\src\\main";
        return resourcesPath;
    }

    public String getEq(String requisicao, String resourcesPath) {
        String request = requisicao;
        Bot bota = new Bot("conhecimentodialogo", resourcesPath);
        Chat chatSession = new Chat(bota);
        String response = chatSession.multisentenceRespond(request);
        if ("I have no answer for that.".equals(response)) {
            response = requisicao;
        }
        return response;

    }

    public void enviarmsg(Vector<Pares> pares) {
        ACLMessage msge = new ACLMessage(INFORM);
        msge.setLanguage("Portugues");
        msge.addReceiver(new AID("Gerenciador", AID.ISLOCALNAME));

        try {
            msge.setContentObject(pares);
        } catch (IOException ex) {
            Logger.getLogger(Semantizador.class.getName()).log(Level.SEVERE, null, ex);
        }
        send(msge);
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
