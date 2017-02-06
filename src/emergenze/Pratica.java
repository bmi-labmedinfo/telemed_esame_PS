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
public class Pratica {

   private Emergenza e;
   private int nPratica;
   private int oraTrattamento;

   public Pratica(Emergenza e, int nPratica, int oraTrattamento) {
      this.e = e;
      this.nPratica = nPratica;
      this.oraTrattamento = oraTrattamento;
      this.e.setStato(Stato.TRATTAMENTO);
   }

   public int getnPratica() {
      return nPratica;
   }

   public int getOraTrattamento() {
      return oraTrattamento;
   }

   public int getOraArrivo() {
      return e.getOraArrivo();
   }

   public int getPriorita() {
      return e.getColore().getPriorita();
   }

   public CodiceColore getCodiceColore() {
      return e.getColore();
   }

   public Stato getStato() {
      return e.getStato();
   }

   @Override
   public String toString() {
      return "\t" + e + "\n\t" + nPratica + ", oraTrattamento=" + oraTrattamento ;
   }

   public void chiudi() {
      this.e.setStato(Stato.CHIUSO);
   }
}
