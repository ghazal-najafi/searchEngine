package RankedRetrieval.search;

import com.opencsv.CSVReader;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogFile {
    private Map<String, Integer> logMap = new HashMap<>();
    private static Map<String, Integer> logMapSentence = new HashMap<>();

    public void readLogFile(String query) throws IOException {
        CSVReader reader = new CSVReader(new FileReader("src\\main\\resources\\index\\logfile.csv"), ',');
        String[] sentence = query.split("\\s+");
        String[] data;
        while ((data = reader.readNext()) != null)
            logMap.put(data[0].trim(), Integer.parseInt(data[1].trim()));

        for (int i = 0; i < sentence.length; i++) {
            int count = 0;
            String str = sentence[i].trim();
            if (logMap.containsKey(str)) {
                count = logMap.get(str);
                logMap.put(str, ++count);
            } else
                logMap.put(str, 1);
        }
        writeLogFile();
    }

    public void readLogFileSentence(String query) throws IOException {
        CSVReader reader = new CSVReader(new FileReader("src\\main\\resources\\index\\logfileSentence.csv"), ',');
        String[] data;
        while ((data = reader.readNext()) != null)
            logMapSentence.put(data[0].trim(), Integer.parseInt(data[1].trim()));
        int count = 0;
        query = query.trim();
        if (logMapSentence.containsKey(query)) {
            count = logMapSentence.get(query);
            logMapSentence.put(query, ++count);
        } else
            logMapSentence.put(query, 1);
        write();
    }

    public void write() throws IOException {
        FileWriter csvWriter = new FileWriter("src\\main\\resources\\index\\logfileSentence.csv", Boolean.parseBoolean("UTF-8"));
        BufferedWriter writer = new BufferedWriter(csvWriter);
        for (Map.Entry<String, Integer> entry : logMapSentence.entrySet())
            csvWriter.append(entry.getKey() + " , " + entry.getValue() + "\n");
        writer.close();
    }

    public static ArrayList<String> getLogFile() {
        ArrayList<String> sentence = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : logMapSentence.entrySet())
            sentence.add(entry.getKey());
        return sentence;
    }

    public void writeLogFile() throws IOException {
        FileWriter csvWriter = new FileWriter("src\\main\\resources\\index\\logfile.csv", Boolean.parseBoolean("UTF-8"));
        BufferedWriter writer = new BufferedWriter(csvWriter);
        for (Map.Entry<String, Integer> entry : logMap.entrySet())
            csvWriter.append(entry.getKey() + " , " + entry.getValue() + "\n");
        writer.close();
    }
}
