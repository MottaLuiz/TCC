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
/**
 *
 * @author Luiz
 */
public class ExecutadorTarefa {
    

    static Vector <Pares> executar(FrameTarefa frame, GerenciadorCasa gc) {
        Vector<Pares> p = new Vector<>();
        Pares paux = new Pares();
        if (!GerenciadorCasa.consultarLocal(frame.getLocal(), gc.getModel()))
        {
          paux.setIntencao("Confirmar Local");
          paux.setArgs(frame.getLocal());
          p.add(paux);
          return p;
        }
        if ( GerenciadorCasa.consultarDispositivo(frame.getLocal(), frame.getDispositivo(), gc.getModel())){
                        
                    }else{
                        paux.setIntencao("Local");
                        paux.setArgs(frame.getLocal());
                        p.add(paux);
                        paux.setIntencao("Confirmar dispoditivo");
                        paux.setArgs(frame.getDispositivo());
                        p.add(paux);
                        return p;
                    }

        if (frame.getTarefa().equals("ContralarDisp")) {
            switch (frame.getAcao()) {
                
                
                case "ligar":
                {
                    if ( GerenciadorCasa.verificaValorPropDisp(frame.getDispositivo(), "desligado" , gc.getModel())){
                        
                        
                        
                    }else{
                        paux.setIntencao("InformarValorProp");
                        paux.setArgs("ligado");
                        p.add(paux);
                        paux.setIntencao("Confirmar dispoditivo");
                        paux.setArgs(frame.getDispositivo());
                        p.add(paux);
                        paux.setIntencao("Local");
                        paux.setArgs(frame.getLocal());
                        p.add(paux);
                        return p;
                    }
                }
                    
          

                case "desligar":

                case "aumentar":

                case "diminuir":

            }

        }

        return null;
    }

}
