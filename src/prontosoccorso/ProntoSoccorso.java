/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prontosoccorso;

import emergenze.CodiceColore;
import emergenze.Emergenza;
import emergenze.Pratica;
import emergenze.Stato;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author cristianalarizza
 */
public class ProntoSoccorso {

   private final Set<Emergenza> emergenzeFile = new TreeSet<>(new Comparator<Emergenza>() {
      @Override
      public int compare(Emergenza e1, Emergenza e2) {
         if (e1.getOraArrivo() == e2.getOraArrivo()) {
            return e1.getColore().compareTo(e2.getColore());
         } else {
            return e1.getOraArrivo() - e2.getOraArrivo();
         }
      }
   });

   private Set<Emergenza> emergenzeInCoda = new TreeSet<>();
   private List<Pratica> pratiche = new ArrayList<>();
   final int nMmax, nImax, nTmax, lettimax;
   private int nM, nI, nT, letti, nPratiche;
   private int currentTime;

   private ProntoSoccorso(int nM, int nI, int nT, int letti) {
      this.nMmax = nM;
      this.nImax = nI;
      this.nTmax = nT;
      this.lettimax = letti;
      this.nM = nM;
      this.nI = nI;
      this.nT = nT;
      this.letti = letti;
   }

   public ProntoSoccorso(int nM, int nI, int nT, int letti, String file) throws IOException {
      this(nM, nI, nT, letti);
      caricaEmergenze(file);
      currentTime = 0;
   }

   private void setEmergenzeInArrivo(Set<Emergenza> e) {
      this.emergenzeInCoda = new TreeSet<>(e);
   }

   private void caricaEmergenze(String file) throws IOException {
      BufferedReader in = new BufferedReader(new FileReader(file));
      String line, tokens[];
      CodiceColore c = null;
      while ((line = in.readLine()) != null) {
         tokens = line.split("\t");
         switch (tokens[1]) {
            case "Bianco":
               c = CodiceColore.BIANCO;
               break;
            case "Verde":
               c = CodiceColore.VERDE;
               break;
            case "Giallo":
               c = CodiceColore.GIALLO;
               break;
            case "Rosso":
               c = CodiceColore.ROSSO;
               break;
         }
         try {
            if (risorseDisponibili(c)) {
               emergenzeFile.add(new Emergenza(tokens[0],
                       Integer.parseInt(tokens[2]),
                       c));
            } else {
               throw new EmergenzaIntrattabileException();
            }
         } catch (EmergenzaIntrattabileException e) {
            System.out.println(e.getMessage());
         }
      }

   }

   private void gestisciEmergenze(int tempo) {
      verificaChiusura(tempo);
      prendiInCarico(tempo);

   }

   public void simula(int tempo) {
      resetSimulazione();
      for (int t = 0; t <= tempo; t++) {
         aggiungiInCoda(t);
         gestisciEmergenze(t);
         currentTime++;
      }
      currentTime--;
   }

   public void fineGiornata() {
      resetSimulazione();
      for (int t = 0; isEmergenzeNotClosed(); t++, currentTime++) {
         aggiungiInCoda(t);
         gestisciEmergenze(t);
      }
   }

   private boolean risorseDisponibili(CodiceColore colore) {
      if (letti > 0
              && nI >= colore.getnInfermieri()
              && nM >= colore.getnMedici()
              && nT >= colore.getnTecnici()) {
         return true;

      }
      return false;
   }

   private void inCarico(Emergenza e, int tempo) {
      System.out.println("ore " + currentTime + " prendo in carico " + e.getCodicePaziente() + "(" + e.getColore().name() + " " + e.getOraArrivo() + " attesa=" + (currentTime - e.getOraArrivo()) + ")");
      allocaRisorse(e.getColore());
      pratiche.add(new Pratica(e, ++nPratiche, tempo));
   }

   private void allocaRisorse(CodiceColore colore) {
      nM -= colore.getnMedici();
      nI -= colore.getnInfermieri();
      nT -= colore.getnTecnici();
      letti--;
   }

   private void rilasciaRisorse(CodiceColore colore) {
      nM += colore.getnMedici();
      nI += colore.getnInfermieri();
      nT += colore.getnTecnici();
      letti++;
   }

   private float attesaMedia() {
      float media = 0;
      int n = 0;
      for (Pratica p : pratiche) {
         media += p.getOraTrattamento() - p.getOraArrivo();
         n++;
      }
      return media / n;
   }

   public String getReport() {
      StringBuilder sb = new StringBuilder();
      sb.append("\n\n--- \nReport ore ").append(currentTime).append("\n");
      sb.append("\tAttesa = ").append(conta(Stato.ATTESA)).append("\n");
      sb.append("\tTrattamento = ").append(conta(Stato.TRATTAMENTO)).append("\n");
      sb.append("\tChiuse = ").append(conta(Stato.CHIUSO)).append("\n");
      sb.append("\tRisorse libere (medici=").append(nM).append(", tecnici=").append(nT).append(", infermieri=").append(nI).append(")\n");
      sb.append("\tAttesa media = ").append(String.format("%.2f", attesaMedia())).append("\n---\n");
      return sb.toString();
   }

   @Override
   public String toString() {
      return "Situazione ore=" + currentTime + " {" + "\n\temergenze in attesa " + getInAttesa().toString()
              + " \n\n\tpratiche  "
              + pratiche
              + "\n\tnM=" + nM + ", nI=" + nI + ", nT=" + nT + ", letti=" + letti + ", nPratiche=" + nPratiche + '}';
   }

   private void verificaChiusura(int tempo) {
      for (Pratica e : pratiche) {
         if (e.getOraTrattamento() + e.getCodiceColore().getDurata() == tempo) {
            e.chiudi();
            rilasciaRisorse(e.getCodiceColore());
         }
      }
   }

   private void prendiInCarico(int tempo) {
      Set<Emergenza> toRemove = new TreeSet<>();
      for (Emergenza e : emergenzeInCoda) {
         if (risorseDisponibili(e.getColore())) {
            inCarico(e, tempo);
            toRemove.add(e);
         }
      }
      emergenzeInCoda.removeAll(toRemove);
   }

   private Set<Emergenza> getInAttesa() {
      Set<Emergenza> inAttesa = new TreeSet<>();
      for (Emergenza e : emergenzeInCoda) {
         if (e.getOraArrivo() <= currentTime) {
            inAttesa.add(e);
         }
      }
      return inAttesa;
   }

   private int conta(Stato stato) {
      int count = 0;
      switch (stato) {
         case ATTESA:
            return emergenzeInCoda.size();
         case TRATTAMENTO:
         case CHIUSO:
            for (Pratica p : pratiche) {
               if (p.getStato().equals(stato)) {
                  count++;
               }
            }
            break;
         default:
            System.out.println("Stato sconosciuto "+stato);
            break;
      }
      return count;
   }

   private boolean isEmergenzeNotClosed() {
      int nchiuse = 0;
      for (Pratica e : pratiche) {
         if (e.getStato().equals(Stato.CHIUSO)) {
            nchiuse++;
         }
      }
      return nchiuse != emergenzeFile.size();
   }

   private void aggiungiInCoda(int t) {
      for (Emergenza em : emergenzeFile) {
         if (em.getOraArrivo() == t) {
            emergenzeInCoda.add(em);
         }
      }
   }

   private void resetSimulazione() {
      currentTime = 0;
      letti = lettimax;
      nM = nMmax;
      nI = nImax;
      nT = nTmax;
      emergenzeInCoda.clear();
      pratiche.clear();
      nPratiche = 0;

   }

}
