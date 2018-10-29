package pdfgenerator.resources;

import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.UnitValue;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;


public class TestImages {
    protected UnitValue originalWidth = UnitValue.createPointValue(65);
    protected UnitValue originalHeight = UnitValue.createPointValue(65);
    protected UnitValue originalMargin = UnitValue.createPointValue(15);

    @Test
    public void testGetLogo() throws IOException{
        Image image = Images.getInstance().getLogo();
        assertThat(image, is(not(nullValue())));
        assertThat(image.getImageScaledHeight(), is(65.0f));
        assertThat(image.getImageScaledWidth(), is(65.0f));
        assertThat(image.getMarginTop(), is(originalMargin));
        assertThat(image.getAccessibilityProperties().getAlternateDescription(), is("Land Registry Logo"));
    }

    @Test
    public void testGetMediumGrayBar() throws IOException{
        Image image = Images.getInstance().getMediumGrayBar();
        assertThat(image, is(not(nullValue())));
        assertThat(image.getImageScaledHeight(), is(not(originalHeight)));
        assertThat(image.getImageScaledWidth(), is(not(originalWidth)));
        assertThat(image.getMarginTop(), is(not(originalMargin)));
        assertThat(image.getAccessibilityProperties().getAlternateDescription(), is("Medium spacer"));
    }

    @Test
    public void testGetLargeGrayBar() throws IOException{
        Image image = Images.getInstance().getLargeGrayBar();
        assertThat(image, is(not(nullValue())));
        assertThat(image.getImageScaledHeight(), is(not(originalHeight)));
        assertThat(image.getImageScaledWidth(), is(not(originalWidth)));
        assertThat(image.getMarginTop(), is(not(originalMargin)));
        assertThat(image.getAccessibilityProperties().getAlternateDescription(), is("Large spacer"));
    }
}
