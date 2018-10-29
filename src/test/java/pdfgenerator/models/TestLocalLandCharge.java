package pdfgenerator.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestLocalLandCharge {

    @Test
    public void testInitialize() {
        LocalLandCharge localLandCharge = new LocalLandCharge();
        Geometry geometry = new Geometry();
        Item item = new Item();

        localLandCharge.setCancelled(false);
        localLandCharge.setDisplayId("abc");
        localLandCharge.setId(1);
        localLandCharge.setType("type");
        localLandCharge.setGeometry(geometry);
        localLandCharge.setItem(item);

        assertThat(localLandCharge.getCancelled(), is(false));
        assertThat(localLandCharge.getDisplayId(), is("abc"));
        assertThat(localLandCharge.getId(), is(1));
        assertThat(localLandCharge.getType(), is("type"));
        assertThat(localLandCharge.getGeometry(), is(geometry));
        assertThat(localLandCharge.getItem(), is(item));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"item\":{\"further-information-reference\":null,\"registration-date\":null,\"author\":null,\"further-information-location\":\"\",\"start-date\":null,\"instrument\":null,\"charge-geographic-description\":\"\",\"charge-type\":null,\"charge-creation-date\":null,\"originating-authority\":null,\"geometry\":null,\"statutory-provision\":null,\"local-land-charge\":null},\"display_id\":\"abc\",\"cancelled\":false,\"geometry\":{\"type\":null,\"features\":[]},\"id\":1,\"type\":\"type\"}";
        LocalLandCharge localLandCharge = mapper.readValue(json, LocalLandCharge.class);

        assertThat(localLandCharge.getCancelled(), is(false));
        assertThat(localLandCharge.getDisplayId(), is("abc"));
        assertThat(localLandCharge.getId(), is(1));
        assertThat(localLandCharge.getType(), is("type"));
        assertThat(localLandCharge.getGeometry(), is(not(nullValue())));
        assertThat(localLandCharge.getItem(), is(not(nullValue())));
    }

    @Test
    public void testCompareTo() {
        Item itemOne = new Item();
        itemOne.setChargeCreationDate(new Date(1));
        LocalLandCharge localLandChargeOne = new LocalLandCharge();
        localLandChargeOne.setItem(itemOne);

        Item itemTwo = new Item();
        itemTwo.setChargeCreationDate(new Date(2));
        LocalLandCharge localLandChargeTwo = new LocalLandCharge();
        localLandChargeTwo.setItem(itemTwo);

        Item itemThree = new Item();
        LocalLandCharge localLandChargeThree = new LocalLandCharge();
        localLandChargeThree.setItem(itemThree);

        assertThat("Should return negative one if the date of the caller is more recent than the parameter.",
            localLandChargeTwo.compareTo(localLandChargeOne), is(-1));

        assertThat("Should return positive one if the date of the caller is less recent than the parameter.",
            localLandChargeOne.compareTo(localLandChargeTwo), is(1));

        assertThat("Should return zero if the date of the caller and the parameter are the same.",
            localLandChargeOne.compareTo(localLandChargeOne), is(0));

        assertThat("Should return positive one if the caller's date is not set.",
            localLandChargeThree.compareTo(localLandChargeOne), is(1));

        assertThat("Should return zero if both the caller and the parameter's date is not set.",
            localLandChargeThree.compareTo(localLandChargeThree), is(0));

        assertThat("Should return negative one if the parameter's date is not set.",
            localLandChargeOne.compareTo(localLandChargeThree), is(-1));
    }

    @Test
    public void testToString() {
        LocalLandCharge localLandCharge = new LocalLandCharge();
        Geometry geometry = new Geometry();
        Item item = new Item();

        localLandCharge.setCancelled(false);
        localLandCharge.setDisplayId("abc");
        localLandCharge.setId(1);
        localLandCharge.setType("type");
        localLandCharge.setGeometry(geometry);
        localLandCharge.setItem(item);

        String result = localLandCharge.toString();

        assertThat(result, containsStringIgnoringCase("item=pdfgenerator.models.Item"));
        assertThat(result, containsStringIgnoringCase("displayId=abc"));
        assertThat(result, containsStringIgnoringCase("cancelled=false"));
        assertThat(result, containsStringIgnoringCase("geometry=pdfgenerator.models.Geometry"));
        assertThat(result, containsStringIgnoringCase("id=1"));
        assertThat(result, containsStringIgnoringCase("type=type"));
    }
}
