package RankedRetrieval.Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PostingList {
    private List<Integer> docIds = new ArrayList<>();

    public PostingList(int... ids) {
        for (int id : ids) {
            docIds.add(id);

        }
    }

    public void add(int id) {
        docIds.add(id);
    }

    public void sort() {
        Collections.sort(docIds);
    }

    public int size() {
        return docIds.size();
    }

    public int get(int i) {
        return docIds.get(i);
    }

    public void addAll(PostingList ids) {
        for (int i = 0; i < ids.size(); i++) {
            docIds.add((Integer) ids.docIds.get(i));
        }
    }

    public ArrayList and(ArrayList<Integer> postingList1, ArrayList<Integer> postingList2) {

        if (postingList1 == null || postingList2 == null)
            return null;
        ArrayList result = new ArrayList();
        int i = 0, j = 0;

        while (i < postingList1.size() && j < postingList2.size()) {
            int a = postingList1.get(i);
            int b = postingList2.get(j);
            if (a == b) {
                result.add(a);
                i++;
                j++;
            } else if (a < b)
                i++;
            else
                j++;
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(docIds.toArray(new Integer[0]));
    }

}
