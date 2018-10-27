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

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + "\\src\\main";
        return resourcesPath;
    }

}
