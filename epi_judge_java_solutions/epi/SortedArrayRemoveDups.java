package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.*;

/**
 * 6.5 DELETE DUPLICATES FROM A SORTED ARRAY
 * <p>
 * This problem is concerned with deleting repeated elements from a sorted array.
 * For example, for the array (2,3,5,5,7,11,11,11,13), then after deletion, the array is
 * (2,3,5,7,11,13,0,0,0). After deleting repeated elements, there are 6 valid entries.
 * There are no requirements as to the values stored beyond the last valid element.
 * <p>
 * Write a program which takes as input a sorted array and updates it so that all
 * duplicates have been removed and the remaining elements have been shifted left to fill
 * the emptied indices. Return the number of valid elements. Many languages have library
 * functions for performing this operation—you cannot use these functions.
 * <p>
 * Hint: There is an 0(n) time and 0(1) space solution.
 */
public class SortedArrayRemoveDups {
    // Returns the number of valid entries after deletion.
    public static int deleteDuplicates(List<Integer> A) {
        return solThree(A);
    }

    /**
     * 思路一：使用哈希集合判断元素是否重复，将不重复的元素添加到新的列表，最后再拷贝回原来的列表
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    private static int solOne(List<Integer> A) {
        Set<Integer> set = new HashSet<>();
        List<Integer> list = new ArrayList<>();
        for (Integer integer : A) {
            if (set.contains(integer)) {
                continue;
            }
            set.add(integer);
            list.add(integer);
        }
        Collections.copy(A, list);
        return list.size();
    }

    /**
     * 思路二：遍历数组，遇到重复的元素就将后面的数组整体左移1位
     * <p>
     * 时间复杂度：O(n ^ 2)
     * 空间复杂度：O(1)
     */
    private static int solTwo(List<Integer> A) {
        if (A.isEmpty()) {
            return 0;
        }

        // 遍历时必须考虑重复次数，不然循环将无法结束
        int writeIndex = 1;
        int repeatCount = 0;
        for (int i = 1; i < A.size() - repeatCount; i++) {
            if (A.get(writeIndex - 1).equals(A.get(i))) {
                // 数组整体左移1位
                for (int j = writeIndex; j < A.size() - 1; j++) {
                    A.set(j, A.get(j + 1));
                }
                if (A.get(writeIndex - 1).equals(A.get(writeIndex))) {
                    i--;
                } else {
                    writeIndex++;
                }
                repeatCount++;
            } else {
                writeIndex++;
            }
        }
        return writeIndex;
    }

    /**
     * 思路三：遍历数组，使用一个指针记录下一个要写入的元素，将不重复的元素写到前部
     * <p>
     * 判断元素是否相等时，不能直接用'=='，不然会报错（Integer.valueOf 会缓存 -128~127 的元素）
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static int solThree(List<Integer> A) {
        if (A.isEmpty()) {
            return 0;
        }

        int writeIndex = 1;
        for (int i = 1; i < A.size(); i++) {
            if (!A.get(writeIndex - 1).equals(A.get(i))) {
                A.set(writeIndex++, A.get(i));
            }
        }
        return writeIndex;
    }


    @EpiTest(testDataFile = "sorted_array_remove_dups.tsv")
    public static List<Integer> deleteDuplicatesWrapper(TimedExecutor executor,
                                                        List<Integer> A)
            throws Exception {
        int end = executor.run(() -> deleteDuplicates(A));
        return A.subList(0, end);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SortedArrayRemoveDups.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
