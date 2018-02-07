/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilita.JWT;


import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leo
 */
public class JWTToken {

//    public String encodeToken() throws IllegalArgumentException, UnsupportedEncodingException  {
//            Algorithm algorithm = Algorithm.HMAC256("secret");
//            String token = JWT.create().sign(algorithm);
//            return token;
//    }
//
//    public String decodeToken(String token) throws IllegalArgumentException, UnsupportedEncodingException {
//        Algorithm algorithm = Algorithm.HMAC256("secret");
//        JWTVerifier verifier = JWT.require(algorithm)
//                .build(); //Reusable verifier instance
//        DecodedJWT jwt = verifier.verify(token);
//        Claim claim = jwt.getClaim("owner");
//        String asString = claim.asString();
//        return asString;
//    }
}
