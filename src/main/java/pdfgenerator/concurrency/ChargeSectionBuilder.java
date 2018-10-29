package pdfgenerator.concurrency;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import pdfgenerationapi.Config;
import pdfgenerator.exceptions.ImageGenerationException;
import pdfgenerator.models.LocalLandCharge;
import pdfgenerator.sections.LineSeparatorSection;
import pdfgenerator.sections.LlcSection;
import pdfgenerator.sections.LonSection;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class ChargeSectionBuilder {
    /**
     * Adds jobs for retrieving charge images into a thread pool to be executed concurrently
     * @param localLandCharges List of {@code LocalLandCharge} containing polygons to be rendered on a map
     * @return A list of {@code Future<Image>} objects that references each job that has been started
     */
    public static List<Future<Image>> startThreads(List<LocalLandCharge> localLandCharges) {
        ExecutorService executor = Executors.newFixedThreadPool(Config.IMAGE_GEN_THREAD_COUNT);
        List<Future<Image>> futures = new ArrayList<>();

        for (LocalLandCharge charge : localLandCharges) {
            GenerateChargeImageCallable task = GenerateChargeImageCallableFactory.getGenerateChargeImageCallable(charge);

            Future<Image> future = executor.submit(task);
            futures.add(future);
        }
        return futures;
    }

    /**
     * Loops through list of {@code Future<Image>} objects, and builds charge sections as the threads finish.
     * Once all threads have finished, the generated sections are appended to the document in order of charges in {@code localLandCharges}
     * @param generateChargeImageThreads List of threads that have been executed.  Returns map image object
     * @param localLandCharges List of {@code LocalLandCharge} objects
     * @param document The PDF document that charge sections will be appended to
     * @throws InterruptedException When a thread is interrupted
     * @throws ExecutionException When attempting to retrieve the result of a task that aborted by throwing an exception
     * @throws IOException When an I/O exception has occurred
     * @throws ImageGenerationException When an exception occurs when generating an image
     */
    public static void appendSectionsToDocument(List<Future<Image>> generateChargeImageThreads, List<LocalLandCharge> localLandCharges, Document document) throws InterruptedException, ExecutionException, IOException, ImageGenerationException {
        HashMap<Integer, Table> documentSections = new HashMap<>();

        int imageProcessedCount = 0;
        while(imageProcessedCount != generateChargeImageThreads.size()) {
            for (int index = 0; index < generateChargeImageThreads.size(); index++) {
                Future<Image> future = generateChargeImageThreads.get(index);
                if (future != null && future.isDone()) {
                    LocalLandCharge charge = localLandCharges.get(index);
                    Image image;
                    try {
                    	image = future.get();
                    }
                    /* If ExecutionException then check if it's cause was an ImageGenerationException and if so throw
                     * that instead otherwise ImageGenerationException is never thrown
                     */
                    catch (ExecutionException e) {
                    	Throwable cause = e.getCause();
                    	if (cause instanceof ImageGenerationException) {
                    		throw (ImageGenerationException)cause;
                    	}
                  		throw e;
                    }

                    if (charge.getType().equals("Light obstruction notice")) {
                        documentSections.put(index, LonSection.generate(charge, image));
                    } else {
                        documentSections.put(index, LlcSection.generate(charge, image));
                    }

                    generateChargeImageThreads.set(index, null);
                    imageProcessedCount++;
                }
            }
        }

        TreeMap<Integer, Table> documentSectionsSorted = new TreeMap<>(documentSections);

        for(Map.Entry<Integer, Table> documentSection: documentSectionsSorted.entrySet()) {
            if (documentSection.getKey() > 0) {
                document.add(LineSeparatorSection.generate());
            }

            document.add(documentSection.getValue());
        }
    }
}
