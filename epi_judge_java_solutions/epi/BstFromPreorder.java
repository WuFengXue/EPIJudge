package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;

/**
 * 15.5 RECONSTRUCT A BST FROM TRAVERSAL DATA
 * <p>
 * As discussed in Problem 10.12 on Page 165 there are many different binary trees
 * that yield the same sequence of visited nodes in an inorder traversal. This is also
 * true for preorder and postorder traversals. Given the sequence of nodes that an
 * inorder traversal sequence visits and either of the other two traversal sequences,
 * there exists a unique binary tree that yields those sequences. Here we study
 * if it is possible to reconstruct the tree with less traversal information when
 * the tree is known to be a BST.
 * <p>
 * It is critical that the elements stored in the tree be unique. If the root contains
 * key v and the tree contains more occurrences of v, we cannot always identify from
 * the sequence whether the subsequent vs are in the left subtree or the right subtree.
 * For example, for the tree rooted at G in Figure 15.2 on Page 260 the preorder traversal
 * sequence is 285,243,285,401. The same preorder traversal sequence is seen if 285
 * appears in the left subtree as the right child of the node with key 243 and 401 is
 * at the root's right child.
 * <p>
 * Suppose you are given the sequence in which keys are visited in an inorder traversal
 * of a BST, and all keys are distinct. Can you reconstruct the BST from the sequence?
 * If so, write a program to do so. Solve the same problem for preorder and postorder
 * traversal sequences.
 * <p>
 * Hint: Draw the five BSTs on the keys 1, 2, 3, and the corresponding traversal orders.
 */
public class BstFromPreorder {
    private static int rootIdx = 0;

    @EpiTest(testDataFile = "bst_from_preorder.tsv")

    public static BstNode<Integer>
    rebuildBSTFromPreorder(List<Integer> preorderSequence) {
        rootIdx = 0;
        return solThree(preorderSequence, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * 思路三：在思路二的基础上进行优化，利用前序遍历数据从左往右构建子树的特性，逐个遍历数据
     * 为了区分子树的切分点，引入上下界字段
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static BstNode<Integer> solThree(List<Integer> preorderSequence, int lowerBound, int upperBound) {
        if (rootIdx >= preorderSequence.size()) {
            return null;
        }

        int rootData = preorderSequence.get(rootIdx);
        if (rootData < lowerBound || rootData > upperBound) {
            return null;
        }

        rootIdx++;
        return new BstNode<>(rootData,
                solThree(preorderSequence, lowerBound, rootData),
                solThree(preorderSequence, rootData, upperBound));
    }

    /**
     * 思路二：利用前序遍历数据将数据分为3个部分（根节点，左子树、右子树）的特性，
     * 递归构建还原树，关键点在找到左子树和右子树的转折点
     * <p>
     * 时间复杂度：O(n)（左偏向树） - O(n ^ 2)（右偏向树），O(n * log(n)) （平衡树）
     * <p>
     * 空间复杂度：O(n)
     */
    private static BstNode<Integer> solTwo(List<Integer> preorderSequence, int start, int end) {
        if (start >= end) {
            return null;
        }

        int rootData = preorderSequence.get(start);
        int transitionPoint = start + 1;
        while (transitionPoint < end && preorderSequence.get(transitionPoint) < rootData) {
            transitionPoint++;
        }
        return new BstNode<>(rootData,
                solTwo(preorderSequence, start + 1, transitionPoint),
                solTwo(preorderSequence, transitionPoint, end));
    }

    /**
     * 思路一：将问题降解为向一棵二叉搜索树逐个添加新节点
     *
     * 时间复杂度：O(n * h) ？？
     *
     * 空间复杂度：O(n)
     */
    private static BstNode<Integer> solOne(List<Integer> preorderSequence) {
        if (preorderSequence == null || preorderSequence.isEmpty()) {
            return null;
        }

        int rootData = preorderSequence.get(0);
        BstNode<Integer> root = new BstNode<>(rootData);
        for (int i = 1; i < preorderSequence.size(); i++) {
            addNodeToTree(root, preorderSequence.get(i));
        }
        return root;
    }

    private static void addNodeToTree(BstNode<Integer> tree, Integer nodeData) {
        if (nodeData < tree.data) {
            if (tree.left == null) {
                tree.left = new BstNode<>(nodeData);
            } else {
                addNodeToTree(tree.left, nodeData);
            }
        } else {
            if (tree.right == null) {
                tree.right = new BstNode<>(nodeData);
            } else {
                addNodeToTree(tree.right, nodeData);
            }
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "BstFromPreorder.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
