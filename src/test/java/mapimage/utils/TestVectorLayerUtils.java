package mapimage.utils;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import mapimage.TestUtils;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.styling.SLD;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerationapi.models.*;
import pdfgenerator.exceptions.ImageGenerationException;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VectorLayerUtils.class)
public class TestVectorLayerUtils extends EasyMockSupport {

    private static final double[][] COORDINATES = {
        {293107.607,91293.008},
        {293105.77,91295.054},
        {293103.258,91292.798},
        {293107.607,91293.008}
    };
    private static final String TEST_JSON = "{\n" +
                "  \"geometry\":{\n" +
                "    \"type\": \"Polygon\", \n" +
                "    \"coordinates\": [[\n" +
                "      [" + COORDINATES[0][0] + "," + COORDINATES[0][1] + "],\n" +
                "      [" + COORDINATES[1][0] + "," + COORDINATES[1][1] + "],\n" +
                "      [" + COORDINATES[2][0] + "," + COORDINATES[2][1] + "],\n" +
                "      [" + COORDINATES[3][0] + "," + COORDINATES[3][1] + "]\n" +
                "    ]]\n" +
                "  },\n" +
                "\"properties\": {}\n" +
                "}";

    private static final String[] JSON_ARRAY = {TEST_JSON};

    @Before
    public void initialize() {
        PowerMock.mockStatic(SLD.class);
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
    public void testGetFeatureGeometryValid() throws ImageGenerationException {
        Feature feature = TestUtils.buildFeatureCollection(JSON_ARRAY).get(0);

        Geometry geometry = VectorLayerUtils.getGeometry(feature);

        assertThat(geometry.getCoordinates()[0].x, equalTo(COORDINATES[0][0]));
        assertThat(geometry.getCoordinates()[0].y, equalTo(COORDINATES[0][1]));
        assertThat(geometry.getCoordinates()[1].x, equalTo(COORDINATES[1][0]));
        assertThat(geometry.getCoordinates()[1].y, equalTo(COORDINATES[1][1]));
        assertThat(geometry.getCoordinates()[2].x, equalTo(COORDINATES[2][0]));
        assertThat(geometry.getCoordinates()[2].y, equalTo(COORDINATES[2][1]));
        assertThat(geometry.getCoordinates()[3].x, equalTo(COORDINATES[3][0]));
        assertThat(geometry.getCoordinates()[3].y, equalTo(COORDINATES[3][1]));
    }

    @Test
    public void testGetFeatureGeometryInvalid() throws Exception {
        Feature feature = TestUtils.buildFeatureCollection(JSON_ARRAY).get(0);
        GeometryJSON geometryJSONMock = mock(GeometryJSON.class);

        PowerMock.expectNew(GeometryJSON.class)
                .andReturn(geometryJSONMock);
        EasyMock.expect(geometryJSONMock.read(EasyMock.anyString()))
                .andThrow(new IOException("Test exception"));

        replayAll();

        try {
            VectorLayerUtils.getGeometry(feature);
            fail("ImageGenerationException not thrown");
        }
        catch (ImageGenerationException e) {
            assertThat(e.getMessage(), equalTo("Failed to convert vector to json string"));
            verifyAll();
        }
    }

    @Test
    public void testGetSimpleFeatureCollection() throws Exception {
        List<Feature> features = TestUtils.buildFeatureCollection(JSON_ARRAY);
        SimpleFeatureBuilder sfbMock = mock(SimpleFeatureBuilder.class);
        SimpleFeatureType sftMock = mock(SimpleFeatureType.class);
        DefaultFeatureCollection sfcMock = mock(DefaultFeatureCollection.class);
        SimpleFeature sfMock = mock(SimpleFeature.class);

        PowerMock.expectNew(DefaultFeatureCollection.class, "internal", sftMock)
                .andReturn(sfcMock)
                .once();

        PowerMock.expectNew(SimpleFeatureBuilder.class, sftMock)
                .andReturn(sfbMock)
                .times(features.size());

        sfbMock.add(EasyMock.anyObject(SimpleFeatureType.class));
        EasyMock.expectLastCall()
                .times(features.size());

        EasyMock.expect(sfbMock.buildFeature(null))
                .andReturn(sfMock)
                .times(features.size());

        EasyMock.expect(sfcMock.add(sfMock))
                .andReturn(true)
                .times(features.size());

        replayAll();

        SimpleFeatureCollection featureCollection = VectorLayerUtils.getSimpleFeatureCollection(features, sftMock);

        verifyAll();

        assertThat(featureCollection, is(not(nullValue())));
    }

    private static final String POINT_TYPE = "Point";
    private static final String LINE_STRING_TYPE = "LineString";
    private static final String POLYGON_TYPE = "Polygon";

    @Test
    public void testFilterByGeometryTypePoint() throws Exception {
        Feature featurePoint = new Feature();
        featurePoint.setGeometry(new pdfgenerationapi.models.Geometry());
        featurePoint.getGeometry().setType(POINT_TYPE);

        Feature featureLineString = new Feature();
        featureLineString.setGeometry(new pdfgenerationapi.models.Geometry());
        featureLineString.getGeometry().setType(LINE_STRING_TYPE);

        Feature featurePolygon = new Feature();
        featurePolygon.setGeometry(new pdfgenerationapi.models.Geometry());
        featurePolygon.getGeometry().setType(POLYGON_TYPE);

        List<Feature> features = Lists.newArrayList(featurePoint, featureLineString, featurePolygon);

        List<Feature> points = VectorLayerUtils.filterByGeometryType(features, POINT_TYPE);
        assertTrue("Should only return features with point type if point is passed as the geometry type.",
            points.contains(featurePoint)
        );

        List<Feature> lineStrings = VectorLayerUtils.filterByGeometryType(features, LINE_STRING_TYPE);
        assertTrue("Should only return features with lineString type if lineString is passed as the geometry type.",
            lineStrings.contains(featureLineString)
        );

        List<Feature> polygons = VectorLayerUtils.filterByGeometryType(features, POLYGON_TYPE);
        assertTrue("Should only return features with Polygon type if Polygon is passed as the geometry type.",
            polygons.contains(featurePolygon)
        );
    }

}
