//MUDAR A LOGICA

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import static jade.lang.acl.ACLMessage.INFORM;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.*;
import java.util.*;
import utils.PilhaDialogo;

/**
 *
 * @author Lidera Consultoria
 */
public class Gerenciador extends Agent {

    private String resposta = new String();

    protected void setup() {
        FrameTarefa frametarefa = new FrameTarefa();
        Vector<FrameTarefa> vetorframestarefa = new Vector<>();
        GerenciadorCasa gc = new GerenciadorCasa();
        FrameTarefa frametarefaconfirmacao = new FrameTarefa();

        System.out.println("Gerenciador incializado");

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                try {
                    gc.init();
                } catch (IOException ex) {
                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {

                    GerenciadorCasa.consultarTodosDispsitivos();
                    GerenciadorCasa.consultarTodosLocais();

                } catch (IOException ex) {
                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*   try {
                        GerenciadorCasa.consultarLocalporDisp(vetorframestarefa.get(vetorframestarefa.size() - 1).getLocal());
                    } catch (IOException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        GerenciadorCasa.consultarLocaisdeDisp(vetorframestarefa.get(vetorframestarefa.size() - 1).getDispositivo());
                    } catch (IOException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                ACLMessage msgr = receive();

                /* if (msgr != null) {

                 
 try {
                        Vector<Pares> pares = (Vector<Pares>) msgr.getContentObject();
                        //transforma vetor de pares em frame
                        for (int i = 0; i < pares.size(); i++) {
                            System.out.println("teste gerenciador argumento: " + pares.get(i).getArgs() + "--- intencao: " + pares.get(i).getIntencao());
                            System.out.println(pares.get(i).getIntencao());
                            if ("Informarcomando".equals(pares.get(i).getIntencao())) {
                               frametarefa.setTarefa(pares.get(i).getArgs());
                            } else if ("Informaracao".equals(pares.get(i).getIntencao())) {
                                frametarefa.setAcao(pares.get(i).getArgs());
                            } else if ("Informardispositivo".equals(pares.get(i).getIntencao())) {
                                frametarefa.setDispositivo(pares.get(i).getArgs());
                            } else if ("Informarlocal".equals(pares.get(i).getIntencao())) {
                                frametarefa.setLocal(pares.get(i).getArgs());
                            }
                        }
                        pares = null;
                        

                    } catch (UnreadableException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                //imprime frame
                 frametarefa.setTarefa("Controlardispositivos");
                frametarefa.setAcao("ligar");
                frametarefa.setDispositivo("ventilador");
                frametarefa.setLocal("sala");
                System.out.println("teste frame tarefa" + frametarefa.getTarefa());
                System.out.println("teste frame Acao" + frametarefa.getAcao());
                System.out.println("teste frame dispositivo " + frametarefa.getDispositivo());
                System.out.println("teste frame local " + frametarefa.getLocal());
                //adiciona frame ao vetor de frames
                vetorframestarefa.add(frametarefa);
                GerenciadorCasa.consultarDispositivo(vetorframestarefa.elementAt(0).getLocal(),
                        vetorframestarefa.elementAt(0).getDispositivo());
                GerenciadorCasa.verificaValorPropDisp("lampada", "sala", "ligar");
                }*/
                frametarefa.setTarefa("Controlardispositivos");
                frametarefa.setAcao("ligar");
                frametarefa.setDispositivo("ventilador");
                frametarefa.setLocal("sala");
                System.out.println("teste frame tarefa " + frametarefa.getTarefa());
                System.out.println("teste frame Acao " + frametarefa.getAcao());
                System.out.println("teste frame dispositivo " + frametarefa.getDispositivo());
                System.out.println("teste frame local " + frametarefa.getLocal());
                //adiciona frame ao vetor de frames
                vetorframestarefa.add(frametarefa);
                if (vetorframestarefa != null) {
                    /*             Lógica de execução:
                 
                 1º Verificar se frame Completo
                    Opções: 
                        Completo -> 
                            2º Consultar existência do dispositivo no local informado
                     */
                    if (vetorframestarefa.elementAt(0).getTarefa().equals("Controlardispositivos")) {
                        if (vetorframestarefa.elementAt(0).getAcao() != null && vetorframestarefa.elementAt(0).getDispositivo() != null
                                && vetorframestarefa.elementAt(0).getLocal() != null) {
                            if (GerenciadorCasa.consultarDispositivo(vetorframestarefa.elementAt(0).getLocal(),
                                    vetorframestarefa.elementAt(0).getDispositivo())) {
                                /*                                  
                               Dispsitivo existe ->
                        3ºVerificar se ação é possível
                                        Se não for possível -> retornar o por que não foi possível executar e aguarda novo 
                                                                comando.
                 
                                        Se possível->
                                            4º executa ação e retorna que a ação foi executada, aguarda por novo comando
                                 */
                                if (vetorframestarefa.elementAt(0).getAcao().equals("ligar")) {
                                    try {
                                        if ("desligado".equals(GerenciadorCasa.EstadoDispositivo(vetorframestarefa.elementAt(0).getDispositivo(),
                                                vetorframestarefa.elementAt(0).getLocal()))) {
                                            GerenciadorCasa.AlterarProp(vetorframestarefa.elementAt(0).getDispositivo(),
                                                    vetorframestarefa.elementAt(0).getLocal(), "Estado", "desligado", "ligado");
                                            resposta = "Foi ligado " + vetorframestarefa.elementAt(0).getDispositivo()
                                                    + " " + vetorframestarefa.elementAt(0).getLocal();
                                            vetorframestarefa.removeElementAt(0);

                                        } else {
                                            resposta = "Nao foi ligado " + vetorframestarefa.elementAt(0).getDispositivo()
                                                    + " " + vetorframestarefa.elementAt(0).getLocal();
                                            vetorframestarefa.removeElementAt(0);

                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    if (vetorframestarefa.elementAt(0).getAcao().equals("desligar")) {
                                        try {
                                            if ("ligado".equals(GerenciadorCasa.EstadoDispositivo(vetorframestarefa.elementAt(0).getDispositivo(),
                                                    vetorframestarefa.elementAt(0).getLocal()))) {
                                                GerenciadorCasa.AlterarProp(vetorframestarefa.elementAt(0).getDispositivo(),
                                                        vetorframestarefa.elementAt(0).getLocal(), "Estado", "ligado", "desligado");
                                                resposta = "Foi desligado " + vetorframestarefa.elementAt(0).getDispositivo()
                                                        + " " + vetorframestarefa.elementAt(0).getLocal();
                                                vetorframestarefa.removeElementAt(0);
                                            } else {
                                                resposta = "Nao foi desligado " + vetorframestarefa.elementAt(0).getDispositivo()
                                                        + " " + vetorframestarefa.elementAt(0).getLocal();
                                                vetorframestarefa.removeElementAt(0);

                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {
                                        if (vetorframestarefa.elementAt(0).getAcao().equals("aumentar")) {
                                        } else {
                                            if (vetorframestarefa.elementAt(0).getAcao().equals("diminuir")) {
                                            }
                                        }
                                    }
                                }
                            } else {/*
                             Dispositivo Não existe no local informado
                                    3ºVerificar se local existe:
                                        Local existe-> retornar que dispositivo não existe no local e não foi possível 
                                        realizar ação, dizer que aguarda novo comando;*/
                                if (GerenciadorCasa.consultarLocal(vetorframestarefa.elementAt(0).getLocal())) {
                                    resposta = "Dispositivo " + vetorframestarefa.elementAt(0).getDispositivo()
                                            + " nao existe no local informado";
                                    vetorframestarefa.removeElementAt(0);
                                    System.out.println(resposta);
                                    
                                }/* Local não existe -> caso frame falta local-> 
                                          Verificar se existe o dispositivo pedido em outros locais:                                                               
                                 */ else {
                                    try {
                                        Vector<String> locais = GerenciadorCasa.consultarLocaisdeDisp(
                                                vetorframestarefa.elementAt(0).getDispositivo());
                                        if (locais.size() == 1) {/*
                                            Existe 1 -> Retornar que dispositivo encontrado em outro local, questionar se
                                                            deseja executar ação nesse dispositivo encontrado:
                                                                Salvar frame e aguardar consfirmação de ação:
                                                                    Se comando pra executar -> realiza ação com frame guardado
                                                                                                retorna que ação foi executada
                                                                                                aguarda por novo  comando.
                 
                                                                    Não executar -> esquece o frame e aguarda novo comando.*/
                                            resposta = "Dispositivo existe em " + locais.elementAt(0) + " confirmar tarefa";
                                            frametarefaconfirmacao.setAcao(vetorframestarefa.elementAt(0).getAcao());
                                            frametarefaconfirmacao.setDispositivo(vetorframestarefa.elementAt(0).getDispositivo());
                                            frametarefaconfirmacao.setLocal(locais.elementAt(0));
                                            System.out.println(resposta);
                                            //Falta a parte da confirmação precisa pensar como implementar
                                        } else {
                                            if (locais.size() > 1) {/*                  
                                                Existe mais de 1 -> Retornar que o dispositivo não foi encontrado no local
                                                                    pedido, mas exite o dispositivo nos outros locais. 
                                                                    Sugerir tentar novamente ação para um dos dispositivos 
                                                                    nos locais onde ele foi encontrado. aguardar novo comando.*/
                                                resposta = "Dispositivo nao encontrado no local mas encontrado em";
                                                for (int i = 0; i < locais.size(); i++) {
                                                    resposta = resposta + " " + locais.elementAt(i);

                                                }
                                                System.out.println(resposta);
                                            } else {/*não existe -> retornar que o dispositvo pedido não existe em nenhum local,
                                                                aguardar por novo comando.*/
                                                resposta = "Dispositivo nao existe em nenhum local";
                                                System.out.println(resposta);
                                            }
                                        }

                                    } catch (IOException ex) {
                                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }
                            }
                        }
                        /*                             
                        Não Completo->
                            2º testar o que falta no frame:
                                
                                Falta dispositivo:
                                Consultar existencia de local:
                                    -> se existe:
                                    3º buscar por dispositivos no local:
                                        1 dispositivo -> verificar se ação possível no dispositivo:
                                                            se não possível ->retornar que comando não entendido e que aguarda
                                                                                novo comando. Aguardar por novo comando 
                                                                                esquecer frame.
                                        
                                        + de 1 dispositivo -> Retornar os dispositivos que exitem no local. 
                                                                    Sugerir tentar novamente ação para um dos dispositivos 
                                                                    no local. Guardar frame incompleto e aguardar novo comando
                                                                    com o dispositivo desejado.
                 
                                        não há dispositivo -> retornar que não existem dispositivos no local escolhido e que 
                                                                aguarda novo comando. Aguardar novo comando.
                                    -> se nao existe -> caso falta dois quaisquer: retornar que dispositivo não entendido e local nao existe
                                                        solicitar novo comando.
                                
                                Falta Local:
                                      3º Verificar se existe o dispositivo pedido em outros locais: 
                                                não existe -> retornar que o dispositvo pedido não existe em nenhum local,
                                                                aguardar por novo comando.
                 
                                                Existe 1 -> Retornar que dispositivo encontrado em outro local, questionar se
                                                            deseja executar ação nesse dispositivo encontrado:
                                                                Salvar frame e aguardar consfirmação de ação:
                                                                    Se comando pra executar -> realiza ação com frame guardado
                                                                                                retorna que ação foi executada
                                                                                                aguarda por novo  comando.
                 
                                                                    Não exxecutar -> esquece o frame e aguarda novo comando.
                 
                                                Existe mais de 1 -> Retornar que o dispositivo não foi encontrado no local
                                                                    pedido, mas exite o dispositivo nos outros locais. 
                                                                    Sugerir tentar novamente ação para um dos dispositivos 
                                                                    nos locais onde ele foi encontrado. aguardar novo comando.
                                                                    
                 
                                Falta Ação:
                                    Verificar se dispositivo solicitado existe no local indicado:
                                        se existe -> guardar frame incompleto e perguntar qual ação deseja ser executa
                                                       no dispositivo. Aguardar informação de ação para completar o Frame.
                    
                                        se não existe -> equivalente ao caso falta dois quaisquer:retornar que comando não
                                                         entendido e dispositivo nao encontrado, solicitar novo comando,
                                                         aguardar por novo comando. 
                 
                                Falta dois quaisquer: retornar que comando não entendido, solicitar novo comando,
                                                        aguardar por novo comando.
                         */
                    } else {
                        if (vetorframestarefa.elementAt(0).getTarefa().equals("confirmacao")) {

                        } else {
                            if (vetorframestarefa.elementAt(0).getTarefa().equals("criarcomando")) {

                            }
                        }
                    }
                }
                ACLMessage msge = new ACLMessage(INFORM);
                msge.setLanguage("Portugues");
                msge.addReceiver(new AID("GeradorLN", AID.ISLOCALNAME));
                msge.setContent(resposta);
                send(msge);
                try {
                    gc.close();
                } catch (IOException ex) {
                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                }

                //}
                // else {
                block();
                //}

            }
        }
        );
    }
}
