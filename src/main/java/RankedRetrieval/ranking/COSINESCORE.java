//package RankedRetrieval.ranking;
//
//import RankedRetrieval.database.Services;
//import com.opencsv.CSVReader;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//
//public class COSINESCORE {
// TFIDF tfidf=new TFIDF();
// Services services=new Services();
//
// MaxHeapMap maxHeap = new MaxHeapMap();
// public ArrayList<Integer> score(Map<Integer, String> document, String Query,int size) throws IOException {
// for (Map.Entry<Integer, String> entry : document.entrySet()) {
// String s = entry.getValue();
//// System.out.println(Vector(Query, s,size));
// maxHeap.insert(entry.getKey(),Vector(Query, s,size));
// }
// return maxHeap.extractMax();
// }
//
// public double Vector(String query, String document,int sizeTotal) throws IOException {
// String doc1[] = query.toLowerCase().trim().split("([\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\0020\\u060C\\u066B\\u200D\\u064E\\u0650\\u064F\\u226B\\u3164\\u00BB\\«\\؛َ]+)");
// String doc2[] = document.toLowerCase().trim().split("([\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\0020\\u060C\\u066B\\u200D\\u064E\\u0650\\u064F\\u226B\\u3164\\u00BB\\«\\؛َ]+)");
//
// HashMap<String, Integer> dict1 = new HashMap<>();
// HashMap<String, Integer> dict2 = new HashMap<>();
//
// for (int i = 0; i < doc1.length; i++) {
// if (!dict1.containsKey(doc1[i])) {
// dict1.put(doc1[i], 1);
// } else if (dict1.containsKey(doc1[i])) {
// dict1.put(doc1[i], dict1.get(doc1[i]) + 1);
// }
// if (!dict2.containsKey(doc1[i])) {
// dict2.put(doc1[i], 0);
// }
// }
//
// for (int i = 0; i < doc2.length; i++) {
// if (!dict2.containsKey(doc2[i])) {
// dict2.put(doc2[i], 1);
// } else if (dict2.containsKey(doc2[i])) {
// dict2.put(doc2[i], dict2.get(doc2[i]) + 1);
// }
// if (!dict1.containsKey(doc2[i])) {
// dict1.put(doc2[i], 0);
// }
// }
//
// double Product = 0;
// double doc1sq = 0;
// double doc2sq = 0;
// for (int i = 0; i < doc1.length; i++) {
//// double wquery=tfidf.tfIdf(doc1,sizeTotal,getTokenIDs(doc1[i]),dict1.get(doc1[i]));
//// double wdoc=tfidf.tfIdf(doc2,sizeTotal,getTokenIDs(doc1[i]),dict2.get(doc1[i]));
// Product = Product + (dict1.get(doc1[i]))* (dict2.get(doc1[i]));
// doc1sq = doc1sq + (dict1.get(doc1[i])) * (dict1.get(doc1[i]));
// doc2sq = doc2sq + (dict2.get(doc1[i])) * (dict2.get(doc1[i]));
// }
// double similarity=0;
// if(Product!=0)
// similarity = Product / Math.sqrt(doc1sq * doc2sq);
// return similarity;
// }
// public int getTokenIDs(String word) throws IOException {
// String[] files = new String[]{"src\\main\\resources\\index\\alf-dal.csv", "src\\main\\resources\\index\\zal-ye.csv", "src\\main\\resources\\index\\other.csv", "src\\main\\resources\\index\\a-z.csv"};
//
// int len = 0;
// for (int i = 0; i < files.length; i++) {
// CSVReader reader = new CSVReader(new FileReader(files[i]), ';');
// String[] data;
// while ((data = reader.readNext()) != null) {
// len=data.length-1;
// }
// }
// return len;
// }
//
//}

package RankedRetrieval.ranking;

import java.util.*;

public class COSINESCORE {
    MaxHeapMap maxHeap = new MaxHeapMap();

    public ArrayList score(Map<Integer, String> document, String Query) {
        for (Map.Entry<Integer, String> entry : document.entrySet()) {
            String s = entry.getValue();
            maxHeap.insert(entry.getKey(), Vector(Query, s));
        }

        return maxHeap.extractMax();
    }

    public double Vector(String query, String document) {
        String doc1[] = query.toLowerCase().trim().split("([\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\0020\\u060C\\u066B\\u200D\\u064E\\u0650\\u064F\\u226B\\u3164\\u00BB\\«\\؛َ]+)");
        String doc2[] = document.toLowerCase().trim().split("([\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\0020\\u060C\\u066B\\u200D\\u064E\\u0650\\u064F\\u226B\\u3164\\u00BB\\«\\؛َ]+)");
        HashMap<String, Integer> dict1 = new HashMap<>();
        HashMap<String, Integer> dict2 = new HashMap<>();
        ArrayList DocSet = new ArrayList<>(new HashSet<>(Arrays.asList(doc2)));
        ArrayList QSet = new ArrayList<>(new HashSet<>(Arrays.asList(doc1)));

        for (int i = 0; i < doc1.length; i++) {
            if (!dict1.containsKey(doc1[i])) {
                dict1.put(doc1[i], 1);

            } else if (dict1.containsKey(doc1[i])) {
                dict1.put(doc1[i], dict1.get(doc1[i]) + 1);
            }
            if (!dict2.containsKey(doc1[i])) {
                dict2.put(doc1[i], 0);
            }
        }

        for (int i = 0; i < doc2.length; i++) {
            if (!dict2.containsKey(doc2[i])) {
                dict2.put(doc2[i], 1);
            } else if (dict2.containsKey(doc2[i])) {
                dict2.put(doc2[i], dict2.get(doc2[i]) + 1);
            }
            if (!dict1.containsKey(doc2[i])) {
                dict1.put(doc2[i], 0);
            }
        }
        double Product = 0;
        double doc1sq = 0;
        double doc2sq = 0;
        for (int i = 0; i < QSet.size(); i++) {
            Product = Product + (dict1.get(QSet.get(i))) * (dict2.get(QSet.get(i)));
            doc1sq += Math.pow(dict1.get(QSet.get(i)), 2.0);
        }
        for (int i = 0; i < DocSet.size(); i++) {
            doc2sq += Math.pow(dict2.get(DocSet.get(i)), 2.0);
        }
        double similarity = 0;
        if (Product != 0)
            similarity = Product / (Math.sqrt(doc1sq) * Math.sqrt(doc2sq));
        return similarity;
    }

}