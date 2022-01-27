package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Map;


public class EntitySignatureValidator {

    public static String calculateEntitySignature(Map payload) {

        Algorithm algo = Algorithm.RSA256(MyKeys.getPublicKey(), MyKeys.getPrivateKey());

        String token = JWT.create()
                .withPayload(payload)
                .sign(algo);

        return token;
    }
}
