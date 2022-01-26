package utils;

//import com.nimbusds.jose.*;
//import com.nimbusds.jose.crypto.MACSigner;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nimbusds.jwt.JWTClaimsSet//import com.nimbusds.jose.*;
//import com.nimbusds.jose.crypto.MACSigner;
;
//import com.nimbusds.jwt.SignedJWT;
//import com.nimbusds.jwt.SignedJWT;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.simple.JSONObject;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Date;

public class JWTGeneratorVerifier {

    private static final String SECRET = "zH1yAC-lqGqZOnUz0-VgOIzmlTyPkc2PUIwhPnowBTvjkD_KOtHJ_TjqvDLoXac6v4taw1YvDobFKJelLyrCYdjTXm_oqW6ORO-wnf2Lb1xDUubELT6dBjoq-pMNCIl_cpmtpmsU74s9fqmuhkoSGbdisfOmuQLbXiE6VJMCfhuKnnM6P5PaXT6XY63vpN3IlrvEw4uOuCn4cLT1F8UWNYn8xj9_GFNLixjqStVx-yKIM2JOgr8gmJq87YZqm4J0wEtxBsf-ZVc-9aAY-bqgSkRWaiNz0UiahWy1RAD-k02-iAC1wq3-lwVb71BVBJ14ghCzIKCwyeGVx8nInPtKVw";
    private static final long JWS_TIMEOUT_MS = 15 * 60 * 1000;

    public static String generateJWTString(CredentialValidationResult credential) {
            Algorithm algo = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    .withIssuer("Zespol1")
                    .withClaim("auth", String.join(",", credential.getCallerGroups()))
                    .withSubject(credential.getCallerPrincipal().getName())
                    .withExpiresAt(new Date(new Date().getTime() + JWS_TIMEOUT_MS))
                    .sign(algo);

            return token;
    }
 }
