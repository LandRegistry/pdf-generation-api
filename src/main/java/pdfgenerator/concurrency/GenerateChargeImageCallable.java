package pdfgenerator.concurrency;

import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.sections.LlcSection;
import pdfgenerator.sections.LonSection;
import pdfgenerator.services.ImageService;

import java.io.IOException;
import java.util.concurrent.Callable;

public class GenerateChargeImageCallable implements Callable<Image> {
    private final LocalLandCharge charge;

    public GenerateChargeImageCallable(LocalLandCharge charge) {
        this.charge = charge;
    }

    @Override
    public Image call() throws ImageGenerationException {
        return ImageService.generate(charge.getGeometry().getFeatures());
    }
}
