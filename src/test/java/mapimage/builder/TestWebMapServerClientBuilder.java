package mapimage.builder;

import org.easymock.EasyMockSupport;
import org.geotools.data.wms.WebMapServer;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WebMapServerClientBuilder.class})
public class TestWebMapServerClientBuilder extends EasyMockSupport {

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
    public void TestBuild() throws Exception {
        WebMapServer webMapServer = mock(WebMapServer.class);
        String urlPath = "http://www.google.com";
        URL url = new URL(urlPath);

        PowerMock.expectNew(WebMapServer.class, url)
                .andReturn(webMapServer)
                .once();

        PowerMock.replayAll();

        WebMapServer result = WebMapServerClientBuilder.build(urlPath);
        PowerMock.verifyAll();

        assertThat(result, is(not(nullValue())));
    }

}
