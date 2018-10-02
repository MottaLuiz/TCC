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
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
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
public class Semantizador extends Agent{
    private Analyzer cogroo;
            protected void setup(){
        System.out.println("Semantizador incializado");
        addBehaviour (new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msgr =receive() ;
                if (msgr!=null ){
            //System.out.println(" - " + myAgent.getLocalName( )+"<- " + msgr.getContent());
                
            String mensagem = msgr.getContent();
            String[] textoseparado = mensagem.split(":");
            System.out.println("frase:"+textoseparado[4].substring(2,textoseparado[4].length()-26));
            System.out.println("confiancafrase:"+textoseparado[5].substring(1,6));
            String[] palavras = textoseparado[4].substring(2,textoseparado[4].length()-26).split(" ");
            for (int i=0; i<=palavras.length -1;i++){
                int auxiliar = textoseparado[6].indexOf(palavras[i])+17+palavras[i].length();
                int auxiliar2 = textoseparado[6].indexOf(palavras[i])+25+palavras[i].length();
                System.out.println("palavra"+ i+":" + palavras[i]);
                System.out.println("confiança palavra "+i+":" +textoseparado[6].substring(auxiliar,auxiliar2));
            }
            ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
	    cogroo = factory.createPipe();
            Document document = new DocumentImpl();
	    document.setText(textoseparado[4].substring(2,textoseparado[4].length()-26));
            cogroo.analyze(document);
            StringBuilder output = new StringBuilder();
	
	    // and now we navigate the document to print its data
	    for (Sentence sentence : document.getSentences()) {
	
	      // Print the sentence. You can also get the sentence span annotation.
	      output.append("Sentence: ").append(sentence.getText()).append("\n");
	
	      output.append("  Tokens: \n");
	
	      // for each token found...
	      for (Token token : sentence.getTokens()) {
	        String lexeme = token.getLexeme();
	        String lemmas = Arrays.toString(token.getLemmas());
	        String pos = token.getPOSTag();
	        String feat = token.getFeatures();
	
	        output.append(String.format("    %-10s %-12s %-6s %-10s\n", lexeme,
	            lemmas, pos, feat));
	      }
	
	      // we can also print the chunks, but printing it is not that easy!
	      output.append("  Chunks: ");
	      for (Chunk chunk : sentence.getChunks()) {
	        output.append("[").append(chunk.getTag()).append(": ");
	        for (Token innerToken : chunk.getTokens()) {
	          output.append(innerToken.getLexeme()).append(" ");
	        }
	        output.append("] ");
	      }
	      output.append("\n");
	
	      // we can also print the shallow parsing results!
	      output.append("  Shallow Structure: ");
	      for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
	        output.append("[").append(structure.getTag()).append(": ");
	        for (Token innerToken : structure.getTokens()) {
	          output.append(innerToken.getLexeme()).append(" ");
	        }
	        output.append("] ");
	      }
	      output.append("\n");
	    }
	
	    System.out.println(output.toString());
          
            
  //fim do teste
            
            
                ACLMessage msge = new ACLMessage(INFORM);
                msge.setLanguage ("Portugues");
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

