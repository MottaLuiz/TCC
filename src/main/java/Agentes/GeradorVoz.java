/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 *
 * @author Lidera Consultoria
 */
public class GeradorVoz extends Agent {

    protected void setup() {
        System.out.println("Gerador de voz incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(" - " + myAgent.getLocalName() + "<- " + msg.getContent());
                    // interrompe este comportamento ate que chegue uma nova mensagem

                    TextToSpeech textToSpeech = new TextToSpeech();
                    textToSpeech.setUsernameAndPassword("474a2e01-7aef-47b3-8dc7-7e6c3be4ee45", "idIWedWDbmfW");
                    try {
                        SynthesizeOptions synthesizeOptions
                                = new SynthesizeOptions.Builder()
                                .text(msg.getContent())
                                .accept("audio/wav")
                                .voice("pt-BR_IsabelaVoice")
                                .build();

                        InputStream inputStream
                                = textToSpeech.synthesize(synthesizeOptions).execute();
                        InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

                        OutputStream out = new FileOutputStream("hello_world.wav");
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }

                        out.close();
                        in.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                block();
            }
        });
    }

}
