package RankedRetrieval.ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MaxHeapMap {
    ArrayList<Map> Heaplist = new ArrayList<Map>();

    public MaxHeapMap() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.MAX_VALUE, Double.MAX_VALUE);
        Heaplist.add(map);
    }

    private int parent(int pos) {
        return pos / 2;
    }

    private boolean hasleft(int pos) {
        return leftChild(pos) < Heaplist.size();
    }

    private boolean hasright(int pos) {
        return rightChild(pos) < Heaplist.size();
    }

    private int leftChild(int pos) {
        return (2 * pos);
    }

    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    private boolean isLeaf(int pos) {
        if (pos >= (Heaplist.size() / 2) && pos <= Heaplist.size()) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos) {
        double tmp = getValue(fpos);
        int tmpkey = getkey(fpos);
        setValue(fpos, getValue(spos));
        setValue(spos, tmp);

        setkey(fpos, getkey(spos));
        setkey(spos, tmpkey);
    }

    private void maxHeapify(int pos) {
        if (hasleft(pos)) {
            if (hasright(pos)) {
                if (getValue(pos) < getValue(leftChild(pos)) ||
                        getValue(pos) < getValue(rightChild(pos))) {

                    if (getValue(leftChild(pos)) > getValue(rightChild(pos))) {
                        swap(pos, leftChild(pos));
                        maxHeapify(leftChild(pos));
                    } else {
                        swap(pos, rightChild(pos));
                        maxHeapify(rightChild(pos));
                    }
                }
            } else if (getValue(pos) < getValue(leftChild(pos))) {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            }

        }


    }

    public void insert(int key, double element) {
        Map<Integer, Double> map = new HashMap<>();
        map.put(key, element);
        Heaplist.add(map);
        int current = Heaplist.size() - 1;
        while (getValue(current) > getValue(parent(current))) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public double getValue(int i) {
        Map<Integer, Double> map = Heaplist.get(i);
        double value = 0;
        for (Map.Entry<Integer, Double> entry : map.entrySet())
            value = entry.getValue();
        return value;
    }

    public int getkey(int i) {
        Map<Integer, Double> map = Heaplist.get(i);
        int key = 0;
        for (Map.Entry<Integer, Double> entry : map.entrySet())
            key = entry.getKey();
        return key;
    }

    public void setValue(int i, double newValue) {
        Map<Integer, Double> map = Heaplist.get(i);
        for (Map.Entry<Integer, Double> entry : map.entrySet())
            map.replace(entry.getKey(), newValue);
    }

    public void setkey(int i, int newkey) {
        Map<Integer, Double> map = Heaplist.get(i);
        double value = 0;
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            value = entry.getValue();
            map.remove(entry.getKey());
            map.put(newkey, value);
        }

    }

    public ArrayList extractMax() {
        ArrayList score = new ArrayList();
        int n = findN();
        for (int i = 0; i < n; i++) {
            double poppedvalue = getValue(1);
            int popedkey = getkey(1);
            int size = Heaplist.size();
            int index = size - 1;
            setValue(1, getValue(index));
            setkey(1, getkey(index));
            Heaplist.remove(index);
            maxHeapify(1);
//            System.out.println(popedkey + "  " + poppedvalue);
            score.add(popedkey);
        }
        return score;
    }

    public int findN() {
        if (Heaplist.size() > 10)
            return 10;
        return Heaplist.size() - 1;

    }

//    public static void main(String[] arg) {
//        MaxHeapMap maxHeap = new MaxHeapMap();
//        maxHeap.insert(5, 4);
//        maxHeap.insert(3, 5);
//        maxHeap.insert(2, 3);
//        maxHeap.insert(1, 17);
//        maxHeap.insert(10, 10);
//
//
//        maxHeap.extractMax();
//
//    }
}
