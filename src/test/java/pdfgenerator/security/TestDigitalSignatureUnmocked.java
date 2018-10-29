package pdfgenerator.security;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.CertificateVerification;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.VerificationException;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import pdfgenerationapi.TestUtils;
import pdfgenerator.exceptions.DigitalSignatureException;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestDigitalSignatureUnmocked {

    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
    }

    @Test
    public void testSignatureValid() throws DigitalSignatureException, IOException, GeneralSecurityException {
        File output = null;
        try{
            File pdf = new File("test-data/unsigned.pdf");

            output = DigitalSignature.sign(pdf);

            assertThat(output, is(not(nullValue())));
            validateSignature(output);
        }
        finally {
            if (output != null && output.exists()) {
                FileUtils.deleteQuietly(output);
            }
        }
    }

    private void validateSignature(File signedPDF) throws IOException, GeneralSecurityException, DigitalSignatureException{
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        PdfReader reader = DigitalSignatureFactory.getPdfReader(signedPDF.getPath());
        PdfDocument pdfDoc = new PdfDocument(reader);
        SignatureUtil signUtil = new SignatureUtil(pdfDoc);

        List<String> names = signUtil.getSignatureNames();

        verifySignatures(signUtil, names);
        reader.close();


    }

    private void verifySignatures(SignatureUtil signUtil, List<String> names) throws IOException, GeneralSecurityException, DigitalSignatureException {
        for (String name : names) {
            PdfPKCS7 pkcs7 = signUtil.verifySignature(name);

            // verify signature integrity
            if (!pkcs7.verify()) {
               assertThat("Signature invalid", false, is(true));
            }

            assertThat(pkcs7.getReason(), is("testReason"));
            assertThat(pkcs7.getLocation(), is("testLocation"));
            verifyCertificates(pkcs7);
        }
    }

    private void verifyCertificates(PdfPKCS7 pkcs7) throws GeneralSecurityException, IOException, DigitalSignatureException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        initKeyStoreForVerification(ks);

        Certificate[] certs = pkcs7.getSignCertificateChain();
        Calendar cal = pkcs7.getSignDate();
        List<VerificationException> errors = CertificateVerification.verifyCertificates(certs, ks, cal);
        if (!errors.isEmpty()) {
            assertThat("Signature invalid", false, is(true));
        }
        for (Certificate cert : certs) {
            X509Certificate X509cert = (X509Certificate) cert;
            checkCertificateInfo(X509cert, cal.getTime(), pkcs7);
        }
    }

    private void initKeyStoreForVerification(KeyStore ks) throws IOException, NoSuchAlgorithmException,
            CertificateException, KeyStoreException, DigitalSignatureException {
        ks.load(null, null);
        Certificate cert = Certificates.getInstance().getCerts()[0];
        ks.setCertificateEntry("main", cert);
    }

    private void checkCertificateInfo(X509Certificate cert, Date signDate, PdfPKCS7 pkcs7) throws GeneralSecurityException {
        try {
            cert.checkValidity(signDate);
        } catch (CertificateExpiredException e) {
            assertThat("The certificate was expired at the time of signing", false, is(true));
        } catch (CertificateNotYetValidException e) {
            assertThat("The certificate wasn't valid yet at the time of signing", false, is(true));
        }

        if (pkcs7.getTimeStampDate() != null) {
            if (!pkcs7.verifyTimestampImprint()) {
                assertThat("Timestamp is invalid.", false, is(true));
            }
        }
    }
}
