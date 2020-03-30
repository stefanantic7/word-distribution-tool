package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCounterTask extends RecursiveTask<Map<BagOfWords, Integer>> {

    private int start;
    private int end;
    private String content;
    private int arity;
    private boolean small;

    private static final int LIMIT = Config.COUNTER_DATA_LIMIT;

    public WordCounterTask(int start, int end, String content, int arity, boolean small) {
        this.start = start;
        this.end = end;
        this.content = content;
        this.arity = arity;
        this.small = small;
    }

    @Override
    protected Map<BagOfWords, Integer> compute() {
        Map<BagOfWords, Integer> bagsMap = new HashMap<>();

        int contentLength = this.content.length();

        if(start >= contentLength) {
            return bagsMap;
        }

        if (this.small) {
            // tovari po brojanju i stavi u mapu
//            synchronized (WordCounterTask.class) {
//                System.out.println("index1: "+start+", index2: "+end);
//                System.out.println(content.substring(start, end));
//            }

            bagsMap = this.countWords(start, end);
        }
        else {

            int index1 = start;
            int index2;

            index2 = index1 + LIMIT;

            while (true) {
                if (index2 >= contentLength) {
                    index2 = contentLength;
                    break;
                }
                if (content.charAt(index2) == ' ') {
                    break;
                }

                index2++;
            }

            WordCounterTask forkJob = new WordCounterTask(index1, index2, content, arity, true);

            index1 = index2+1;
            if(index1 < contentLength) {
                // Go back to take words before
                int wordsBefore = this.arity - 1;
                if (wordsBefore > 0) {
                    index1 -= 2;
                }
                while (true) {
                    if (index1 <= 0) {
                        index1 = 0;
                        break;
                    }
                    if (wordsBefore == 0) {
                        break;
                    }
                    if (this.content.charAt(index1) == ' ') {
                        wordsBefore--;
                        if (wordsBefore == 0) {
                            index1++;

                            break;
                        }
                    }
                    index1--;
                }
            }
            boolean isEnd = contentLength - index1-1 <= LIMIT;
            WordCounterTask computeJob = new WordCounterTask(index1, contentLength, content, arity, isEnd);




            forkJob.fork();
            Map<BagOfWords, Integer> rightResult = computeJob.compute();
            Map<BagOfWords, Integer> leftResult = forkJob.join();

            // Merging
            for (Map.Entry<BagOfWords, Integer> entry : rightResult.entrySet()) {
                leftResult.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
            bagsMap =  leftResult;
        }

        return bagsMap;
    }

    private Map<BagOfWords, Integer> countWords(int index1, int index2) {
        HashMap<BagOfWords, Integer> bagsMap = new HashMap<>();

        ArrayList<String> words = this.extractWords(index1, index2);

//        System.out.println(words);
        for (int i = 0; i < (words.size() - (this.arity-1)); i++) {
            BagOfWords bagOfWords = new BagOfWords();
            for (int j = i; j < i + this.arity; j++) {
                bagOfWords.add(words.get(j));
            }
            bagsMap.merge(bagOfWords, 1, Integer::sum);
        }

        return bagsMap;
    }

    private ArrayList<String> extractWords(int index1, int index2) {
        int startOfWord = index1;

        ArrayList<String> words = new ArrayList<>();
        while (index1 < index2) {
            if (!Character.isWhitespace(this.content.charAt(index1))) {
                index1++;
            } else {
                String word = content.substring(startOfWord, index1).intern();
                words.add(word);

                index1++;
                startOfWord = index1;
            }
        }
        if (startOfWord<index1) {
            words.add(content.substring(startOfWord, index1).intern());
        }

        return words;
    }
}
