//FUNCIONANDO + TIRAR OS TESTES 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import jade.core.behaviours.CyclicBehaviour;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author Lidera Consultoria
 */
public class ReconhecedorVoz extends Agent {
    //teste

    //private FrameControle teste = new FrameControle();
    /**
     * @param args the command line arguments
     */
    protected void setup() {

        System.out.println("Reconhecedor de voz incializado");
        addBehaviour(new CyclicBehaviour(this) {

            public void action() {
                String aux = "";

                Scanner sc = new Scanner(System.in);
                String msgr = "";
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (!aux.equals("c")) {

                    System.out.println("Digite c para enviar comando:");
                    aux = sc.nextLine();
                }
                float sampleRate = 48000;
                int sampleSizeInBits = 16;
                int channels = 1;
                boolean signed = true;
                boolean bigEndian = true;

                AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
// format of audio file
                AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                // checks if system supports the data line
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Line not supported");
                    System.exit(0);
                }

                TargetDataLine line;
                try {
                    line = (TargetDataLine) AudioSystem.getLine(info);

                    line.open(format);
                    System.out.println("Gravando Comando:");
                    line.start();

                    Thread stopper = new Thread(new Runnable() {
                        
                        @Override
                        public void run() {
                            AudioInputStream ais = new AudioInputStream(line);
                            File currDir = new File(".");
                            String path = currDir.getAbsolutePath();
                            path = path.substring(0, path.length() - 2);
                            //System.out.println(path);
                            String resourcesPath = path + "\\src\\main\\resources\\comando.wav";
                            File wavFile = new File(resourcesPath);
                            try {
                                AudioSystem.write(ais, fileType, wavFile);
                            } catch (IOException ex) {
                                Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                    });
                    stopper.start();
                    Thread.sleep(6000);
                    line.stop();
                    line.close();
                    System.out.println("Comando gravado");

                } catch (LineUnavailableException ex) {
                    Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                }

                try (SpeechClient speech = SpeechClient.create()) {
                    File currDir = new File(".");
                    String path = currDir.getAbsolutePath();
                    path = path.substring(0, path.length() - 2);
                    //System.out.println(path);
                    String resourcesPath = path + "\\src\\main\\resources\\comando.wav";

                    byte[] content = Files.readAllBytes(Path.of(resourcesPath));
                    // Configure request with video media type
                    RecognitionConfig recConfig
                            = RecognitionConfig.newBuilder()
                                    // encoding may either be omitted or must match the value in the file header
                                    .setEncoding(AudioEncoding.LINEAR16)
                                    .setLanguageCode("pt-BR")
                                    // sample rate hertz may be either be omitted or must match the value in the file
                                    // header
                                    .setSampleRateHertz(48000)
                                    .setModel("command_and_search")
                                    .build();

                    RecognitionAudio recognitionAudio
                            = RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(content)).build();

                    RecognizeResponse recognizeResponse = speech.recognize(recConfig, recognitionAudio);
                    // Just print the first result here.
                    SpeechRecognitionResult result = recognizeResponse.getResultsList().get(0);
                    // There can be several alternative transcripts for a given chunk of speech. Just use the
                    // first (most likely) one here.
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    System.out.printf("Transcript : %s\n", alternative.getTranscript());

                    msgr = alternative.getTranscript();
                } catch (IOException ex) {
                    Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(msgr);
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
                System.out.println(
                        "Reconhecedor Finalizado");

                //block();
                // interrompe este comportamento ate que chegue uma nova mensagem
            }
        }
        );
    }

}
