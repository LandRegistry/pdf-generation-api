package mapimage.layers;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pdfgenerator.exceptions.ImageGenerationException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WMSLayerRetriever.class, WMSUtils.class})
public class TestWMSLayerRetriever extends EasyMockSupport {

    @Before
    public void initialize() {
        PowerMock.mockStatic(WMSUtils.class);
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
    public void TestGetLayerFound() throws ImageGenerationException {
        String layerName = "layer1";
        List<Layer> layers = new ArrayList<>();
        layers.add(getLayerMock("layer1"));
        layers.add(getLayerMock("layer2"));
        layers.add(getLayerMock("layer3"));
        WebMapServer wmsMock = setupWmsMock(layers);

        replayAll();

        Layer layer = WMSLayerRetriever.get(layerName, wmsMock);

        verifyAll();
        assertThat(layer, is(not(nullValue())));
    }

    @Test(expected = ImageGenerationException.class)
    public void TestGetLayerNotFound() throws ImageGenerationException {
        String layerName = "invalidLayerName";
        List<Layer> layers = new ArrayList<>();
        layers.add(getLayerMock("layer1"));
        layers.add(getLayerMock("layer2"));
        layers.add(getLayerMock("layer3"));

        WebMapServer wmsMock = setupWmsMock(layers);

        replayAll();

        WMSLayerRetriever.get(layerName, wmsMock);
    }

    @Test
    public void TestGetLayerWithStyleFound() throws ImageGenerationException {
        String layerName = "layer1";
        String styleName = "style1";

        List<Layer> layers = new ArrayList<>();
        layers.add(getLayerWithStyleMock(layerName, styleName, true));

        WebMapServer wmsMock = setupWmsMock(layers);

        replayAll();

        Layer layer = WMSLayerRetriever.get(layerName, styleName, wmsMock);
        assertThat(layer, is(not(nullValue())));
    }

    @Test(expected = ImageGenerationException.class)
    public void TestGetLayerWithStyleNotFound() throws ImageGenerationException {
        String layerName = "layer1";
        String styleName = "invalidStyleName";

        List<Layer> layers = new ArrayList<>();
        layers.add(getLayerWithStyleMock(layerName, "style1", false));

        WebMapServer wmsMock = setupWmsMock(layers);

        replayAll();

        WMSLayerRetriever.get(layerName, styleName, wmsMock);
    }

    private WebMapServer setupWmsMock(List<Layer> layers) {
        WebMapServer wmsMock = mock(WebMapServer.class);
        WMSCapabilities capabilitiesMock = mock(WMSCapabilities.class);

        EasyMock.expect(wmsMock.getCapabilities())
                .andReturn(capabilitiesMock)
                .once();
        EasyMock.expect(WMSUtils.getNamedLayers(capabilitiesMock))
                .andReturn(layers.toArray(new Layer[layers.size()]))
                .once();

        return wmsMock;
    }

    private Layer getLayerWithStyleMock(String layerName, String styleName, boolean applyStyle) {
        Layer layer = getLayerMock(layerName);

        List<StyleImpl> styles = new ArrayList<>();
        styles.add(getStyleMock(styleName));

        if (applyStyle) {
            layer.setStyles(EasyMock.anyObject(List.class));
            EasyMock.expectLastCall();
        }

        EasyMock.expect(layer.getStyles()).andReturn(styles);

        return layer;
    }

    private StyleImpl getStyleMock(String styleName) {
        StyleImpl style = mock(StyleImpl.class);
        EasyMock.expect(style.getName())
                .andReturn(styleName)
                .times(0, 1);

        return style;
    }

    private Layer getLayerMock(String layerName) {
        Layer layer = mock(Layer.class);
        EasyMock.expect(layer.getName())
                .andReturn(layerName)
                .times(0, 1);

        return layer;
    }

}
