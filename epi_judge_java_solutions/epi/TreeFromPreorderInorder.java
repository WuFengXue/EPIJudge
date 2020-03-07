package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 10.12 RECONSTRUCT A BINARY TREE FROM TRAVERSAL DATA
 * <p>
 * Many different binary trees yield the same sequence of keys in an inorder, preorder,
 * or postorder traversal. However, given an inorder traversal and one of any two other
 * traversal orders of a binary tree, there exists a unique binary tree that yields those
 * orders, assuming each node holds a distinct key. For example, the unique binary tree whose
 * inorder traversal sequence is (F,B,A,E,H,C,D,I,G) and whose preorder traversal sequence
 * is (H,B,F,E,A,C,D,G,I) is given in Figure 10.5 on the following page.
 * <p>
 * Given an inorder traversal sequence and a preorder traversal sequence of a binary tree
 * write a program to reconstruct the tree. Assume each node has a unique key.
 * <p>
 * Hint: Focus on the root.
 */
public class TreeFromPreorderInorder {
    @EpiTest(testDataFile = "tree_from_preorder_inorder.tsv")

    public static BinaryTreeNode<Integer>
    binaryTreeFromPreorderInorder(List<Integer> preorder, List<Integer> inorder) {
        return solThree(preorder, inorder);
    }

    /**
     * 思路三：观察法。
     * <p>
     * 前序数组 = 【根节点，左子树，右子树】
     * <p>
     * 中序数组 = 【左子树，根节点，右子树】
     * <p>
     * 通过前序数组的首个元素取得根节点，再遍历中序数组，找到根节点对应的下标，
     * 就可以取得左子树的大小（根节点下标），进而取得左子树和右子树在前序数组和中序数组的分布
     * <p>
     * 用一个表缓存中序数组的节点及其下标对应关系，计算并移动左子树和右子树的首尾节点下标
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n + h), h 为树高（递归调用堆栈）
     */
    private static BinaryTreeNode<Integer> solThree(List<Integer> preorder,
                                                    List<Integer> inorder) {
        Map<Integer, Integer> nodeToInorderIdx = new HashMap<>();
        for (int i = 0; i < inorder.size(); i++) {
            nodeToInorderIdx.put(inorder.get(i), i);
        }
        return treeHelper(preorder, nodeToInorderIdx,
                0, preorder.size(),
                0, nodeToInorderIdx.size());
    }

    /**
     * Builds the subtree with preorder.subList(preorderStart, preorderEnd) and
     * inorder.subList(inorderStart, inorderEnd).
     */
    private static BinaryTreeNode<Integer> treeHelper(List<Integer> preorder,
                                                      Map<Integer, Integer> nodeToInorderIdx,
                                                      int preorderStart, int preorderEnd,
                                                      int inorderStart, int inorderEnd) {
        if (preorderStart >= preorderEnd || inorderStart >= inorderEnd) {
            return null;
        }

        Integer rootData = preorder.get(preorderStart);
        int rootInorderIdx = nodeToInorderIdx.get(rootData);
        int leftSubtreeSize = rootInorderIdx - inorderStart;

        BinaryTreeNode<Integer> rootNode = new BinaryTreeNode<>(rootData);
        // Recursively builds the left subtree.
        rootNode.left = treeHelper(preorder, nodeToInorderIdx,
                preorderStart + 1, preorderStart + 1 + leftSubtreeSize,
                inorderStart, rootInorderIdx);
        // Recursively builds the right subtree.
        rootNode.right = treeHelper(preorder, nodeToInorderIdx,
                preorderStart + 1 + leftSubtreeSize, preorderEnd,
                rootInorderIdx + 1, inorderEnd);
        return rootNode;
    }

    /**
     * 思路二：计算并移动前序数组和中序数组的首尾节点下标，每次都重新遍历中序数组
     * 计算新的根节点下标
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static BinaryTreeNode<Integer> solTwo(List<Integer> preorder,
                                                  List<Integer> inorder) {
        return treeHelper(preorder, inorder,
                0, preorder.size(),
                0, inorder.size());
    }

    /**
     * 思路一：计算子节点时，传递的不是数组和首尾节点下标，而是截取的子数组
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(n ^ 2) （不确定）
     */
    private static BinaryTreeNode<Integer> solOne(List<Integer> preorder,
                                                  List<Integer> inorder) {
        if (preorder.isEmpty() || inorder.isEmpty()) {
            return null;
        }

        Integer rootData = preorder.get(0);
        int rootInorderIdx = getRootInorderIdx(inorder, rootData);
        int leftSubtreeSize = rootInorderIdx;
        int rightSubtreeSize = inorder.size() - leftSubtreeSize - 1;
        BinaryTreeNode<Integer> rootNode = new BinaryTreeNode<>(rootData);
        if (leftSubtreeSize > 0) {
            rootNode.left = solOne(preorder.subList(1, 1 + leftSubtreeSize),
                    inorder.subList(0, rootInorderIdx));
        }
        if (rightSubtreeSize > 0) {
            rootNode.right = solOne(preorder.subList(1 + leftSubtreeSize, preorder.size()),
                    inorder.subList(rootInorderIdx + 1, inorder.size()));
        }
        return rootNode;
    }

    private static BinaryTreeNode<Integer> treeHelper(List<Integer> preorder,
                                                      List<Integer> inorder,
                                                      int preorderStart, int preorderEnd,
                                                      int inorderStart, int inorderEnd) {
        if (preorderStart >= preorderEnd || inorderStart >= inorderEnd) {
            return null;
        }

        Integer rootData = preorder.get(preorderStart);
        int rootInorderIdx = getRootInorderIdx(inorder, rootData);
        int leftSubtreeSize = rootInorderIdx - inorderStart;
        BinaryTreeNode<Integer> rootNode = new BinaryTreeNode<>(rootData);
        rootNode.left = treeHelper(preorder, inorder,
                preorderStart + 1, preorderStart + 1 + leftSubtreeSize,
                inorderStart, rootInorderIdx);
        rootNode.right = treeHelper(preorder, inorder,
                preorderStart + 1 + leftSubtreeSize, preorderEnd,
                rootInorderIdx + 1, inorderEnd);
        return rootNode;
    }

    private static int getRootInorderIdx(List<Integer> inorder, Integer rootData) {
        for (int i = 0; i < inorder.size(); i++) {
            if (Objects.equals(inorder.get(i), rootData)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeFromPreorderInorder.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
