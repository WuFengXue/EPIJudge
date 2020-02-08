package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 9.9 IMPLEMENT A QUEUE USING STACKS
 * <p>
 * Queue insertion and deletion follows first-in, first-out semantics;
 * stack insertion and deletion is last-in, first-out.
 * <p>
 * How would you implement a queue given a library implementing stacks?
 * <p>
 * Hint: It is impossible to solve this problem with a single stack.
 */
public class QueueFromStacks {

    @EpiTest(testDataFile = "queue_from_stacks.tsv")
    public static void queueTest(List<QueueOp> ops) throws TestFailure {
        try {
            Queue q = new Queue();

            for (QueueOp op : ops) {
                switch (op.op) {
                    case "QueueWithMax":
                        q = new Queue();
                        break;
                    case "enqueue":
                        q.enqueue(op.arg);
                        break;
                    case "dequeue":
                        int result = q.dequeue();
                        if (result != op.arg) {
                            throw new TestFailure("Dequeue: expected " +
                                    String.valueOf(op.arg) + ", got " +
                                    String.valueOf(result));
                        }
                        break;
                }
            }
        } catch (NoSuchElementException e) {
            throw new TestFailure("Unexpected NoSuchElement exception");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "QueueFromStacks.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public static class Queue {
        AbstQueueImpl queueImpl = new QueueImplTwo();

        public void enqueue(Integer x) {
            queueImpl.enqueue(x);
            return;
        }

        public Integer dequeue() {
            return queueImpl.dequeue();
        }
    }

    /**
     * 思路二：用一个堆栈存储入队元素，另一个堆栈存储出队元素。
     * <p>
     * 入队时，直接将元素压入入队堆栈。
     * <p>
     * 出队时，直接从出队堆栈推出栈顶元素
     * （如果出队堆栈为空，则先将入队堆栈的全部元素导入出队堆栈）。
     * <p>
     * 入队时间复杂度：O(1)
     * <p>
     * 出队时间复杂度：O(m), m < 2n （出队堆栈不为空时为O(1) ）
     */
    private static class QueueImplTwo extends AbstQueueImpl {
        Deque<Integer> enq = new LinkedList<>();
        Deque<Integer> deq = new LinkedList<>();

        @Override
        void enqueue(Integer x) {
            enq.addFirst(x);
        }

        @Override
        Integer dequeue() {
            if (deq.isEmpty()) {
                // Transfers the elements from enq to deq.
                while (!enq.isEmpty()) {
                    deq.addFirst(enq.removeFirst());
                }
            }

            if (deq.isEmpty()) {
                throw new NoSuchElementException("Dequeue called on an empty queue.");
            }

            return deq.removeFirst();
        }
    }

    /**
     * 思路一：用一个堆栈存储出队元素，另一个临时堆栈用于翻转堆栈。
     * （另一变种：存储入队元素，翻转堆栈的操作改在出队时操作）
     * <p>
     * 入队时，先将出队堆栈的全部元素导入到临时堆栈，然后将新元素压入出队堆栈，
     * 最后再将临时堆栈的全部元素添加到出队堆栈
     * <p>
     * 出队时，直接从出队堆栈推出栈顶元素。
     * <p>
     * 入队时间复杂度：O(n)
     * <p>
     * 出队时间复杂度：O(1)
     */
    private static class QueueImplOne extends AbstQueueImpl {
        Deque<Integer> deq = new LinkedList<>();
        Deque<Integer> tmp = new LinkedList<>();

        @Override
        void enqueue(Integer x) {
            while (!deq.isEmpty()) {
                tmp.addFirst(deq.removeFirst());
            }
            deq.addFirst(x);
            while (!tmp.isEmpty()) {
                deq.addFirst(tmp.removeFirst());
            }
        }

        @Override
        Integer dequeue() {
            if (deq.isEmpty()) {
                throw new NoSuchElementException("Dequeue called on an empty queue.");
            }

            return deq.removeFirst();
        }
    }

    private static abstract class AbstQueueImpl {
        abstract void enqueue(Integer x);

        abstract Integer dequeue();
    }

    @EpiUserType(ctorParams = {String.class, int.class})
    public static class QueueOp {
        public String op;
        public int arg;

        public QueueOp(String op, int arg) {
            this.op = op;
            this.arg = arg;
        }
    }
}
