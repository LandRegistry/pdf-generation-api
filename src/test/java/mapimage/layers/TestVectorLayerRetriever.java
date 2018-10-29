package mapimage.layers;

import com.google.common.collect.Lists;
import mapimage.TestUtils;
import mapimage.builder.vector.LineVectorLayerBuilder;
import mapimage.builder.vector.PointVectorLayerBuilder;
import mapimage.builder.vector.PolygonVectorLayerBuilder;
import mapimage.builder.vector.MultiPolygonVectorLayerBuilder;
import mapimage.builder.vector.VectorLayerBuilder;
import mapimage.utils.VectorLayerUtils;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.map.FeatureLayer;
import org.geotools.styling.SLD;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.models.Feature;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VectorLayerRetriever.class, FeatureLayer.class, SLD.class, VectorLayerUtils.class})
public class TestVectorLayerRetriever extends EasyMockSupport {

    private static final double[][] coords = new double[][]{
        {293107.607,91293.008},
        {293105.77,91295.054},
        {293103.258,91292.798},
        {293107.607,91293.008}
    };
    private static final String testJson = "{\n" +
                "  \"geometry\":{\n" +
                "    \"type\": \"Polygon\", \n" +
                "    \"coordinates\": [[\n" +
                "      [" + coords[0][0] + "," + coords[0][1] + "],\n" +
                "      [" + coords[1][0] + "," + coords[1][1] + "],\n" +
                "      [" + coords[2][0] + "," + coords[2][1] + "],\n" +
                "      [" + coords[3][0] + "," + coords[3][1] + "]\n" +
                "    ]]\n" +
                "  },\n" +
                "\"properties\": {}\n" +
                "}";

    private static final String[] jsonArray = new String[] { testJson };

    @Before
    public void initialize() {
        PowerMock.mockStatic(SLD.class);
        PowerMock.mockStatic(VectorLayerUtils.class);
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
    public void TestGetLayersAllGeometryTypes() throws Exception {
        List<Feature> featureCollection = TestUtils.buildFeatureCollection(jsonArray);
        FeatureLayer pointLayerMock = mock(FeatureLayer.class);
        FeatureLayer lineLayerMock = mock(FeatureLayer.class);
        FeatureLayer polygonLayerMock = mock(FeatureLayer.class);
        FeatureLayer multiPolygonFeatureLayerMock = mock(FeatureLayer.class);

        setupMocksForGetLayers(pointLayerMock, lineLayerMock, polygonLayerMock, multiPolygonFeatureLayerMock);

        replayAll();

        List<FeatureLayer> featureLayer = VectorLayerRetriever.getLayers(featureCollection);

        verifyAll();
        assertThat(featureLayer, is(not(nullValue())));
        assertThat(featureLayer.size(), equalTo(4));
    }

    @Test
    public void TestGetLayersSingleGeometryType() throws Exception {
        List<Feature> featureCollection = TestUtils.buildFeatureCollection(jsonArray);

        FeatureLayer pointLayerMock = mock(FeatureLayer.class);
        FeatureLayer lineLayerMock = null;
        FeatureLayer polygonLayerMock = null;
        FeatureLayer multiPolygonFeatureLayerMock = null;

        setupMocksForGetLayers(pointLayerMock, lineLayerMock, polygonLayerMock, multiPolygonFeatureLayerMock);

        replayAll();

        List<FeatureLayer> featureLayer = VectorLayerRetriever.getLayers(featureCollection);

        verifyAll();
        assertThat(featureLayer, is(not(nullValue())));
        assertThat(featureLayer.size(), equalTo(1));
    }

    private void setupMocksForGetLayers(FeatureLayer pointFeatureLayerMock,
                                                FeatureLayer lineFeatureLayerMock,
                                                FeatureLayer polygonFeatureLayerMock,
                                                FeatureLayer multiPolygonFeatureLayerMock) throws Exception {
        VectorLayerBuilder pointBuilderMock = mock(PointVectorLayerBuilder.class);
        VectorLayerBuilder lineBuilderMock = mock(LineVectorLayerBuilder.class);
        VectorLayerBuilder polygonBuilderMock = mock(PolygonVectorLayerBuilder.class);
        VectorLayerBuilder multiPolygonBuilderMock = mock(MultiPolygonVectorLayerBuilder.class);
        
        List<Feature> features = Lists.newArrayList(mock(Feature.class));

        PowerMock.expectNew(PointVectorLayerBuilder.class)
                .andReturn((PointVectorLayerBuilder)pointBuilderMock)
                .once();

        PowerMock.expectNew(LineVectorLayerBuilder.class)
                .andReturn((LineVectorLayerBuilder)lineBuilderMock)
                .once();

        PowerMock.expectNew(PolygonVectorLayerBuilder.class)
                .andReturn((PolygonVectorLayerBuilder)polygonBuilderMock)
                .once();
        
        PowerMock.expectNew(MultiPolygonVectorLayerBuilder.class)
        		.andReturn((MultiPolygonVectorLayerBuilder)multiPolygonBuilderMock)
        		.once();

        EasyMock.expect(VectorLayerUtils.filterByGeometryType(EasyMock.anyObject(), EasyMock.anyString()))
                .andReturn(features)
                .times(4);

        EasyMock.expect(pointBuilderMock.get(EasyMock.anyObject()))
                .andReturn(pointFeatureLayerMock)
                .once();

        EasyMock.expect(lineBuilderMock.get(EasyMock.anyObject()))
                .andReturn(lineFeatureLayerMock)
                .once();

        EasyMock.expect(polygonBuilderMock.get(EasyMock.anyObject()))
                .andReturn(polygonFeatureLayerMock)
                .once();
        
        EasyMock.expect(multiPolygonBuilderMock.get(EasyMock.anyObject()))
        		.andReturn(multiPolygonFeatureLayerMock)
        		.once();

    }

}
