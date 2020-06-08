//FALTA CONSEGUIR TOCAR DIRETO O ROLE

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

/**
 *
 * @author Lidera Consultoria
 */
public class GeradorVoz extends Agent {
    // to store current position 

    Long currentFrame;
    Clip clip;
    AudioInputStream audioInputStream;
    static String filePath;

    protected void setup() {
        System.out.println("Gerador de voz incializado");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if ((msg != null)&&msg.getContent()!="") {
                    System.out.println(" - " + myAgent.getLocalName() + "<- " + msg.getContent());
//                    try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
//                        // Set the text input to be synthesized
//
//                        SynthesisInput input = SynthesisInput.newBuilder()
//                                .setText(msg.getContent())
//                                .build();
//
//                        // Build the voice request, select the language code ("en-US") and the ssml voice gender
//                        // ("neutral")
//                        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
//                                .setLanguageCode("pt-BR")
//                                .setSsmlGender(SsmlVoiceGender.NEUTRAL)
//                                .build();
//
//                        // Select the type of audio file you want returned
//                        AudioConfig audioConfig = AudioConfig.newBuilder()
//                                .setAudioEncoding(AudioEncoding.MP3)
//                                .build();
//
//                        // Perform the text-to-speech request on the text input with the selected voice parameters and
//                        // audio file type
//                        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice,
//                                audioConfig);
//
//                        // Get the audio contents from the response
//                        ByteString audioContents = response.getAudioContent();
//
//                        // Write the response to the output file.
//                        try (OutputStream out = new FileOutputStream("output.mp3")) {
//                            out.write(audioContents.toByteArray());
//                            System.out.println("Audio content written to file \"output.mp3\"");
//                        }
//                    } catch (IOException ex) {
//                        Logger.getLogger(GeradorVoz.class.getName()).log(Level.SEVERE, null, ex);
//                    }

                    try {
                        System.out.println("Descomentar linha a seguir para tocar o som direto");

//                        play("output.mp3");

                    } catch (Exception ex) {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                ACLMessage msge = new ACLMessage(ACLMessage.INFORM);
                msge.setLanguage("Portugues");
                msge.addReceiver(new AID("SimuladorCasa", AID.ISLOCALNAME));
                msge.setContent("Simular");
                send(msge);

                block();
            }
        }
        );

    }

    public void play(String filePath) {
        final File file = new File(filePath);

        try (final AudioInputStream in = getAudioInputStream(file)) {

            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final Info info = new Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine line
                    = (SourceDataLine) AudioSystem.getLine(info)) {

                if (line != null) {
                    line.open(outFormat);
                    line.start();
                    stream(getAudioInputStream(outFormat, in), line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException
                | LineUnavailableException
                | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();

        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[4096];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }

}
