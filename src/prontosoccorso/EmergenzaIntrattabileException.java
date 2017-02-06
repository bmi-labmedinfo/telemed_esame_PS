/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prontosoccorso;

/**
 *
 * @author enea
 */
public class EmergenzaIntrattabileException extends Exception {

    public EmergenzaIntrattabileException() {
        super("le risorse massime di questo PS non consentono di trattare questa emergenza");
    }
    
}
