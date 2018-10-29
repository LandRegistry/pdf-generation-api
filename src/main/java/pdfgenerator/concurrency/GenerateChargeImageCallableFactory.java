package pdfgenerator.concurrency;

import pdfgenerator.models.LocalLandCharge;

public class GenerateChargeImageCallableFactory {
    public static GenerateChargeImageCallable getGenerateChargeImageCallable(LocalLandCharge charge) { return new GenerateChargeImageCallable(charge); }
}
