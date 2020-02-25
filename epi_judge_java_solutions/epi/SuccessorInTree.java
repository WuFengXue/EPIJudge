package epi;

import epi.test_framework.BinaryTreeUtils;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 10.10 COMPUTE THE SUCCESSOR
 * <p>
 * The successor of a node in a binary tree is the node that appears
 * immediately after the given node in an inorder traversal. For example,
 * in Figure 10.1 on Page 150, the successor of G is A, and the successor
 * of A is J.
 * <p>
 * Design an algorithm that computes the successor of a node in a binary
 * tree. Assume that each node stores its parent.
 * <p>
 * Hint: Study the node's right subtree. What if the node does not have a
 * right subtree?
 */
public class SuccessorInTree {

    public static BinaryTree<Integer> findSuccessor(BinaryTree<Integer> node) {
        return solOne(node);
    }

    /**
     * 思路二：观察法：
     * <p>
     * 当节点有右节点时，目标节点为其右子树的最左节点
     * <p>
     * 当节点没有右节点，且其为其父节点的左节点时，目标节点为其父节点
     * <p>
     * 当节点没有右节点，且其为其父节点的右节点时，向上遍历直到遇到第一个满足条件
     * 的节点（该节点为其父节点的左节点），该节点的父节点即为目标节点
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(1)
     */
    private static BinaryTree<Integer> solTwo(BinaryTree<Integer> node) {
        BinaryTree<Integer> iter = node;
        if (iter.right != null) {
            // Find the left most element in node’s right subtree.
            iter = iter.right;
            while (iter.left != null) {
                iter = iter.left;
            }
            return iter;
        }

        // Find the closest ancestor whose left subtree contains node.
        while (iter.parent != null && iter.parent.right == iter) {
            iter = iter.parent;
        }
        // A return value of null means node does not have successor,
        // i.e., it is the rightmost node in the tree.
        return iter.parent;
    }

    /**
     * 思路一：先获得根节点，然后再从根节点开始遍历树，用迭代方式遍历，
     * 在发现入参节点时置一个标志位，再下个节点即为目标节点
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTree<Integer> solOne(BinaryTree<Integer> node) {
        BinaryTree<Integer> root = node;
        while (root.parent != null) {
            root = root.parent;
        }

        Deque<BinaryTree<Integer>> deque = new LinkedList<>();
        BinaryTree<Integer> curr = root;
        boolean isNodeVisited = false;
        while (!deque.isEmpty() || curr != null) {
            if (curr != null) {
                deque.addFirst(curr);
                curr = curr.left;
            } else {
                curr = deque.removeFirst();
                if (isNodeVisited) {
                    return curr;
                }
                if (curr == node) {
                    isNodeVisited = true;
                }
                curr = curr.right;
            }
        }
        return null;
    }

    @EpiTest(testDataFile = "successor_in_tree.tsv")
    public static int findSuccessorWrapper(TimedExecutor executor,
                                           BinaryTree<Integer> tree, int nodeIdx)
            throws Exception {
        BinaryTree<Integer> n = BinaryTreeUtils.mustFindNode(tree, nodeIdx);

        BinaryTree<Integer> result = executor.run(() -> findSuccessor(n));

        return result == null ? -1 : result.data;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SuccessorInTree.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
