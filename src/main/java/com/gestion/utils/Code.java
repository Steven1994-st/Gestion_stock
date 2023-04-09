package com.gestion.utils;

/** Utility class to create random code
 *
 */
public class Code {

    /** Generate a random code
     *
     * @return a number between 10000 and 99999
     */
    public static String generateCode(){
        return String.valueOf(10000 + (int) (Math.random()*(99999-10000)));
    }
}
