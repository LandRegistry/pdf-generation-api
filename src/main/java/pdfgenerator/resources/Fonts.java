package pdfgenerator.resources;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Builds instances of each of the fonts used to generate the LLC1 PDF.
 */
public final class Fonts {
    private static final Logger LOGGER = LoggerFactory.getLogger(Fonts.class);

    private static Fonts s_instance;
    
    private static final String GDS_TRANSPORT_REGULAR_FILE_PATH = "fonts/GDSTransportWebsite.ttf";
    private static final String GDS_TRANSPORT_BOLD_FILE_PATH = "fonts/GDSTransportWebsite-Bold.ttf";
    private static final String GEO_HEADING_FILE_PATH = "fonts/GeoHeadline.ttf";
    
    private final byte[] transFontBytes;
    private final byte[] transBoldFontBytes;
    private final byte[] geoHeadingFontBytes;
    private ThreadLocal<PdfFont> transFont = new ThreadLocal<PdfFont>();
    private ThreadLocal<PdfFont> transBoldFont = new ThreadLocal<PdfFont>();
    private ThreadLocal<PdfFont> geoHeadingFont = new ThreadLocal<PdfFont>();

    private Fonts() throws IOException {
        this.transFontBytes = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream(GDS_TRANSPORT_REGULAR_FILE_PATH));
        this.transBoldFontBytes = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream(GDS_TRANSPORT_BOLD_FILE_PATH));
        this.geoHeadingFontBytes = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream(GEO_HEADING_FILE_PATH));
        LOGGER.debug("Read font files.");
    }
    
    /**
     * (re)Initialise font instances for the current thread, to be run when starting a new document to ensure fonts
     * are not reused across documents
     * @throws IOException
     */
    public void initFonts() throws IOException {
    	geoHeadingFont.set(PdfFontFactory.createFont(geoHeadingFontBytes, PdfEncodings.WINANSI, true));
		LOGGER.debug("Built new geoHeadingFont.");
		transBoldFont.set(PdfFontFactory.createFont(transBoldFontBytes, PdfEncodings.WINANSI, true));
		LOGGER.debug("Built new transBoldFont.");
		transFont.set(PdfFontFactory.createFont(transFontBytes, PdfEncodings.WINANSI, true));
		LOGGER.debug("Built new transFont.");
    }
    
    /**
     * Builds and returns a singleton instance of this class.
     * @return A singleton instance of this class.
     * @throws IOException If any of the font resources could not be read.
     */
    public static synchronized Fonts getInstance() throws IOException {
    	if (s_instance == null) {
    		s_instance = new Fonts();
    	}
        return s_instance;
    }

    /**
     * Returns a Transport {@link PdfFont} instance per thread.
     * @return A Transport {@link PdfFont} instance.
     * @throws IOException If the font file could not be accessed on disk.
     */
    public PdfFont getTransFont() throws IOException {
    	return transFont.get();
    }

    /**
     * Returns a TransportBold {@link PdfFont} instance per thread.
     * @return A TransportBold {@link PdfFont} instance.
     * @throws IOException If the font file could not be accessed on disk.
     */
    public PdfFont getTransBoldFont() throws IOException {
    	return transBoldFont.get();
    }

    /**
     * Returns a GeoHeading {@link PdfFont} instance per thread.
     * @return A GeoHeading {@link PdfFont} instance.
     * @throws IOException If the font file could not be accessed on disk.
     */
    public PdfFont getGeoHeadingFont() throws IOException {
    	return geoHeadingFont.get();
    }
}
