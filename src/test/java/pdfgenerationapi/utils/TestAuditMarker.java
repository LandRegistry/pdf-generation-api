package pdfgenerationapi.utils;

import org.junit.Test;
import org.slf4j.Marker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;


public class TestAuditMarker {

    @Test
    public void TestAuditMarkerValue() {
        Marker result = AuditMarker.auditMarker;

        assertThat(result.getName(), is(not(nullValue())));
        assertThat(result.getName(), is("AUDIT"));
    }

    @Test()
    public void Testinitialize() {
        AuditMarker auditMarker = new AuditMarker();
        assertThat(auditMarker, is(not(nullValue())));
    }
}




