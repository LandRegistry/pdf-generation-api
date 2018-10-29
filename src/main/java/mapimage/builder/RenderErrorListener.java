package mapimage.builder;

import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.opengis.feature.simple.SimpleFeature;

/*
 * Listener for GTRenderer to stop rendering when an exception occurs and set
 * exceptionThrow to allow cause to be known afterwards
 */
public class RenderErrorListener implements RenderListener {
	
	private Throwable exceptionThrown;
	private GTRenderer renderer;
	
	public RenderErrorListener(GTRenderer renderer) {
		this.exceptionThrown = null;
		this.renderer = renderer;
	}

	public void featureRenderer(SimpleFeature feature) {
		return;
	}

	public void errorOccurred(Exception e) {
		renderer.stopRendering();
		this.exceptionThrown = e;
	}
	
	public Throwable getExceptionThrown() {
		return exceptionThrown;
	}

}
