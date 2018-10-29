package mapimage.builder.vector;

import com.vividsolutions.jts.geom.Geometry;
import mapimage.TestUtils;
import mapimage.utils.VectorLayerUtils;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.map.FeatureLayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.models.Feature;
import pdfgenerator.exceptions.ImageGenerationException;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    VectorLayerUtils.class,
    VectorLayerBuilder.class,
    DefaultFeatureCollection.class,
    CollectionFeatureSource.class
})
public class TestVectorLayerBuilder extends EasyMockSupport {
    private SimpleFeatureTypeBuilder mockBuilder;

    @Before
    public void initialize() {
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
    public void testGetSimpleFeature() throws Exception{
        VectorLayerBuilder builder = new VectorLayerBuilder() {
            @Override
            public FeatureLayer get(List<Feature> featureCollection) throws ImageGenerationException {
                return null;
            }

            @Override
            SimpleFeatureTypeBuilder getFeatureTypeBuilder() {
                return null;
            }
        };

        List<Feature> featureCollection = TestUtils.buildFeatureCollection(buildJsonArray());

        SimpleFeatureType type = mock(SimpleFeatureType.class);
        SimpleFeature simpleFeature = mock(SimpleFeature.class);
        SimpleFeatureBuilder simpleFeatureBuilder = mock(SimpleFeatureBuilder.class);
        simpleFeatureBuilder.add(EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.expect(simpleFeatureBuilder.buildFeature(null)).andReturn(simpleFeature).atLeastOnce();
        PowerMock.expectNew(SimpleFeatureBuilder.class, type).andReturn(simpleFeatureBuilder);

        Geometry geo = mock(Geometry.class);
        EasyMock.expect(VectorLayerUtils.getGeometry(featureCollection.get(0))).andReturn(geo).atLeastOnce();

        replayAll();

        SimpleFeature result = builder.getSimpleFeature(featureCollection.get(0),type);

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(simpleFeature));

        verifyAll();
    }

    @Test
    public void testGetFeatureSource() throws Exception {
        VectorLayerBuilder builder = new VectorLayerBuilder() {
            @Override
            public FeatureLayer get(List<Feature> featureCollection) throws ImageGenerationException {
                return null;
            }

            @Override
            SimpleFeatureTypeBuilder getFeatureTypeBuilder() {
                return mockBuilder;
            }
        };

        List<Feature> featureCollection = TestUtils.buildFeatureCollection(buildJsonArray());

        SimpleFeatureType type = mock(SimpleFeatureType.class);
        SimpleFeature simpleFeature = mock(SimpleFeature.class);
        SimpleFeatureBuilder simpleFeatureBuilder = mock(SimpleFeatureBuilder.class);
        simpleFeatureBuilder.add(EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.expect(simpleFeatureBuilder.buildFeature(null)).andReturn(simpleFeature).atLeastOnce();
        PowerMock.expectNew(SimpleFeatureBuilder.class, type).andReturn(simpleFeatureBuilder);

        Geometry geo = mock(Geometry.class);
        EasyMock.expect(VectorLayerUtils.getGeometry(featureCollection.get(0))).andReturn(geo).atLeastOnce();

        mockBuilder = mock(SimpleFeatureTypeBuilder.class);
        EasyMock.expect(mockBuilder.buildFeatureType()).andReturn(type).atLeastOnce();

        DefaultFeatureCollection collection = mock(DefaultFeatureCollection.class);
        EasyMock.expect(collection.add(EasyMock.anyObject(SimpleFeature.class))).andReturn(true).atLeastOnce();
        PowerMock.expectNew(DefaultFeatureCollection.class, "internal",type).andReturn(collection);

        CollectionFeatureSource featureSource = mock(CollectionFeatureSource.class);
        PowerMock.expectNew(CollectionFeatureSource.class,collection).andReturn(featureSource);

        replayAll();

        SimpleFeatureSource source = builder.getFeatureSource(featureCollection);
        assertThat(source, is(not(nullValue())));
        assertThat(source, is(featureSource));

        verifyAll();
    }

   public static String[] buildJsonArray() {
        double[][] coords = new double[][]{
                {293107.607,91293.008},
                {293105.77,91295.054},
                {293103.258,91292.798},
                {293107.607,91293.008}
        };

        String testJson = "{\n" +
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

        return new String[] { testJson };
    }
}
