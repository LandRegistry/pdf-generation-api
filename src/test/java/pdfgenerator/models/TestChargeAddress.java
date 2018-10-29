package pdfgenerator.models;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class TestChargeAddress {

    @Test
    public void testToString() {
        ChargeAddress address = new ChargeAddress();
        address.setLine1("1 Line Street");
        address.setLine2("Twoville");
        address.setLine3("Threeton");
        address.setPostcode("FO0 0UR");

        String asString = address.toAddressString();
        assertThat(asString, containsString("1 Line Street"));
        assertThat(asString, containsString("Twoville"));
        assertThat(asString, containsString("Threeton"));
        assertThat(asString, containsString("FO0 0UR"));
    }
}
