package mapimage.builder;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pdfgenerator.exceptions.ImageGenerationException;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MapImageBuilder.class})
public class TestMapImageBuilder extends EasyMockSupport {

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
    public void testBuild() throws Exception {
        BufferedImage imageMock = mock(BufferedImage.class);
        MapContent mapContentMock = mock(MapContent.class);
        ReferencedEnvelope mapBoundsMock = mock(ReferencedEnvelope.class);
        StreamingRenderer streamingRendererMock = mock(StreamingRenderer.class);
        RenderErrorListener renderListenMock = mock(RenderErrorListener.class);
        Graphics2D graphicsMock = mock(Graphics2D.class);

        Rectangle imageBounds = new Rectangle(0, 0, 175, 175);

        // drawMap mocks
        PowerMock.expectNew(BufferedImage.class, imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB)
                .andReturn(imageMock)
                .once();
        PowerMock.expectNew(StreamingRenderer.class)
                .andReturn(streamingRendererMock)
                .once();
        PowerMock.expectNew(RenderErrorListener.class, streamingRendererMock)
        		.andReturn(renderListenMock)
        		.once();
        streamingRendererMock.addRenderListener(EasyMock.anyObject(RenderErrorListener.class));
        EasyMock.expectLastCall();
        streamingRendererMock.setMapContent(mapContentMock);
        EasyMock.expectLastCall();
        streamingRendererMock.setJava2DHints(EasyMock.anyObject(RenderingHints.class));
        EasyMock.expectLastCall();
        EasyMock.expect(imageMock.createGraphics())
                .andReturn(graphicsMock)
                .once();
        graphicsMock.setPaint(EasyMock.anyObject(Color.class));
        EasyMock.expectLastCall();
        graphicsMock.fill(imageBounds);
        EasyMock.expectLastCall();
        streamingRendererMock.paint(graphicsMock, imageBounds, mapBoundsMock);
        EasyMock.expectLastCall();
        EasyMock.expect(renderListenMock.getExceptionThrown())
        		.andReturn(null)
        		.once();

        // drawCopyright mocks
        graphicsMock.setFont(EasyMock.anyObject(Font.class));
        EasyMock.expectLastCall();
        graphicsMock.setColor(EasyMock.anyObject(Color.class));
        EasyMock.expectLastCall();
        graphicsMock.drawString(EasyMock.anyString(), EasyMock.anyInt(), EasyMock.anyInt());
        EasyMock.expectLastCall();

        replayAll();

        BufferedImage mapImage = MapImageBuilder.build(mapContentMock, imageBounds, mapBoundsMock);

        verifyAll();
        assertThat(mapImage, is(not(nullValue())));
    }

    @Test
    public void testBuildExcept() throws Exception {
        BufferedImage imageMock = mock(BufferedImage.class);
        MapContent mapContentMock = mock(MapContent.class);
        ReferencedEnvelope mapBoundsMock = mock(ReferencedEnvelope.class);
        StreamingRenderer streamingRendererMock = mock(StreamingRenderer.class);
        RenderErrorListener renderListenMock = mock(RenderErrorListener.class);
        Graphics2D graphicsMock = mock(Graphics2D.class);

        Rectangle imageBounds = new Rectangle(0, 0, 175, 175);

        // drawMap mocks
        PowerMock.expectNew(BufferedImage.class, imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB)
                .andReturn(imageMock)
                .once();
        PowerMock.expectNew(StreamingRenderer.class)
                .andReturn(streamingRendererMock)
                .once();
        PowerMock.expectNew(RenderErrorListener.class, streamingRendererMock)
        		.andReturn(renderListenMock)
        		.once();
        streamingRendererMock.addRenderListener(EasyMock.anyObject(RenderErrorListener.class));
        EasyMock.expectLastCall();
        streamingRendererMock.setMapContent(mapContentMock);
        EasyMock.expectLastCall();
        streamingRendererMock.setJava2DHints(EasyMock.anyObject(RenderingHints.class));
        EasyMock.expectLastCall();
        EasyMock.expect(imageMock.createGraphics())
                .andReturn(graphicsMock)
                .once();
        graphicsMock.setPaint(EasyMock.anyObject(Color.class));
        EasyMock.expectLastCall();
        graphicsMock.fill(imageBounds);
        EasyMock.expectLastCall();
        streamingRendererMock.paint(graphicsMock, imageBounds, mapBoundsMock);
        EasyMock.expectLastCall();
        EasyMock.expect(renderListenMock.getExceptionThrown())
        		.andReturn(new Exception("blah"))
        		.times(2);

        replayAll();
        Exception exception = null;
        try {
        	MapImageBuilder.build(mapContentMock, imageBounds, mapBoundsMock);
        } catch (Exception e) {
        	exception = e;
        }
        assertTrue(exception instanceof ImageGenerationException);

        verifyAll();
    }

    @Test
    public void testGetMapNoResize() throws Exception {
        double expectedMinX = 0;
        double expectedMinY = 0;
        double expectedMaxX = 500;
        double expectedMaxY = 500;

        runGetMapBounds(0, 0, 500, 500, 200, 200, expectedMinX, expectedMinY, expectedMaxX, expectedMaxY);
    }

    @Test
    public void testGetMapBoundsImageWider() throws Exception {
        double minX = 0;
        double minY = 0;
        double maxX = 800;
        double maxY = 1000;
        int imageBoundsX = 200;
        int imageBoundsY = 150;
        double expectedMinX = -480;
        double expectedMinY = -160;
        double expectedMaxX = 1280;
        double expectedMaxY = 1160;

        runGetMapBounds(minX, minY, maxX, maxY, imageBoundsX, imageBoundsY, expectedMinX, expectedMinY, expectedMaxX, expectedMaxY);
    }

    @Test
    public void testGetMapBoundsImageTaller() throws Exception {
        double minX = 0;
        double minY = 0;
        double maxX = 800;
        double maxY = 1000;
        int imageBoundsX = 150;
        int imageBoundsY = 200;
        double expectedMinX = -260;
        double expectedMinY = -380;
        double expectedMaxX = 1060;
        double expectedMaxY = 1380;

        runGetMapBounds(minX, minY, maxX, maxY, imageBoundsX, imageBoundsY, expectedMinX, expectedMinY, expectedMaxX, expectedMaxY);
    }

    private void runGetMapBounds(double minX,
                                 double minY,
                                 double maxX,
                                 double maxY,
                                 int imageBoundsX,
                                 int imageBoundsY,
                                 double expectedMinX,
                                 double expectedMinY,
                                 double expectedMaxX,
                                 double expectedMaxY) throws Exception {
        ReferencedEnvelope featureLayerBoundsMock = mock(ReferencedEnvelope.class);
        CoordinateReferenceSystem crsMock = CRS.decode("EPSG:27700");
        Rectangle imageBounds = new Rectangle(0, 0,imageBoundsX, imageBoundsY);

        EasyMock.expect(featureLayerBoundsMock.getMinX())
                .andReturn(minX)
                .once();
        EasyMock.expect(featureLayerBoundsMock.getMaxX())
                .andReturn(maxX)
                .once();
        EasyMock.expect(featureLayerBoundsMock.getMinY())
                .andReturn(minY)
                .once();
        EasyMock.expect(featureLayerBoundsMock.getMaxY())
                .andReturn(maxY)
                .once();

        replayAll();

        ReferencedEnvelope mapBounds = MapImageBuilder.getMapBounds(featureLayerBoundsMock, imageBounds, crsMock);

        verifyAll();
        assertThat(mapBounds, is(not(nullValue())));
        assertThat(mapBounds.getMinX(), equalTo(expectedMinX));
        assertThat(mapBounds.getMinY(), equalTo(expectedMinY));
        assertThat(mapBounds.getMaxX(), equalTo(expectedMaxX));
        assertThat(mapBounds.getMaxY(), equalTo(expectedMaxY));
    }
}
