package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;

/**
 * 11.5 COMPUTE THE MEDIAN OF ONLINE DATA
 * <p>
 * You want to compute the running median of a sequence of numbers. The
 * sequence is presented to you in a streaming fashion—you cannot back up to
 * read an earlier value, and you need to output the median after reading in
 * each new element. For example, if the input is 1,0,3,5,2,0,1 the output is
 * 1,0.5,1,2,2,1.5,1.
 * <p>
 * Design an algorithm for computing the running median of a sequence.
 * <p>
 * Hint: Avoid looking at all values each time you read a new value.
 */
public class OnlineMedian {
    public static List<Double> onlineMedian(Iterator<Integer> sequence) {
        return solTwo(sequence);
    }

    /**
     * 思路二：将有序集合平分成两部分，比较小的部分和比较大的部分，分别用大根堆和
     * 小根堆进行存储（只关注中间元素），遍历数据，每读取一次数据就将其添加到对应的堆，
     * 动态调整使两个堆的元素数量保持一致或者较大部分的元素多一个，此时：
     * <p>
     * 当两个堆的数量一致时，中位数为两个堆的首个元素的和的一半
     * <p>
     * 较大部分比较小部分多一个元素时，中位数为较大部分的首个元素
     * <p>
     * 时间复杂度（单次操作）：O(log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static List<Double> solTwo(Iterator<Integer> sequence) {
        // maxHeap stores the smaller half seen so far.
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        // minHeap stores the larger half seen so far.
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        List<Double> result = new ArrayList<>();
        while (sequence.hasNext()) {
            Integer x = sequence.next();
            // This is the very first element.
            if (minHeap.isEmpty()
                    || x >= minHeap.peek()) {
                minHeap.add(x);
            } else {
                maxHeap.add(x);
            }
            // Ensure minHeap and maxHeap have equal number of elements if
            // an even number of elements is read; otherwise, minHeap must have
            // one more element than maxHeap.
            if (minHeap.size() > maxHeap.size() + 1) {
                maxHeap.add(minHeap.remove());
            } else if (maxHeap.size() > minHeap.size()) {
                minHeap.add(maxHeap.remove());
            }
            result.add(maxHeap.size() == minHeap.size()
                    ? 0.5 * (maxHeap.peek() + minHeap.peek())
                    : 1.0 * minHeap.peek());
        }
        return result;
    }

    /**
     * 思路一：缓存全部的数据，每读取一个数据就对缓存做一次排序，然后依照中位数的定义
     * 读取数据：
     * <p>
     * 数据量为奇数时，中位数为有序集合的中间元素
     * <p>
     * 数据量为偶数时，中位数为有序集合中间两个元素的和的一半
     * <p>
     * 时间复杂度（单次操作）：O(n * log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static List<Double> solOne(Iterator<Integer> sequence) {
        List<Integer> dataList = new ArrayList<>();
        List<Double> result = new ArrayList<>();
        while (sequence.hasNext()) {
            dataList.add(sequence.next());
            Collections.sort(dataList);
            int halfSize = dataList.size() / 2;
            result.add(dataList.size() % 2 == 1
                    ? 1.0 * dataList.get(halfSize)
                    : 0.5 * (dataList.get(halfSize - 1) + dataList.get(halfSize)));
        }
        return result;
    }

    @EpiTest(testDataFile = "online_median.tsv")
    public static List<Double> onlineMedianWrapper(List<Integer> sequence) {
        return onlineMedian(sequence.iterator());
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "OnlineMedian.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
