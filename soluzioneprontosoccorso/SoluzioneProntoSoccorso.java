/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soluzioneprontosoccorso;

import java.io.IOException;

/**
 *
 * @author cristianalarizza
 */
public class SoluzioneProntoSoccorso {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws IOException {
      ProntoSoccorso ps = new ProntoSoccorso(10, 10, 10, 10, "dati/emergenze.txt");
   }
   
}
