package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 6.9 PERMUTE THE ELEMENTS OF AN ARRAY
 * <p>
 * A permutation is a rearrangement of members of a sequence into a new sequence. For
 * example, there are 24 permutations of {a,b,c,d); some of these are (b,a,d,c),
 * (d,a,b,c), and {a,d,b,c).
 * <p>
 * A permutation can be specified by an array P, where P[i] represents the location
 * of the element at i in the permutation. For example, the array (2,0,1,3} represents
 * the permutation that maps the element at location 0 to location 2, the element at
 * location 1 to location 0, the element at location 2 to location 1, and keep the
 * element at location 3 unchanged. A permutation can be applied to an array to
 * reorder the array. For example, the permutation (2,0,1,3} applied to A = (a,b,c,d}
 * yields the array (b,c,a, d).
 * <p>
 * Given an array A of n elements and a permutation P, apply P to A.
 * <p>
 * Hint: Any permutation can be viewed as a set of cyclic permutations. For an element
 * in a cycle, how would you identify if it has been permuted?
 */
public class ApplyPermutation {
    public static void applyPermutation(List<Integer> perm, List<Integer> A) {
        solThree(perm, A);
        return;
    }

    /**
     * 思路一：使用一个额外的整形数组 B，逐个更新（B[P[i]] = A[i]），最后再将 B 拷贝回 A
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    private static void solOne(List<Integer> perm, List<Integer> A) {
        List<Integer> B = new ArrayList<>(Collections.nCopies(A.size(), 0));
        for (int i = 0; i < perm.size(); i++) {
            B.set(perm.get(i), A.get(i));
        }
        Collections.copy(A, B);
    }

    /**
     * 思路二：利用 P 中新组合的环形特性，使用一个额外的布尔型数组 done，用于记录 P 的每个元素是否处理过，
     * 遍历所有的圆环
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    private static void solTwo(List<Integer> perm, List<Integer> A) {
        List<Boolean> done = new ArrayList<>(Collections.nCopies(perm.size(), false));
        for (int i = 0; i < perm.size(); i++) {
            int next = i;
            while (!done.get(next)) {
                Collections.swap(A, i, next);
                done.set(next, true);
                next = perm.get(next);
            }
        }
    }

    /**
     * 思路三：利用 P 中新组合的环形特性，P 中处理过的元素，值先修改成负值（P[i] = P[i] - size），
     * 遍历所有的圆环，全部修改完成后，再还原P（P[i] = P[i] + size）
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static void solThree(List<Integer> perm, List<Integer> A) {
        for (int i = 0; i < perm.size(); i++) {
            // Check if the element at index i has not been moved by checking if
            // perm.get(i) is nonnegative.
            int next = i;
            while (perm.get(next) >= 0) {
                Collections.swap(A, i, perm.get(next));
                int temp = perm.get(next);
                // Subtracts perm.size() from an entry in perm to make it negative,
                // which indicates the corresponding move has been performed.
                perm.set(next, temp - perm.size());
                next = temp;
            }
        }

        // Restore perm.
        for (int i = 0; i < perm.size(); i++) {
            perm.set(i, perm.get(i) + perm.size());
        }
    }

    /**
     * 思路四：不修改 P，每次处理圆环的时候，都从圆环的最小（最左）元素开始处理，然后遍历所有圆环
     * <p>
     * 时间复杂度：O(n ^ 2)
     * 空间复杂度：O(1)
     */
    private static void solFour(List<Integer> perm, List<Integer> A) {
        for (int i = 0; i < perm.size(); i++) {
            // Traverses the cycle to see if i is the minimum element.
            boolean isMin = true;
            int j = perm.get(i);
            while (j != i) {
                if (j < i) {
                    isMin = false;
                    break;
                }
                j = perm.get(j);
            }
            if (isMin) {
                cyclicPermutation(i, perm, A);
            }
        }
    }

    private static void cyclicPermutation(int start, List<Integer> perm, List<Integer> A) {
        int i = start;
        int temp = A.get(i);
        do {
            int nextI = perm.get(i);
            int nextTemp = A.get(nextI);
            A.set(nextI, temp);
            i = nextI;
            temp = nextTemp;
        } while (i != start);
    }

    @EpiTest(testDataFile = "apply_permutation.tsv")
    public static List<Integer> applyPermutationWrapper(List<Integer> perm,
                                                        List<Integer> A) {
        applyPermutation(perm, A);
        return A;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ApplyPermutation.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
