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

import java.security.PrivateKey;
import java.security.cert.Certificate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Config.class})
public class TestCertificates extends EasyMockSupport {

    @BeforeClass
    public static void suitSetup() throws Exception {
        TestUtils.SetupEnvironment();
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

    @Test
    public void testInstance() throws Exception {
        TestUtils.SetupEnvironment();
        Certificates certs = Certificates.getInstance();
        assertThat(certs, is(not(nullValue())));
    }

    @Test
    public void testGetCert() throws Exception {
        TestUtils.SetupEnvironment();
        Certificate[] certs = Certificates.getInstance().getCerts();
        assertThat(certs, is(not(nullValue())));
    }

    @Test
    public void testGetKey() throws Exception {
        TestUtils.SetupEnvironment();
        PrivateKey key = Certificates.getInstance().getKey();
        assertThat(key, is(not(nullValue())));
    }
}
