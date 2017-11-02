/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author djordjebozic
 */
public class ControllerUtils {
    
    private static ControllerUtils instance;
    
    private ControllerUtils() {
    }
    
    public static ControllerUtils instance() {
        if (instance == null) instance = new ControllerUtils();
        return instance;
    }
    
     public boolean sequenceLength(String password) {
        boolean found = false;
        for (int i = 2; i < password.length(); i++) {
            if (password.charAt(i) == password.charAt(i - 1) && password.charAt(i) == password.charAt(i - 2)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public boolean countNumerics(String password) {
        int number = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                number++;
            }
        }
        return number > 0;
    }

    public boolean countCapital(String password) {
        int number = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                number++;
            }
        }
        return number > 0;
    }

    public boolean countSpecialChars(String password) {
        boolean found = false;
        for (int i = 0; i < password.length(); i++) {
            if (!Character.isLetter(password.charAt(i)) && !Character.isDigit(password.charAt(i))
                    && !Character.isSpaceChar(password.charAt(i))) {
                found = true;
                break;
            }
        }
        return found;
    }
    
}
