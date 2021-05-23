package RankedRetrieval.search;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SpellCorrection {
    private HashMap<String, ArrayList<String>> bigramFrequency = new HashMap<>();
    private LinkedHashMap<String, Float> jaccardCoefficient = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> editDistance = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> frequency = new LinkedHashMap<>();
    private Map<String, Integer> tokenNum = new HashMap<>();
    private ArrayList<String> bigrams = new ArrayList<>();
    private String[] words = {"یا", "را", "بر", "از", "هم", "در", "این", "آن", "که", "به", "و", "برای", "ما", "با"};
    private List<String> stopWords = Arrays.asList(words);

    private void reset() {
        bigrams.clear();
        tokenNum.clear();
        editDistance.clear();
        jaccardCoefficient.clear();
        bigramFrequency.clear();
    }

    public void produceBigrams(String word) {
        String bigram = "";
        int wordLength = 0;
        while (wordLength < word.length() - 1) {
            bigram = word.substring(wordLength, wordLength + 2);
            wordLength += 1;
            bigrams.add(bigram);
        }
        bigram = word.substring(wordLength, word.length());
        if (!bigram.equals("") && bigram.length() > 1)
            bigrams.add(bigram);
        bigrams.add(String.valueOf(word.charAt(0)));
        bigrams.add(String.valueOf(word.charAt(word.length() - 1)));
    }

    public void smallWords(String word) {
        String bigram = "";
        int wordLength = 0;
        while (wordLength < word.length() - 1) {
            bigram = word.substring(wordLength, wordLength + 2);
            wordLength += 1;
            bigrams.add(bigram);
        }
        bigram = word.substring(wordLength, word.length());
        if (!bigram.equals("") && bigram.length() > 1 && word.length() >= 3)
            bigrams.add(bigram);

        char kgram = 0;
        for (int i = 0; i < word.length(); i++) {
            kgram = word.charAt(i);
            bigrams.add(String.valueOf(kgram));
        }
    }

    public ArrayList<String> spellCorrection(String word) throws IOException {
        reset();
        getTokens(word);
        ArrayList<String> answer = new ArrayList<>();

        for (Map.Entry<String, ArrayList<String>> entry : bigramFrequency.entrySet()) {
            ArrayList<String> tokens = entry.getValue();
            for (int i = 0; i < tokens.size(); i++) {
                String token = tokens.get(i);
                tokenNum.putIfAbsent(token, 0);
                int num = tokenNum.get(token);
                tokenNum.put(token, num + 1);
            }
        }
        LinkedHashMap<String, Float> unSortedJaccardCoefficient = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : tokenNum.entrySet()) {
            float res = (float) entry.getValue() / (entry.getKey().length() - 1 + word.length() - 1);
            unSortedJaccardCoefficient.put(entry.getKey(), res);
        }
        unSortedJaccardCoefficient.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> jaccardCoefficient.put(x.getKey(), x.getValue()));

        LinkedHashMap<String, Integer> unSortedEditDistance = new LinkedHashMap<>();
        for (Map.Entry<String, Float> entry : jaccardCoefficient.entrySet())
            unSortedEditDistance.put(entry.getKey(), editDistance(word.trim(), entry.getKey().trim()));

        unSortedEditDistance.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> editDistance.put(x.getKey(), x.getValue()));

        System.out.println(editDistance);
//        System.out.println(frequency);
        Map<String, Integer> test = new HashMap();
        int count = 0, z = 1;
        for (Map.Entry<String, Integer> entry : editDistance.entrySet()) {
            if (entry.getKey().length() != 1) {
                int value = entry.getValue();
                if (value <= z && count < 10) {
                    test.put(entry.getKey(), frequency.get(entry.getKey()));
                    count++;
                } else if (count <= 10) {
                    z++;
                    LinkedHashMap<String, Integer> test2 = new LinkedHashMap<>();
                    test.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> test2.put(x.getKey(), x.getValue()));
                    for (Map.Entry<String, Integer> entry1 : test2.entrySet())
                        answer.add(entry1.getKey());
                    test.clear();
                } else
                    break;
            }
        }
        return answer;
    }

    public void getTokens(String word) throws IOException {
        if (word.length() < 4)
            smallWords(word);
        else
            produceBigrams(word);
        String files[] = null;
        if (!word.matches("[a-zA-Z]"))
            files = new String[]{"src\\main\\resources\\index\\alf-dal.csv", "src\\main\\resources\\index\\zal-ye.csv", "src\\main\\resources\\index\\other.csv"};
        else
            files = new String[]{"src\\main\\resources\\index\\alf-dal.csv", "src\\main\\resources\\index\\zal-ye.csv", "src\\main\\resources\\index\\other.csv", "src\\main\\resources\\index\\a-z.csv"};

        for (int i = 0; i < files.length; i++) {
            CSVReader reader = new CSVReader(new FileReader(files[i]), ';');
            String[] data;
            while ((data = reader.readNext()) != null) {
                for (int j = 0; j < bigrams.size(); j++) {
                    if (data[0].trim().contains(bigrams.get(j))) {
                        if (!stopWords.contains(data[0].trim()) && data[0].trim().contains(bigrams.get(j))) {
                            if (bigramFrequency.containsKey(bigrams.get(j)))
                                bigramFrequency.get(bigrams.get(j)).add(data[0]);
                            else bigramFrequency.put(bigrams.get(j), new ArrayList<>());
                            frequency.put(data[0], data.length - 1);
                        }
                    }
                }
            }
        }
    }

    public int editDistance(String token1, String token2) {

        int[][] dp = new int[token1.length() + 1][token2.length() + 1];

        for (int i = 0; i <= token1.length(); i++)
            dp[i][0] = i;

        for (int j = 0; j <= token2.length(); j++)
            dp[0][j] = j;

        for (int i = 0; i < token1.length(); i++) {
            char c1 = token1.charAt(i);
            for (int j = 0; j < token2.length(); j++) {
                char c2 = token2.charAt(j);

                if (c1 == c2) {
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }
        return dp[token1.length()][token2.length()];
    }
}
