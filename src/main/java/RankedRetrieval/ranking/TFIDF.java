package RankedRetrieval.ranking;

public class TFIDF {

    public double tf(String[] doc, double termNum) {
        return termNum / doc.length;
    }

    public double idf(double docsSize, double docterm) {
        return Math.log(docsSize / docterm);
    }

    public double tfIdf(String[] doc, double docsSize, double docterm, double termNum) {
        return tf(doc, termNum) * idf(docsSize, docterm);
    }

}