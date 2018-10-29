package pdfgenerator.security;

import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdfgenerationapi.Config;
import pdfgenerator.exceptions.DigitalSignatureException;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;

/**
 * Class responsible for digitally signing the pdf document
 */
public class DigitalSignature {

    private static final Logger LOGGER = LoggerFactory.getLogger(Certificates.class);

    /**
     * Digitally sign a PDF document
     * @param pdf The unsigned pdf file
     * @return A new signed PDF file
     * @throws DigitalSignatureException If there is an issue signing the document
     */
    public static File sign(File pdf) throws DigitalSignatureException {
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);

            String name = pdf.getName();
            String substring = name.substring(0, name.lastIndexOf('.'));
            File outFile = new File(pdf.getParent(), substring + "_signed.pdf");

            PdfSigner signer = buildSigner(pdf,outFile);

            IExternalSignature pks = new PrivateKeySignature(Certificates.getInstance().getKey(), "SHA-512", provider.getName());
            IExternalDigest digest = new BouncyCastleDigest();
            signer.signDetached(
                    digest,
                    pks,
                    Certificates.getInstance().getCerts(),
                    null,
                    null,
                    null,
                    0,
                    PdfSigner.CryptoStandard.CADES);
            return outFile;
        }
        catch (GeneralSecurityException | IOException | DigitalSignatureException ex) {
            LOGGER.error("Error signing document", ex);
            throw new DigitalSignatureException("Error signing document", ex);
        }
    }

    private static PdfSigner buildSigner(File input, File outputFile) throws IOException{
        PdfSigner signer = DigitalSignatureFactory.getPdfSigner(
                DigitalSignatureFactory.getPdfReader(input.getPath()),
                outputFile);

        signer.setCertificationLevel(PdfSigner.CERTIFIED_NO_CHANGES_ALLOWED);
        signer.setFieldName(signer.getNewSigFieldName());
        setInfo(signer);
        return signer;
    }

    private static void setInfo(PdfSigner signer) {
        if(Config.SIGNATURE_LOCATION != null) {
            signer.getSignatureAppearance().setLocation(Config.SIGNATURE_LOCATION);
        }

        if(Config.SIGNATURE_REASON != null) {
            signer.getSignatureAppearance().setReason(Config.SIGNATURE_REASON);
        }
    }
}
