package pdfgenerator.sections;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.element.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerator.utility.ParagraphBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Class responsible for generating the copyright section of the PDF.
 */
public final class CopyrightSection {
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyrightSection.class);

    private static final Integer FONT_SIZE = 11;

    private static final Integer TERMS_LINK_FONT_COLOR_RED = 0;
    private static final Integer TERMS_LINK_FONT_COLOR_GREEN = 0;
    private static final Integer TERMS_LINK_FONT_COLOR_BLUE = 255;

    private CopyrightSection() {}

    /**
     * Builds and returns the Copyright Section of the LLC1 PDF.
     * @return The Copyright Section of the LLC1 PDF.
     * @throws IOException If the font file could not be accessed.
     */
    public static Paragraph generate() throws IOException {
        LOGGER.debug("Generating copyright section.");

        Link osTermsLink = new Link("https://www.ordnancesurvey.co.uk/about/governance/policies/hm-land-registry-local-land-\nsearches-service.html", PdfAction.createURI("https://www.ordnancesurvey.co.uk/about/governance/policies/hm-land-registry-local-land-searches-service.html"));

        return ParagraphBuilder.normal("© Crown copyright and database right " + LocalDateTime.now().getYear() + " Ordnance Survey licence no. 100026316. Use of address and\n")
            .setFontSize(FONT_SIZE)
            .setPaddingTop(40)
            .add("mapping data (including the link between the address and its location) is subject to Ordnance Survey licence\n terms and conditions. ")
            .add(osTermsLink
                    .setUnderline()
                    .setFontColor(
                        new DeviceRgb(TERMS_LINK_FONT_COLOR_RED, TERMS_LINK_FONT_COLOR_GREEN, TERMS_LINK_FONT_COLOR_BLUE)))
            .setKeepTogether(true);
    }
}
