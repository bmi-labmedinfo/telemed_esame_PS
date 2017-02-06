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
public class Emergenza implements Comparable {

   private String codicePaziente;
   private int oraArrivo;
   private Stato stato;
   private CodiceColore colore;

   public Emergenza(String codicePaziente, int oraArrivo, CodiceColore colore) {
      this.codicePaziente = codicePaziente;
      this.oraArrivo = oraArrivo;
      this.colore = colore;
      this.stato = Stato.ATTESA;
   }

   public String getCodicePaziente() {
      return codicePaziente;
   }

   public int getOraArrivo() {
      return oraArrivo;
   }

   public CodiceColore getColore() {
      return colore;
   }

   public int getPriorita() {
      return getColore().getPriorita();
   }

   void setStato(Stato stato) {
      this.stato = stato;
   }

   @Override
   public int compareTo(Object o) {
      Emergenza e = (Emergenza) o;
      if (e.getOraArrivo() < this.getOraArrivo()) {
         return 1;
      } else if (e.getOraArrivo() > this.getOraArrivo()) {
         return -1;
      } else {
         return e.getPriorita() - getPriorita();
      }
   }

   @Override
   public boolean equals(Object obj) {
      return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public String toString() {
      return "\n\t\t{codicePaziente=" + codicePaziente + ", oraArrivo=" + oraArrivo + ", colore=" + colore + " stato = " + stato.name();
   }

   public Stato getStato() {
      return stato;
   }

}
