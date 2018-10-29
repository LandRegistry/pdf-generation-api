package pdfgenerator.security;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.Config;
import pdfgenerationapi.TestUtils;
import pdfgenerator.exceptions.DigitalSignatureException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Config.class})
public class TestCertificatesException extends EasyMockSupport {

    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironmentBadCert();
    }

    @After
    public void after() {
        resetAll();
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

    @Test(expected = DigitalSignatureException.class)
    public void testException() throws Exception {
        Certificates.getInstance().getKey();
    }
}
