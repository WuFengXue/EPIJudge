package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;

import java.util.*;

/**
 * 9.10 IMPLEMENT A QUEUE WITH MAX API
 * <p>
 * Implement a queue with enqueue, dequeue, and max operations. The max
 * operation returns the maximum element currently stored in the queue.
 * <p>
 * Hint: When can an element never be returned by max, regardless of future
 * updates?
 */
public class QueueWithMax {
    private AbstQueueWithMaxImpl queueWithMaxImpl = new QueueWithMaxImplThree();

    @EpiTest(testDataFile = "queue_with_max.tsv")
    public static void queueTest(List<QueueOp> ops) throws TestFailure {
        try {
            QueueWithMax q = new QueueWithMax();

            for (QueueOp op : ops) {
                switch (op.op) {
                    case "QueueWithMax":
                        q = new QueueWithMax();
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
                    case "max":
                        int s = q.max();
                        if (s != op.arg) {
                            throw new TestFailure("Max: expected " + String.valueOf(op.arg) +
                                    ", got " + String.valueOf(s));
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
                        .runFromAnnotations(args, "QueueWithMax.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public void enqueue(Integer x) {
        queueWithMaxImpl.enqueue(x);
        return;
    }

    public Integer dequeue() {
        return queueWithMaxImpl.dequeue();
    }

    public Integer max() {
        return queueWithMaxImpl.max();
    }

    /**
     * 思路三：用两个支持 max() API 的堆栈{@link epi.StackWithMax}实现
     * 队列{@link QueueFromStacks}，最大值即为入队队列和出队队列的最大值。
     * <p>
     * 入队时，将元素压入入队堆栈
     * <p>
     * 出队时，将元素从出队队列推出（如果出队队列为空，则先将入队队列的全部元素导入出队队列）
     * <p>
     * 入队时间复杂度：O(1)
     * <p>
     * 出队时间复杂度：O(1)
     */
    private static class QueueWithMaxImplThree extends AbstQueueWithMaxImpl {
        StackWithMax enq = new StackWithMax();
        StackWithMax deq = new StackWithMax();

        @Override
        void enqueue(Integer x) {
            enq.push(x);
        }

        @Override
        Integer dequeue() {
            if (deq.isEmpty()) {
                while (!enq.isEmpty()) {
                    deq.push(enq.pop());
                }
            }

            if (deq.isEmpty()) {
                throw new NoSuchElementException("Cannot get dequeue() on an empty queue.");
            }

            return deq.pop();
        }

        @Override
        Integer max() {
            if (enq.isEmpty() && deq.isEmpty()) {
                throw new NoSuchElementException("Cannot get max() on an empty queue.");
            } else if (enq.isEmpty()) {
                return deq.max();
            } else if (deq.isEmpty()) {
                return enq.max();
            } else {
                return Math.max(enq.max(), deq.max());
            }
        }

        private static class StackWithMax {
            Deque<ElementWithMax> entries = new LinkedList<>();

            void push(Integer x) {
                Integer max = entries.isEmpty() ? x : Math.max(x, max());
                entries.addFirst(new ElementWithMax(x, max));
            }

            Integer pop() {
                if (entries.isEmpty()) {
                    throw new NoSuchElementException("Cannot get pop() on an empty stack.");
                }

                return entries.removeFirst().element;
            }

            Integer max() {
                if (entries.isEmpty()) {
                    throw new NoSuchElementException("Cannot get max() on an empty stack.");
                }

                return entries.peekFirst().max;
            }

            boolean isEmpty() {
                return entries.isEmpty();
            }

            private static class ElementWithMax {
                Integer element;
                Integer max;

                ElementWithMax(Integer element, Integer max) {
                    this.element = element;
                    this.max = max;
                }
            }
        }
    }

    /**
     * 思路二：用一个队列存储元素，另一个双端队列缓存最大值
     * <p>
     * 入队时，将新元素压入元素队列，然后将最大值队列的队尾中比新元素小的元素推出队列，
     * 最后再将新元素压入最大值队列的队尾
     * <p>
     * 出队时，推出元素队列的队头元素，如果其与最大值队列的队头是同一个元素，
     * 则将其从最大值队列推出
     * <p>
     * 最大值即为最大值队列的队头元素
     * <p>
     * 入队时间复杂度：O(1)
     * <p>
     * 出队时间复杂度：< O(n)
     */
    private static class QueueWithMaxImplTwo extends AbstQueueWithMaxImpl {
        Queue<Integer> entries = new LinkedList<>();
        Deque<Integer> candidatesForMax = new LinkedList<>();

        @Override
        void enqueue(Integer x) {
            entries.add(x);
            // Eliminate dominated elements in candidatesForMax.
            while (!candidatesForMax.isEmpty()
                    && candidatesForMax.peekLast().compareTo(x) < 0) {
                candidatesForMax.removeLast();
            }
            candidatesForMax.addLast(x);
        }

        @Override
        Integer dequeue() {
            if (entries.isEmpty()) {
                throw new NoSuchElementException("Cannot get dequeue() on an empty queue.");
            }

            Integer result = entries.remove();
            if (result.compareTo(candidatesForMax.peekFirst()) == 0) {
                candidatesForMax.removeFirst();
            }
            return result;
        }

        @Override
        Integer max() {
            if (candidatesForMax.isEmpty()) {
                throw new NoSuchElementException("Cannot get max() on an empty queue.");
            }

            return candidatesForMax.peekFirst();
        }
    }


    /**
     * 思路一：用一个变量存储最大值
     * <p>
     * 入队时，将元素压入队列，如果新元素比最大值大，则更新最大值
     * <p>
     * 出队时，将队头元素推出队列，如果该元素为最大值，则遍历队列计算出新的最大值
     * <p>
     * 入队时间复杂度：O(1)
     * <p>
     * 出队时间复杂度：O(n)
     */
    private static class QueueWithMaxImplOne extends AbstQueueWithMaxImpl {
        private Queue<Integer> entries = new LinkedList<>();
        private Integer max = Integer.MIN_VALUE;

        @Override
        void enqueue(Integer x) {
            entries.add(x);
            if (max.compareTo(x) < 0) {
                max = x;
            }
        }

        @Override
        Integer dequeue() {
            if (entries.isEmpty()) {
                throw new NoSuchElementException("Cannot get dequeue() on an empty queue.");
            }

            Integer result = entries.remove();
            if (max.compareTo(result) == 0) {
                max = Integer.MIN_VALUE;
                for (Integer x : entries) {
                    if (max.compareTo(x) < 0) {
                        max = x;
                    }
                }
            }
            return result;
        }

        @Override
        Integer max() {
            if (entries.isEmpty()) {
                throw new NoSuchElementException("Cannot get max() on an empty queue.");
            }

            return max;
        }
    }

    private static abstract class AbstQueueWithMaxImpl {
        abstract void enqueue(Integer x);

        abstract Integer dequeue();

        abstract Integer max();
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
