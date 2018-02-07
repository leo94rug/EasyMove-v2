/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author leo
 */
public interface Ijwt {
    String encodeToken(String id) throws UnsupportedEncodingException;

    String decodeToken(String id) throws UnsupportedEncodingException;
}

