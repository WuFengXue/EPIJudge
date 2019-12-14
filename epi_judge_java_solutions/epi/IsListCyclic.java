package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.HashSet;
import java.util.Set;

/**
 * 8.3 TEST FOR CYCLICITY
 * <p>
 * Although a linked list is supposed to be a sequence of nodes ending in null, it is possible
 * to create a cycle in a linked list by making the next field of an element reference to one
 * of the earlier nodes.
 * <p>
 * Write a program that takes the head of a singly linked list and returns null if there does
 * not exist a cycle, and the node at the start of the cycle, if a cycle is present. (You do
 * not know the length of the list in advance.)
 * <p>
 * Hint: Consider using two iterators, one fast and one slow.
 */
public class IsListCyclic {

    public static ListNode<Integer> hasCycle(ListNode<Integer> head) {
        return solThree(head);
    }

    /**
     * 思路一：利用集合的唯一性，逐个将链表中的节点添加到集合中，如果遍历过程出现重复的元素，则表示存在环
     * 且重复元素为环的起点
     * <p>
     * 空间复杂度：O(n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> head) {
        Set<ListNode<Integer>> nodes = new HashSet<>();
        ListNode<Integer> p = head;
        while (p != null) {
            if (!nodes.add(p)) {
                return p;
            }
            p = p.next;
        }
        return null;
    }

    /**
     * 思路二：使用两个指针，快指针每次走两步，慢指针每次走一步，遍历整个链表，如果快指针与慢指针指向
     * 同一个元素，则表明存在环。（因为快指针比慢指针快一步，每走一次快指针就会靠近慢指针一步，
     * 所以如果存在环一定会相遇）
     * <p>
     * 相遇后，快指针不动，移动慢指针，计算快慢指针再次相遇的步数，即为环的长度，即为 cycleLen。
     * <p>
     * 取得环的长度后，将快指针置于距离头结点 cycleLen 步的节点，慢指针置于头结点，同时移动两个指针，
     * 快慢指针相遇的点即为环的起点。（快慢指针的距离刚好为环的长度，所以肯定会相遇）
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> head) {
        ListNode<Integer> slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                // There is a cycle, so now let’s calculate the cycle length.
                int cycleLen = 0;
                do {
                    cycleLen++;
                    slow = slow.next;
                } while (slow != fast);

                // Finds the start of the cycle.
                fast = head;
                // fast pointer advances cycleLen first.
                while (cycleLen-- > 0) {
                    fast = fast.next;
                }
                slow = head;
                // Both iterators advance in tandem.
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                // slow is the start of cycle.
                return slow;
            }
        }
        // no cycle.
        return null;
    }

    /**
     * 思路三：判断是否存在环的方法同思路二，区别在于：
     * <p>
     * 相遇后，将慢指针置于头结点，然后同时移动快慢指针（每次都只移动一步），他们相遇的点即为环的起点。
     * <p>
     * S ：慢指针走的距离，2S：快指针走的距离<br>
     * L：起点到环的起点的距离，C：环的长度<br>
     * X：环的起点到相遇点的距离，H：相遇点到环的起点的距离<br>
     * S = L + X && 2S = L + n * C + X --> L + X = n * C --> L = n * C - X<br>
     * --> L = (n - 1) * C + C - x --> L = (n - 1) * C + H --> 快慢指针一定会在环的起点相遇
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solThree(ListNode<Integer> head) {
        ListNode<Integer> slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                do {
                    slow = slow.next;
                    fast = fast.next;
                } while (slow != fast);
                return slow;
            }
        }
        return null;
    }

    @EpiTest(testDataFile = "is_list_cyclic.tsv")
    public static void HasCycleWrapper(TimedExecutor executor,
                                       ListNode<Integer> head, int cycleIdx)
            throws Exception {
        int cycleLength = 0;
        if (cycleIdx != -1) {
            if (head == null) {
                throw new RuntimeException("Can't cycle empty list");
            }
            ListNode<Integer> cycleStart = null, cursor = head;
            while (cursor.next != null) {
                if (cursor.data == cycleIdx) {
                    cycleStart = cursor;
                }
                cursor = cursor.next;
                if (cycleStart != null) {
                    cycleLength++;
                }
            }
            if (cursor.data == cycleIdx) {
                cycleStart = cursor;
            }
            if (cycleStart == null) {
                throw new RuntimeException("Can't find a cycle start");
            }
            cursor.next = cycleStart;
            cycleLength++;
        }

        ListNode<Integer> result = executor.run(() -> hasCycle(head));

        if (cycleIdx == -1) {
            if (result != null) {
                throw new TestFailure("Found a non-existing cycle");
            }
        } else {
            if (result == null) {
                throw new TestFailure("Existing cycle was not found");
            }

            ListNode<Integer> cursor = result;
            do {
                cursor = cursor.next;
                cycleLength--;
                if (cursor == null || cycleLength < 0) {
                    throw new TestFailure(
                            "Returned node does not belong to the cycle or is not the closest node to the head");
                }
            } while (cursor != result);

            if (cycleLength != 0) {
                throw new TestFailure(
                        "Returned node does not belong to the cycle or is not the closest node to the head");
            }
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsListCyclic.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
