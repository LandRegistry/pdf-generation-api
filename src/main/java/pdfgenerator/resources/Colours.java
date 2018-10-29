package pdfgenerator.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public final class Colours {
    private static final Logger LOGGER = LoggerFactory.getLogger(Colours.class);

    private static Colours s_instance;

    private static final String COLOURS_FILE_PATH = "colours/sRGB_CS_profile.icm";

    private Colours() {}

    /**
     * Builds and returns a singleton instance of this class.
     * @return A singleton instance of this class.
     */
    public static synchronized Colours getInstance() {
        if (s_instance == null) {
            s_instance = new Colours();
        }
        return s_instance;
    }

    /**
     * Builds and returns an {@link InputStream} for the colour file used in the LLC1 PDF.
     * @return A {@link InputStream} for the colour file used in the LLC1 PDF.
     */
    public InputStream getColour() {
        LOGGER.debug("Built new colour.");
        return this.getClass().getClassLoader().getResourceAsStream(COLOURS_FILE_PATH);
    }
}
