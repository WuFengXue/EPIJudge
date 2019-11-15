package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

/**
 * 7.6 REVERSE ALL THE WORDS IN A SENTENCE
 * <p>
 * Given a string containing a set of words separated by whitespace, we would like to
 * transform it to a string in which the words appear in the reverse order. For example,
 * "Alice likes Bob" transforms to "Bob likes Alice". We do not need to keep the original
 * string.
 * <p>
 * Implement a function for reversing the words in a string s.
 * <p>
 * Hint: It's difficult to solve this with one pass.
 */
public class ReverseWords {

    public static void reverseWords(char[] input) {
        solTwo(input);
        return;
    }

    /**
     * 思路零：先转成字符串，利用字符串的 {@link String#split(String)} 方法拆分词语，
     * 然后反向拼接词语
     * <p>
     * 说明：因为测试用例的空格数不固定，而且头尾都可能有空格，所以该思路无法通过测试
     * <p>
     * 时间复杂度：
     * <p>
     * 空间复杂度：O(n)
     */
    private static void solZero(char[] input) {
        String sentence = new String(input);
        StringBuilder sb = new StringBuilder();
        String[] words = sentence.split(" ");
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if (i != 0) {
                sb.append(" ");
            }
        }
        System.arraycopy(sb.toString().toCharArray(), 0, input, 0, input.length);
    }

    /**
     * 思路一：先翻转整个句子，再逐一翻转每个词语，使用 for 循环进行迭代
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solOne(char[] input) {
        // Reverses the whole string first.
        reverse(input, 0, input.length - 1);

        int start = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] == ' ') {
                // Reverses each word in the string.
                reverse(input, start, i - 1);
                start = i + 1;
            }
        }
        // Reverses the last word.
        reverse(input, start, input.length - 1);
    }

    /**
     * 思路二：先翻转整个句子，再逐一翻转每个词语，使用 while 循环进行迭代
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solTwo(char[] input) {
        // Reverses the whole string first.
        reverse(input, 0, input.length - 1);

        int start = 0, end = 0;
        while (start < input.length) {
            // Skip spaces chars.
            while (start < end || start < input.length && input[start] == ' ') {
                start++;
            }
            // Skip non-spaces chars.
            while (end < start || end < input.length && input[end] != ' ') {
                end++;
            }
            // Reverses each word in the string.
            reverse(input, start, end - 1);
        }
    }

    private static void reverse(char[] input, int start, int end) {
        while (start < end) {
            char tmp = input[start];
            input[start++] = input[end];
            input[end--] = tmp;
        }
    }

    @EpiTest(testDataFile = "reverse_words.tsv")
    public static String reverseWordsWrapper(TimedExecutor executor, String s)
            throws Exception {
        char[] sCopy = s.toCharArray();

        executor.run(() -> reverseWords(sCopy));

        return String.valueOf(sCopy);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ReverseWords.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
