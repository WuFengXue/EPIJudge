package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;

/**
 * 6.4 ADVANCING THROUGH AN ARRAY
 * <p>
 * In a particular board game, a player has to try to advance through a sequence of
 * positions. Each position has a nonnegative integer associated with it, representing
 * the maximum you can advance from that position in one move. You begin at the first
 * position,and winbygettingtothelastposition. Forexample,letA =(3,3,1,0,2,0,1}
 * represent the board game, i.e., the ith entry in A is the maximum we can advance
 * from i. Then the game can be won by the following sequence of advances through
 * A: take 1 step from A[0] to A[1], then 3 steps from A[l] to A[4], then 2 steps from
 * A[4] to A[6], which is the last position. Note that A[0] = 3 > 1, A[l] = 3 > 3, and
 * A[4] = 2 > 2, so all moves are valid. If A instead was (3, 2, 0,0, 2, 0,1), it would not
 * possible to advance past position 3, so the game cannot be won.
 * <p>
 * Write a program which takes an array of n integers, where A[i] denotes the maximum
 * you can advance from index i, and returns whether it is possible to advance to the
 * last index starting from the beginning of the array.
 * <p>
 * Hint: Analyze each location, starting from the beginning.
 */
public class AdvanceByOffsets {
    @EpiTest(testDataFile = "advance_by_offsets.tsv")
    public static boolean canReachEnd(List<Integer> maxAdvanceSteps) {
        return solOne(maxAdvanceSteps);
    }

    /**
     * 思路一：在到达当前可到达的最远目标之前，逐一获取每一步的最远目标(i + get(i))，
     * 如果最终的最远目标比最后一步还远，则判定位可到达
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static boolean solOne(List<Integer> maxAdvanceSteps) {
        int furthestReachSoFar = 0, lastIndex = maxAdvanceSteps.size() - 1;
        for (int i = 0; i <= furthestReachSoFar && furthestReachSoFar < lastIndex; i++) {
            furthestReachSoFar =
                    Math.max(furthestReachSoFar, i + maxAdvanceSteps.get(i));
        }
        return furthestReachSoFar >= lastIndex;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "AdvanceByOffsets.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
