package com.example.mobileappws.shared;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//    private final int ITERATIONS = 10000;
//    private final int KEY_LENGTH = 256;


   public String generateUserId(int length){
       return generateRandomString( length);
   }

   private String generateRandomString(int length){
       StringBuilder returnValue = new StringBuilder(length);

       for(int i = 0; i<length; i++){
           returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
       }
       return new String(returnValue);
   }

}
