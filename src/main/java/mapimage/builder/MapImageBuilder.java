package mapimage.builder;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdfgenerator.exceptions.ImageGenerationException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for drawing the map content and writing it to a BufferedImage object
 */
public class MapImageBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapImageBuilder.class);
    private static final String COPYRIGHT_TEXT = "© Crown copyright";

    private MapImageBuilder() { }

    /**
     * Creates a {@link BufferedImage} object containing a view of the map centered on any Features defined in the {@code content} parameter
     * @param content The {@link MapContent} object defining the base layer and feature layer(s)
     * @param imageBounds The image bounds which defines the size of the final image
     * @param mapBounds The map bounds which define where on the map the image will be taken
     * @return A {@link BufferedImage} object of the map
     * @throws IOException If an error occurs when creating the font for the copyright text
     * @throws FontFormatException If an error occurs applying the font for the copyright text
     * @throws ImageGenerationException If an error occurs generating the map image
     */
    public static BufferedImage build(MapContent content, Rectangle imageBounds, ReferencedEnvelope mapBounds)
    		throws IOException, FontFormatException, ImageGenerationException {
        BufferedImage bufferedImage = new BufferedImage(imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D image = drawMap(bufferedImage, content, imageBounds, mapBounds);

        int copyrightX = 5;
        int copyrightY = imageBounds.height - 5;

        drawCopyright(image, copyrightX, copyrightY);

        return bufferedImage;
    }

    /**
     * Returns a {@link ReferencedEnvelope} object that defines where on the map the image will be taken.
     * Uses the {@code featureLayerBounds} to determine the minimum bounding box area
     * and adjusts the map bounds to ensure the correct aspect ratio
     * @param featureLayerBounds The minimum allowable map bounds for the specified features
     * @param imageBounds A rectangle representing the image resolution
     * @param crs The specified {@link CoordinateReferenceSystem}
     * @return A {@link ReferencedEnvelope} centered on {@code featureLayerBounds}, zoomed out to show the surrounding area
     */
    public static ReferencedEnvelope getMapBounds(ReferencedEnvelope featureLayerBounds, Rectangle imageBounds, CoordinateReferenceSystem crs) {
        double minX = featureLayerBounds.getMinX();
        double maxX = featureLayerBounds.getMaxX();
        double minY = featureLayerBounds.getMinY();
        double maxY = featureLayerBounds.getMaxY();
        ReferencedEnvelope mapBounds = new ReferencedEnvelope(minX, maxX, minY, maxY, crs);

        setAspectRatio(mapBounds, imageBounds);

        return mapBounds;
    }

    private static void setAspectRatio(ReferencedEnvelope mapBounds, Rectangle imageBounds) {
        // Compare the spans of the x and y axis, and increase the span on the smaller axis so that both spans are equal
        // Then expands on either x or y axis to take into account the aspect ratio of the intended image dimensions
        // Also zoom the map out a little bit so that features aren't clipping the edge of the image
        double xSpan = mapBounds.getSpan(0);
        double ySpan = mapBounds.getSpan(1);

        if (xSpan < ySpan) {
            double expandBy = (ySpan - xSpan) / 2;
            mapBounds.expandBy(expandBy, 0);
            mapBounds.expandBy(xSpan * 0.2);
        } else if (xSpan > ySpan) {
            double expandBy = (xSpan - ySpan) / 2;
            mapBounds.expandBy(0, expandBy);
            mapBounds.expandBy(ySpan * 0.2);
        }

        double aspectRatio = imageBounds.getWidth() / imageBounds.getHeight();

        // xSpan and ySpan will be the same at this point because of the above code
        double mapBoundsEdgeLength = mapBounds.getSpan(0);
        if (aspectRatio > 1) {
            mapBounds.expandBy(((mapBoundsEdgeLength * aspectRatio) - mapBoundsEdgeLength) / 2,0);
        } else if (aspectRatio < 1) {
            mapBounds.expandBy(0,((mapBoundsEdgeLength / aspectRatio) - mapBoundsEdgeLength) / 2);
        }
    }

    private static Graphics2D drawMap(BufferedImage bufferedImage, MapContent content, Rectangle imageBounds,
    		ReferencedEnvelope mapBounds) throws ImageGenerationException {
        LOGGER.debug("Drawing map");
        GTRenderer renderer = new StreamingRenderer();
        RenderErrorListener renderListener = new RenderErrorListener(renderer);
        renderer.addRenderListener(renderListener);
        renderer.setMapContent(content);

        Map<RenderingHints.Key, Object> hintsMap = new HashMap<>();
        hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RenderingHints hints = new RenderingHints(hintsMap);
        renderer.setJava2DHints(hints);

        Graphics2D gr = bufferedImage.createGraphics();

        gr.setPaint(Color.WHITE);
        gr.fill(imageBounds);
        renderer.paint(gr, imageBounds, mapBounds);
        if (renderListener.getExceptionThrown() != null) {
        	throw new ImageGenerationException("Error creating map image",
        			renderListener.getExceptionThrown());
        }

        return gr;
    }

    private static void drawCopyright(Graphics2D gr, int x, int y) throws IOException, FontFormatException {
        LOGGER.debug("Overlaying copyright onto map");
        String fontPath = "fonts/GDSTransportWebsite.ttf";

        InputStream fontStream = MapImageBuilder.class.getClassLoader().getResourceAsStream(fontPath);
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(9f);

        gr.setFont(font);
        gr.setColor(Color.BLACK);
        gr.drawString(COPYRIGHT_TEXT, x, y);
    }
}
