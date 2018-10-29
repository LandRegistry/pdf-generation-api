package pdfgenerationapi.services.LLC1;

import pdfgenerationapi.models.Llc1GenerationResult;
import pdfgenerationapi.models.Llc1PdfRequest;
import pdfgenerator.exceptions.PdfGenerationException;

public interface IPdfGenerationService {
    Llc1GenerationResult generatePDF(Llc1PdfRequest Llc1PdfRequest) throws PdfGenerationException;
}
