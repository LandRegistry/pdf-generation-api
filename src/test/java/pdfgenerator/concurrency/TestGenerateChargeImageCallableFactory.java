package pdfgenerator.concurrency;

import org.junit.Test;
import pdfgenerationapi.TestUtils;
import pdfgenerator.models.LocalLandCharge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestGenerateChargeImageCallableFactory {
    @Test
    public void TestConstructor() {
        GenerateChargeImageCallableFactory generateChargeImageCallableFactory = new GenerateChargeImageCallableFactory();
        assertThat(generateChargeImageCallableFactory, is(not(nullValue())));
    }

    @Test
    public void TestValidCreation() {
        LocalLandCharge charge = TestUtils.buildLocalLandCharge();
        GenerateChargeImageCallable callable = GenerateChargeImageCallableFactory.getGenerateChargeImageCallable(charge);
        assertThat(callable, is(not(nullValue())));
    }
}
