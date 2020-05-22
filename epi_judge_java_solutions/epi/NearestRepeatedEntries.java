package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 13.6 FIND THE NEAREST REPEATED ENTRIES IN AN ARRAY
 * <p>
 * People do not like reading text in which a word is used multiple
 * times in a short paragraph. You are to write a program which helps
 * identify such a problem.
 * <p>
 * Write a program which takes as input an array and finds the distance
 * between a closest pair of equal entries. For example, if s =
 * ("All", "work", "and", "no", "play", "makes", "for", "no", "work",
 * "no", "fun", "and", "no", "results"), then the second and third
 * occurrences of "no" is the closest pair.
 * <p>
 * Hint: Each entry in the array is a candidate.
 */
public class NearestRepeatedEntries {
    @EpiTest(testDataFile = "nearest_repeated_entries.tsv")

    public static int findNearestRepetition(List<String> paragraph) {
        return solThree(paragraph);
    }

    /**
     * 思路三：在思路二的基础上进行优化，遍历全部单词，用一个哈希表记录每种
     * 单词最新的下标
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(d)，d 为单词的种类数
     */
    private static int solThree(List<String> paragraph) {
        int nearestRepeatedDistance = Integer.MAX_VALUE;
        Map<String, Integer> wordToLatestIndex = new HashMap<>();
        for (int i = 0; i < paragraph.size(); i++) {
            String word = paragraph.get(i);
            if (wordToLatestIndex.containsKey(word)) {
                nearestRepeatedDistance = Math.min(nearestRepeatedDistance,
                        i - wordToLatestIndex.get(word));
            }
            wordToLatestIndex.put(word, i);
        }
        return (nearestRepeatedDistance == Integer.MAX_VALUE)
                ? -1
                : nearestRepeatedDistance;
    }

    /**
     * 思路二：遍历全部的单词，用哈希表记录同一单词出现位置的下标，最后再计算相邻下标
     * 的差
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(d)，单词的种类数
     */
    private static int solTwo(List<String> paragraph) {
        int result = Integer.MAX_VALUE;
        Map<String, List<Integer>> wordToIndices = new HashMap<>();
        for (int i = 0; i < paragraph.size(); i++) {
            String word = paragraph.get(i);
            List<Integer> indices = wordToIndices.getOrDefault(word,
                    new ArrayList<>());
            indices.add(i);
            wordToIndices.putIfAbsent(word, indices);
        }
        for (List<Integer> indices : wordToIndices.values()) {
            for (int i = 1; i < indices.size(); i++) {
                result = Math.min(result,
                        indices.get(i) - indices.get(i - 1));
            }
        }
        return (result == Integer.MAX_VALUE) ? -1 : result;
    }

    /**
     * 思路一：逐个遍历每个单词，查找与它最近的重复单词
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(List<String> paragraph) {
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < paragraph.size(); i++) {
            String word = paragraph.get(i);
            for (int j = i + 1; j < paragraph.size(); j++) {
                if (paragraph.get(j).equals(word)) {
                    result = Math.min(result, j - i);
                    break;
                }
            }
        }
        return (result == Integer.MAX_VALUE) ? -1 : result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "NearestRepeatedEntries.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
