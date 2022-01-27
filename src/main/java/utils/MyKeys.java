package utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class MyKeys {

    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmbv7AeMDuOYOPhQ95gup" +
            "6WFUhFX5Nu5np7ha0zo8NDTduWQs6dPXAiolMWRG0IZSUP4NjdY0dqeFp1QaMPni" +
            "gsUgsRMrWY7awB02U7IfFgJzfFGZvh9h2NxfhNVxfcikb4zX4arCU44mEr12bv3M" +
            "+EzTMyO4MW73MWrjq12Ft6JGtQgvxuRYJfeZ3GUF8J/XmftOd7xsQkakj7r7A8xX" +
            "oZg5ykzWAFqOjzJDnp2rhlGbA/b8xTO2HPi5bs39NbdB+FAMbOZbnkrUkYsFskso" +
            "K+8QYlqlpDo+lltHfoHUH4ozWKasoJKvef/70XaJDI9uYVzrEMAPGOLFpNrJil6W" +
            "TwIDAQAB";
    private static final String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCZu/sB4wO45g4+" +
            "FD3mC6npYVSEVfk27menuFrTOjw0NN25ZCzp09cCKiUxZEbQhlJQ/g2N1jR2p4Wn" +
            "VBow+eKCxSCxEytZjtrAHTZTsh8WAnN8UZm+H2HY3F+E1XF9yKRvjNfhqsJTjiYS" +
            "vXZu/cz4TNMzI7gxbvcxauOrXYW3oka1CC/G5Fgl95ncZQXwn9eZ+053vGxCRqSP" +
            "uvsDzFehmDnKTNYAWo6PMkOenauGUZsD9vzFM7Yc+Lluzf01t0H4UAxs5lueStSR" +
            "iwWySygr7xBiWqWkOj6WW0d+gdQfijNYpqygkq95//vRdokMj25hXOsQwA8Y4sWk" +
            "2smKXpZPAgMBAAECggEARxYFGvo93mGCdHInrRf3mVLIIekfM6dJhuRYGxPygrFv" +
            "VSpdGkI6SWXzjMMxe3qg1ZKj8dH6sGMGCjl5YuwpMQ617ytOADtuKydW15ylgyfe" +
            "/r08ug0I0Z4VDPclY2RDpNEbgd3TvY8dS80P1oLFJIthnWQG59THuLIUVEkKa76G" +
            "OcUT+eORbE4+l7RZ01mdyrbMtUSlJpJozg8nHSiC8vJjS8FJB5PCRFQ9x3zlcuP4" +
            "yvtvgNrShyxs0nWt6GUVtGHQkFHgxyRsWEKAqXdY/pGp2/xs5FLhO0o9m4Cc2aFR" +
            "+0fJQXf+gDG1m+OXbgPbcIXY5/MbWDcENQonGPKOAQKBgQDIZTojatU3Jlt0QDxX" +
            "3syKXB18UPXc081byHbOpEXN5BfX/K2JAY5EB2XTVVhCb1UvyU+7qFOk38Kd82EV" +
            "pS4GesWZfLc2tKQxtjYQTXeKJhLlHrMgEtdqOvsSYJP/N1RYhi3UgBR0/XJ6hSoq" +
            "w3rrc5/bvWfxIUfs+OqLzrDDhwKBgQDEZEEXc6Yldb7tVOcc1rrT0/6aXLHKzw1L" +
            "NRZPJDEdG2UpUgtBkRJ+OtXh3BqW6X3OlU/8Ne2wtyNJLUeYpTDZ9RjepUhpGBpx" +
            "JQF8ugwYS8jnkS9r4WemAuHBGRQDLoLMyznR1Mp2BkhjLUSizMT0kHb3Jl5Oh833" +
            "7YG1u3ZY+QKBgAidGUemcu3A71Nq420ffYlA9l9+l2Lbk8srLbgqJXn5IPE+wjLO" +
            "Z1ATRVQ1KesJIA9CxvOZA6dlmtbxPC4TAIx0wIOdcb6fOyOL4n0G52l/w3YnwKlT" +
            "RgEh8eLTVek1dqGretLUgoyYzE/p42sdJe5y9N8cIBikRpQwCtzhkIWHAoGAQwj9" +
            "duXQ1oSEcTpD0QIyr09zXnk5bsKSBRPP05/E6+yH8TzB/tVn/JJUo02f/dchTDyH" +
            "Gs3FnkZE0xuTjrUEIcGZhZXhS44gqHI7Obm/myNzJifWifpciLv/FwxaWmOr3f+Y" +
            "ctkxamvmlYPEI9XFfL06/k2obyHH2lLFraSZVvECgYBzQ/aT7orHQaxxj+FPA/3k" +
            "l6IZYip7+vDiFil8keoVMdZpA2/7ySGrJu8ZdjWZ6gbk1UKZEaPGTLsVLDugPuK3" +
            "Sxrh0exlM5Vh+oTbgXBgI2/gjbOVhnW0p6jxgHjtahW4jmHaG5julOGAaJQYcT7B" +
            "i7hLL4pK1XJi9AbknTFhHA==";

    public static RSAPrivateKey getPrivateKey() {
        try {
            return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY)));
//                    .generatePrivate(new X509EncodedKeySpec(Base64.getDecoder().decode(key)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RSAPublicKey getPublicKey() {
        try {
            return (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY)));
//                    .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
