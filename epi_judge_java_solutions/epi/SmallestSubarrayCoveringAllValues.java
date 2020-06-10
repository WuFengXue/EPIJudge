package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 13.8 FIND SMALLEST SUBARRAY SEQUENTIALLY COVERING ALL VALUES
 * <p>
 * In Problem 13.7 on Page 218 we did not differentiate between the order
 * in which keywords appeared. If the digest has to include the keywords
 * in the order in which they appear in the search textbox, we may get a
 * different digest. For example, for the search keywords "Union" and
 * "save", in that order, the digest would be "Union, and is not either
 * to save".
 * <p>
 * Write a program that takes two arrays of strings, and return the
 * indices of the starting and ending index of a shortest subarray of the
 * first array (the "paragraph" array) that "sequentially covers", i.e.,
 * contains all the strings in the second array (the "keywords" array),
 * in the order in which they appear in the keywords array. You can assume
 * all keywords are distinct. For example, let the paragraph array be
 * (apple,banana,cat,apple), and the keywords array be (banana,apple).
 * The paragraph subarray starting at index 0 and ending at index 1 does
 * not fulfill the specification, even though it contains all the
 * keywords, since they do not appear in the specified order. On the other
 * hand, the subarray starting at index 1 and ending at index 3 does
 * fulfill the specification.
 * <p>
 * Hint: For each index in the paragraph array, compute the shortest
 * subarray ending at that index which fulfills the specification.
 */
public class SmallestSubarrayCoveringAllValues {

    public static Subarray
    findSmallestSequentiallyCoveringSubset(List<String> paragraph,
                                           List<String> keywords) {
        return solTwo(paragraph, keywords);
    }

    /**
     * 思路二：用哈希表记录每个关键字的序号、每个关键字最新出现时的下标以及
     * 从首个关键字出现位置到当前关键字的长度：
     * <p>
     * 假定 i 为当前遍历到的字的下标，idx 为其对应的关键字的序号
     * <p>
     * idx == 0 时，首个关键字的长度 = 0
     * <p>
     * idx > 0 时，第 idx 个关键字的长度 = 第 idx - 1 个关键字的长度 + i - 第 idx - 1 个关键字的最新下标
     * <p>
     * 因为关键字的序号可以直接使用入参的列表获取，所以只需使用两个列表记录下标
     * 和长度即可。
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(m)，m 为关键字的数量
     */
    private static Subarray solTwo(List<String> paragraph,
                                   List<String> keywords) {
        Subarray result = new Subarray(-1, -1);
        // Since keywords are uniquely identified by their indices in keywords
        // array, we can use those indices as keys to lookup in a vector.
        List<Integer> latestOccurrence = new ArrayList<>(keywords.size());
        // For each keyword (identified by its index in keywords array), stores the
        // length of the shortest subarray ending at the most recent occurrence of
        // that keyword that sequentially cover all keywords up to that keyword.
        List<Integer> shortestSubarrayLength = new ArrayList<>(keywords.size());
        // Initializes latestOccurrence, shortestSubarrayLength.
        for (int i = 0; i < keywords.size(); i++) {
            latestOccurrence.add(-1);
            shortestSubarrayLength.add(Integer.MAX_VALUE);
        }

        for (int i = 0; i < paragraph.size(); i++) {
            String word = paragraph.get(i);
            int keywordIdx = keywords.indexOf(word);
            if (keywordIdx == -1) {
                continue;
            }

            // First keyword.
            if (keywordIdx == 0) {
                shortestSubarrayLength.set(0, 0);
            } else if (shortestSubarrayLength.get(keywordIdx - 1) != Integer.MAX_VALUE) {
                int distanceToPreviousKeyword = i - latestOccurrence.get(keywordIdx - 1);
                shortestSubarrayLength.set(keywordIdx, shortestSubarrayLength.get(keywordIdx - 1)
                        + distanceToPreviousKeyword);
            }
            latestOccurrence.set(keywordIdx, i);

            // Last keyword, look for improved subarray.
            if (keywordIdx == keywords.size() - 1) {
                if ((result.start == -1 && result.end == -1)
                        || (shortestSubarrayLength.get(keywordIdx) < result.end - result.start)) {
                    result.start = i - shortestSubarrayLength.get(keywordIdx);
                    result.end = i;
                }
            }
        }
        return result;
    }

    /**
     * 思路一：遍历正向子数组，遇到首个关键字时，新建一个指针进行遍历，直到依次覆盖
     * 到全部的关键字，重复上述过程直到遍历结束
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(m)，m 为关键字的数量
     */
    private static Subarray solOne(List<String> paragraph,
                                   List<String> keywords) {
        Subarray result = new Subarray(-1, -1);
        for (int i = 0; i < paragraph.size(); i++) {
            String word = paragraph.get(i);
            if (!word.equals(keywords.get(0))) {
                continue;
            }

            Queue<String> remainingKeywords = new LinkedList<>(keywords);
            remainingKeywords.remove();
            for (int j = i + 1; j < paragraph.size(); j++) {
                word = paragraph.get(j);
                if (!word.equals(remainingKeywords.peek())) {
                    continue;
                }

                remainingKeywords.remove();
                if (remainingKeywords.isEmpty()) {
                    if ((result.start == -1 && result.end == -1)
                            || (j - i < result.end - result.start)) {
                        result.start = i;
                        result.end = j;
                    }
                    break;
                }
            }
        }
        return result;
    }

    @EpiTest(testDataFile = "smallest_subarray_covering_all_values.tsv")
    public static int findSmallestSequentiallyCoveringSubsetWrapper(
            TimedExecutor executor, List<String> paragraph, List<String> keywords)
            throws Exception {
        Subarray result = executor.run(
                () -> findSmallestSequentiallyCoveringSubset(paragraph, keywords));

        int kwIdx = 0;
        if (result.start < 0) {
            throw new TestFailure("Subarray start index is negative");
        }
        int paraIdx = result.start;

        while (kwIdx < keywords.size()) {
            if (paraIdx >= paragraph.size()) {
                throw new TestFailure("Not all keywords are in the generated subarray");
            }
            if (paraIdx >= paragraph.size()) {
                throw new TestFailure("Subarray end index exceeds array size");
            }
            if (paragraph.get(paraIdx).equals(keywords.get(kwIdx))) {
                kwIdx++;
            }
            paraIdx++;
        }
        return result.end - result.start + 1;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SmallestSubarrayCoveringAllValues.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public static class Subarray {
        // Represent subarray by starting and ending indices, inclusive.
        public Integer start;
        public Integer end;

        public Subarray(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }
    }
}
