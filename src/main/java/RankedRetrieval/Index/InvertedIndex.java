package RankedRetrieval.Index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {
    private Map<String, Map<Integer, List>> table = new HashMap<>();
    PersianNormalizer normalizer = new PersianNormalizer();
    CSV csv = new CSV();
    SPIM spim = new SPIM();
    public static final int BLOCK_SIZE = 150_000_000;
    int filecounter = 0;
    int size = 0;

    public InvertedIndex() throws IOException {
    }

    public void add(int id, String string) throws IOException {
        String[] prevTokens = string.trim().split("([\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\0020\\u060C\\u066B\\u200D\\u064E\\u0650\\u064F\\u226B\\u3164\\u00BB\\«\\؛َ]+)");
        String[] tokens = normalizer.nomallize(prevTokens);
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
            if ((table.size() + size) < BLOCK_SIZE) {
                if (!table.containsKey(tokens[i])) {
                    Map<Integer, List> map = new HashMap<>();
                    List<Integer> positionList = new ArrayList<>();
                    positionList.add(i);
                    map.put(id, positionList);
                    table.put(tokens[i], map);
                } else {
                    Map<Integer, List> map = table.get(tokens[i]);
                    if (map.containsKey(id)) {
                        map.get(id).add(i);
                    } else {
                        List<Integer> positionList = new ArrayList<>();
                        positionList.add(i);
                        map.put(id, positionList);
                    }
                    table.put(tokens[i], map);
                }
            } else {
                csv.releaseMemory(table, filecounter);
                filecounter++;
                table.clear();
                size++;
                i--;
            }
        }

    }

    public void last() throws IOException {
        csv.releaseMemory(table, filecounter);
        filecounter++;
        table.clear();
        spim.mergeBlocks(filecounter);
    }

}
