package sps.util;

import org.apache.commons.io.FileUtils;
import sps.core.Logger;
import sps.core.RNG;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Based on: https:import static org.apache.commons.io.FileUtils.readLines;//gist.github.com/veryphatic/3190969

public class Markov {
    private static final String __startId = "$$start$$";
    private static final String __endId = "$$end$$";

    public static Map<String, ArrayList<String>> markovChain = new HashMap<String, ArrayList<String>>();

    public static void main(String[] args) throws IOException {
        digest("game/assets/data/markov-seed.txt", 3);
        Logger.info("WORD: " + genWord(9));
    }


    public static void digest(String corpusPath, int order) {
        if (markovChain.keySet().size() == 0) {
            markovChain.put(__startId, new ArrayList<String>());
            markovChain.put(__endId, new ArrayList<String>());
        }
        try {
            List<String> words = FileUtils.readLines(new File(corpusPath));
            for (String word : words) {
                addWord(word, order);
            }
        }
        catch (IOException e) {
            Logger.exception(e);
        }
    }

    public static void addWord(String word, int order) {
        if (order < 1) {
            throw new RuntimeException("order must be larger than 0");
        }

        if (word.length() == 0 || word.length() < order * 2) {
            return;
        }

        word = word.toLowerCase();

        String[] nGrams = new String[(int) Math.ceil(word.length() / (double) order)];
        for (int ii = 0; ii < word.length(); ii += order) {
            int index = ii / order;
            nGrams[index] = "";
            for (int jj = 0; jj < order; jj++) {
                nGrams[index] += word.charAt(jj);
            }
        }

        for (int ii = 0; ii < nGrams.length; ii++) {
            if (ii == 0) {
                ArrayList<String> startWords = markovChain.get(__startId);
                startWords.add(nGrams[ii]);

                ArrayList<String> suffix = markovChain.get(nGrams[ii]);
                if (suffix == null) {
                    suffix = new ArrayList<String>();
                    suffix.add(nGrams[ii + 1]);
                    markovChain.put(nGrams[ii], suffix);
                }

            }
            else if (ii == nGrams.length - 1) {
                ArrayList<String> endWords = markovChain.get(__endId);
                endWords.add(nGrams[ii]);

            }
            else {
                ArrayList<String> suffix = markovChain.get(nGrams[ii]);
                if (suffix == null) {
                    suffix = new ArrayList<String>();
                }
                if (nGrams[ii] != nGrams[ii + 1]) {
                    suffix.add(nGrams[ii + 1]);
                    markovChain.put(nGrams[ii], suffix);
                }
            }
        }
    }

    public static String genWord(int length) {
        String result = "";

        ArrayList<String> startWords = markovChain.get(__startId);
        String nextGram = startWords.get(RNG.next(startWords.size()));

        while (result.length() < length) {
            ArrayList<String> wordSelection = markovChain.get(nextGram);
            nextGram = wordSelection.get(RNG.next(wordSelection.size()));
            result += nextGram;
        }

        return result;
    }
}