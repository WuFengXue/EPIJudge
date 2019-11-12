package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * 7.4 REPLACE AND REMOVE
 * <p>
 * Consider the following two rules that are to be applied to an array of characters.
 * • Replace each 'a' by two 'd's.
 * • Delete each entry containing a 'b'.
 * For example, applying these rules to the array (a,c,d,b,b,c,a) results in the array
 * (d,d,c,d,c,d,d)
 * Write a program which takes as input an array of characters, and removes each 'b' and
 * replaces each 'a' by two 'd's. Specifically, along with the array, you are provided an
 * integer-valued size. Size denotes the number of entries of the array that the operation
 * is to be applied to. You do not have to worry preserving about subsequent entries. For
 * example, if the array is {a,b,a,c,_) and the size is 4, then you can return (d,d,d,d,c).
 * You can assume there is enough space in the array to hold the final result.
 * <p>
 * Hint: Consider performing multiples passes on s.
 */
public class ReplaceAndRemove {

    public static int replaceAndRemove(int size, char[] s) {
        return solThree(size, s);
    }

    /**
     * 思路一：利用 {@link String} 类的替换接口，先将字符数组转成字符串，替换字符后，
     * 再将结果复制回原来的字符数组
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static int solOne(int size, char[] s) {
        // 将字符数组转成字符串并移除后面的无效部分
        String str = new String(s).trim();
        str = str.replace("a", "dd");
        str = str.replace("b", "");
        System.arraycopy(str.toCharArray(), 0, s, 0, str.length());
        return str.length();
    }

    /**
     * 思路二：遍历字符数组，先将结果存储到 {@link StringBuilder} 中，最后再将结果拷贝回原来的
     * 字符数组
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static int solTwo(int size, char[] s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s) {
            if (c == 'a') {
                sb.append("dd");
            } else if (c == 'b' || c == '\0') {
                continue;
            } else {
                sb.append(c);
            }
        }
        String str = sb.toString();
        System.arraycopy(str.toCharArray(), 0, s, 0, str.length());
        return str.length();
    }

    /**
     * 思路三：分两步操作
     * <p>
     * 第1步，正向遍历，将结果写到头部，移除 'b' 字符，并统计 'a' 字符的个数
     * <p>
     * 第2步，反向遍历，将结果写到尾部，尾部的起始坐标 = 第 1 步写入指针的位置 - 1 + 'a' 字符数
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solThree(int size, char[] s) {
        int writeIdx = 0, aCount = 0;
        // Forward iteration: remove "b"s and count the number of "a"s.
        for (int i = 0; i < size; i++) {
            if (s[i] != 'b') {
                s[writeIdx++] = s[i];
            }
            if (s[i] == 'a') {
                aCount++;
            }
        }

        // Backward iteration: replace "a"s with "dd"s starting from the end.
        final int finalSize = writeIdx + aCount;
        int curIdx = writeIdx - 1;
        writeIdx = finalSize - 1;
        while (curIdx >= 0) {
            if (s[curIdx] == 'a') {
                s[writeIdx--] = 'd';
                s[writeIdx--] = 'd';
            } else {
                s[writeIdx--] = s[curIdx];
            }
            curIdx--;
        }
        return finalSize;
    }

    @EpiTest(testDataFile = "replace_and_remove.tsv")
    public static List<String>
    replaceAndRemoveWrapper(TimedExecutor executor, Integer size, List<String> s)
            throws Exception {
        char[] sCopy = new char[s.size()];
        for (int i = 0; i < size; ++i) {
            if (!s.get(i).isEmpty()) {
                sCopy[i] = s.get(i).charAt(0);
            }
        }

        Integer resSize = executor.run(() -> replaceAndRemove(size, sCopy));

        List<String> result = new ArrayList<>();
        for (int i = 0; i < resSize; ++i) {
            result.add(Character.toString(sCopy[i]));
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ReplaceAndRemove.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
