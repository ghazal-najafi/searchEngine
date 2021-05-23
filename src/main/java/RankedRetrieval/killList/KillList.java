package RankedRetrieval.killList;

import RankedRetrieval.Index.CSV;
import RankedRetrieval.Index.PersianNormalizer;
import RankedRetrieval.controller.DocumentController;
import RankedRetrieval.service.DocumentService;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.IndexTreeList;
import org.mapdb.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillList {
    static DB db = DBMaker.fileDB("InsertList.db")
            .checksumHeaderBypass()
            .transactionEnable()
            .closeOnJvmShutdown()
            .make();
    static DB db2 = DBMaker.fileDB("DeleteList.db")
            .checksumHeaderBypass()
            .transactionEnable()
            .closeOnJvmShutdown()
            .make();
    Map<String, Map<Integer, List>> tableInsert;

    IndexTreeList<Integer> listDelete;

    PersianNormalizer normalizer = new PersianNormalizer();

    public static final int BLOCK_SIZE = 100_000_000;
    int size = 0;

    public boolean add(String string, int id) {
        OpenDB();
        String[] prevTokens = string.trim().split("([\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\0020\\u060C\\u066B\\u200D\\u064E\\u0650\\u064F\\u226B\\u3164\\u00BB\\«\\؛َ]+)");
        String[] tokens = normalizer.nomallize(prevTokens);
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
            if ((tableInsert.size() + size) < BLOCK_SIZE) {
                if (!tableInsert.containsKey(tokens[i])) {
                    Map<Integer, List> map = new HashMap<>();
                    List<Integer> positionList = new ArrayList<>();
                    positionList.add(i);
                    size++;
                    map.put(id, positionList);
                    tableInsert.put(tokens[i], map);
                } else {
                    Map<Integer, List> map = tableInsert.get(tokens[i]);
                    if (map.containsKey(id)) {
                        map.get(id).add(i);
                        size++;
                    } else {
                        List<Integer> positionList = new ArrayList<>();
                        positionList.add(i);
                        size++;
                        map.put(id, positionList);
                    }
                    tableInsert.put(tokens[i], map);
                }
            } else {
                tableInsert.clear();
                listDelete.clear();
                size = 0;
                return false;
            }
        }
        closeDB();
        return true;
    }

    public boolean delete(int id) throws IOException, SQLException {
        OpenDB();
        if (listDelete.size() < BLOCK_SIZE) {
            if (!listDelete.contains(id))
                listDelete.add(id);
        } else {
            listDelete.clear();
            tableInsert.clear();
            return false;
        }
        closeDB();
        return true;
    }

    public void update(String string, int id) throws IOException, SQLException {
        OpenDB();
        delete(id);
        add(string, id);
        closeDB();
    }

    public void closeDB() {
        db.commit();
        db2.commit();
    }

    public void OpenDB() {
        tableInsert = (Map<String, Map<Integer, List>>) db.hashMap("killListInsert").createOrOpen();
        listDelete = db2.indexTreeList("killListDelete", Serializer.INTEGER).createOrOpen();
    }

    public ArrayList checkIDs(String string, ArrayList ids) {
        if (listDelete != null)
            for (int DeleteId : listDelete)
                if (ids.contains(DeleteId))
                    ids.remove(ids.indexOf(DeleteId));

        for (int InsertId : getInsertId(string))
            if (!ids.contains(InsertId))
                ids.add(InsertId);
        return ids;
    }

    public ArrayList<Integer> getInsertId(String string) {
        ArrayList<Integer> ids = new ArrayList();
        if (tableInsert != null)
            for (Map.Entry<String, Map<Integer, List>> entry : tableInsert.entrySet())
                if (entry.getKey().equals(string))
                    for (Map.Entry<Integer, List> entry2 : entry.getValue().entrySet())
                        ids.add(entry2.getKey());
        return ids;
    }

//    public static void main(String[] args) {
//        DocumentController documentService = new DocumentController();
////        documentService.x();
////        System.out.println(documentService.count());
//    }

}