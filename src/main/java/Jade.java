
import jade.core.Agent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lidera Consultoria
 */
public class Jade extends Agent{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Jade jade = new Jade();
        jade.setup();
    }
    
     protected void setup() 
     { 
          System.out.println(getLocalName()); 
     }
}
