package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 14.5 MERGING INTERVALS
 * <p>
 * Suppose the time during the day that a person is busy is stored as a set of
 * disjoint time intervals. If an event is added to the person's calendar, the
 * set of busy times may need to be updated.
 * <p>
 * In the abstract, we want a way to add an interval to a set of disjoint intervals
 * and represent the new set as a set of disjoint intervals. For example, if the
 * initial set of intervals is [-4,-1],[0,2],[3,6],[7,9],[11,12],[14,17], and the
 * added interval is [1,8], the result is [-4,-1],[0,9],[11,12],[14,17].
 * <p>
 * Write a program which takes as input an array of disjoint closed intervals with
 * integer endpoints, sorted by increasing order of left endpoint, and an interval
 * to be added, and returns the union of the intervals in the array and the added
 * interval. Your result should be expressed as a union of disjoint intervals sorted
 * by left endpoint.
 * <p>
 * Hint: What is the union of two closed intervals?
 */
public class IntervalAdd {
    @EpiTest(testDataFile = "interval_add.tsv")

    public static List<Interval> addInterval(List<Interval> disjointIntervals,
                                             Interval newInterval) {
        return solOne(disjointIntervals, newInterval);
    }

    /**
     * 思路一：利用段集合有序的特性，将原来的段集合分成三部分：
     * <p>
     * 与新段无交集，且在新段左侧的部分，直接添加到结果集合
     * <p>
     * 与新段存在交集，逐一与新段合并，最终将合并后的段添加到结果集合
     * <p>
     * 与新段无交集，且在新段右侧的部分，直接添加到结果集合
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static List<Interval> solOne(List<Interval> disjointIntervals,
                                         Interval newInterval) {
        List<Interval> result = new ArrayList<>();
        int i = 0;
        // Processes intervals in disjointIntervals which come before newInterval.
        while (i < disjointIntervals.size()
                && disjointIntervals.get(i).right < newInterval.left) {
            result.add(disjointIntervals.get(i++));
        }

        // Processes intervals in disjointIntervals which overlap with newInterval.
        while (i < disjointIntervals.size()
                && disjointIntervals.get(i).left <= newInterval.right) {
            // If [a, b] and [c, d] overlap, their union is [min(a , c),max(b, d)].
            newInterval = new Interval(
                    Math.min(newInterval.left, disjointIntervals.get(i).left),
                    Math.max(newInterval.right, disjointIntervals.get(i).right)
            );
            i++;
        }
        result.add(newInterval);

        // Processes intervals in disjointIntervals which come after newInterval .
        result.addAll(disjointIntervals.subList(i, disjointIntervals.size()));
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IntervalAdd.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @EpiUserType(ctorParams = {int.class, int.class})

    public static class Interval {
        public int left, right;

        public Interval(int l, int r) {
            this.left = l;
            this.right = r;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Interval interval = (Interval) o;

            if (left != interval.left) {
                return false;
            }
            return right == interval.right;
        }

        @Override
        public String toString() {
            return "[" + left + ", " + right + "]";
        }
    }
}
