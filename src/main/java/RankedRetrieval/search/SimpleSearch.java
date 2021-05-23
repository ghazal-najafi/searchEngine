package RankedRetrieval.search;

import RankedRetrieval.Index.CSV;
import RankedRetrieval.Index.PostingList;
import RankedRetrieval.killList.KillList;

import java.io.IOException;
import java.util.*;

public class SimpleSearch {
    private PostingList postingList = new PostingList();
    private Map<String, Map<Integer, List>> queryMap = new HashMap<>();
    private String[] sentence;
    private LogFile logFile;
    private CSV csv;
    private SpellCorrection spellCorrection;
    private Map<String, ArrayList<String>> cw = new HashMap<>();
    private KillList killList;

    public void initialize() {
        logFile = new LogFile();
        csv = new CSV();
        spellCorrection = new SpellCorrection();
        killList = new KillList();
    }

    public Map<String, ArrayList<String>> getQuery(String query) throws IOException {
        initialize();
        cw.clear();
        queryMap.clear();
        String input = "";
        input = query.toLowerCase().trim();
        sentence = input.split("and");
        Map<String, Map<Integer, List>> map;
        ArrayList<String> correctWords;
        ArrayList<String> correctSentences = new ArrayList<>();

        for (int i = 0; i < sentence.length; i++) {
            sentence[i] = sentence[i].trim();
            String[] str = sentence[i].split("\\s+");
            String correctSentence = "";
            for (int j = 0; j < str.length; j++) {
                if (str[j].matches("[0-9][/]"))
                    continue;
                map = csv.readFile(csv.getPath(str[j]), str[j]);
                if (map != null)
                    queryMap.put(str[j], map.get(str[j]));
                else {
                    correctWords = spellCorrection.spellCorrection(str[j]);
                    System.out.println("correct words of " + str[j] + " : " + correctWords);
                    if (correctWords.size() > 0) {
                        cw.put(str[j], correctWords);
                        str[j] = correctWords.get(0);
                        map = csv.readFile(csv.getPath(str[j]), str[j]);
                        queryMap.put(str[j], map.get(str[j]));
                    }
                }
                correctSentence += str[j];
                correctSentence += " ";
            }
            sentence[i] = correctSentence;
            correctSentences.add(sentence[i]);
        }

        cw.put("cs", correctSentences);
        return cw;
    }

    public ArrayList returnDocId(String sentence) throws IOException {
        String[] str = sentence.split("\\s+");
        int distance = 0;
        ArrayList result = new ArrayList();

        if (queryMap.get(str[0]) != null)
            result = killList.checkIDs(str[0], returnList(queryMap.get(str[0]).keySet()));
        for (int j = 1; j < str.length; j++) {
            if (str[j].matches("[0-9][/]")) {
                distance = Integer.parseInt(str[j].substring(0, str[j].length() - 1));
                j++;
                result = postingList.and(result, killList.checkIDs(str[j], returnList(queryMap.get(str[j]).keySet())));
                if (result != null)
                    result = (finalAnswer(result, str[j - 2], str[j], distance));

            } else {
                result = postingList.and(result, killList.checkIDs(str[j], returnList(queryMap.get(str[j]).keySet())));
                if (result != null) {
                    result = finalAnswer(result, str[j - 1], str[j], distance);
                }
            }
        }
        logFile.readLogFile(sentence);
        logFile.readLogFileSentence(sentence);
        return result;
    }

    public ArrayList<Integer> returnList(Set<Integer> index) {
        ArrayList<Integer> indicies = new ArrayList<>();
        for (int id : index) {
            indicies.add(id);
        }
        Collections.sort(indicies);
        return indicies;
    }

    public ArrayList<Integer> finalAnswer(ArrayList<Integer> indexes, String token1, String token2, int distance) {
        Map<Integer, List> pos = queryMap.get(token1);
        Map<Integer, List> pos2 = queryMap.get(token2);
        ArrayList<Integer> result = new ArrayList<>();

        for (int k = 0; k < indexes.size(); k++) {
            ArrayList<Integer> positionalIndex1 = (ArrayList) pos.get(indexes.get(k));
            ArrayList<Integer> positionalIndex2 = (ArrayList) pos2.get(indexes.get(k));
            int i = 0, j = 0;
            while (i < positionalIndex1.size() && j < positionalIndex2.size()) {
                int a = positionalIndex1.get(i);
                int b = positionalIndex2.get(j);
                int res = Math.abs(a - b);

                if (res <= distance + 1) {
                    result.add(indexes.get(k));
                    break;
                } else if (a < b)
                    i++;
                else
                    j++;
            }
        }
        return result;
    }
}
