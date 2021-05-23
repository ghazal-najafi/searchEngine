package RankedRetrieval.search;

import RankedRetrieval.Index.PostingList;
import java.util.Collection;
import java.util.HashMap;

public class DocumentStore {
    private HashMap<Integer, Documents> docs = new HashMap();

    public void add(Documents doc) {
        docs.put(doc.getDocId(), doc);
    }

    public Documents get(Object id) {
        return docs.get(id);
    }

    public int getId(Object id) {
        return docs.get(id).getDocId();
    }

    public String getTitle(int id) {
        return docs.get(id).getTitle();
    }

    public Collection<Documents> getAll() {
        return docs.values();
    }

    public PostingList retAll() {
        PostingList AllId = new PostingList();
        for (int i = 1; i <= docs.size(); i++)
            AllId.add(getId(i));
        return AllId;
    }
}