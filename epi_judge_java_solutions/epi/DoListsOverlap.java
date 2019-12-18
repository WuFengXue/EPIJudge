package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.HashSet;
import java.util.Set;

/**
 * 8.5 TEST FOR OVERLAPPING LISTS —— LISTS MAY HAVE CYCLES
 * <p>
 * Solve Problem 8.4 on Page 119 for the case where the lists may each or both have a cycle.
 * If such a node exists, return a node that appears first when traversing the lists. This
 * node may not be unique —— if one node ends in a cycle, the first cycle node encountered
 * when traversing it may be different from the first cycle node encountered when traversing
 * the second list, even though the cycle is the same. In such cases, you may return either
 * of the two nodes.
 * <p>
 * For example, Figure 8.7 shows an example of lists which overlap and have cycles. For this
 * example, both A and B are acceptable answers.
 * <p>
 * Hint: Use case analysis. What if both lists have cycles? What if they end in a common
 * cycle? What if one list has cycle and the other does not?
 */
public class DoListsOverlap {

    public static ListNode<Integer> overlappingLists(ListNode<Integer> l0,
                                                     ListNode<Integer> l1) {
        return solTwo(l0, l1);
    }

    /**
     * 思路一：先将第一个链表的全部元素添加到一个集合，再遍历第二个链表，如果元素在第一个集合中，
     * 则该元素为重叠的起始节点
     * <p>
     * 注意：需要使用两个集合，否则第一个链表无环、第二个链表有环时将得出错误的结果
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：O(m + n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> l0,
                                            ListNode<Integer> l1) {
        Set<ListNode<Integer>> nodes0 = new HashSet<>();
        while (l0 != null) {
            if (!nodes0.add(l0)) {
                break;
            }
            l0 = l0.next;
        }

        Set<ListNode<Integer>> nodes1 = new HashSet<>();
        while (l1 != null) {
            if (nodes0.contains(l1)) {
                return l1;
            }
            if (!nodes1.add(l1)) {
                break;
            }
            l1 = l1.next;
        }
        return null;
    }

    /**
     * 思路二：区分不同的场景：<br>
     * 1、两个链表都没有环：使用 {@link DoTerminatedListsOverlap} 的算法<br>
     * 2、一个有环一个没有环：不存在重叠部分，直接返回<br>
     * 3、两个都有环：<br>
     * 先判断环是否相同，移动到各自环的起点，然后一个不动，另一个绕环走一圈，
     * 如果走完一圈都没遇到第一个环的起点，则表明不是同一个环
     * <p>
     * 如果是同一个环，则先计算各自起始节点到环的起点的距离，移动长链表使其距环的起点的距离一致，
     * 然后再同时移动两个节点查找重叠部分的起始节点（只需检测到环的起点）
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> l0,
                                            ListNode<Integer> l1) {
        // Store the start of cycle if any.
        ListNode<Integer> root0 = hasCycle(l0);
        ListNode<Integer> root1 = hasCycle(l1);
        if (root0 == null && root1 == null) {
            // Both lists don’t have cycles.
            return overlappingNoCycleLists(l0, l1);
        } else if ((root0 != null && root1 == null)
                || (root0 == null && root1 != null)) {
            // One list has cycle, and one list has no cycle.
            return null;
        }
        // Both lists have cycles.
        ListNode<Integer> temp = root0;
        do {
            temp = temp.next;
        } while (temp != root0 && temp != root1);
        // l0 and l1 do not end in the same cycle.
        if (temp != root1) {
            // Cycles are disjoint.
            return null;
        }

        // l0 and l1 end in the same cycle, locate the overlapping node if they
        // first overlap before cycle starts.
        int dis0 = distance(l0, root0), dis1 = distance(l1, root1);
        if (dis0 > dis1) {
            l0 = advanceListByK(l0, dis0 - dis1);
        } else {
            l1 = advanceListByK(l1, dis1 - dis0);
        }
        while (l0 != l1 && l0 != root0 && l1 != root1) {
            l0 = l0.next;
            l1 = l1.next;
        }
        // If l0 == l1 before reaching root0, it means the overlap first occurs
        // before the cycle starts; otherwise, the first overlapping node is not
        // unique, so we can return any node on the cycle.
        return l0;
    }

    private static ListNode<Integer> hasCycle(ListNode<Integer> head) {
        ListNode<Integer> slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }

    private static ListNode<Integer> overlappingNoCycleLists(ListNode<Integer> l0,
                                                             ListNode<Integer> l1) {
        int length0 = length(l0), length1 = length(l1);
        if (length0 > length1) {
            l0 = advanceListByK(l0, length0 - length1);
        } else {
            l1 = advanceListByK(l1, length1 - length0);
        }
        while (l0 != null && l1 != null && l0 != l1) {
            l0 = l0.next;
            l1 = l1.next;
        }
        return l0;
    }

    private static int length(ListNode<Integer> head) {
        int length = 0;
        while (head != null) {
            length++;
            head = head.next;
        }
        return length;
    }

    private static ListNode<Integer> advanceListByK(ListNode<Integer> l, int k) {
        while (l != null && k-- > 0) {
            l = l.next;
        }
        return l;
    }

    /**
     * Calculates the distance between a and b.
     */
    private static int distance(ListNode<Integer> a, ListNode<Integer> b) {
        int dis = 0;
        while (a != b) {
            dis++;
            a = a.next;
        }
        return dis;
    }

    @EpiTest(testDataFile = "do_lists_overlap.tsv")
    public static void
    overlappingListsWrapper(TimedExecutor executor, ListNode<Integer> l0,
                            ListNode<Integer> l1, ListNode<Integer> common,
                            int cycle0, int cycle1) throws Exception {
        if (common != null) {
            if (l0 == null) {
                l0 = common;
            } else {
                ListNode<Integer> it = l0;
                while (it.next != null) {
                    it = it.next;
                }
                it.next = common;
            }

            if (l1 == null) {
                l1 = common;
            } else {
                ListNode<Integer> it = l1;
                while (it.next != null) {
                    it = it.next;
                }
                it.next = common;
            }
        }

        if (cycle0 != -1 && l0 != null) {
            ListNode<Integer> last = l0;
            while (last.next != null) {
                last = last.next;
            }
            ListNode<Integer> it = l0;
            while (cycle0-- > 0) {
                if (it == null) {
                    throw new RuntimeException("Invalid input data");
                }
                it = it.next;
            }
            last.next = it;
        }

        if (cycle1 != -1 && l1 != null) {
            ListNode<Integer> last = l1;
            while (last.next != null) {
                last = last.next;
            }
            ListNode<Integer> it = l1;
            while (cycle1-- > 0) {
                if (it == null) {
                    throw new RuntimeException("Invalid input data");
                }
                it = it.next;
            }
            last.next = it;
        }

        Set<Integer> commonNodes = new HashSet<>();
        ListNode<Integer> it = common;
        while (it != null && !commonNodes.contains(it.data)) {
            commonNodes.add(it.data);
            it = it.next;
        }

        final ListNode<Integer> finalL0 = l0;
        final ListNode<Integer> finalL1 = l1;
        ListNode<Integer> result =
                executor.run(() -> overlappingLists(finalL0, finalL1));

        if (!((commonNodes.isEmpty() && result == null) ||
                (result != null && commonNodes.contains(result.data)))) {
            throw new TestFailure("Invalid result");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DoListsOverlap.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
