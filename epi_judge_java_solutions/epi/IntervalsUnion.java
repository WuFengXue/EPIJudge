package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 14.6 COMPUTE THE UNION OF INTERVALS
 * <p>
 * In this problem we consider sets of intervals with integer endpoints;
 * the intervals may be open or closed at either end. We want to compute
 * the union of the intervals in such sets. A concrete example is given in
 * Figure 14.2.
 * <p>
 * Design an algorithm that takes as input a set of intervals, and outputs
 * their union expressed as a set of disjoint intervals.
 * <p>
 * Hint: Do a case analysis.
 */
public class IntervalsUnion {

    public static List<Interval> unionOfIntervals(List<Interval> intervals) {
        return solTwo(intervals);
    }

    /**
     * 思路二：先对段做排序（按左端点从小到大），然后再遍历段，比较当前段和下个段：
     * <p>
     * 如果当前段和下个段没有交集，则将当前段添加到结果集合，并将当前段更新为下个段
     * <p>
     * 如果当前段和下个段有交集，且下个段的右端点比当前段的右端点更大，则更新当前段的右端点为下个段的右端点
     * <p>
     * 如果当前段和下个段有交集，但下个段的右端点比当前段的右端点更小，则跳过
     * <p>
     * 在比较端点的时候，需要注意开闭情况
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static List<Interval> solTwo(List<Interval> intervals) {
        // null or empty input.
        if (intervals == null || intervals.isEmpty()) {
            return Collections.emptyList();
        }

        // Sort intervals according to left endpoints of intervals.
        Collections.sort(intervals);
        List<Interval> result = new ArrayList<>();
        Interval curr = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++) {
            Interval next = intervals.get(i);
            if (next.left.val < curr.right.val
                    || (next.left.val == curr.right.val
                    && (next.left.isClosed || curr.right.isClosed))) {
                if (next.right.val > curr.right.val || (next.right.val == curr.right.val && next.right.isClosed)) {
                    curr.right = next.right;
                }
            } else {
                result.add(curr);
                curr = next;
            }
        }
        result.add(curr);
        return result;
    }

    /**
     * 思路一：使用 14.5 的算法 IntervalAdd，将问题拆解成多次向集合添加新段
     * <p>
     * 需要注意本题的端点新增了开闭属性
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(n ^ 2)，可降低至 O(n)
     */
    private static List<Interval> solOne(List<Interval> intervals) {
        List<Interval> result = new ArrayList<>();
        for (Interval newInterval : intervals) {
            result = addInterval(result, newInterval);
        }
        return result;
    }

    /**
     * 向段集合添加一个新的元素，如果新段与已有的段存在交集，则合并段
     * <p>
     * 将段集合分为三个部分：
     * <p>
     * 在新段左侧的部分
     * <p>
     * 与新段存在交集的部分
     * <p>
     * 在新段右侧的部分
     */
    private static List<Interval> addInterval(List<Interval> intervals,
                                              Interval newInterval) {
        List<Interval> result = new ArrayList<>();
        int i = 0;
        while (i < intervals.size() &&
                (intervals.get(i).right.val < newInterval.left.val
                        || (intervals.get(i).right.val == newInterval.left.val
                        && !intervals.get(i).right.isClosed && !newInterval.left.isClosed))) {
            result.add(intervals.get(i++));
        }

        while (i < intervals.size()
                && (intervals.get(i).left.val < newInterval.right.val
                || (intervals.get(i).left.val == newInterval.right.val
                && (intervals.get(i).left.isClosed || newInterval.right.isClosed)))) {
            if (intervals.get(i).left.val < newInterval.left.val
                    || (intervals.get(i).left.val == newInterval.left.val
                    && intervals.get(i).left.isClosed)) {
                newInterval.left = intervals.get(i).left;
            }
            if (intervals.get(i).right.val > newInterval.right.val
                    || (intervals.get(i).right.val == newInterval.right.val
                    && intervals.get(i).right.isClosed)) {
                newInterval.right = intervals.get(i).right;
            }
            i++;
        }
        result.add(newInterval);

        result.addAll(intervals.subList(i, intervals.size()));
        return result;
    }

    @EpiTest(testDataFile = "intervals_union.tsv")
    public static List<FlatInterval>
    unionIntervalWrapper(TimedExecutor executor, List<FlatInterval> intervals)
            throws Exception {
        List<Interval> casted = new ArrayList<>(intervals.size());
        for (FlatInterval in : intervals) {
            casted.add(in.toInterval());
        }

        List<Interval> result = executor.run(() -> unionOfIntervals(casted));

        intervals = new ArrayList<>(result.size());
        for (Interval i : result) {
            intervals.add(new FlatInterval(i));
        }
        return intervals;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IntervalsUnion.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public static class Interval implements Comparable<Interval> {
        public Endpoint left = new Endpoint();
        public Endpoint right = new Endpoint();

        @Override
        public int compareTo(Interval o) {
            if (left.val != o.left.val) {
                return Integer.compare(left.val, o.left.val);
            }

            return left.isClosed && !o.left.isClosed
                    ? -1
                    : !left.isClosed && o.left.isClosed ? 1 : 0;
        }

        private static class Endpoint {
            public boolean isClosed;
            public int val;
        }
    }

    @EpiUserType(
            ctorParams = {int.class, boolean.class, int.class, boolean.class})
    public static class FlatInterval {
        int leftVal;
        boolean leftIsClosed;
        int rightVal;
        boolean rightIsClosed;

        public FlatInterval(int leftVal, boolean leftIsClosed, int rightVal,
                            boolean rightIsClosed) {
            this.leftVal = leftVal;
            this.leftIsClosed = leftIsClosed;
            this.rightVal = rightVal;
            this.rightIsClosed = rightIsClosed;
        }

        public FlatInterval(Interval i) {
            if (i != null) {
                leftVal = i.left.val;
                leftIsClosed = i.left.isClosed;
                rightVal = i.right.val;
                rightIsClosed = i.right.isClosed;
            }
        }

        public Interval toInterval() {
            Interval i = new Interval();
            i.left.val = leftVal;
            i.left.isClosed = leftIsClosed;
            i.right.val = rightVal;
            i.right.isClosed = rightIsClosed;
            return i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            FlatInterval that = (FlatInterval) o;

            if (leftVal != that.leftVal) {
                return false;
            }
            if (leftIsClosed != that.leftIsClosed) {
                return false;
            }
            if (rightVal != that.rightVal) {
                return false;
            }
            return rightIsClosed == that.rightIsClosed;
        }

        @Override
        public int hashCode() {
            int result = leftVal;
            result = 31 * result + (leftIsClosed ? 1 : 0);
            result = 31 * result + rightVal;
            result = 31 * result + (rightIsClosed ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "" + (leftIsClosed ? "<" : "(") + leftVal + ", " + rightVal +
                    (rightIsClosed ? ">" : ")");
        }
    }
}
