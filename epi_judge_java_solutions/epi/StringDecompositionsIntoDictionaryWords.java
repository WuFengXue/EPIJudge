package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 13.12 COMPUTE ALL STRING DECOMPOSITIONS
 * <p>
 * This problem is concerned with taking a string (the "sentence" string)
 * and a set of strings (the "words"), and finding the substrings of the
 * sentence which are the concatenation of all the words (in any order).
 * For example, if the sentence string is "amanaplanacanal" and the set of
 * words is {"can","apl","ana"), "aplanacan" is a substring of the sentence
 * that is the concatenation of all words.
 * <p>
 * Write a program which takes as input a string (the "sentence") and an array
 * of strings (the "words"), and returns the starting indices of substrings of
 * the sentence string which are the concatenation of all the strings in the
 * words array. Each string must appear exactly once, and their ordering is
 * immaterial. Assume all strings in the words array have equal length. It is
 * possible for the words array to contain duplicates.
 * <p>
 * Hint: Exploit the fact that the words have the same length.
 */
public class StringDecompositionsIntoDictionaryWords {
    @EpiTest(testDataFile = "string_decompositions_into_dictionary_words.tsv")

    public static List<Integer> findAllSubstrings(String s, List<String> words) {
        return solOne(s, words);
    }

    /**
     * 思路一：逐个检测句子的正向子串，看它是否由单词组成，检测过的单词就移除掉。
     * 用一个哈希集合统计每个单词出现的次数，然后在每次检测的时候另起一个哈希表用于
     * 记录当前单词的出现次数，如果当前单词的出现次数超出，则检测不通过并结束本次
     * 检测。
     * <p>
     * 时间复杂度：O(N * m * n)，N 为句子的长度，m 为单词的数量，n 为单词的长度
     * <p>
     * 空间复杂度：O(m * n)，m 为单词的数量，n 为单词的长度
     */
    private static List<Integer> solOne(String s, List<String> words) {
        List<Integer> result = new ArrayList<>();
        Map<String, Integer> wordToFreq = new HashMap<>();
        int wordLen = 0;
        int substringLen = 0;
        for (String word : words) {
            if (wordLen == 0) {
                wordLen = word.length();
                substringLen = wordLen * words.size();
            }
            int freq = wordToFreq.getOrDefault(word, 0);
            wordToFreq.put(word, freq + 1);
        }
        for (int i = 0; i + substringLen <= s.length(); i++) {
            Map<String, Integer> currWordToFreq = new HashMap<>();
            boolean found = true;
            for (int j = i; j < i + substringLen; j += wordLen) {
                String word = s.substring(j, j + wordLen);
                int freq = currWordToFreq.getOrDefault(word, 0);
                currWordToFreq.put(word, freq + 1);
                // 当前单词不在单词表或其出现频率超出单词表中的频率
                if (currWordToFreq.get(word) > wordToFreq.getOrDefault(word, 0)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                result.add(i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(GenericTest
                .runFromAnnotations(
                        args, "StringDecompositionsIntoDictionaryWords.java",
                        new Object() {
                        }.getClass().getEnclosingClass())
                .ordinal());
    }
}
