package utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import utils.Signable;

public class EntitySigner {
    private static final String SECRET = "51655468576D5A7134743777217A25432A46294A404E635266556A586E327235";
    public static String calculateSignature(Signable s) {
        try {
            JWSSigner signer = new MACSigner(SECRET);
            JWSObject obj = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(s.getSignableFields()));
            obj.sign(signer);
            return obj.serialize();
        } catch (Exception ex) {
            return "Failed";
        }
    }
    public static boolean validateSignature(String tag) {
        try {
            JWSObject obj =JWSObject.parse(tag);
            JWSVerifier verifier = new MACVerifier(SECRET);
            return obj.verify(verifier);
        } catch (Exception ex) {
            return false;
        }
    }
    public static boolean verifyEntity(String tag, Signable s) {
        try {
            final String tagVal = JWSObject.parse(tag).getPayload().toString();
            final String entityVal = s.getSignableFields();
            return validateSignature(tag) && tagVal.equals(entityVal);
        } catch (Exception ex) {
            return false;
        }
    }
}