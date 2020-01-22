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
 * 9.1 IMPLEMENT A STACK WITH MAX API
 * <p>
 * Design a stack that includes a max operation, in addition to push and pop. The max
 * method should return the maximum value stored in the stack.
 * <p>
 * Hint: Use additional storage to track the maximum value.
 */
public class StackWithMax {

    @EpiTest(testDataFile = "stack_with_max.tsv")
    public static void stackTest(List<StackOp> ops) throws TestFailure {
        try {
            Stack s = new Stack();
            int result;
            for (StackOp op : ops) {
                switch (op.op) {
                    case "Stack":
                        s = new Stack();
                        break;
                    case "push":
                        s.push(op.arg);
                        break;
                    case "pop":
                        result = s.pop();
                        if (result != op.arg) {
                            throw new TestFailure("Pop: expected " + String.valueOf(op.arg) +
                                    ", got " + String.valueOf(result));
                        }
                        break;
                    case "max":
                        result = s.max();
                        if (result != op.arg) {
                            throw new TestFailure("Max: expected " + String.valueOf(op.arg) +
                                    ", got " + String.valueOf(result));
                        }
                        break;
                    case "empty":
                        result = s.empty() ? 1 : 0;
                        if (result != op.arg) {
                            throw new TestFailure("Empty: expected " + String.valueOf(op.arg) +
                                    ", got " + String.valueOf(s));
                        }
                        break;
                    default:
                        throw new RuntimeException("Unsupported stack operation: " + op.op);
                }
            }
        } catch (NoSuchElementException e) {
            throw new TestFailure("Unexpected NoSuchElement exception");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "StackWithMax.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    interface IStack {
        boolean empty();

        Integer max();

        Integer pop();

        void push(Integer x);
    }

    public static class Stack {
        IStack stackImpl = new StackImplThree();

        public boolean empty() {
            return stackImpl.empty();
        }

        public Integer max() {
            return stackImpl.max();
        }

        public Integer pop() {
            return stackImpl.pop();
        }

        public void push(Integer x) {
            stackImpl.push(x);
        }
    }

    public static class MaxWithCount {
        Integer max;
        Integer count;

        public MaxWithCount(Integer max, Integer count) {
            this.max = max;
            this.count = count;
        }
    }

    /**
     * 思路三：只在新添加的元素比当前最大值大时，才需更新最大值。用一个堆栈缓存最大值，
     * 考虑到重复元素的场景，添加一个字段用于记录出现的次数。
     * <p>
     * 时间复杂度：O(1)
     * <p>
     * 空间复杂度：<= O(n)
     */
    public static class StackImplThree implements IStack {

        Deque<Integer> deque = new LinkedList<>();
        Deque<MaxWithCount> maxDeque = new LinkedList<>();

        @Override
        public boolean empty() {
            return deque.isEmpty();
        }

        @Override
        public Integer max() {
            if (empty()) {
                throw new IllegalStateException("max(): empty stack");
            }

            return maxDeque.peek().max;
        }

        @Override
        public Integer pop() {
            if (empty()) {
                throw new IllegalStateException("pop(): empty stack");
            }

            Integer x = deque.removeFirst();
            if (x.equals(maxDeque.peek().max)) {
                maxDeque.peek().count--;
                if (maxDeque.peek().count == 0) {
                    maxDeque.removeFirst();
                }
            }
            return x;
        }

        @Override
        public void push(Integer x) {
            if (maxDeque.isEmpty()) {
                maxDeque.addFirst(new MaxWithCount(x, 1));
            } else {
                if (maxDeque.peek().max.equals(x)) {
                    maxDeque.peek().count++;
                } else if (maxDeque.peek().max.compareTo(x) < 0) {
                    maxDeque.addFirst(new MaxWithCount(x, 1));
                }
            }
            deque.addFirst(x);
        }
    }

    public static class ElementWithCachedMax {
        Integer element;
        Integer max;

        public ElementWithCachedMax(Integer element, Integer max) {
            this.element = element;
            this.max = max;
        }
    }

    /**
     * 思路二：为每个元素附加一个字段，用于记录其对应的最大值。
     * <p>
     * 时间复杂度：O(1)
     * <p>
     * 空间复杂度：O(n)
     */
    public static class StackImplTwo implements IStack {
        // Stores (element, cached maximum) pair.
        Deque<ElementWithCachedMax> deque = new LinkedList<>();

        @Override
        public boolean empty() {
            return deque.isEmpty();
        }

        @Override
        public Integer max() {
            if (empty()) {
                throw new IllegalStateException("max(): empty stack");
            }

            return deque.peek().max;
        }

        @Override
        public Integer pop() {
            if (empty()) {
                throw new IllegalStateException("pop(): empty stack");
            }

            return deque.removeFirst().element;
        }

        @Override
        public void push(Integer x) {
            Integer max = empty() ? x : Math.max(x, max());
            deque.addFirst(new ElementWithCachedMax(x, max));
        }
    }

    /**
     * 思路一：在调用最大值接口时，遍历所有元素取得最大值。
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    public static class StackImplOne implements IStack {
        Deque<Integer> deque = new LinkedList<>();

        @Override
        public boolean empty() {
            return deque.isEmpty();
        }

        @Override
        public Integer max() {
            if (empty()) {
                throw new IllegalStateException("max(): empty stack");
            }

            Integer max = Integer.MIN_VALUE;
            for (Integer x : deque) {
                if (max.compareTo(x) < 0) {
                    max = x;
                }
            }
            return max;
        }

        @Override
        public Integer pop() {
            if (empty()) {
                throw new IllegalStateException("pop(): empty stack");
            }

            return deque.removeFirst();
        }

        @Override
        public void push(Integer x) {
            deque.push(x);
        }
    }

    @EpiUserType(ctorParams = {String.class, int.class})
    public static class StackOp {
        public String op;
        public int arg;

        public StackOp(String op, int arg) {
            this.op = op;
            this.arg = arg;
        }
    }
}
