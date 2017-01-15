package pl.masyk.chat;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by adamm on 26.11.2016.
 */
public class RSA {
    private static int KEY_LENGTH = 512;
    private static SecureRandom secureRandom = new SecureRandom();
    private static BigInteger e;
    private static BigInteger n;
    private static BigInteger d;
    private static BigInteger p = new BigInteger(KEY_LENGTH, 100, secureRandom);
    private static BigInteger q = new BigInteger(KEY_LENGTH, 100, secureRandom);
    private BigInteger forE;
    private BigInteger forN;

    public void setForeginKey(String foreginKey) {
        forE = new BigInteger(foreginKey.substring(foreginKey.indexOf("e:")+2,foreginKey.indexOf("n:")));
        forN = new BigInteger(foreginKey.substring(foreginKey.indexOf("n:")+2));
    }
    private static void createKey () {
        n = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q
                .subtract(BigInteger.ONE));
        e = new BigInteger("65537");
        while (m.gcd(e).intValue() > 1) {
            e = e.add(new BigInteger("2"));
        }
        d = e.modInverse(m);
    }
    public RSA() {
        createKey();
    }

    public   String decrypt(String messageToDecrypt) {
        return new String((new BigInteger(messageToDecrypt)).modPow(d, n).toByteArray());
    }

    public   String getPublicKey() {
        return "e:" + e.toString() + "n:" + n.toString();
    }

    public String encryptMessageToSend(String messageToSend) {
        return (new BigInteger(messageToSend.getBytes())).modPow(forE, forN).toString();
    }

}
