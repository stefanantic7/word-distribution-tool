package rs.raf.word_distribution.counter_cruncher;

import java.util.*;
import java.util.stream.Collectors;

public class BagOfWords {

    private String[] words;

    public BagOfWords(int size) {
        this.words = new String[size];

        for (int i = 0; i < size; i++) {
            this.words[i] = "";
        }
    }

    public boolean add(String word) {
        this.words[0] = word;

        Arrays.sort(this.words);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BagOfWords that = (BagOfWords) o;
        return Arrays.equals(words, that.words);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(words);
    }

    @Override
    public String toString() {
        return "BagOfWords{" +
                "words=" + Arrays.toString(words) +
                '}';
    }

//    @Override
//    public boolean add(String string) {
//        int index = Collections.binarySearch(this, string);
//        if (index < 0) index = ~index;
//        super.add(index, string);
//        return true;
//    }

}
