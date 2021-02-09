package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiTestComparator;
import epi.test_framework.GenericTest;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * 15.3 FIND THE k LARGEST ELEMENTS IN A BST
 * <p>
 * A BST is a sorted data structure, which suggests that it should be possible
 * to find the k largest keys easily.
 * <p>
 * Write a program that takes as input a BST and an integer k, and returns
 * the k largest elements in the BST in decreasing order. For example, if
 * the input is the BST in Figure 15.1 on Page 255 and k = 3, your program
 * should return (53,47,43).
 * <p>
 * Hint: What does an inorder traversal yield?
 */
public class KLargestValuesInBst {
    @EpiTestComparator
    public static BiPredicate<List<Integer>, List<Integer>> comp =
            (expected, result) -> {
                if (result == null) {
                    return false;
                }
                Collections.sort(expected);
                Collections.sort(result);
                return expected.equals(result);
            };

    @EpiTest(testDataFile = "k_largest_values_in_bst.tsv")

    public static List<Integer> findKLargestInBst(BstNode<Integer> tree, int k) {
        return solTwo(tree, k);
    }

    /**
     * 思路二：中序（反向）遍历树，将节点添加到一个集合中（尾部），当集合的数量等于 k 时，
     * 结束遍历，此时集合即为结果
     * <p>
     * 时间复杂度：O(h + k)
     * <p>
     * 空间复杂度：O(h + k)
     */
    private static List<Integer> solTwo(BstNode<Integer> tree, int k) {
        List<Integer> kLargestElements = new ArrayList<>();
        findKLargestInBstHelper(tree, k, kLargestElements);
        return kLargestElements;
    }

    private static void findKLargestInBstHelper(BstNode<Integer> tree,
                                                int k,
                                                List<Integer> kLargestElements) {
        // Perform reverse inorder traversal.
        if (tree == null || kLargestElements.size() >= k) {
            return;
        }

        findKLargestInBstHelper(tree.right, k, kLargestElements);
        if (kLargestElements.size() >= k) {
            return;
        }

        kLargestElements.add(tree.data);
        findKLargestInBstHelper(tree.left, k, kLargestElements);
    }

    /**
     * 思路一：中序遍历树，用一个队列缓存访问的节点，并将队列的长度限制为 k，
     * 遍历完成时队列即为结果
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h + k)
     */
    private static List<Integer> solOne(BstNode<Integer> tree, int k) {
        Queue<Integer> queue = new LinkedList<>();
        findKLargestInBstHelper(tree, k, queue);
        return (List<Integer>) queue;
    }

    private static void findKLargestInBstHelper(BstNode<Integer> tree,
                                                int k,
                                                Queue queue) {
        if (tree == null) {
            return;
        }

        findKLargestInBstHelper(tree.left, k, queue);
        queue.add(tree.data);
        if (queue.size() > k) {
            queue.remove();
        }
        findKLargestInBstHelper(tree.right, k, queue);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "KLargestValuesInBst.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
