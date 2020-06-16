/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Agentes.ReconhecedorVoz;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author felip
 */
public class EscreverAIML {

    public static void escreverAIML(String request, String response) throws IOException {
        request = request.toUpperCase();
        request = StringUtils.stripAccents(request);
        response = StringUtils.stripAccents(response);
        System.out.println("IMPRIME A RESPOSTA " + response);

        try {
            //muda aiml
            String resourcesPath = getResourcesPath();
            resourcesPath = resourcesPath + "\\bots\\conhecimentodialogo\\aiml\\rotinascriadas.aiml";
            String texto = new Scanner(new File(resourcesPath)).useDelimiter("\\A").next();
            texto = texto.substring(0, texto.length() - 8);
            texto = texto
                    + "<category><pattern>" + request + "</pattern>\n"
                    + "<template>" + response + "</template>\n"
                    + "</category>\n"
                    + " </aiml>";
            FileOutputStream fileOut = new FileOutputStream(resourcesPath);
            fileOut.write(texto.getBytes());
            //muda aimlif
            String resourcesPath2 = getResourcesPath();
            resourcesPath2 = resourcesPath2 + "\\bots\\conhecimentodialogo\\aimlif\\rotinascriadas.aiml.csv";
            String texto2 = new Scanner(new File(resourcesPath2)).useDelimiter("\\A").next();
            texto2 = texto2 + "0," + request + ",*,*," + response + ",rotinascriadas.aiml";
            //System.out.println(texto2);
            FileOutputStream fileOut2 = new FileOutputStream(resourcesPath2);
            fileOut2.write(texto2.getBytes());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReconhecedorVoz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void GravaComando(String local, String dispositivo, String acao) throws IOException {
        local = StringUtils.stripAccents(local.toUpperCase());
        dispositivo = StringUtils.stripAccents(dispositivo.toUpperCase());
        acao = StringUtils.stripAccents(acao.toUpperCase());
        String resourcesPath2 = getResourcesPath();
        resourcesPath2 = resourcesPath2 + "\\resources\\historico_comandos.txt";
        String texto2 = new Scanner(new File(resourcesPath2)).useDelimiter("\\A").next();
        texto2 = texto2 + acao + "," + dispositivo + "," + local + ",";
        FileOutputStream fileOut2 = new FileOutputStream(resourcesPath2);
        fileOut2.write(texto2.getBytes());

    }

    public String[] LeComando(String quantidade) throws IOException {
        String[] retorno = new String[2];
        int qtd = 0;
        String resourcesPath2 = getResourcesPath();
        String aux = "";
        resourcesPath2 = resourcesPath2 + "\\resources\\historico_comandos.txt";
        String texto2 = new Scanner(new File(resourcesPath2)).useDelimiter("\\A").next();

        String[] palavras = texto2.split(",");
        System.out.println("DADOS LIDOS DO ARQUIVO " + texto2);
        if (quantidade.contains("1")) {
            qtd = 1;
        } else if (quantidade.contains("2")) {
            qtd = 2;
        } else if (quantidade.contains("3")) {
            qtd = 3;
        } else {
            retorno[0] = "ERRO CRIACAO DE ROTINA";
            retorno[1] = "0";
            return retorno;
        }
        System.out.println("VALOR DO QTD " + qtd);
        System.out.println("TAMANHO DO PALAVRAS " + palavras.length);
        if (palavras.length < qtd * 3) {
            for (int i = palavras.length; i > 0; i--) {
                if ((i % 3 == 0)&& (i != palavras.length)) {
                    aux = aux + " E ";
                }
                else {
                    aux = aux + " ";
                }
                aux = aux+palavras[palavras.length - i];
            }
            System.out.println("PARTE DA ROTINA " + aux);
            retorno[0] = aux;
            retorno[1] = Integer.toString((palavras.length) / 3);
            return retorno;
        } else {
            for (int i = qtd * 3; i > 0; i--) {
                if ((i % 3 == 0)&& (i != qtd*3)) {
                    aux = aux + " E ";
                }
                else {
                    aux = aux + " ";
                }
                aux = aux+palavras[palavras.length - i];
            }
            System.out.println("DADOS QUE SERAO SALVOS NA ROTINA " + aux);
            retorno[0] = aux;
            retorno[1] = Integer.toString(qtd);
            return retorno;
        }
    }

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + "\\src\\main";
        return resourcesPath;
    }

}
