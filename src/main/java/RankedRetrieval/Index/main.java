package RankedRetrieval.Index;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) throws IOException, XMLStreamException, SQLException {
        double x = System.currentTimeMillis();
//        query input = new query();
//        input.indexing();
//        DocumentService index =new DocumentService();
//        index.parser();
        System.out.println(System.currentTimeMillis() - x);
    }
}
