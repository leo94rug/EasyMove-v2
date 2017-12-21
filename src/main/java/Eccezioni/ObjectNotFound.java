/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eccezioni;

/**
 *
 * @author leo
 */
public class ObjectNotFound extends RuntimeException{
    public ObjectNotFound(){
        super("L'oggetto ricercato non è presente nel database");
    }
}
