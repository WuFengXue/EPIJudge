package epi;

import epi.test_framework.*;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 10.4 COMPUTE THE LCA WHEN NODES HAVE PARENT POINTERS
 * <p>
 * Given two nodes in a binary tree, design an algorithm that computes
 * their LCA. Assume that each node has a parent pointer.
 * <p>
 * Hint: The problem is easy if both nodes are the same distance from the root.
 */
public class LowestCommonAncestorWithParent {

    public static BinaryTree<Integer> LCA(BinaryTree<Integer> node0,
                                          BinaryTree<Integer> node1) {
        return solThree(node0, node1);
    }

    /**
     * 思路三：先获取两个节点对应的深度，然后向上移动比较深的节点直到两个节点深度一致，
     * 最后同时向上移动两个节点，遇到的第一个相同节点即为目标节点
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(1)
     */
    private static BinaryTree<Integer> solThree(BinaryTree<Integer> node0,
                                                BinaryTree<Integer> node1) {
        int depth0 = getDepth(node0);
        int depth1 = getDepth(node1);
        // Makes node0 as the deeper node in order to simplify the code.
        if (depth0 < depth1) {
            BinaryTree<Integer> temp = node0;
            node0 = node1;
            node1 = temp;
        }
        // Ascends from the deeper node.
        int depthDiff = Math.abs(depth0 - depth1);
        while (depthDiff-- > 0) {
            node0 = node0.parent;
        }

        // Now ascends both nodes until we reach the LCA.
        while (node0 != node1) {
            node0 = node0.parent;
            node1 = node1.parent;
        }
        return node0;
    }

    private static int getDepth(BinaryTree<Integer> node) {
        int depth = 0;
        while (node != null && node.parent != null) {
            depth++;
            node = node.parent;
        }
        return depth;
    }

    /**
     * 思路二：遍历第一个节点所在路径并将全部节点添加到一个哈希集合中，然后遍历
     * 第二个节点所在路径，遇到的第一个在集合中的节点即为目标节点
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTree<Integer> solTwo(BinaryTree<Integer> node0,
                                              BinaryTree<Integer> node1) {
        Set<BinaryTree<Integer>> nodeSet = new HashSet<>();
        while (node0 != null) {
            nodeSet.add(node0);
            node0 = node0.parent;
        }
        while (node1 != null) {
            if (nodeSet.contains(node1)) {
                return node1;
            }
            node1 = node1.parent;
        }
        return null;
    }

    /**
     * 思路一：分别遍历两个节点所在路径，将全部节点存储在各自的堆栈中（栈顶为根节点），
     * 最后同时遍历两个堆栈，遇到的最后一个相同节点即为目标节点
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTree<Integer> solOne(BinaryTree<Integer> node0,
                                              BinaryTree<Integer> node1) {
        Deque<BinaryTree<Integer>> deque0 = getPath(node0);
        Deque<BinaryTree<Integer>> deque1 = getPath(node1);
        BinaryTree<Integer> result = null;
        while (!deque0.isEmpty() && !deque1.isEmpty()) {
            BinaryTree<Integer> n0 = deque0.removeFirst();
            BinaryTree<Integer> n1 = deque1.removeFirst();
            if (n0 != n1) {
                break;
            }
            result = n0;
        }
        return result;
    }

    private static Deque<BinaryTree<Integer>> getPath(BinaryTree<Integer> node) {
        Deque<BinaryTree<Integer>> deque = new LinkedList<>();
        while (node != null) {
            deque.addFirst(node);
            node = node.parent;
        }
        return deque;
    }

    @EpiTest(testDataFile = "lowest_common_ancestor.tsv")
    public static int lcaWrapper(TimedExecutor executor, BinaryTree<Integer> tree,
                                 Integer key0, Integer key1) throws Exception {
        BinaryTree<Integer> node0 = BinaryTreeUtils.mustFindNode(tree, key0);
        BinaryTree<Integer> node1 = BinaryTreeUtils.mustFindNode(tree, key1);

        BinaryTree<Integer> result = executor.run(() -> LCA(node0, node1));

        if (result == null) {
            throw new TestFailure("Result can not be null");
        }
        return result.data;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "LowestCommonAncestorWithParent.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
