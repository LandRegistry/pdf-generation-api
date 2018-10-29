package pdfgenerator.security;

import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.*;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.Config;
import pdfgenerationapi.TestUtils;
import pdfgenerator.exceptions.DigitalSignatureException;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DigitalSignatureFactory.class, Certificates.class, Config.class})
public class TestDigitalSignature extends EasyMockSupport {

    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
    }

    @Before
    public void initialize() throws Exception {
        PowerMock.mockStatic(DigitalSignatureFactory.class);
        PowerMock.mockStatic(Certificates.class);
    }

    @After
    public void after() {
        super.resetAll();
        PowerMock.resetAll();
    }

    @Override
    public void verifyAll() {
        super.verifyAll();
        PowerMock.verifyAll();
    }

    @Override
    public void replayAll() {
        super.replayAll();
        PowerMock.replayAll();
    }

    @Test
    public void SuccessfulSigning() throws DigitalSignatureException, IOException, GeneralSecurityException {

        File pdf = mock(File.class);
        EasyMock.expect(pdf.getName()).andReturn("abc.pdf");
        EasyMock.expect(pdf.getParent()).andReturn("/").atLeastOnce();
        EasyMock.expect(pdf.getPath()).andReturn("/");

        PrivateKey key = mock(PrivateKey.class);
        Certificate cert =  mock(Certificate.class);
        Certificate[] certificates = new Certificate[]{cert};
        EasyMock.expect(key.getAlgorithm()).andReturn("SHA-512");
        PdfReader reader = mock(PdfReader.class);
        PdfSigner signer = mock(PdfSigner.class);
        EasyMock.expect(signer.getNewSigFieldName()).andReturn("abc");
        signer.setCertificationLevel(1);
        EasyMock.expectLastCall();
        signer.setFieldName("abc");
        EasyMock.expectLastCall();

        PdfSignatureAppearance appearance = mock(PdfSignatureAppearance.class);
        EasyMock.expect(appearance.setContact(EasyMock.anyString())).andReturn(appearance).atLeastOnce();
        EasyMock.expect(appearance.setReason(EasyMock.anyString())).andReturn(appearance).atLeastOnce();
        EasyMock.expect(appearance.setLocation(EasyMock.anyString())).andReturn(appearance).atLeastOnce();
        EasyMock.expect(signer.getSignatureAppearance()).andReturn(appearance).atLeastOnce();

        EasyMock.expect(DigitalSignatureFactory.getPdfReader(EasyMock.anyString())).andReturn(reader).atLeastOnce();
        EasyMock.expect(DigitalSignatureFactory.getPdfSigner(EasyMock.anyObject(PdfReader.class),EasyMock.anyObject(File.class))).andReturn(signer).atLeastOnce();

        Certificates mockCertificates = mock(Certificates.class);
        EasyMock.expect(mockCertificates.getKey()).andReturn(key);
        EasyMock.expect(mockCertificates.getCerts()).andReturn(certificates);
        EasyMock.expect(Certificates.getInstance()).andReturn(mockCertificates).atLeastOnce();

        signer.signDetached(EasyMock.anyObject(IExternalDigest.class),
                EasyMock.anyObject(IExternalSignature.class),
                EasyMock.anyObject(Certificate[].class),
                EasyMock.anyObject(),
                EasyMock.anyObject(IOcspClient.class),
                EasyMock.anyObject(ITSAClient.class),
                EasyMock.anyInt(),
                EasyMock.anyObject(PdfSigner.CryptoStandard.class)
                );
        EasyMock.expectLastCall();

        replayAll();

        File outfile = DigitalSignature.sign(pdf);

        assertThat(outfile, is(not(nullValue())));
    }

    @Test(expected = DigitalSignatureException.class)
    public void signingException() throws DigitalSignatureException, IOException, GeneralSecurityException {

        File pdf = mock(File.class);
        EasyMock.expect(pdf.getName()).andReturn("abc.pdf");
        EasyMock.expect(pdf.getParent()).andReturn("/").atLeastOnce();
        EasyMock.expect(pdf.getPath()).andReturn("/");

        EasyMock.expect(DigitalSignatureFactory.getPdfReader(EasyMock.anyString())).andThrow(new IOException("Test"));

        replayAll();

        DigitalSignature.sign(pdf);
    }

    @Test
    public void testInitialization() {
        DigitalSignature signature = new DigitalSignature();
        assertThat(signature, is(not(nullValue())));
    }
}
