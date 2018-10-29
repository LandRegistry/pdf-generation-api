package pdfgenerator.utility;

import java.text.DecimalFormat;

/**
 * A class for formatting certain data types for pdf display
 */
public final class Formatters {
	
    /**
     * Formats amount string for pdf display
     * @param amount The original amount string
     * @return The amount formatted
     */
    public static String formatAmount(String amount) {
    	try {
    		double amountDbl = Double.parseDouble(amount);
    		DecimalFormat amountFormater = new DecimalFormat("\u00A3###,###.00");
    		return amountFormater.format(amountDbl);
    	}
    	//Not numerical so just return without formatting
    	catch (NumberFormatException ex) {
    		return amount;
    	}
    }

    /**
     * Formats percentage string for pdf display
     * @param amount The original percentage string
     * @return The percentage formatted
     */
    public static String formatPercentage(String percentage) {
    	try {
    		double percentageDbl = Double.parseDouble(percentage);
    		DecimalFormat percentageFormater = new DecimalFormat("###,###.###'%'");
    		return percentageFormater.format(percentageDbl);
    	}
    	//Not numerical so just return without formatting
    	catch (NumberFormatException ex) {
    		return percentage;
    	}
    }
}
