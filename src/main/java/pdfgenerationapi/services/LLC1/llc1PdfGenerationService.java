package pdfgenerationapi.services.LLC1;

import pdfgenerationapi.models.Llc1GenerationResult;
import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerator.core.LLC1;
import pdfgenerator.core.LLC1GenerationFactory;
import pdfgenerator.exceptions.PdfGenerationException;

public class llc1PdfGenerationService implements IPdfGenerationService {

    @Override
    public Llc1GenerationResult generatePDF(Llc1PdfRequest llc1PdfRequest) throws PdfGenerationException {
        return LLC1GenerationFactory.getLlc1PdfGenerator().generate(llc1PdfRequest);
    }
}
