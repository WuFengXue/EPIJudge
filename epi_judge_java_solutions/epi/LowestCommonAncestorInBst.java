package epi;

import epi.test_framework.*;

/**
 * 15.4 COMPUTE THE LCA IN A BST
 * <p>
 * Since a BST is a specialized binary tree, the notion of lowest common ancestor,
 * as expressed in Problem 10.4 on Page 157, holds for BSTs too.
 * <p>
 * In general, computing the LCA of two nodes in a BST is no easier than computing the LCA
 * in a binary tree, since structurally a binary tree can be viewed as a BST where
 * all the keys are equal. However, when the keys are distinct, it is possible to improve
 * on the LCA algorithms for binary trees.
 * <p>
 * Design an algorithm that takes as input a BST and two nodes, and returns the LCA of
 * the two nodes. For example, for the BST in Figure 15.1 on Page 255, and nodes C and G,
 * your algorithm should return B. Assume all keys are distinct. Nodes do not have references
 * to their parents.
 * <p>
 * Hint: Take advantage of the BST property.
 */
public class LowestCommonAncestorInBst {

    // Input nodes are nonempty and the key at s is less than or equal to that at
    // b.
    public static BstNode<Integer>
    findLCA(BstNode<Integer> tree, BstNode<Integer> s, BstNode<Integer> b) {
        return solTwo(tree, s, b);
    }

    /**
     * 思路二：利用 BST 有序的特性，一共有四种情况：
     * <p>
     * 1、根节点为 s 或 b 节点，则根节点为 LCA
     * <p>
     * 2、s < 根节点 < b，则根节点为 LCA
     * <p>
     * 3、根节点 < s，则 LCA 在根节点的右子树
     * <p>
     * 4、根节点 > b，则 LCA 在根节点的左子树
     * <p>
     * 从根节点开始遍历，找到的第一个满足【s <= 节点 <= b】的节点
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(1)
     */
    private static BstNode<Integer> solTwo(BstNode<Integer> tree, BstNode<Integer> s, BstNode<Integer> b) {
        // Keep searching since tree is outside of [s, b].
        while (tree != null && (tree.data < s.data || tree.data > b.data)) {
            // LCA must be in tree’s right child.
            while (tree != null && tree.data < s.data) {
                tree = tree.right;
            }
            // LCA must be in tree’s left child.
            while (tree != null && tree.data > b.data) {
                tree = tree.left;
            }
        }
        return tree;
    }

    /**
     * 思路一：同 {@link LowestCommonAncestor#solTwo(BinaryTreeNode, BinaryTreeNode, BinaryTreeNode)}
     * <p>
     * 用一个类记录已匹配的节点数和祖先节点，后续遍历树，当一个节点及其子节点同时包含两个目标节点，则该节点为LCA
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static Status solOne(BstNode<Integer> tree, BstNode<Integer> s, BstNode<Integer> b) {
        if (tree == null) {
            return new Status(0, null);
        }

        Status leftResult = solOne(tree.left, s, b);
        if (leftResult.numTargetNodes == 2) {
            return leftResult;
        }

        Status rightResult = solOne(tree.right, s, b);
        if (rightResult.numTargetNodes == 2) {
            return rightResult;
        }

        int numTargetNodes = leftResult.numTargetNodes + rightResult.numTargetNodes
                + (tree.data == s.data ? 1 : 0) + (tree.data == b.data ? 1 : 0);
        return new Status(numTargetNodes, numTargetNodes == 2 ? tree : null);
    }

    @EpiTest(testDataFile = "lowest_common_ancestor_in_bst.tsv")
    public static int lcaWrapper(TimedExecutor executor, BstNode<Integer> tree,
                                 Integer key0, Integer key1) throws Exception {
        BstNode<Integer> node0 = BinaryTreeUtils.mustFindNode(tree, key0);
        BstNode<Integer> node1 = BinaryTreeUtils.mustFindNode(tree, key1);

        BstNode<Integer> result = executor.run(() -> findLCA(tree, node0, node1));

        if (result == null) {
            throw new TestFailure("Result can't be null");
        }
        return result.data;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "LowestCommonAncestorInBst.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    static class Status {
        int numTargetNodes;
        BstNode<Integer> ancestor;

        Status(int numTargetNodes, BstNode<Integer> ancestor) {
            this.numTargetNodes = numTargetNodes;
            this.ancestor = ancestor;
        }
    }
}
