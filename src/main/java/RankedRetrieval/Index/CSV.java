package RankedRetrieval.Index;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CSV {
    public CSV() {
    }

    public String[] parseBlocksNames(int filecount) {
        String[] blocks = new String[filecount];
        for (int i = 0; i < filecount; i++)
            blocks[i] = i + ".csv";
        return blocks;
    }

    public void releaseMemory(Map<String, Map<Integer, List>> table, int i) throws IOException {
        Map<String, Map<Integer, List>> treeMap = new TreeMap<>(table);
        String x = "src\\main\\resources\\index\\" + i + ".csv";
        FileWriter csvWriter = new FileWriter(x, Boolean.parseBoolean("UTF-8"));
        for (Map.Entry<String, Map<Integer, List>> entry : treeMap.entrySet()) {
            csvWriter.append(entry.getKey() + ";");
            for (Map.Entry<Integer, List> entry2 : entry.getValue().entrySet())
                csvWriter.append(entry2.getKey() + ":" + entry2.getValue() + ";");
            csvWriter.append("\n");

        }
        csvWriter.close();
    }

    public void releaseMemoryfinal(Map<String, Map<Integer, List>> table) throws IOException {
        Map<String, Map<Integer, List>> treeMap = new TreeMap<>(table);
        FileWriter csvWriter = new FileWriter("src\\main\\resources\\index\\a-z.csv", Boolean.parseBoolean("UTF-8"));
        FileWriter csvWriter2 = new FileWriter("src\\main\\resources\\index\\alf-dal.csv", Boolean.parseBoolean("UTF-8"));
        FileWriter csvWriter3 = new FileWriter("src\\main\\resources\\index\\zal-ye.csv", Boolean.parseBoolean("UTF-8"));
        FileWriter csvWriter4 = new FileWriter("src\\main\\resources\\index\\other.csv", Boolean.parseBoolean("UTF-8"));
        for (Map.Entry<String, Map<Integer, List>> entry : treeMap.entrySet()) {
            if (entry.getKey().equals(""))
                continue;
            char avvali = entry.getKey().charAt(0);
            if ('a' <= avvali && avvali <= 'z') {
                csvWriter.append(entry.getKey() + ";");
                for (Map.Entry<Integer, List> entry2 : entry.getValue().entrySet())
                    csvWriter.append(entry2.getKey() + ":" + entry2.getValue() + ";");
                csvWriter.append("\n");
            } else if ('آ' <= avvali && avvali <= 'د') {
                csvWriter2.append(entry.getKey() + ";");
                for (Map.Entry<Integer, List> entry2 : entry.getValue().entrySet())
                    csvWriter2.append(entry2.getKey() + ":" + entry2.getValue() + ";");
                csvWriter2.append("\n");
            } else if ('د' < avvali && avvali <= 'ی') {
                csvWriter3.append(entry.getKey() + ";");
                for (Map.Entry<Integer, List> entry2 : entry.getValue().entrySet())
                    csvWriter3.append(entry2.getKey() + ":" + entry2.getValue() + ";");
                csvWriter3.append("\n");
            } else {
                csvWriter4.append(entry.getKey() + ";");
                for (Map.Entry<Integer, List> entry2 : entry.getValue().entrySet())
                    csvWriter4.append(entry2.getKey() + ":" + entry2.getValue() + ";");
                csvWriter4.append("\n");
            }
        }
        csvWriter.close();
        csvWriter2.close();
        csvWriter3.close();
        csvWriter4.close();
    }

    public TreeMap<String, Map<Integer, List>> addChunkToBlock(String path) throws IOException {
        TreeMap<String, Map<Integer, List>> blockMap = new TreeMap<>();
        path = "src\\main\\resources\\index\\" + path;
        CSVReader reader = new CSVReader(new FileReader(path), ';');
        String[] data;
        while ((data = reader.readNext()) != null) {
            Map<Integer, List> map = new HashMap();
            List<Integer> positionList;
            for (int i = 1; i < data.length; i++) {
                if (data[i].equals(""))
                    continue;
                String arr[] = data[i].trim().split(":");
                positionList = new ArrayList();
                for (int j = 1; j < arr.length; j++) {
                    String positon[] = arr[j].trim().split("[\\u005B,\\u005D\\u2009]");
                    for (String pos : positon) {
                        if (pos.equals(""))
                            continue;
                        positionList.add(Integer.valueOf(pos.trim()));
                    }
                }
                map.put(Integer.valueOf(arr[0]), positionList);
            }
            blockMap.put(data[0], map);
        }

        reader.close();
        return blockMap;
    }

    public TreeMap<String, Map<Integer, List>> readFile(String path, String word) throws IOException {
        TreeMap<String, Map<Integer, List>> blockMap = new TreeMap<>();
        CSVReader reader = new CSVReader(new FileReader(path), ';');
        String[] data;
        while ((data = reader.readNext()) != null) {
            if (data[0].trim().equals(word)) {
                Map<Integer, List> map = new HashMap();
                List<Integer> positionList;
                for (int i = 1; i < data.length; i++) {
                    if (data[i].equals(""))
                        continue;
                    String arr[] = data[i].trim().split(":");
                    positionList = new ArrayList();
                    for (int j = 1; j < arr.length; j++) {
                        String positon[] = arr[j].trim().split("[\\u005B,\\u005D\\u2009]");
                        for (String pos : positon) {
                            if (pos.equals(""))
                                continue;
                            positionList.add(Integer.valueOf(pos.trim()));
                        }
                    }
                    map.put(Integer.valueOf(arr[0]), positionList);
                }
                blockMap.put(data[0].trim(), map);
            }
        }
        reader.close();
        if (blockMap.isEmpty())
            return null;
        return blockMap;
    }

    public String getPath(String token) {
        char avvali = token.charAt(0);
        String path = "";
        if ('a' <= avvali && avvali <= 'z')
            path = "src\\main\\resources\\index\\a-z.csv";
        else if ('آ' <= avvali && avvali <= 'د')
            path = "src\\main\\resources\\index\\alf-dal.csv";
        else if ('د' < avvali && avvali <= 'ی')
            path = "src\\main\\resources\\index\\zal-ye.csv";
        else
            path = "src\\main\\resources\\index\\other.csv";
        return path;
    }
}