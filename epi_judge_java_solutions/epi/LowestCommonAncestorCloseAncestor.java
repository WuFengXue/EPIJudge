package epi;

import epi.test_framework.*;

import java.util.HashSet;

/**
 * 13.4 COMPUTE THE LCA, OPTIMIZING FOR CLOSE ANCESTORS
 * <p>
 * Problem 10.4 on Page 157 is concerned with computing the LCA in a binary
 * tree with parent pointers in time proportional to the height of the tree.
 * The algorithm presented in Solution 10.4 on Page 157 entails traversing
 * all the way to the root even if the nodes whose LCA is being computed are
 * very close to their LCA.
 * <p>
 * Design an algorithm for computing the LCA of two nodes in a binary tree.
 * The algorithm's time complexity should depend only on the distance from
 * the nodes to the LCA.
 * <p>
 * Hint: Focus on the extreme case described in the problem introduction.
 */
public class LowestCommonAncestorCloseAncestor {

    public static BinaryTree<Integer> LCA(BinaryTree<Integer> node0,
                                          BinaryTree<Integer> node1) {
        return solThree(node0, node1);
    }

    /**
     * 思路一：同时移动两个节点，将节点添加到同一个哈希集合，遇到的第一个重复节点即为 LCA
     * <p>
     * 时间复杂度：O(D0 + D1)，D0 为第一个节点到 LCA 的距离，D1 为第二个节点到 LCA 的距离
     * <p>
     * 空间复杂度：O(D0 + D1)
     */
    private static BinaryTree<Integer> solThree(BinaryTree<Integer> node0,
                                                BinaryTree<Integer> node1) {
        HashSet<BinaryTree<Integer>> hash = new HashSet<>();
        while (node0 != null || node1 != null) {
            // Ascend tree in tandem from these two nodes.
            if (node0 != null) {
                if (!hash.add(node0)) {
                    return node0;
                }
                node0 = node0.parent;
            }
            if (node1 != null) {
                if (!hash.add(node1)) {
                    return node1;
                }
                node1 = node1.parent;
            }
        }
        throw new IllegalArgumentException(
                "node0 and node1 are not in the same tree");
    }

    /**
     * 思路二：先遍历第一个节点所在路径，将全部节点添加到一个集合中，然后再遍历
     * 第二个节点所在路径，将节点添加到同一个集合，遇到的第一个重复的元素即为 LCA
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTree<Integer> solTwo(BinaryTree<Integer> node0,
                                              BinaryTree<Integer> node1) {
        HashSet<BinaryTree<Integer>> nodeSet = new HashSet<>();
        while (node0 != null) {
            nodeSet.add(node0);
            node0 = node0.parent;
        }
        while (node1 != null) {
            if (!nodeSet.add(node1)) {
                break;
            }
            node1 = node1.parent;
        }
        return node1;
    }

    /**
     * 思路一：先分别计算两个节点的深度，然后移动比较深的节点使两个节点的深度一致，
     * 然后再同时移动两个节点，第一个相同的节点即为 LCA
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(1)
     */
    private static BinaryTree<Integer> solOne(BinaryTree<Integer> node0,
                                              BinaryTree<Integer> node1) {
        int depth0 = getDepth(node0);
        int depth1 = getDepth(node1);
        if (depth0 < depth1) {
            BinaryTree<Integer> tmp = node0;
            node0 = node1;
            node1 = tmp;
        }

        int depthDiff = Math.abs(depth0 - depth1);
        while (depthDiff-- > 0) {
            node0 = node0.parent;
        }

        while (node0 != null && node1 != null) {
            if (node0 == node1) {
                break;
            }

            node0 = node0.parent;
            node1 = node1.parent;
        }

        return node0;
    }

    private static int getDepth(BinaryTree<Integer> node) {
        int depth = 0;
        while (node != null) {
            depth++;
            node = node.parent;
        }
        return depth;
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
                        .runFromAnnotations(args, "LowestCommonAncestorCloseAncestor.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
