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
import utils.EscreverAIML;

/**
 *
 * @author Lidera Consultoria
 */
public class Gerenciador extends Agent {

    private String resposta = new String();
    private int modocriacao = 0;
    private String nome_rotina = new String();
    private int flag_numeral = 0;
    private int flag_confirmacao = 0;

    protected void setup() {
        FrameTarefa frametarefa = new FrameTarefa();
        Vector<FrameTarefa> vetorframestarefa = new Vector<>();
        GerenciadorCasa gc = new GerenciadorCasa();
        FrameTarefa frametarefaconfirmacao = new FrameTarefa();
        Vector<FrameTarefa> vetorframestarefaconfirmacao = new Vector<>();
        EscreverAIML escreveAIML = new EscreverAIML();
        HistoricoTarefas historicotarefas = new HistoricoTarefas();
        historicotarefas.init();
        try {
            gc.init();
        } catch (IOException ex) {
            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
        }

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {

                ACLMessage msgr = receive();

                if (msgr != null) {

                    try {
                        Vector<Pares> pares = (Vector<Pares>) msgr.getContentObject();
                        //transforma vetor de pares em frame
                        for (int i = 0; i < pares.size(); i++) {
                            if ("Informarcomando".equals(pares.get(i).getIntencao())) {
                                frametarefa.setTarefa(pares.get(i).getArgs());
                            } else if ("Informaracao".equals(pares.get(i).getIntencao())) {
                                frametarefa.setAcao(pares.get(i).getArgs());
                            } else if ("Informardispositivo".equals(pares.get(i).getIntencao())) {
                                frametarefa.setDispositivo(pares.get(i).getArgs());
                            } else if ("Informarlocal".equals(pares.get(i).getIntencao())) {
                                frametarefa.setLocal(pares.get(i).getArgs());
                            } else if ("informarintencao".equals(pares.get(i).getIntencao())) {
                                resposta = "Informe o nome da rotina.";
                                modocriacao = 1;
                            } else if (("Informarnome".equals(pares.get(i).getIntencao())) && (modocriacao == 1)) {
                                nome_rotina = pares.get(i).getArgs();
                                resposta = "Informe quantos comandos adicionar";
                                modocriacao = 2;
                            } else if (("Informarnumeral".equals(pares.get(i).getIntencao())) && (modocriacao == 2)) {
                                modocriacao = 0;
                                if ("ERRO CRIACAO DE ROTINA".equals(escreveAIML.LeComando(pares.get(i).getArgs())[0])) {
                                    resposta = "Erro na criacao de rotinas";
                                } else {
                                    escreveAIML.escreverAIML(nome_rotina, escreveAIML.LeComando(pares.get(i).getArgs())[0]);

                                    resposta = "A rotina com " + escreveAIML.LeComando(pares.get(i).getArgs())[1] + " comandos";
                                }
                                flag_numeral = 1;
                            } else if (("Informarconfirmacao".equals(pares.get(i).getIntencao())) && (vetorframestarefaconfirmacao.size() != 0)) {
                                if ((vetorframestarefaconfirmacao.elementAt(0).getAcao() != null) && (vetorframestarefaconfirmacao.elementAt(0).getDispositivo() != null) && (vetorframestarefaconfirmacao.elementAt(0).getLocal() != null)) {

                                    flag_confirmacao = 1;
                                    if ("sim".equals(pares.get(i).getArgs())) {
//                                        ----------------------------------------------------------
                                        if (vetorframestarefaconfirmacao.elementAt(0).getAcao().equals("ligar")) {
                                            try {
                                                if ("desligado".equals(GerenciadorCasa.EstadoDispositivo(vetorframestarefaconfirmacao.elementAt(0).getDispositivo(),
                                                        vetorframestarefaconfirmacao.elementAt(0).getLocal()))) {
                                                    GerenciadorCasa.AlterarProp(vetorframestarefaconfirmacao.elementAt(0).getDispositivo(),
                                                            vetorframestarefaconfirmacao.elementAt(0).getLocal(), "Estado", "desligado", "ligado");
                                                    resposta = "Foi ligado " + vetorframestarefaconfirmacao.elementAt(0).getDispositivo()
                                                            + " em " + vetorframestarefaconfirmacao.elementAt(0).getLocal();
                                                    /*ADICIONAR A FUNCAO DE SALVAR HISTORICO*/
                                                    escreveAIML.GravaComando(vetorframestarefaconfirmacao.elementAt(0).getLocal(), vetorframestarefaconfirmacao.elementAt(0).getDispositivo(), vetorframestarefaconfirmacao.elementAt(0).getAcao());

                                                    vetorframestarefaconfirmacao.removeElementAt(0);

                                                } else {
                                                    resposta = "Nao foi ligado " + vetorframestarefaconfirmacao.elementAt(0).getDispositivo()
                                                            + " em " + vetorframestarefaconfirmacao.elementAt(0).getLocal();
                                                    vetorframestarefaconfirmacao.removeElementAt(0);

                                                }
                                            } catch (IOException ex) {
                                                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            if (vetorframestarefaconfirmacao.elementAt(0).getAcao().equals("desligar")) {
                                                try {
                                                    if ("ligado".equals(GerenciadorCasa.EstadoDispositivo(vetorframestarefaconfirmacao.elementAt(0).getDispositivo(),
                                                            vetorframestarefaconfirmacao.elementAt(0).getLocal()))) {
                                                        GerenciadorCasa.AlterarProp(vetorframestarefaconfirmacao.elementAt(0).getDispositivo(),
                                                                vetorframestarefaconfirmacao.elementAt(0).getLocal(), "Estado", "ligado", "desligado");
                                                        resposta = "Foi desligado " + vetorframestarefaconfirmacao.elementAt(0).getDispositivo()
                                                                + " em " + vetorframestarefaconfirmacao.elementAt(0).getLocal();
                                                        /*ADICIONAR A FUNCAO DE SALVAR HISTORICO*/
                                                        escreveAIML.GravaComando(vetorframestarefaconfirmacao.elementAt(0).getLocal(), vetorframestarefaconfirmacao.elementAt(0).getDispositivo(), vetorframestarefaconfirmacao.elementAt(0).getAcao());

                                                        vetorframestarefaconfirmacao.removeElementAt(0);
                                                    } else {
                                                        resposta = "Nao foi desligado " + vetorframestarefaconfirmacao.elementAt(0).getDispositivo()
                                                                + " em " + vetorframestarefaconfirmacao.elementAt(0).getLocal();

                                                        vetorframestarefaconfirmacao.removeElementAt(0);

                                                    }
                                                } catch (IOException ex) {
                                                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                                }

                                            } else {
                                                if (vetorframestarefaconfirmacao.elementAt(0).getAcao().equals("aumentar")
                                                        || vetorframestarefaconfirmacao.elementAt(0).getAcao().equals("diminuir")) {
                                                    try {
                                                        resposta = GerenciadorCasa.AlterarPropVolume(vetorframestarefaconfirmacao.elementAt(0).getDispositivo(),
                                                                vetorframestarefaconfirmacao.elementAt(0).getLocal(), vetorframestarefaconfirmacao.elementAt(0).getAcao());
//                                                 
                                                        escreveAIML.GravaComando(vetorframestarefaconfirmacao.elementAt(0).getLocal(), vetorframestarefaconfirmacao.elementAt(0).getDispositivo(), vetorframestarefaconfirmacao.elementAt(0).getAcao());

                                                        vetorframestarefaconfirmacao.removeElementAt(0);
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                                    }

                                                }
                                            }
                                        }

//                                         -------------------------------
                                    } else {
                                        resposta = "Comando nao executado";
                                        vetorframestarefaconfirmacao.removeElementAt(0);

                                    }
                                }
                            }
                        }
                        if (((frametarefa.getAcao() != null) || (frametarefa.getDispositivo() != null) || (frametarefa.getLocal() != null))) {

                            vetorframestarefa.add(frametarefa);
                        }
                        pares = null;

                    } catch (UnreadableException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if ((modocriacao == 0) && (flag_numeral == 0) && (flag_confirmacao == 0)) {
                        if (vetorframestarefa.size() != 0) {
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
                                                            + " em " + vetorframestarefa.elementAt(0).getLocal();
                                                    /*ADICIONAR A FUNCAO DE SALVAR HISTORICO*/
//                                                historicotarefas.insere(vetorframestarefa.elementAt(0));
                                                    escreveAIML.GravaComando(vetorframestarefa.elementAt(0).getLocal(), vetorframestarefa.elementAt(0).getDispositivo(), vetorframestarefa.elementAt(0).getAcao());

                                                    vetorframestarefa.removeElementAt(0);

                                                } else {
                                                    resposta = "Nao foi ligado " + vetorframestarefa.elementAt(0).getDispositivo()
                                                            + " em " + vetorframestarefa.elementAt(0).getLocal();
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
                                                                + " em " + vetorframestarefa.elementAt(0).getLocal();
                                                        /*ADICIONAR A FUNCAO DE SALVAR HISTORICO*/
                                                        escreveAIML.GravaComando(vetorframestarefa.elementAt(0).getLocal(), vetorframestarefa.elementAt(0).getDispositivo(), vetorframestarefa.elementAt(0).getAcao());

                                                        vetorframestarefa.removeElementAt(0);
                                                    } else {
                                                        resposta = "Nao foi desligado " + vetorframestarefa.elementAt(0).getDispositivo()
                                                                + " em " + vetorframestarefa.elementAt(0).getLocal();

                                                        vetorframestarefa.removeElementAt(0);

                                                    }
                                                } catch (IOException ex) {
                                                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                                }

                                            } else {
                                                if (vetorframestarefa.elementAt(0).getAcao().equals("aumentar")
                                                        || vetorframestarefa.elementAt(0).getAcao().equals("diminuir")) {
                                                    if (vetorframestarefa.elementAt(0).getDispositivo().equals("lampada")) {
                                                        resposta = "lampada nao suporta funcao";
                                                        vetorframestarefa.removeElementAt(0);
                                                    } else {

                                                        try {
                                                            resposta = GerenciadorCasa.AlterarPropVolume(vetorframestarefa.elementAt(0).getDispositivo(),
                                                                    vetorframestarefa.elementAt(0).getLocal(), vetorframestarefa.elementAt(0).getAcao());
//                                                
                                                            escreveAIML.GravaComando(vetorframestarefa.elementAt(0).getLocal(), vetorframestarefa.elementAt(0).getDispositivo(), vetorframestarefa.elementAt(0).getAcao());

                                                            vetorframestarefa.removeElementAt(0);
                                                        } catch (IOException ex) {
                                                            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                                        }

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
                                                    + " nao existe no local " + vetorframestarefa.elementAt(0).getLocal();
                                            vetorframestarefa.removeElementAt(0);

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
                                                    resposta = "Dispositivo " + vetorframestarefa.elementAt(0).getDispositivo()
                                                            + " existe em " + locais.elementAt(0) + " confirmar tarefa";
                                                    frametarefaconfirmacao.setAcao(vetorframestarefa.elementAt(0).getAcao());
                                                    frametarefaconfirmacao.setDispositivo(vetorframestarefa.elementAt(0).getDispositivo());
                                                    frametarefaconfirmacao.setLocal(locais.elementAt(0));
                                                    vetorframestarefaconfirmacao.add(frametarefaconfirmacao);

                                                    System.out.println(resposta);
                                                    vetorframestarefa.removeElementAt(0);
                                                    //Falta a parte da confirmação precisa pensar como implementar
                                                } else {
                                                    if (locais.size() > 1) {/*                  
                                                Existe mais de 1 -> Retornar que o dispositivo não foi encontrado no local
                                                                    pedido, mas exite o dispositivo nos outros locais. 
                                                                    Sugerir tentar novamente ação para um dos dispositivos 
                                                                    nos locais onde ele foi encontrado. aguardar novo comando.*/
                                                        resposta = "Dispositivo " + vetorframestarefa.elementAt(0).getDispositivo() + " "
                                                                + "nao encontrado no local mas encontrado em";
                                                        for (int i = 0; i < locais.size(); i++) {
                                                            if (i != locais.size()) {
                                                                resposta = resposta + " " + locais.elementAt(i);
                                                            } else {
                                                                if (i == locais.size()) {
                                                                    resposta = resposta + " e " + locais.elementAt(i);
                                                                }
                                                            }
                                                        }
                                                        System.out.println(resposta);
                                                        vetorframestarefa.removeElementAt(0);
                                                    } else {/*não existe -> retornar que o dispositvo pedido não existe em nenhum local,
                                                                aguardar por novo comando.*/
                                                        resposta = "Dispositivo " + vetorframestarefa.elementAt(0).getDispositivo() + " nao existe em nenhum local";
                                                        System.out.println(resposta);
                                                        vetorframestarefa.removeElementAt(0);
                                                    }
                                                }

                                            } catch (IOException ex) {
                                                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                            }

                                        }
                                    }
                                } else {
                                    /*                             
                        Não Completo->
                            2º testar o que falta no frame:
                                
                                Falta dispositivo:
                                Consultar existencia de local:
                                    -> se existe:
                                    3º buscar por dispositivos no local:*/
                                    if (vetorframestarefa.elementAt(0).getAcao() != null && vetorframestarefa.elementAt(0).getDispositivo() == null
                                            && vetorframestarefa.elementAt(0).getLocal() != null) {
                                        if (GerenciadorCasa.consultarLocal(vetorframestarefa.elementAt(0).getLocal())) {
                                            try {
                                                Vector<String> dispositivos = GerenciadorCasa.consultarDispsNoLocal(
                                                        vetorframestarefa.elementAt(0).getLocal());
                                                if (dispositivos.isEmpty()) {/*não há dispositivo -> retornar que não existem dispositivos no local escolhido e que 
                                                                aguarda novo comando. Aguardar novo comando.*/

                                                    resposta = "Nao existe dispositivos no local " + vetorframestarefa.elementAt(0).getLocal();
                                                    System.out.println(resposta);
                                                    vetorframestarefa.removeElementAt(0);
                                                } else {
                                                    if (dispositivos.size() == 1) {
                                                        /* 1 dispositivo -> verificar se ação possível no dispositivo:
                                                            se possível -> perguntar se deseja executar a tarefa, salvar frame 
                                                                            completo e aguardar confirmação.
                                                            se não possível ->retornar que comando não entendido e que aguarda
                                                                                novo comando. Aguardar por novo comando 
                                                                                esquecer frame.*/
                                                        if (vetorframestarefa.elementAt(0).getAcao().equals("ligar")) {
                                                            try {
                                                                if ("desligado".equals(GerenciadorCasa.EstadoDispositivo(vetorframestarefa.elementAt(0).getDispositivo(),
                                                                        vetorframestarefa.elementAt(0).getLocal()))) {
                                                                    frametarefaconfirmacao.setAcao(vetorframestarefa.elementAt(0).getAcao());
                                                                    frametarefaconfirmacao.setDispositivo(dispositivos.elementAt(0));
                                                                    frametarefaconfirmacao.setLocal(vetorframestarefa.elementAt(0).getLocal());
                                                                    resposta = "Dispositivo nao entendido. Mas encontrado outro. Deseja " + frametarefaconfirmacao.getAcao() + " o " + frametarefaconfirmacao.getDispositivo()
                                                                            + " no local " + frametarefaconfirmacao.getLocal();
                                                                    vetorframestarefaconfirmacao.add(frametarefaconfirmacao);
                                                                    System.out.println(resposta);
                                                                    vetorframestarefa.removeElementAt(0);

                                                                } else {
                                                                    resposta = "Comando nao entendido, faltou dispositivo";
                                                                    vetorframestarefa.removeElementAt(0);

                                                                }
                                                            } catch (IOException ex) {
                                                                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                                            }
                                                        } else {
                                                            if (vetorframestarefa.elementAt(0).getAcao().equals("desligar")) {
                                                                try {
                                                                    if ("ligado".equals(GerenciadorCasa.EstadoDispositivo(dispositivos.elementAt(0),
                                                                            vetorframestarefa.elementAt(0).getLocal()))) {
                                                                        frametarefaconfirmacao.setAcao(vetorframestarefa.elementAt(0).getAcao());
                                                                        frametarefaconfirmacao.setDispositivo(dispositivos.elementAt(0));
                                                                        frametarefaconfirmacao.setLocal(vetorframestarefa.elementAt(0).getLocal());
                                                                        vetorframestarefaconfirmacao.add(frametarefaconfirmacao);
                                                                        resposta = "Dispositivo nao entendido. Mas encontrado outro. Deseja " + frametarefaconfirmacao.getAcao() + " o " + frametarefaconfirmacao.getDispositivo()
                                                                                + " no local " + frametarefaconfirmacao.getLocal();
                                                                        System.out.println(resposta);
                                                                        vetorframestarefa.removeElementAt(0);
                                                                    } else {
                                                                        resposta = "Comando nao entendido. faltou dispositivo";
                                                                        vetorframestarefa.removeElementAt(0);

                                                                    }
                                                                } catch (IOException ex) {
                                                                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                                                }

                                                            } else {
                                                                if ((vetorframestarefa.elementAt(0).getAcao().equals("aumentar")
                                                                        || vetorframestarefa.elementAt(0).getAcao().equals("diminuir"))
                                                                        && (dispositivos.elementAt(0).equals("som")
                                                                        || dispositivos.elementAt(0).equals("televisao"))) {
                                                                    frametarefaconfirmacao.setAcao(vetorframestarefa.elementAt(0).getAcao());
                                                                    frametarefaconfirmacao.setDispositivo(dispositivos.elementAt(0));
                                                                    frametarefaconfirmacao.setLocal(vetorframestarefa.elementAt(0).getLocal());
                                                                    vetorframestarefaconfirmacao.add(frametarefaconfirmacao);
                                                                    resposta = "Dispositivo nao entendido. Mas encontrado outro. Deseja " + frametarefaconfirmacao.getAcao() + " o volume do " + frametarefaconfirmacao.getDispositivo()
                                                                            + " do local " + frametarefaconfirmacao.getLocal();
                                                                    System.out.println(resposta);
                                                                    vetorframestarefa.removeElementAt(0);
                                                                } else {
                                                                    resposta = "Comando nao entendido, faltou dispositivo";
                                                                    vetorframestarefa.removeElementAt(0);

                                                                }
                                                            }
                                                        }

                                                    } else {
                                                        /*   + de 1 dispositivo -> Retornar os dispositivos que exitem no local. 
                                                                    Sugerir tentar novamente ação para um dos dispositivos 
                                                                    no local. solicitar repetir comando para algum dos disps.
                                                         */
                                                        resposta = "Dispositivo nao entendido. no local " + vetorframestarefa.elementAt(0).getLocal() + " encontrados os dispositivos ";
                                                        for (int i = 0; i < dispositivos.size(); i++) {
                                                            if (i != dispositivos.size()) {
                                                                resposta = resposta + " " + dispositivos.elementAt(i);
                                                            } else {
                                                                if (i == dispositivos.size()) {
                                                                    resposta = resposta + " e " + dispositivos.elementAt(i);
                                                                }
                                                            }
                                                        }
                                                        System.out.println(resposta);
                                                        vetorframestarefa.removeElementAt(0);
                                                    }
                                                }

                                            } catch (IOException ex) {
                                                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {/*-> se nao existe -> caso falta dois quaisquer: retornar que dispositivo não entendido e local nao existe
                                                        solicitar novo comando.*/
                                            resposta = "Dispositivo nao entendido e o local " + vetorframestarefa.elementAt(0).getLocal() + " informado nao existe";
                                            System.out.println(resposta);
                                            vetorframestarefa.removeElementAt(0);
                                        }
                                    } else {/* Falta Local:
                                      3º Verificar se existe o dispositivo pedido em outros locais: */
 /*
                                                não existe -> retornar que o dispositvo pedido não existe em nenhum local,
                                                                aguardar por novo comando.*/

                                        if (vetorframestarefa.elementAt(0).getAcao() != null && vetorframestarefa.elementAt(0).getDispositivo() != null
                                                && vetorframestarefa.elementAt(0).getLocal() == null) {
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
                                                    resposta = "Local nao entendido. Dispositivo " + vetorframestarefa.elementAt(0).getDispositivo() + " encontrado em " + locais.elementAt(0) + " confirmar tarefa";
                                                    frametarefaconfirmacao.setAcao(vetorframestarefa.elementAt(0).getAcao());
                                                    frametarefaconfirmacao.setDispositivo(vetorframestarefa.elementAt(0).getDispositivo());
                                                    frametarefaconfirmacao.setLocal(locais.elementAt(0));
                                                    vetorframestarefaconfirmacao.add(frametarefaconfirmacao);
                                                    System.out.println(resposta);
                                                    vetorframestarefa.removeElementAt(0);
                                                    //Falta a parte da confirmação precisa pensar como implementar
                                                } else {
                                                    if (locais.size() > 1) {/*                  
                                                Existe mais de 1 -> Retornar que o dispositivo não foi encontrado no local
                                                                    pedido, mas exite o dispositivo nos outros locais. 
                                                                    Sugerir tentar novamente ação para um dos dispositivos 
                                                                    nos locais onde ele foi encontrado. aguardar novo comando.*/
                                                        resposta = "local nao entendido mas dispositivo " + vetorframestarefa.elementAt(0).getDispositivo() + " encontrado em";
                                                        for (int i = 0; i < locais.size(); i++) {
                                                            if (i != locais.size()) {
                                                                resposta = resposta + " " + locais.elementAt(i);
                                                            } else {
                                                                if (i == locais.size()) {
                                                                    resposta = resposta + " e " + locais.elementAt(i);
                                                                }
                                                            }
                                                        }
                                                        vetorframestarefa.removeElementAt(0);
                                                    } else {/*não existe -> retornar que o dispositvo pedido não existe em nenhum local,
                                                                aguardar por novo comando.*/
                                                        resposta = "local nao entendido e dispositivo " + vetorframestarefa.elementAt(0).getDispositivo() + " nao existe";
                                                        vetorframestarefa.removeElementAt(0);
                                                    }
                                                }

                                            } catch (IOException ex) {
                                                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {/*Falta Ação:
                                    Verificar se dispositivo solicitado existe no local indicado:
                                        se existe -> tv e som : informar que ação nao entendida, solicitar para repetir o comando.
                                    lampada : verificar estado e sugerir ação, aguardar confirmação e executar se for o caso*/
                                            if (vetorframestarefa.elementAt(0).getAcao() == null && vetorframestarefa.elementAt(0).getDispositivo() != null
                                                    && vetorframestarefa.elementAt(0).getLocal() != null) {
                                                if (GerenciadorCasa.consultarDispositivo(vetorframestarefa.elementAt(0).getLocal(),
                                                        vetorframestarefa.elementAt(0).getDispositivo())) {
                                                    if ("som".equals(vetorframestarefa.elementAt(0).getDispositivo())
                                                            || "televisao".equals(vetorframestarefa.elementAt(0).getDispositivo())) {
                                                        resposta = "acao para " + vetorframestarefa.elementAt(0).getDispositivo()
                                                                + " no local " + vetorframestarefa.elementAt(0).getLocal() + " nao entendida";
                                                    }
                                                    if ("lampada".equals(vetorframestarefa.elementAt(0).getDispositivo())) {
                                                        try {
                                                            if ("ligado".equals(GerenciadorCasa.EstadoDispositivo(vetorframestarefa.elementAt(0).getDispositivo(),
                                                                    vetorframestarefa.elementAt(0).getLocal()))) {
                                                                frametarefaconfirmacao.setAcao("desligar");
                                                                frametarefaconfirmacao.setDispositivo(vetorframestarefa.elementAt(0).getDispositivo());
                                                                frametarefaconfirmacao.setLocal(vetorframestarefa.elementAt(0).getLocal());
                                                                resposta = "Deseja " + frametarefaconfirmacao.getAcao() + " a " + frametarefaconfirmacao.getDispositivo()
                                                                        + " do local " + frametarefaconfirmacao.getLocal();
                                                                vetorframestarefaconfirmacao.add(frametarefaconfirmacao);
//                                                                vetorframestarefa.removeElementAt(0);
                                                            }
                                                            if ("desligado".equals(GerenciadorCasa.EstadoDispositivo(vetorframestarefa.elementAt(0).getDispositivo(),
                                                                    vetorframestarefa.elementAt(0).getLocal()))) {
                                                                frametarefaconfirmacao.setAcao("ligar");
                                                                frametarefaconfirmacao.setDispositivo(vetorframestarefa.elementAt(0).getDispositivo());
                                                                frametarefaconfirmacao.setLocal(vetorframestarefa.elementAt(0).getLocal());
                                                                resposta = "Deseja " + frametarefaconfirmacao.getAcao() + " a " + frametarefaconfirmacao.getDispositivo()
                                                                        + " do local " + frametarefaconfirmacao.getLocal();
                                                                vetorframestarefaconfirmacao.add(frametarefaconfirmacao);
//                                                                vetorframestarefa.removeElementAt(0);
                                                            }
                                                        } catch (IOException ex) {
                                                            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }
                                                } else {
                                                    /*
                                         se não existe -> equivalente ao caso falta dois quaisquer:retornar que comando não
                                                         entendido e dispositivo nao encontrado, solicitar novo comando,
                                                         aguardar por novo comando. */
                                                    resposta = "Acao nao entendida e " + vetorframestarefa.elementAt(0).getDispositivo()
                                                            + " em " + vetorframestarefa.elementAt(0).getLocal() + " nao encontrado";
                                                    vetorframestarefa.removeElementAt(0);
                                                }
                                            } else {
                                                /*
                                Falta dois quaisquer: retornar que comando não entendido, solicitar novo comando,
                                                        aguardar por novo comando.
                                                 */
                                                resposta = "Comando nao entendido";
                                                vetorframestarefa.removeElementAt(0);
                                            }
                                        }
                                    }
                                }

                            } else {
                                if (vetorframestarefa.elementAt(0).getTarefa().equals("confirmacao")) {

                                } else {
                                    if (vetorframestarefa.elementAt(0).getTarefa().equals("criarcomando")) {

                                    }
                                }
                            }
                        }
                    }

//                    PARA OS CASOS QUE NÃO EXECUTA O COMANDO PRECISAMOS APAGAR O FRAME
                    if (vetorframestarefa.size() != 0) {
                        vetorframestarefa.removeAllElements();
                    }

                    frametarefa.setAcao(null);
                    frametarefa.setDispositivo(null);
                    frametarefa.setLocal(null);
                    ACLMessage msge = new ACLMessage(INFORM);

                    msge.setLanguage(
                            "Portugues");
                    msge.addReceiver(
                            new AID("GeradorLN", AID.ISLOCALNAME));
                    msge.setContent(resposta);

                    send(msge);
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    resposta = "NAO ENVIAR NADA";
                    flag_numeral = 0;
                    flag_confirmacao = 0;
                }

                block();

            }
        }
        );
    }
}
