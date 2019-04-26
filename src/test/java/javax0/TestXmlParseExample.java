package javax0;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.stream.IntStream;

class TestXmlParseExample {

    @Test
    void testThrows() throws ParserConfigurationException, IOException, SAXException {
        final var builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        final var doc = builder.parse(getClass().getClassLoader().getResourceAsStream("library.xml"));
        final var books = doc.getElementsByTagName("book");
        Assertions.assertThrows(NullPointerException.class, () ->
                IntStream.range(0, books.getLength()).forEach(
                        i -> {
                            final var title = ((Element) books.item(i)).getElementsByTagName("title").item(0).getTextContent();
                            final var reviewer = ((Element) books.item(i)).getElementsByTagName("reviewer").item(0).getTextContent();
                            System.out.println("Reviewer of the book " + title + " is " + reviewer);
                        }
                ));
    }


    @Test
    void testConventional() throws ParserConfigurationException, IOException, SAXException {
        final var builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        final var doc = builder.parse(getClass().getClassLoader().getResourceAsStream("library.xml"));
        final var books = doc.getElementsByTagName("book");
        IntStream.range(0, books.getLength()).forEach(
                i -> {
                    final var title = ((Element) books.item(i)).getElementsByTagName("title").item(0).getTextContent();
                    final var reviewers = ((Element) books.item(i)).getElementsByTagName("reviewer");
                    final String reviewer;
                    if (reviewers != null && reviewers.getLength() > 0) {
                        reviewer = reviewers.item(0).getTextContent();
                    }else{
                        reviewer = "nobody";
                    }
                    System.out.println("Reviewer of the book " + title + " is " + reviewer);
                }
        );
    }

    @Test
    void test() throws ParserConfigurationException, IOException, SAXException {
        final var builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        final var doc = builder.parse(getClass().getClassLoader().getResourceAsStream("library.xml"));
        final var books = doc.getElementsByTagName("book");
        IntStream.range(0, books.getLength()).forEach(
                i -> {
                    final var title = ((Element) books.item(i)).getElementsByTagName("title").item(0).getTextContent();
                    final var reviewer = Exceptional.of(() -> ((Element) books.item(i)).getElementsByTagName("reviewer").item(0).getTextContent()).orElse("nobody");
                    System.out.println("Reviewer of the book " + title + " is " + reviewer);
                }
        );
    }
}
