package RankedRetrieval.scraping;

import RankedRetrieval.model.Resources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.sql.SQLException;

public class WebScraping {
    public static Resources webScrap(String url) throws IOException, SQLException {
        Document doc = Jsoup.connect(url).get();
        Resources resource = new Resources();
        resource.setLink(url);
        resource.setTitle(doc.title());
        resource.setBody(doc.body().text());
        return resource;
    }
}
