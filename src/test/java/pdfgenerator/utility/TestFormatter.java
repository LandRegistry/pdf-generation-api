package pdfgenerator.utility;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestFormatter {
    
	@Test
    public void testformatAmountOk() {
		String response = Formatters.formatAmount("123456.78");
		assertThat(response, is("\u00A3123,456.78"));
	}

	@Test
    public void testformatAmountNotParsable() {
		String response = Formatters.formatAmount("Rhubarb");
		assertThat(response, is("Rhubarb"));
	}

	@Test
    public void testformatPercentageOk() {
		String response = Formatters.formatPercentage("123456.78");
		assertThat(response, is("123,456.78%"));
	}

	@Test
    public void testformatPercentageNotParsable() {
		String response = Formatters.formatPercentage("Rhubarb");
		assertThat(response, is("Rhubarb"));
	}
}
