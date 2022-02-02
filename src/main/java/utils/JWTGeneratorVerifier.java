package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class JWTGeneratorVerifier {

//    private static final String SECRET = "zH1yAC-lqGqZOnUz0-VgOIzmlTyPkc2PUIwhPnowBTvjkD_KOtHJ_TjqvDLoXac6v4taw1YvDobFKJelLyrCYdjTXm_oqW6ORO-wnf2Lb1xDUubELT6dBjoq-pMNCIl_cpmtpmsU74s9fqmuhkoSGbdisfOmuQLbXiE6VJMCfhuKnnM6P5PaXT6XY63vpN3IlrvEw4uOuCn4cLT1F8UWNYn8xj9_GFNLixjqStVx-yKIM2JOgr8gmJq87YZqm4J0wEtxBsf-ZVc-9aAY-bqgSkRWaiNz0UiahWy1RAD-k02-iAC1wq3-lwVb71BVBJ14ghCzIKCwyeGVx8nInPtKVw";
    private static final long JWS_TIMEOUT_MS = 15 * 60 * 1000;

    public static String generateJWTString(CredentialValidationResult credential) {

            Algorithm algo = Algorithm.RSA256(MyKeys.getPublicKey(), MyKeys.getPrivateKey());

            String token = JWT.create()
                    .withKeyId("abc-1234567890")
                    .withIssuer("zespol1")
                    .withClaim("groups", new ArrayList<>(credential.getCallerGroups()))
                    .withClaim("userid", credential.getCallerUniqueId())
                    .withSubject(credential.getCallerUniqueId())
                    .withJWTId("a-123")
                    .withAudience("s6BhdRkqt3")
                    .withExpiresAt(new Date(new Date().getTime() + JWS_TIMEOUT_MS))
                    .withIssuedAt(new Date(new Date().getTime()))
                    .withClaim("upn", credential.getCallerPrincipal().getName())
                    .sign(algo);

            return token;
    }

 }
