package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * 10.13 RECONSTRUCT A BINARY TREE FROM A PREORDER TRAVERSAL WITH MARKERS
 * <p>
 * Many different binary trees have the same preorder traversal sequence.
 * <p>
 * In this problem, the preorder traversal computation is modified to mark where a left
 * or right child is empty. For example, the binary tree in Figure 10.5 on the facing
 * page yields the following preorder traversal sequence:
 * <p>
 * (H,B,F,null,null,E,A,null,null,null,C,null,D,null,G,/,null,null,null)
 * <p>
 * Design an algorithm for reconstructing a binary tree from a preorder traversal visit
 * sequence that uses null to mark empty children.
 * <p>
 * Hint: It's difficult to solve this problem by examining the preorder traversal visit
 * sequence from left-to-right.
 */
public class TreeFromPreorderWithNull {
    public static BinaryTreeNode<Integer>
    reconstructPreorder(List<Integer> preorder) {
        return solOne(preorder);
    }

    /**
     * 思路一：创建一个下标包装类以实现下标传递，依次遍历前序数据列表，通过递归方式构建树。
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTreeNode<Integer> solOne(List<Integer> preorder) {
        return treeHelper(preorder, new IdxWrapper(0));
    }

    private static BinaryTreeNode<Integer> treeHelper(List<Integer> preorder,
                                                      IdxWrapper idxWrapper) {
        if (idxWrapper.idx >= preorder.size()) {
            return null;
        }

        Integer data = preorder.get(idxWrapper.idx++);
        if (data == null) {
            return null;
        }

        BinaryTreeNode<Integer> node = new BinaryTreeNode<>(data);
        // Note that reconstruct Preorder Subtree updates idxWrapper. So the order of
        // following two calls are critical.
        node.left = treeHelper(preorder, idxWrapper);
        node.right = treeHelper(preorder, idxWrapper);
        return node;
    }

    @EpiTest(testDataFile = "tree_from_preorder_with_null.tsv")
    public static BinaryTreeNode<Integer>
    reconstructPreorderWrapper(TimedExecutor executor, List<String> strings)
            throws Exception {
        List<Integer> ints = new ArrayList<>();
        for (String s : strings) {
            if (s.equals("null")) {
                ints.add(null);
            } else {
                ints.add(Integer.parseInt(s));
            }
        }

        return executor.run(() -> reconstructPreorder(ints));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeFromPreorderWithNull.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class IdxWrapper {
        int idx;

        public IdxWrapper(int idx) {
            this.idx = idx;
        }
    }
}
