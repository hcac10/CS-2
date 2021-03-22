package edu.caltech.cs2.project01;

import java.util.*;

public class SubstitutionCipher {
    private String ciphertext;
    private Map<Character, Character> key;

    // Use this Random object to generate random numbers in your code,
    // but do not modify this line.
    private static final Random RANDOM = new Random();

    /**
     * Construct a SubstitutionCipher with the given cipher text and key
     * @param ciphertext the cipher text for this substitution cipher
     * @param key the map from cipher text characters to plaintext characters
     */
    public SubstitutionCipher(String ciphertext, Map<Character, Character> key) {
        this.ciphertext = ciphertext;
        this.key = key;
    }

    /**
     * Construct a SubstitutionCipher with the given cipher text and a randomly
     * initialized key.
     * @param ciphertext the cipher text for this substitution cipher
     */
    public SubstitutionCipher(String ciphertext) {
        this.ciphertext = ciphertext;
        Map<Character,Character> m = new HashMap<>();
        for(char c: CaesarCipher.ALPHABET){
            m.put(c, c);
        }
        SubstitutionCipher s = new SubstitutionCipher(ciphertext, m);
        for (int i = 0; i < 10000; i++){
            s = s.randomSwap();
        }
        this.key = s.key;
    }

    /**
     * Returns the unedited cipher text that was provided by the user.
     * @return the cipher text for this substitution cipher
     */
    public String getCipherText() {
        return this.ciphertext;
    }

    /**
     * Applies this cipher's key onto this cipher's text.
     * That is, each letter should be replaced with whichever
     * letter it maps to in this cipher's key.
     * @return the resulting plain text after the transformation using the key
     */
    public String getPlainText() {
        String ret = "";
        for (int i = 0; i < this.ciphertext.length(); i++) {
            ret += this.key.get(this.ciphertext.charAt(i));
        }
        return ret;
    }

    /**
     * Returns a new SubstitutionCipher with the same cipher text as this one
     * and a modified key with exactly one random pair of characters exchanged.
     *
     * @return the new SubstitutionCipher
     */
    public SubstitutionCipher randomSwap() {
        Map<Character, Character> m = new HashMap<Character, Character>();
        m.putAll(this.key);
        char c1 = CaesarCipher.ALPHABET[RANDOM.nextInt(26)];
        char c2 = CaesarCipher.ALPHABET[RANDOM.nextInt(26)];
        while (c1 == c2) {
            c2 = CaesarCipher.ALPHABET[RANDOM.nextInt(26)];
        }
        char r1 = m.get(c1);
        char r2 = m.get(c2);
        m.put(c1, r2);
        m.put(c2, r1);
        SubstitutionCipher s = new SubstitutionCipher(this.ciphertext, m);
        return s;
    }

    /**
     * Returns the "score" for the "plain text" for this cipher.
     * The score for each individual quadgram is calculated by
     * the provided likelihoods object. The total score for the text is just
     * the sum of these scores.
     * @param likelihoods the object used to find a score for a quadgram
     * @return the score of the plain text as calculated by likelihoods
     */
    public double getScore(QuadGramLikelihoods likelihoods) {
        String s = getPlainText();
        String quad = "";
        double score = 0.0;
        for (int i = 0; i < s.length()-3; i++){
            quad = s.substring(i, i+4);
            score += likelihoods.get(quad);
        }

        return score;
    }

    /**
     * Attempt to solve this substitution cipher through the hill
     * climbing algorithm. The SubstitutionCipher this is called from
     * should not be modified.
     * @param likelihoods the object used to find a score for a quadgram
     * @return a SubstitutionCipher with the same ciphertext and the optimal
     *  found through hill climbing
     */
    public SubstitutionCipher getSolution(QuadGramLikelihoods likelihoods) {
        SubstitutionCipher s = new SubstitutionCipher(this.ciphertext);
        SubstitutionCipher best = new SubstitutionCipher(this.ciphertext);

        String po = getCipherText();
        int i = 0;
        double d = s.getScore(likelihoods);
        double e = 0.0;
        while (i < 1000){
            s = best.randomSwap();
            e = s.getScore(likelihoods);
            if(e > d) {
                best = s;
                i = 0;
                d = e;
            }
            i++;
        }
        return best;
    }
}
