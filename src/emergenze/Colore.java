/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emergenze;

/**
 *
 * @author cristianalarizza
 */
public class Colore implements Comparable{

   private final int nMedici, nInfermieri, nTecnici, durata;
   private final Integer priorita;

   public Colore(int priorita, int nMedici, int nInfermieri, int nTecnici, int durata) {
      this.priorita = priorita;
      this.nMedici = nMedici;
      this.nInfermieri = nInfermieri;
      this.nTecnici = nTecnici;
      this.durata = durata;
   }

   public int getPriorita() {
      return priorita;
   }

   public int getnMedici() {
      return nMedici;
   }

   public int getnInfermieri() {
      return nInfermieri;
   }

   public int getnTecnici() {
      return nTecnici;
   }

   public int getDurata() {
      return durata;
   }

   @Override
   public int compareTo(Object o) {
      return compareTo(priorita);
   }

}
