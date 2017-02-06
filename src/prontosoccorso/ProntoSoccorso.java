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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author cristianalarizza
 */
public class ProntoSoccorso {

   private final Set<Emergenza> emergenzeFile = new TreeSet<>();
   private Set<Emergenza> emergenzeIn = new TreeSet<>();
//   private List<Emergenza> emergenzeIn = new ArrayList<>();
   private List<Pratica> pratiche = new ArrayList<>();
   int nM, nI, nT, letti, nPratiche;
   private int currentTime;

   private ProntoSoccorso(int nM, int nI, int nT, int letti) {
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

   public void setEmergenzeInArrivo(Set<Emergenza> e) {
      this.emergenzeIn = new TreeSet<>(e);
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
         emergenzeFile.add(new Emergenza(line.split("\t")[0],
                 Integer.parseInt(line.split("\t")[2]),
                 c));
      }
   }

   private void gestisciEmergenze(int tempo) {
      verificaChiusura(tempo);
      prendiInCarico(tempo);

   }

   public void simula(int tempo) {
      setEmergenzeInArrivo(emergenzeFile);
      currentTime = 0;
      for (int t = 0; t <= tempo; t++, currentTime++) {
         gestisciEmergenze(t);
      }
      currentTime--;
   }

   public void fineGiornata() {
      setEmergenzeInArrivo(emergenzeFile);
      currentTime = 0;
      for (int t = 0;; t++, currentTime++) {
         if (isEmergenzeNotClosed()) {
            gestisciEmergenze(t);
         }
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

   public float attesaMedia() {
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
      return "ProntoSoccorso ore=" + currentTime + " {" + "\n\temergenze in attesa " + getInAttesa().toString() 
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
      for (Emergenza e : emergenzeIn) {
         if (e.getOraArrivo() <= tempo) {
            if (risorseDisponibili(e.getColore())) {
               inCarico(e, tempo);
               toRemove.add(e);
            }
         } else if (e.getOraArrivo() > tempo) {
            break;
         }
      }
      emergenzeIn.removeAll(toRemove);
   }

   private Set<Emergenza> getInAttesa(){
      Set<Emergenza> inAttesa= new TreeSet<>();
      for(Emergenza e:emergenzeIn){
         if(e.getOraArrivo()<=currentTime)
            inAttesa.add(e);
      }
      return inAttesa;
   }
   
   private int conta(Stato stato) {
      int count = 0;
      for (Pratica p : pratiche) {
         if (p.getStato().equals(stato)) {
            count++;
         }
      }
      return count;
   }

   private boolean isEmergenzeNotClosed() {
      for (Emergenza e : emergenzeIn) {
         if (!e.getStato().equals(Stato.CHIUSO)) {
            return false;
         }
      }
      return true;
   }

}
