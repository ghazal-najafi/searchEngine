package RankedRetrieval.search;

import java.util.concurrent.atomic.AtomicInteger;

public class Documents {
    private static final AtomicInteger lastId = new AtomicInteger(0);
    private final int docId;
    private String title;
    private String body;


    public Documents() {
        this.docId = lastId.incrementAndGet();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public int getDocId() {
        return docId;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Document{" +
                "docId=" + docId +
                '}';
    }
}

