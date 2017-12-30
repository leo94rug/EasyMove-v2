package Utilita.Crypt;

import Interfaces.ICrypt;

/**
 *
 * @author leo
 */
public class SimpleCrypt implements ICrypt {

    /* encrypt and decrypt a text using a simple algorithm of offsetting the letters */
    private final int offset;
    private final char[] chars;

    public SimpleCrypt() {
        this.chars = new char[]{'d', 'b', 's', 'g', '^', '&', '(', ')', '+', '-', '*', '[', ']', '{', '}', '=', '<', '>', '?', '_', '"', '.', ',', ' ', 'u', 'm', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '#', '$', '%', 'c', 'a', 'e', 'f', 't', 'h', 'i', 'j', 'o', 'l', 'v', 'n', 'k', 'p', 'q', 'r', '^', '&', '(', ')', '+', '-', '*', '[', ']', '{', '}', '=', '<', '>', '?', '_', '"', '.', ',', ' '};
        this.offset = 11;
    }

    // Caesar cipher
    @Override
    public String encrypt(String text) {
        char[] plain = text.toCharArray();

        for (int i = 0; i < plain.length; i++) {
            for (int j = 0; j < chars.length; j++) {
                if (j <= chars.length - offset) {
                    if (plain[i] == chars[j]) {
                        plain[i] = chars[j + offset];
                        break;
                    }
                } else if (plain[i] == chars[j]) {
                    plain[i] = chars[j - (chars.length - offset + 1)];
                }
            }
        }
        return String.valueOf(plain);
    }

    @Override
    public String decrypt(String cip) {
        char[] cipher = cip.toCharArray();
        for (int i = 0; i < cipher.length; i++) {
            for (int j = 0; j < chars.length; j++) {
                if (j >= offset && cipher[i] == chars[j]) {
                    cipher[i] = chars[j - offset];
                    break;
                }
                if (cipher[i] == chars[j] && j < offset) {
                    cipher[i] = chars[(chars.length - offset + 1) + j];
                    break;
                }
            }
        }
        return String.valueOf(cipher);
    }
}
