package pdfgenerator.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestStructurePositionDimension {

    @Test
    public void testUnlimitedHeight() {
        StructurePositionDimension position = new StructurePositionDimension();
        position.setHeight("Unlimited height");
        position.setExtentCovered("All of the extent");

        assertThat(position.toHeightString(), is("Unlimited height"));
    }

    @Test
    public void testLimitedHeight() {
        StructurePositionDimension position = new StructurePositionDimension();
        position.setHeight("42");
        position.setUnits("Metres");
        position.setExtentCovered("All of the extent");

        assertThat(position.toHeightString(), is("42 Metres"));
    }

    @Test
    public void testAllOfTheExtent() {
        StructurePositionDimension position = new StructurePositionDimension();
        position.setHeight("Unlimited height");
        position.setExtentCovered("All of the extent");

        assertThat(position.toPositionString(), is("All of the extent"));
    }

    @Test
    public void testPartOfTheExtent() {
        StructurePositionDimension position = new StructurePositionDimension();
        position.setHeight("Unlimited height");
        position.setExtentCovered("Part of the extent");
        position.setPartExplanatoryText("Because this is a test");

        assertThat(position.toPositionString(), is("Because this is a test"));
    }


}