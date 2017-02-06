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
public enum CodiceColore {
   ROSSO(3,2,1,2,300),GIALLO(2,1,1,1,130),VERDE(1,1,0,1,55),BIANCO(0,0,1,1,30);
   private final int priorita, nMedici, nInfermieri, nTecnici, durata;

   private CodiceColore(int priorita, int nMedici, int nTecnici, int nInfermieri, int durata) {
      this.priorita = priorita;
      this.nMedici = nMedici;
      this.nInfermieri = nInfermieri;
      this.nTecnici = nTecnici;
      this.durata = durata;
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
  

   public int getPriorita() {
      return priorita;
   }

   @Override
   public String toString() {
      return this.name() + " priorita=" + priorita+']' ;//+ ", nMedici=" + nMedici + ", nInfermieri=" + nInfermieri + ", nTecnici=" + nTecnici + ", durata=" + durata + '}';
   }
   
}
