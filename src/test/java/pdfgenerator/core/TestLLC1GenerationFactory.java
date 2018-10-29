package pdfgenerator.core;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestLLC1GenerationFactory {

    @Test
    public void TestConstructor() {
        LLC1GenerationFactory llc1GenerationFactory = new LLC1GenerationFactory();
        assertThat(llc1GenerationFactory, is(not(nullValue())));
    }

    @Test
    public void TestValidCreation() {
        LLC1 llc1 = LLC1GenerationFactory.getLlc1PdfGenerator();
        assertThat(llc1, is(not(nullValue())));
    }
}