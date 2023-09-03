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

    /**
     * Générer une chaine de caractére aleatoire
     * @param length
     * @return Chaine aleatoire créé
     */
    public static String getRandomStr(int length)
    {
        //choisir un caractére au hasard à partir de cette chaîne
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        StringBuilder s = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int)(str.length() * Math.random());
            s.append(str.charAt(index));
        }
        return s.toString();
    }
}
