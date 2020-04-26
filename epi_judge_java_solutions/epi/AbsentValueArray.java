package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 12.9 FIND THE MISSING IP ADDRESS
 * <p>
 * The storage capacity of hard drives dwarfs that of RAM. This can lead to
 * interesting space-time trade-offs.
 * <p>
 * Suppose you were given a file containing roughly one billion IP addresses,
 * each of which is a 32-bit quantity. How would you programmatically find an
 * IP address that is not in the file? Assume you have unlimited drive space
 * but only a few megabytes of RAM at your disposal.
 * <p>
 * Hint: Can you be sure there is an address which is not in the file?
 */
public class AbsentValueArray {

    @EpiTest(testDataFile = "absent_value_array.tsv")
    public static int findMissingElement(Iterable<Integer> stream) {
        return solOne(stream);
    }

    /**
     * 思路一：将IP地址拆分成高位和低位两部分（本算法各取16位），如果IP地址是满的，
     * 则相同高位的IP地址总数应为 2 ^ 16（低位的排列数），分4步进行查找：
     * <p>
     * 第一步，记录相同高位的IP地址总数，结果存储在数组 counter 中
     * <p>
     * 第二步，遍历 counter 数组，遇到的第一个数量小于 2 ^ 16 的数 x，它的低位肯定
     * 有缺少
     * <p>
     * 第三步，用一个位集合（BitSet）标注低位是否存在，重新遍历全部IP地址，遇到高位
     * 与 x 相同的就置位集合中的对应低位
     * <p>
     * 第四步，遍历位集合，遇到的第一个未设置的元素即为目标元素
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(4 * (2 ^ 16)) = O(0.25M)
     */
    private static int solOne(Iterable<Integer> stream) {
        final int NUM_BUCKET = 1 << 16;
        int[] counter = new int[NUM_BUCKET];
        Iterator<Integer> s = stream.iterator();
        while (s.hasNext()) {
            int idx = s.next() >>> 16;
            counter[idx]++;
        }

        for (int i = 0; i < counter.length; i++) {
            // Look for a bucket that contains less than NUN_BUCKET elements.
            if (counter[i] < NUM_BUCKET) {
                BitSet bitVec = new BitSet(NUM_BUCKET);
                // Search from the beginning again.
                s = stream.iterator();
                while (s.hasNext()) {
                    int x = s.next();
                    if ((x >>> 16) == i) {
                        // Gets the lower 16 bits of x.
                        bitVec.set(x & (NUM_BUCKET - 1));
                    }
                }

                for (int j = 0; j < bitVec.size(); j++) {
                    if (!bitVec.get(j)) {
                        return (i << 16) | j;
                    }
                }
            }
        }

        throw new NoSuchElementException();
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "AbsentValueArray.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
