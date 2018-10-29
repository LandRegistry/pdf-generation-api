package mapimage.builder.vector;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import mapimage.TestUtils;
import mapimage.utils.VectorLayerUtils;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
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

import java.awt.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SimpleFeatureTypeBuilder.class, PolygonVectorLayerBuilder.class, FeatureLayer.class,
        DefaultFeatureCollection.class, CollectionFeatureSource.class, SLD.class, VectorLayerUtils.class})
public class TestPolygonVectorLayerBuilder extends EasyMockSupport {

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
    public void getFeatureTypeBuilder() throws Exception {
        SimpleFeatureTypeBuilder builder = mock(SimpleFeatureTypeBuilder.class);
        PowerMock.expectNew(SimpleFeatureTypeBuilder.class).andReturn(builder);
        builder.setName( "PolygonFeature" );
        EasyMock.expectLastCall();
        builder.add( "polygon", Polygon.class );
        EasyMock.expectLastCall();

        replayAll();

        PolygonVectorLayerBuilder polygonVectorLayerBuilder = new PolygonVectorLayerBuilder();
        SimpleFeatureTypeBuilder result = polygonVectorLayerBuilder.getFeatureTypeBuilder();

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(builder));

    }

    @Test
    public void TestGet() throws Exception {

        SimpleFeatureTypeBuilder builder = mock(SimpleFeatureTypeBuilder.class);
        PowerMock.expectNew(SimpleFeatureTypeBuilder.class).andReturn(builder);
        builder.setName( "PolygonFeature" );
        EasyMock.expectLastCall();
        builder.add( "polygon", Polygon.class );
        EasyMock.expectLastCall();

        List<Feature> featureCollection = TestUtils.buildFeatureCollection(TestVectorLayerBuilder.buildJsonArray());

        SimpleFeatureType type = mock(SimpleFeatureType.class);
        SimpleFeature simpleFeature = mock(SimpleFeature.class);
        SimpleFeatureBuilder simpleFeatureBuilder = mock(SimpleFeatureBuilder.class);
        simpleFeatureBuilder.add(EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.expect(simpleFeatureBuilder.buildFeature(null)).andReturn(simpleFeature).atLeastOnce();
        PowerMock.expectNew(SimpleFeatureBuilder.class, type).andReturn(simpleFeatureBuilder);

        Geometry geo = mock(Geometry.class);
        EasyMock.expect(VectorLayerUtils.getGeometry(featureCollection.get(0))).andReturn(geo).atLeastOnce();

        EasyMock.expect(builder.buildFeatureType()).andReturn(type).atLeastOnce();

        DefaultFeatureCollection collection = mock(DefaultFeatureCollection.class);
        EasyMock.expect(collection.add(EasyMock.anyObject(SimpleFeature.class))).andReturn(true).atLeastOnce();
        PowerMock.expectNew(DefaultFeatureCollection.class, "internal",type).andReturn(collection);

        CollectionFeatureSource featureSource = mock(CollectionFeatureSource.class);
        PowerMock.expectNew(CollectionFeatureSource.class,collection).andReturn(featureSource);

        Style mockStyle = mock(Style.class);

        EasyMock.expect(SLD.createPolygonStyle(Color.BLUE, Color.BLUE, 0.1f)).andReturn(mockStyle).atLeastOnce();

        FeatureLayer mockLayer = mock(FeatureLayer.class);

        PowerMock.expectNew(FeatureLayer.class,featureSource,mockStyle).andReturn(mockLayer);

        replayAll();

        PolygonVectorLayerBuilder polygonVectorLayerBuilder = new PolygonVectorLayerBuilder();

        FeatureLayer result = polygonVectorLayerBuilder.get(featureCollection);

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(mockLayer));

        verifyAll();

    }
}
