/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.*;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import jade.core.behaviours.CyclicBehaviour;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import utils.FrameControle;
/**
 *
 * @author Lidera Consultoria
 */
public class ReconhecedorVoz  extends Agent{
    //teste
    
    //private FrameControle teste = new FrameControle();

    /**
     * @param args the command line arguments
     */
    protected void setup() {
        System.out.println("Reconhecedor de voz incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                SpeechToText service = new SpeechToText();

                service.setUsernameAndPassword("b556be8d-b0d3-4820-a458-b17ba6398137", "2tYzwqkqzrZt");

// Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
                int sampleRate = 16000;
                AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Line not supported");
                    System.exit(0);
                }

                TargetDataLine line;
                try {
                    line = (TargetDataLine) AudioSystem.getLine(info);
                

                line.open(format);

                line.start();

                AudioInputStream audio = new AudioInputStream(line);

                RecognizeOptions options = new RecognizeOptions.Builder()
                        //.interimResults(true)
                        .inactivityTimeout(5) // use this to stop listening when the speaker pauses, i.e. for 5s
                        .audio(audio)
                        //.contentType(HttpMediaType.AUDIO_RAW)
                        .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
                        /*.model(RecognizeOptions.pt-BR_BroadbandModel)  PT_BR_BROADBANDMODEL*/
                        .model("pt-BR_BroadbandModel")
                        .wordConfidence(Boolean.TRUE)
                        .build();
                /*aqui começa a leitura*/
                service.recognizeUsingWebSocket(options, new BaseRecognizeCallback() {
                    @Override
                    public void onTranscription(SpeechRecognitionResults speechResults) {
                       System.out.println(speechResults);
                        String msgr;
                        msgr="";
                        msgr=speechResults.toString();
                       /* msgr="{\n" +
"  \"results\": [\n" +
"    {\n" +
"      \"final\": true,\n" +
"      \"alternatives\": [\n" +
"        {\n" +
"          \"transcript\": \"quero criar uma rotina \",\n" +
"          \"confidence\": 1.0,\n" +
"          \"word_confidence\": [\n" +
"            [\n" +
"              \"quero\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"criar\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"uma\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"rotina\",\n" +
"              1.0\n" +
"            ]\n" +
"          ]\n" +
"        }\n" +
"      ]\n" +
"    }\n" +
"  ],\n" +
"  \"result_index\": 0\n" +
"}";
                        msgr="{\n" +
"  \"results\": [\n" +
"    {\n" +
"      \"final\": true,\n" +
"      \"alternatives\": [\n" +
"        {\n" +
"          \"transcript\": \"ligue a luz \",\n" +
"          \"confidence\": 0.771,\n" +
"          \"word_confidence\": [\n" +
"            [\n" +
"              \"ligue\",\n" +
"              0.596\n" +
"            ],\n" +
"            [\n" +
"              \"a\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"luz\",\n" +
"              0.923\n" +
"            ]\n" +
"          ]\n" +
"        }\n" +
"      ]\n" +
"    }\n" +
"  ],\n" +
"  \"result_index\": 0\n" +
"}";*/
                        
                      /*  msgr="{\n" +
"  \"results\": [\n" +
"    {\n" +
"      \"final\": true,\n" +
"      \"alternatives\": [\n" +
"        {\n" +
"          \"transcript\": \"ligue luz da sala e desligue a televisão do quarto \",\n" +
"          \"confidence\": 0.873,\n" +
"          \"word_confidence\": [\n" +
"            [\n" +
"              \"ligue\",\n" +
"              0.157\n" +
"            ],\n" +
"            [\n" +
"              \"luz\",\n" +
"              0.975\n" +
"            ],\n" +
"            [\n" +
"              \"da\",\n" +
"              0.998\n" +
"            ],\n" +
"            [\n" +
"              \"sala\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"e\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"desligue\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"a\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"televisão\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"do\",\n" +
"              1.0\n" +
"            ],\n" +
"            [\n" +
"              \"quarto\",\n" +
"              0.847\n" +
"            ]\n" +
"          ]\n" +
"        }\n" +
"      ]\n" +
"    }\n" +
"  ],\n" +
"  \"result_index\": 0\n" +
"}";*/
                        
                        ACLMessage msge = new ACLMessage(ACLMessage.INFORM);
                        msge.setLanguage("Portugues");
                        msge.addReceiver(new AID("Semantizador", AID.ISLOCALNAME));
                        msge.setContent(msgr);
                        send(msge);
                        /*
                        //TESTE 
                        ACLMessage mensagem = new ACLMessage(ACLMessage.INFORM);
                        teste.setEndereco("asd");
                        teste.setProtocolo("sad");
                        teste.setAcao("ligar");
                        teste.setDispositivo("televisao");

                        mensagem.setLanguage("Portugues");
                        mensagem.addReceiver(new AID("SimuladorCasa", AID.ISLOCALNAME));
        try {
            mensagem.setContentObject((Serializable) teste);
        } catch (IOException ex) {
            Logger.getLogger(Semantizador.class.getName()).log(Level.SEVERE, null, ex);
        }
                        send(mensagem);

                        */

                    }
                });

                System.out.println("Listening to your voice for the next 5s...");
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                    }

// closing the WebSockets underlying InputStream will close the WebSocket itself.
        line.stop();

        line.close();
} catch (LineUnavailableException ex) {
                    Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                }
        System.out.println("Fin.");
                
               
                block();
        // interrompe este comportamento ate que chegue uma nova mensagem
            }
            });
    }
        
    
}
