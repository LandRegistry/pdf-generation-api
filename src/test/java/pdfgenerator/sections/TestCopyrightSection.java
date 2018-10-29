package pdfgenerator.sections;

import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.layout.property.UnitValue;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestCopyrightSection {

    @Test
    public void testGenerate() throws IOException {
        Paragraph paragraph = CopyrightSection.generate();

        UnitValue expectedFontSize = UnitValue.createPointValue(11);
        String expectedTermsText = ((Text)paragraph.getChildren().get(1)).getText() + ((Text)paragraph.getChildren().get(2)).getText();
        String expectedTermsUrl = ((Link)paragraph.getChildren().get(2)).getLinkAnnotation().getAction().get(PdfName.URI).toString();

        assertThat(paragraph, is(not(nullValue())));
        assertThat(paragraph.getProperty(24), is(expectedFontSize));
        assertThat(((Text)paragraph.getChildren().get(0)).getText(),
                is("© Crown copyright and database right " + LocalDateTime.now().getYear() + " Ordnance Survey licence no. 100026316. Use of address and\n") );

        assertThat(expectedTermsText,
                is("mapping data (including the link between the address and its location) is subject to Ordnance Survey licence\n terms and conditions. https://www.ordnancesurvey.co.uk/about/governance/policies/hm-land-registry-local-land-\nsearches-service.html") );
        assertThat(expectedTermsUrl, is("https://www.ordnancesurvey.co.uk/about/governance/policies/hm-land-registry-local-land-searches-service.html"));
    }
}
