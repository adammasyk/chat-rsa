package pl.masyk.chat;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by adamm on 26.11.2016.
 */
public class RSA {
    private static int KEY_LENGTH = 512;
    private static SecureRandom secureRandom = new SecureRandom();
    private static BigInteger e = new BigInteger("3");
    private static BigInteger n;
    private static BigInteger d;
    private static BigInteger p = BigInteger.probablePrime(KEY_LENGTH,secureRandom); // dwie duze liczby pierwsze
    private static BigInteger q = BigInteger.probablePrime(KEY_LENGTH,secureRandom); // dwie duze liczby pierwsze
    private BigInteger forE;
    private BigInteger forN;

    public void setForeginKey(String foreginKey) {
        forE = new BigInteger(foreginKey.substring(foreginKey.indexOf("e:")+2,foreginKey.indexOf("n:")));
        forN = new BigInteger(foreginKey.substring(foreginKey.indexOf("n:")+2));
    }
    private static void createKey () {
        n = p.multiply(q); // obliczenie n, drugiej liczby w kluczu tajnym i publicznym
        BigInteger fi = (p.subtract(BigInteger.ONE)).multiply(q
                .subtract(BigInteger.ONE)); // obliczenie fi = (p-1) x (q-1)
        while (fi.gcd(e).intValue() != 1 && e.compareTo(BigInteger.ONE) == 1 && e.compareTo(n) == -1) {
            e = e.add(new BigInteger("1"));
            /*
            Znalezienie dobrej wartosci liczby e czyli najwiekszego wspolnego dzielnika e oraz fi, dodatkowo
             liczba ta musi spelnic warunek  1 < e < n
             */
        }
        d = e.modInverse(fi); // obliczenie odwrotnej modulo fi
    }
    public RSA() {
        createKey();
    }

    public   String encrypt(String messageToEncrypt) {
        return new String((new BigInteger(messageToEncrypt)).modPow(d, n).toByteArray()); // szyfrowanie kluczem prywatnym
    }

    public   String getPublicKey() {
        return "e:" + e.toString() + "n:" + n.toString();
    }

    public String decrypt(String messageToDecrypt) {
        return (new BigInteger(messageToDecrypt.getBytes())).modPow(forE, forN).toString(); // odszyfrowanie kluczem publicznym
    }

}
