package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 9.8 IMPLEMENT A CIRCULAR QUEUE
 * <p>
 * A queue can be implemented using an array and two additional fields, the
 * beginning and the end indices. This structure is sometimes referred to as
 * a circular queue. Both enqueue and dequeue have 0(1) time complexity. If the
 * array is fixed, there is a maximum number of entries that can be stored. If
 * the array is dynamically resized, the total time for m combined enqueue and
 * dequeue operations is 0(m).
 * <p>
 * Implement a queue API using an array for storing elements. Your API should
 * include a constructor function, which takes as argument the initial capacity
 * of the queue, enqueue and dequeue functions, and a function which returns the
 * number of elements stored. Implement dynamic resizing to support storing an
 * arbitrarily large number of elements.
 * <p>
 * Hint: Track the head and tail. How can you differentiate a full queue from
 * an empty one?
 */
public class CircularQueue {

    @EpiTest(testDataFile = "circular_queue.tsv")
    public static void queueTest(List<QueueOp> ops) throws TestFailure {
        Queue q = new Queue(1);
        int opIdx = 0;
        for (QueueOp op : ops) {
            switch (op.op) {
                case "Queue":
                    q = new Queue(op.arg);
                    break;
                case "enqueue":
                    q.enqueue(op.arg);
                    break;
                case "dequeue":
                    int result = q.dequeue();
                    if (result != op.arg) {
                        throw new TestFailure()
                                .withProperty(TestFailure.PropertyName.STATE, q)
                                .withProperty(TestFailure.PropertyName.COMMAND, op)
                                .withMismatchInfo(opIdx, op.arg, result);
                    }
                    break;
                case "size":
                    int s = q.size();
                    if (s != op.arg) {
                        throw new TestFailure()
                                .withProperty(TestFailure.PropertyName.STATE, q)
                                .withProperty(TestFailure.PropertyName.COMMAND, op)
                                .withMismatchInfo(opIdx, op.arg, s);
                    }
                    break;
            }
            opIdx++;
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "CircularQueue.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }


    private static abstract class AbstractQueueImpl {

        public AbstractQueueImpl(int capacity) {

        }

        public abstract void enqueue(Integer x);

        public abstract Integer dequeue();

        public abstract int size();

        public abstract String toString();
    }

    /**
     * 思路一：用数组存放元素，队头下标固定为首个元素，用一个变量记录队尾下标。
     * <p>
     * 入队时，如果队列已满，则进行扩容。
     * <p>
     * 出队时，取队头元素，然后整个数组循环左移一位并重置队尾下标。
     * <p>
     * 入队时间复杂度：O(1) （非扩容时）
     * <p>
     * 出队时间复杂度：O(n)
     */
    private static class QueueImplOne extends AbstractQueueImpl {
        private static final int SCALE_FACTOR = 2;
        private Integer[] entries;
        private int tail;

        public QueueImplOne(int capacity) {
            super(capacity);
            entries = new Integer[capacity];
        }

        @Override
        public void enqueue(Integer x) {
            if (size() == entries.length) {
                entries = Arrays.copyOf(entries, entries.length * SCALE_FACTOR);
            }
            entries[tail++] = x;
        }

        @Override
        public Integer dequeue() {
            if (size() == 0) {
                throw new NoSuchElementException("Dequeue called on an empty queue.");
            }

            Integer ret = entries[0];
            Collections.rotate(Arrays.asList(entries), -1);
            tail--;
            return ret;
        }

        @Override
        public int size() {
            return tail;
        }

        @Override
        public String toString() {
            return Arrays.toString(entries);
        }
    }

    /**
     * 思路二：用数组存放元素，两个变量记录队头和队尾下标，一个变量记录已入队数。
     * <p>
     * 入队时，向队尾写入元素然后更新队尾下标和已入队数（如果队列已满，则先循环左移
     * 使首个元素对应队头，然后重置队头和队尾下标，最后进行扩容）。
     * <p>
     * 出队时，从队头读取元素然后更新队头下标和已入队数。
     * <p>
     * 入队时间复杂度：O(1) （非扩容时）
     * <p>
     * 出队时间复杂度：O(1)
     */
    private static class QueueImplTwo extends AbstractQueueImpl {
        private static final int SCALE_FACTOR = 2;
        private Integer[] entries;
        private int head = 0, tail = 0, numQueuedElements = 0;

        public QueueImplTwo(int capacity) {
            super(capacity);
            entries = new Integer[capacity];
        }

        @Override
        public void enqueue(Integer x) {
            // Need to resize.
            if (numQueuedElements == entries.length) {
                // Makes the queue elements appear consecutively.
                Collections.rotate(Arrays.asList(entries), -head);
                // Resets head and tail.
                head = 0;
                tail = numQueuedElements;
                entries = Arrays.copyOf(entries, numQueuedElements * SCALE_FACTOR);
            }

            entries[tail] = x;
            tail = (tail + 1) % entries.length;
            numQueuedElements++;
        }

        @Override
        public Integer dequeue() {
            if (numQueuedElements == 0) {
                throw new NoSuchElementException("Dequeue called on an empty queue.");
            }

            Integer ret = entries[head];
            head = (head + 1) % entries.length;
            numQueuedElements--;
            return ret;
        }

        @Override
        public int size() {
            return numQueuedElements;
        }

        @Override
        public String toString() {
            return Arrays.toString(entries);
        }
    }

    public static class Queue {
        AbstractQueueImpl queueImpl;

        public Queue(int capacity) {
            queueImpl = new QueueImplTwo(capacity);
        }

        public void enqueue(Integer x) {
            queueImpl.enqueue(x);
            return;
        }

        public Integer dequeue() {
            return queueImpl.dequeue();
        }

        public int size() {
            return queueImpl.size();
        }

        @Override
        public String toString() {
            return queueImpl.toString();
        }
    }

    @EpiUserType(ctorParams = {String.class, int.class})
    public static class QueueOp {
        public String op;
        public int arg;

        public QueueOp(String op, int arg) {
            this.op = op;
            this.arg = arg;
        }

        @Override
        public String toString() {
            return op;
        }
    }
}
