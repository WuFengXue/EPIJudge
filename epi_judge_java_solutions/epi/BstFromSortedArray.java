package epi;

import epi.test_framework.*;

import java.util.List;

/**
 * 15.9 BUILD A MINIMUM HEIGHT BST FROM A SORTED ARRAY
 * <p>
 * Given a sorted array, the number of BSTs that can be built on the entries in the array
 * grows enormously with its size. Some of these trees are skewed, and are closer to lists;
 * others are more balanced. See Figure 15.3 on Page 263 for an example.
 * <p>
 * How would you build a BST of minimum possible height from a sorted array?
 * <p>
 * Hint: Which element should be the root?
 */
public class BstFromSortedArray {

    public static BstNode<Integer>
    buildMinHeightBSTFromSortedArray(List<Integer> A) {
        return solOne(A, 0, A.size());
    }

    /**
     * 思路一：根据平衡树的定义，将中间节点作为树的根节点，递归构建树
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static BstNode<Integer> solOne(List<Integer> A, int start, int end) {
        if (start >= end) {
            return null;
        }

        int mid = start + (end - start) / 2;
        return new BstNode<>(
                A.get(mid),
                solOne(A, start, mid),
                solOne(A, mid + 1, end));
    }

    /**
     * 思路零：将有序数组的元素逐个添加到树中，最终输出一个右偏向树
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static BstNode<Integer> solZero(List<Integer> A) {
        BstNode<Integer> dummyRoot = new BstNode<>(Integer.MIN_VALUE);
        BstNode<Integer> preNode = dummyRoot;
        for (Integer a : A) {
            BstNode<Integer> newNode = new BstNode<>(a);
            preNode.right = newNode;
            preNode = newNode;
        }
        return dummyRoot.right;
    }

    @EpiTest(testDataFile = "bst_from_sorted_array.tsv")
    public static int
    buildMinHeightBSTFromSortedArrayWrapper(TimedExecutor executor,
                                            List<Integer> A) throws Exception {
        BstNode<Integer> result =
                executor.run(() -> buildMinHeightBSTFromSortedArray(A));

        List<Integer> inorder = BinaryTreeUtils.generateInorder(result);

        TestUtils.assertAllValuesPresent(A, inorder);
        BinaryTreeUtils.assertTreeIsBst(result);
        return BinaryTreeUtils.binaryTreeHeight(result);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "BstFromSortedArray.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
