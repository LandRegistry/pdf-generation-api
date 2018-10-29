package pdfgenerator.security;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerationapi.Config;
import pdfgenerator.exceptions.DigitalSignatureException;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Singleton class responsible for loading the certificates and private keys
 */
public class Certificates {
    private static final Logger LOGGER = LoggerFactory.getLogger(Certificates.class);

    private PrivateKey private_key = null;
    private Certificate[] certificates;

    private static Certificates instance;

    /**
     * Returns a single instance of the certificate class
     * @return Instance of the Certificate class
     * @throws DigitalSignatureException if there is a failure loading the certificate or key
     */
    public static synchronized Certificates getInstance() throws DigitalSignatureException {
        if(instance == null) {
            try {
                instance = new Certificates();
            }
            catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException| CertificateException ex) {
                LOGGER.error("Error loading certificate or private key", ex);
                throw new DigitalSignatureException("Error loading certificate or private key", ex);
            }
        }
        return instance;
    }

    /**
     * Returns the private key for the certificate
     * @return Private key
     */
    PrivateKey getKey() {
        return this.private_key;
    }

    /**
     * Returns the collection of certificates
     * @return collection of certificates
     */
    Certificate[] getCerts() {
        return this.certificates;
    }

    private Certificates() throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, CertificateException, DigitalSignatureException {
        this.private_key = loadKey();
        this.certificates = loadCerts();
    }

    private PrivateKey loadKey() throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        File keyFile = new File(Config.PRIVATE_KEY_PATH);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(keyFile));
        byte[] privateKeyBytes = new byte[(int)keyFile.length()];
        bis.read(privateKeyBytes);
        bis.close();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec ks = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(ks);
    }

    private Certificate[] loadCerts() throws CertificateException, IOException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(
                FileUtils.readFileToByteArray(
                        new File(Config.CERTIFICATE_PATH)
                )));
        return new Certificate[]{cert};
    }
}
