package RankedRetrieval.Index;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SPIM {
    private int chunkSize = 100000; //n line to load from block
    private Map<String, Map<Integer, List>> table = new TreeMap<>();
    private Map<Integer, List> map = new TreeMap<>();
    CSV csv = new CSV();
    String[] blockNames;

    public SPIM() throws IOException {
    }

    public void mergeBlocks(int blockID) throws IOException {
        table = new TreeMap<>();
        String[] blockNames = csv.parseBlocksNames(blockID);
        Map<String, Map<Integer, List>> blockMaps[] = new TreeMap[blockID];

        for (int i = 0; i < blockID; i++) {
            blockMaps[i] = new TreeMap<>();
            blockMaps[i] = csv.addChunkToBlock(blockNames[i]);
        }

        String top = "";
        PriorityQueue<String> pq = null;
        String[] topTerms = new String[blockID];

        while ((topTerms = getTops((TreeMap<String, Map<Integer, List>>[]) blockMaps, blockID)) != null) {

            pq = new PriorityQueue<String>();
            for (int i = 0; i < blockID; i++)
                if (topTerms[i] != null)
                    pq.add(topTerms[i]);

            top = pq.peek();
            table.put(top, pullTerm(top, blockMaps));
//            table=pullTerm(top,blockMaps);
            if (table.size() > chunkSize * 1000)
//                csv.releaseMemory(table,blockID+3);
                csv.releaseMemoryfinal(table);

        }
//        csv.releaseMemory(table,blockID+3);
        csv.releaseMemoryfinal(table);
    }

    private String[] getTops(TreeMap<String, Map<Integer, List>>[] blockMaps, int blockID) throws IOException {
        String[] terms = new String[blockID];
        int isEmpty = 0;
        for (int i = 0; i < blockID; i++) {
            if (blockMaps[i].size() != 0)
                terms[i] = blockMaps[i].firstKey();
            else
                isEmpty++;
        }
        if (isEmpty == blockID)
            return null;
        return terms;
    }

    private Map<Integer, List> pullkey(List<Map<Integer, List>> dataset) {
        Map<Integer, List> uniqueSets = dataset.stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collector.of(
                                ArrayList::new,
                                (list, item) -> list.addAll(item.getValue()),
                                (left, right) -> {
                                    left.addAll(right);
                                    return left;
                                })
                ));
        return uniqueSets;
    }

    private Map<Integer, List> pullTerm(String term, Map<String, Map<Integer, List>>[] maps) {
        Map<Integer, List> map = new HashMap<>();
        List<Map<Integer, List>> valueList = new ArrayList<Map<Integer, List>>();
        for (int i = 0; i < maps.length; i++)
            if (maps[i].containsKey(term)) {
                valueList.add(maps[i].get(term));
                maps[i].remove((term));
            }

        if (valueList.size() > 1)
            map = pullkey(valueList);
        else
            map = valueList.get(0);

        return map;
    }


}
