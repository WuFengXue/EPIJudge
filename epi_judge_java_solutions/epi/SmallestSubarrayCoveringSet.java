package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.*;

/**
 * 13.7 FIND THE SMALLEST SUBARRAY COVERING ALL VALUES
 * <p>
 * When you type keywords in a search engine, the search engine will return results,
 * and each result contains a digest of the web page, i.e., a highlighting within
 * that page of the keywords that you searched for. For example, a search for the
 * keywords "Union" and "save" on a page with the text of the Emancipation
 * Proclamation should return the result shown in Figure 13.1.
 * <p>
 * My paramount object in this struggle is to 【save the Union】, and is not either to
 * save or to destroy slavery. If I could save the Union without freeing any slave
 * I would do it, and if I could save it by freeing all the slaves I would do it;
 * and if I could save it by freeing some and leaving others alone I would also do
 * that.
 * <p>
 * Figure 13.1: Search result with digest in boldface and search keywords underlined.
 * <p>
 * The digest for this page is the text in boldface, with the keywords underlined
 * for emphasis. It is the shortest substring of the page which contains all the
 * keywords in the search. The problem of computing the digest is abstracted as
 * follows.
 * <p>
 * Write a program which takes an array of strings and a set of strings, and return
 * the indices of the starting and ending index of a shortest subarray of the given
 * array that "covers" the set, i.e., contains all strings in the set.
 * <p>
 * Hint: What is the maximum number of minimal subarrays that can cover the query?
 */
public class SmallestSubarrayCoveringSet {

    public static Subarray findSmallestSubarrayCoveringSet(List<String> paragraph,
                                                           Set<String> keywords) {
        return solThree(paragraph, keywords);
    }

    /**
     * 思路三：在思路二上进行优化，支持流模式，用 LRU 缓存搜索关键字的最新下标，
     * 当全部覆盖关键字后，其结果为当前下标减去 LRU 首个关键字的下标
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(k)，k 为关键字的数量
     */
    private static Subarray solThree(List<String> paragraph,
                                     Set<String> keywords) {
        Subarray result = new Subarray(-1, -1);
        LinkedHashMap<String, Integer> dict = new LinkedHashMap<>();
        for (String s : keywords) {
            dict.put(s, null);
        }
        int remainingToCover = dict.size();
        int idx = 0;
        Iterator<String> it = paragraph.iterator();
        while (it.hasNext()) {
            String s = it.next();
            // s is in keywords.
            if (dict.containsKey(s)) {
                if (dict.get(s) == null) {
                    // First time seeing this string from keywords.
                    remainingToCover--;
                }
                // diet.put(s,idx) won't work because it does not move the entry to
                // the front of the queue if an entry with key s is already present.
                // So we explicitly remove the existing entry with key s, then put
                // (s,idx).
                dict.remove(s);
                dict.put(s, idx);
                if (remainingToCover == 0) {
                    // We have seen all strings in keywords, let’s get to work.
                    if ((result.start == -1 && result.end == -1)
                            || (idx - getValueForLatestEntry(dict) < result.end - result.start)) {
                        result.start = getValueForLatestEntry(dict);
                        result.end = idx;
                    }
                }
            }
            idx++;
        }
        return result;
    }

    private static int getValueForLatestEntry(LinkedHashMap<String, Integer> dict) {
        // LinkedHashMap guarantees iteration over key-value pairs takes place in
        // insertion order, most recent first.
        int result = -1;
        for (Map.Entry<String, Integer> entry : dict.entrySet()) {
            result = entry.getValue();
            break;
        }
        return result;
    }

    /**
     * 思路二：在思路一上进行优化，用左右两个指针定义窗口的左右边界，先移动右指针，直到覆盖
     * 全部关键字，然后移动左指针，直到不再覆盖全部关键字，然后移动右指针……
     * <p>
     * 时间复杂度：O(n)，左、右指针最多各遍历一遍
     * <p>
     * 空间复杂度：O(k)，k 为关键字的数量
     */
    private static Subarray solTwo(List<String> paragraph,
                                   Set<String> keywords) {
        Subarray result = new Subarray(-1, -1);
        Map<String, Integer> keywordsToCover = new HashMap<>();
        for (String s : keywords) {
            keywordsToCover.put(s, keywordsToCover.getOrDefault(s, 0) + 1);
        }
        int remainingToCover = keywordsToCover.size();
        for (int left = 0, right = 0; right < paragraph.size(); right++) {
            String word = paragraph.get(right);
            if (keywordsToCover.containsKey(word)) {
                keywordsToCover.put(word, keywordsToCover.get(word) - 1);
                if (keywordsToCover.get(word) == 0) {
                    remainingToCover--;
                }
            }
            // Keeps advancing left until it reaches end or keywordsToCover does not
            // have all keywords.
            while (remainingToCover == 0) {
                if ((result.start == -1 && result.end == -1)
                        || (right - left < result.end - result.start)) {
                    result.start = left;
                    result.end = right;
                }
                word = paragraph.get(left);
                if (keywordsToCover.containsKey(word)) {
                    if (keywordsToCover.get(word) == 0) {
                        remainingToCover++;
                    }
                    keywordsToCover.put(word, keywordsToCover.get(word) + 1);
                }
                left++;
            }
        }
        return result;
    }

    /**
     * 思路一：遍历全部的正向子串，每次覆盖全部关键字后马上停止
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(k)，k 为关键字的数量
     */
    private static Subarray solOne(List<String> paragraph,
                                   Set<String> keywords) {
        Subarray result = new Subarray(-1, -1);
        for (int i = 0; i < paragraph.size(); i++) {
            String word = paragraph.get(i);
            if (!keywords.contains(word)) {
                continue;
            }

            Map<String, Integer> keywordsToCover = new HashMap<>();
            for (String s : keywords) {
                keywordsToCover.put(s, keywordsToCover.getOrDefault(s, 0) + 1);
            }
            int remainingToCover = keywordsToCover.size();
            for (int j = i; j < paragraph.size(); j++) {
                word = paragraph.get(j);
                if (keywordsToCover.containsKey(word)) {
                    keywordsToCover.put(word, keywordsToCover.get(word) - 1);
                    if (keywordsToCover.get(word) == 0) {
                        remainingToCover--;
                    }
                    if (remainingToCover == 0) {
                        if ((result.start == -1 && result.end == -1)
                                || (j - i < result.end - result.start)) {
                            result.start = i;
                            result.end = j;
                        }
                        break;
                    }
                }
            }
        }
        return result;
    }

    @EpiTest(testDataFile = "smallest_subarray_covering_set.tsv")
    public static int findSmallestSubarrayCoveringSetWrapper(
            TimedExecutor executor, List<String> paragraph, Set<String> keywords)
            throws Exception {
        Set<String> copy = new HashSet<>(keywords);

        Subarray result = executor.run(
                () -> findSmallestSubarrayCoveringSet(paragraph, keywords));

        if (result.start < 0 || result.start >= paragraph.size() ||
                result.end < 0 || result.end >= paragraph.size() ||
                result.start > result.end)
            throw new TestFailure("Index out of range");

        for (int i = result.start; i <= result.end; i++) {
            copy.remove(paragraph.get(i));
        }

        if (!copy.isEmpty()) {
            throw new TestFailure("Not all keywords are in the range");
        }
        return result.end - result.start + 1;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SmallestSubarrayCoveringSet.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    // Represent subarray by starting and ending indices, inclusive.
    private static class Subarray {
        public Integer start;
        public Integer end;

        public Subarray(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }
    }
}
