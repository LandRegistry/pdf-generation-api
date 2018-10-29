package mapimage.builder;

import com.google.common.collect.Lists;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MapContentBuilder.class})
public class TestMapContentBuilder extends EasyMockSupport {

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
        MapContent mapContentMock = EasyMock.createMock(MapContent.class);
        MapViewport mapViewportMock = EasyMock.createMock(MapViewport.class);
        CoordinateReferenceSystem crsMock = mock(CoordinateReferenceSystem.class);
        ReferencedEnvelope mapBoundsMock = mock(ReferencedEnvelope.class);

        PowerMock.expectNew(MapContent.class)
                .andReturn(mapContentMock)
                .once();
        PowerMock.expectNew(MapViewport.class)
                .andReturn(mapViewportMock)
                .once();

        replayAll();

        MapContent mapContent = MapContentBuilder.build(Lists.newArrayList(), crsMock, mapBoundsMock);

        PowerMock.verifyAll();
        assertThat(mapContent, is(not(nullValue())));
    }
}
