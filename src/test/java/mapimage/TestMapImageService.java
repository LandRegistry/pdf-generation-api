package mapimage;

import com.google.common.collect.Lists;
import mapimage.builder.MapContentBuilder;
import mapimage.builder.MapImageBuilder;
import mapimage.builder.WebMapServerClientBuilder;
import mapimage.layers.WMSLayerRetriever;
import mapimage.layers.VectorLayerRetriever;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.WMSLayer;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.TestUtils;
import pdfgenerationapi.models.Feature;
import pdfgenerator.exceptions.ImageGenerationException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;

import static org.easymock.EasyMock.eq;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    MapImageService.class,
    CRS.class, MapContentBuilder.class,
    WebMapServerClientBuilder.class,
    WMSLayerRetriever.class,
    VectorLayerRetriever.class,
    MapImageBuilder.class,
    MapContentBuilder.class
})
public class TestMapImageService extends EasyMockSupport {

    private static String BASE_WMS_SERVER_URL;
    private static String GEOSERVER_WMS_SERVER_URL;

    @Before
    public void initialize() throws Exception {
        TestUtils.SetupEnvironment();

        BASE_WMS_SERVER_URL = "http://www.google.com/";
        GEOSERVER_WMS_SERVER_URL = "http://www.google.co.uk/";

        PowerMock.mockStatic(WebMapServerClientBuilder.class);
        PowerMock.mockStatic(CRS.class);
        PowerMock.mockStatic(WMSLayerRetriever.class);
        PowerMock.mockStatic(VectorLayerRetriever.class);
        PowerMock.mockStatic(MapImageBuilder.class);
        PowerMock.mockStatic(MapContentBuilder.class);
    }

    @After
    public void after() throws NoSuchFieldException, IllegalAccessException {
        super.resetAll();
        PowerMock.resetAll();
        resetMapImageService();
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

    private static final int WIDTH = 175;
    private static final int HEIGHT = 175;

    @Test
    public void TestSuccessfulCreationWithFeatures() throws Exception {
        setupMocksForGenerateImage(BASE_WMS_SERVER_URL, GEOSERVER_WMS_SERVER_URL,0, WIDTH, 0, HEIGHT);

        List<Feature> featureList = Lists.newArrayList(mock(Feature.class));

        replayAll();

        MapImageService mapImageService = MapImageService.getInstance();
        BufferedImage image = mapImageService.generateImage(featureList, WIDTH, HEIGHT);

        verifyAll();
        assertThat(image, is(not(nullValue())));
    }

    @Test
    public void TestSuccessfulCreationWithoutFeatures() throws Exception {
        setupMocksForGenerateImage(BASE_WMS_SERVER_URL, GEOSERVER_WMS_SERVER_URL, 0, WIDTH, 0, HEIGHT);

        replayAll();

        MapImageService mapImageService = MapImageService.getInstance();

        try {
            mapImageService.generateImage(Lists.newArrayList(), WIDTH, HEIGHT);
            fail("Feature collection validation failed");
        } catch (ImageGenerationException e) {
            assertThat(e, isA(ImageGenerationException.class));
            assertThat(e.getMessage(), equalTo("Error generating map image, no features defined in feature collection"));
        }
    }

    @Test
    public void TestGetInstance() throws Exception {
        CoordinateReferenceSystem crsMock = mock(CoordinateReferenceSystem.class);
        Layer baseLayer = mock(Layer.class);
        Layer nonMigratedLayer = mock(Layer.class);
        WebMapServer baseWmsMock = mock(WebMapServer.class);
        WebMapServer geoserverWmsMock = mock(WebMapServer.class);
        setupMocksForConstructor(baseWmsMock, geoserverWmsMock, crsMock, baseLayer, nonMigratedLayer, BASE_WMS_SERVER_URL, GEOSERVER_WMS_SERVER_URL);

        replayAll();

        // Call verifyAll twice to ensure constructor hasn't been called twice
        MapImageService mapImageService = MapImageService.getInstance();
        verifyAll();

        MapImageService mapImageService2 = MapImageService.getInstance();
        verifyAll();

        assertThat(mapImageService, is(not(nullValue())));
        assertThat(mapImageService2, is(not(nullValue())));
        assertThat(mapImageService, equalTo(mapImageService2));
    }

    @Test
    public void TestGetMinBoundsForPoint() throws Exception {
        ReferencedEnvelope minBoundsMock = setupMocksForGenerateImage(BASE_WMS_SERVER_URL, GEOSERVER_WMS_SERVER_URL, 0, 0, 0, 0);

        minBoundsMock.expandBy(20d);
        EasyMock.expectLastCall()
                .once();

        List<Feature> featureList = Lists.newArrayList(mock(Feature.class));

        replayAll();

        MapImageService mapImageService = MapImageService.getInstance();
        BufferedImage image = mapImageService.generateImage(featureList, WIDTH, HEIGHT);

        verifyAll();

        assertThat(image, is(not(nullValue())));
    }

    private void setupMocksForConstructor(WebMapServer baseWmsMock, WebMapServer geoserverWmsMock,
                                          CoordinateReferenceSystem crs, Layer baseLayer, Layer nonMigratedLayer,
                                          String wmsServerUrl, String geoserverUrl) throws Exception {
        EasyMock.expect(WebMapServerClientBuilder.build(eq(wmsServerUrl)))
                .andReturn(baseWmsMock);

        EasyMock.expect(WebMapServerClientBuilder.build(eq(geoserverUrl)))
                .andReturn(geoserverWmsMock);

        EasyMock.expect(CRS.decode(EasyMock.anyString()))
                .andReturn(crs)
                .once();

        EasyMock.expect(WMSLayerRetriever.get(EasyMock.anyString(), eq(baseWmsMock)))
                .andReturn(baseLayer)
                .once();

        EasyMock.expect(WMSLayerRetriever.get(EasyMock.anyString(), EasyMock.anyString(), eq(geoserverWmsMock)))
                .andReturn(nonMigratedLayer)
                .once();
    }

    private ReferencedEnvelope setupMocksForGenerateImage(String baseWmsServerUrl,
                                                          String geoserverWmsServerUrl,
                                                          double boundsMinX,
                                                          double boundsMaxX,
                                                          double boundsMinY,
                                                          double boundsMaxY) throws Exception {
        CoordinateReferenceSystem crs = mock(CoordinateReferenceSystem.class);
        Layer baseLayer = mock(Layer.class);
        Layer nonMigratedLayer = mock(Layer.class);
        WebMapServer baseWmsMock = mock(WebMapServer.class);
        WebMapServer geoserverWmsMock = mock(WebMapServer.class);

        setupMocksForConstructor(baseWmsMock, geoserverWmsMock, crs, baseLayer, nonMigratedLayer, baseWmsServerUrl, geoserverWmsServerUrl);

        WMSLayer baseWMSLayer = mock(WMSLayer.class);
        WMSLayer nonMigratedWMSLayer = mock(WMSLayer.class);
        FeatureLayer featureLayer = mock(FeatureLayer.class);
        ReferencedEnvelope featureLayerBounds = mock(ReferencedEnvelope.class);
        ReferencedEnvelope mapBounds = mock(ReferencedEnvelope.class);
        MapContent mapContent = mock(MapContent.class);
        BufferedImage mapImage = mock(BufferedImage.class);
        ReferencedEnvelope minBounds = mock(ReferencedEnvelope.class);

        List<FeatureLayer> featureLayers = Lists.newArrayList(featureLayer);

        PowerMock.expectNew(WMSLayer.class, baseWmsMock, baseLayer)
                .andReturn(baseWMSLayer)
                .once();

        PowerMock.expectNew(WMSLayer.class, geoserverWmsMock, nonMigratedLayer)
                .andReturn(nonMigratedWMSLayer)
                .once();

        EasyMock.expect(VectorLayerRetriever.getLayers(EasyMock.anyObject()))
                .andReturn(featureLayers)
                .once();

        PowerMock.expectNew(ReferencedEnvelope.class)
                .andReturn(minBounds)
                .once();

        EasyMock.expect(featureLayer.getBounds())
                .andReturn(featureLayerBounds)
                .once();

        minBounds.expandToInclude(featureLayerBounds);
        EasyMock.expectLastCall()
                .once();

        EasyMock.expect(minBounds.getMinX())
                .andReturn(boundsMinX)
                .once();

        EasyMock.expect(minBounds.getMaxX())
                .andReturn(boundsMaxX)
                .once();

        EasyMock.expect(minBounds.getMinY())
                .andReturn(boundsMinY)
                .once();

        EasyMock.expect(minBounds.getMaxY())
            .andReturn(boundsMaxY)
            .once();

        EasyMock.expect(
                MapImageBuilder.getMapBounds(
                        EasyMock.anyObject(ReferencedEnvelope.class),
                        EasyMock.anyObject(Rectangle.class),
                        EasyMock.anyObject(CoordinateReferenceSystem.class)))
                .andReturn(mapBounds)
                .once();

        EasyMock.expect(
                MapContentBuilder.build(
                        EasyMock.anyObject(),
                        EasyMock.anyObject(CoordinateReferenceSystem.class),
                        EasyMock.anyObject(ReferencedEnvelope.class)))
                .andReturn(mapContent)
                .once();

        EasyMock.expect(
                MapImageBuilder.build(
                        EasyMock.anyObject(MapContent.class),
                        EasyMock.anyObject(Rectangle.class),
                        EasyMock.anyObject(ReferencedEnvelope.class)))
                .andReturn(mapImage)
                .once();

        mapContent.dispose();
        EasyMock.expectLastCall()
                .once();

        return minBounds;
    }

    private static void resetMapImageService() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to reset singleton instance to null
        Field instance = MapImageService.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

}
