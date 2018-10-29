/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;

import java.util.Vector;
import utils.FrameTarefa;
import utils.Pares;
import utils.GerenciadorCasa;
import utils.HistoricoTarefas;

/**
 *
 * @author Luiz
 */
public class ExecutadorTarefa {
    
    static HistoricoTarefas historico = new HistoricoTarefas();

    static String executar(FrameTarefa frame, GerenciadorCasa gc) {
        String resp = new String();
        Pares paux = new Pares();
        Vector<String> comm = new Vector<>();

        if (!GerenciadorCasa.consultarLocal(frame.getLocal())) {
            resp = "Informar Local "+frame.getLocal();
            
            return resp;
        }
        if (GerenciadorCasa.consultarDispositivo(frame.getLocal(), frame.getDispositivo())) {

        } else {
            resp = "informar Dispositivo "+frame.getLocal()+" "+frame.getDispositivo();
            
            return resp;
        }

        if (frame.getTarefa().equals("ContralarDisp")) {
            switch (frame.getAcao()) {

                case "ligar": {
                    if (GerenciadorCasa.verificaValorPropDisp(frame.getDispositivo(), "desligado")) {
                        comm = GerenciadorCasa.obterComm(frame.getDispositivo(), frame.getLocal());
                        if (ControladorDispositivos.executa(comm, "ligar")
                                && GerenciadorCasa.AlterarProp(frame.getDispositivo(), "Estado", "desligado", "ligado")) {
                            
                            historico.insere(frame);
                           resp = "InformarExecucao Sucesso";
                            return resp;
                        } else {
                            resp = "InformarExecucao Falha";
                            
                            return resp;
                        }

                    } else {
                        resp = "InformarValorProp "+frame.getLocal()+" "
                                +frame.getDispositivo()+" ligado";
                            
                            return resp;
                        
                       
                    }
                }

                case "desligar":{
                    if (GerenciadorCasa.verificaValorPropDisp(frame.getDispositivo(), "ligado")) {
                        comm = GerenciadorCasa.obterComm(frame.getDispositivo(), frame.getLocal());
                        if (ControladorDispositivos.executa(comm, "desligar")
                                && GerenciadorCasa.AlterarProp(frame.getDispositivo(), "Estado", "ligado", "ligado")) {
                            
                            historico.insere(frame);
                            resp = "InformarExecucao Sucesso";
                            return resp;
                        } else {
                            resp = "InformarExecucao Falha";
                            
                            return resp;
                        }

                    } else {
                       resp = "InformarValorProp "+frame.getLocal()+" "
                                +frame.getDispositivo()+" desligado";
                            
                            return resp;
                    }
                }

                case "aumentar":

                case "diminuir":

            }

        }

        return null;
    }

}
