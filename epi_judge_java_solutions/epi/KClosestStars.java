package epi;

import epi.test_framework.*;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * 11.4 COMPUTE THE k CLOSEST STARS
 * <p>
 * Consider a coordinate system for the Milky Way in which Earth is at (0,0,0).
 * Model stars as points, and assume distances are in light years. The Milky
 * Way consists of approximately 10 ^ 12 stars, and their coordinates are
 * stored in a file.
 * <p>
 * How would you compute the k stars which are closest to Earth?
 * <p>
 * Hint: Suppose you know the k closest stars in the first n stars. If the
 * (n + 1)th star is to be added to the set of k closest stars, which element
 * in that set should be evicted?
 */
public class KClosestStars {
    @EpiTestExpectedType
    public static List<Double> expectedType;
    @EpiTestComparator
    public static BiPredicate<List<Double>, List<Star>> comp =
            (expected, result) -> {
                if (expected.size() != result.size()) {
                    return false;
                }
                Collections.sort(result);
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).distance() != expected.get(i)) {
                        return false;
                    }
                }
                return true;
            };

    public static List<Star> findClosestKStars(Iterator<Star> stars, int k) {
        return solTwo(stars, k);
    }

    /**
     * 思路二：维护一个集合，用于记录目前最近的 k 个行星，遍历全部行星，遍历时及时更新该集合。
     * （利用大根堆实现）
     * <p>
     * 时间复杂度：O(n * log(k))
     * <p>
     * 空间复杂度：O(k)
     */
    private static List<Star> solTwo(Iterator<Star> stars, int k) {
        // maxHeap to store the closest k stars seen so far.
        PriorityQueue<Star> maxHeap = new PriorityQueue<>(k + 1, Collections.reverseOrder());
        while (stars.hasNext()) {
            // Add each star to the max-heap. If the max-heap size exceeds k,
            // remove the maximum element from the max-heap.
            maxHeap.add(stars.next());
            if (maxHeap.size() == k + 1) {
                maxHeap.remove();
            }
        }
        List<Star> result = new ArrayList<>();
        // The only guarantee PriorityQueue makes about ordering is that the
        // maximum element comes first, so we extract one and add to head
        // onetime.
        while (!maxHeap.isEmpty()) {
            result.add(0, maxHeap.remove());
        }
        return result;
    }

    /**
     * 思路一：将全部行星的数据添加到列表，对列表进行排序，然后截取距离最近的 k 个行星
     * （内存空间可能不足）
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static List<Star> solOne(Iterator<Star> stars, int k) {
        List<Star> starList = new ArrayList<>();
        while (stars.hasNext()) {
            starList.add(stars.next());
        }
        Collections.sort(starList);
        return starList.size() > k
                ? starList.subList(0, k)
                : starList;
    }

    @EpiTest(testDataFile = "k_closest_stars.tsv")
    public static List<Star> findClosestKStarsWrapper(List<Star> stars, int k) {
        return findClosestKStars(stars.iterator(), k);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "KClosestStars.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @EpiUserType(ctorParams = {double.class, double.class, double.class})

    public static class Star implements Comparable<Star> {
        private double x, y, z;

        public Star(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double distance() {
            return Math.sqrt(x * x + y * y + z * z);
        }

        @Override
        public int compareTo(Star that) {
            return Double.compare(this.distance(), that.distance());
        }

        @Override
        public String toString() {
            return String.valueOf(distance());
        }
    }
}
