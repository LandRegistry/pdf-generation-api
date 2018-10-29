package pdfgenerator.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

public class TestAuthor {

    @Test
    public void testInitialize() {
        Author author = new Author();
        author.setOrganisation("abc");
        author.setEmail("a@b.com");
        author.setFullName("Joe Bloggs");

        assertThat(author.getOrganisation(), is("abc"));
        assertThat(author.getEmail(), is("a@b.com"));
        assertThat(author.getFullName(), is("Joe Bloggs"));
    }

    @Test
    public void testInitializeFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"organisation\": \"abc\",\"email\": \"a@b.com\", \"full-name\":\"Joe Bloggs\"}";
        Author author = mapper.readValue(json,Author.class);

        assertThat(author.getOrganisation(), is("abc"));
        assertThat(author.getEmail(), is("a@b.com"));
        assertThat(author.getFullName(), is("Joe Bloggs"));
    }

    @Test
    public void testToString() {
        Author author = new Author();
        author.setOrganisation("abc");
        author.setEmail("a@b.com");
        author.setFullName("Joe Bloggs");

        String result = author.toString();

        assertThat(result, containsStringIgnoringCase("organisation=abc"));
    }
}