/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import prontosoccorso.ProntoSoccorso;

/**
 *
 * @author cristianalarizza
 */
public class TestPS {

    public static void main(String[] args) throws IOException {
        int med = 2;
        int inf = 2;
        int tec = 1;
        int bed = 4;
        ProntoSoccorso ps = new ProntoSoccorso(med, inf, tec, bed, "dati/emergenze.txt");
        int time = 450;
        ps.simula(time);
        System.out.println(ps.getReport());
        System.out.println("ps = " + ps);
//      time = 24*60;
//      ps.simula(time);
//      System.out.println(ps.getReport());
        System.out.println("\n----simula fine giornata----\n");
        ps.fineGiornata();
        System.out.println(ps.getReport());
    }
}
