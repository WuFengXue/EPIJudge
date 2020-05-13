package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;

import java.util.*;

/**
 * 13.3 IMPLEMENT AN ISBN CACHE
 * <p>
 * The International Standard Book Number (ISBN) is a unique commercial
 * book identifier. It is a string of length 10. The first 9 characters
 * are digits; the last character is a check character. The check
 * character is the sum of the first 9 digits, modulo 11, with 10
 * represented by 'X'. (Modern ISBNs use 13 digits, and the check digit
 * is taken modulo 10; this problem is concerned with 10-digit ISBNs.)
 * <p>
 * Create a cache for looking up prices of books identified by their
 * ISBN. You implement lookup, insert, and remove methods. Use the Least
 * Recently Used (LRU) policy for cache eviction. If an ISBN is already
 * present, insert should not change the price, but it should update that
 * entry to be the most recently used entry. Lookup should also update
 * that entry to be the most recently used entry.
 * <p>
 * Hint: Amortize the cost of deletion. Alternatively, use an auxiliary
 * data structure.
 */
public class LruCache {
    private AbsLruCacheSol sol;

    LruCache(final int capacity) {
        sol = new LruCacheSolTwo(capacity);
    }

    @EpiTest(testDataFile = "lru_cache.tsv")
    public static void runTest(List<Op> commands) throws TestFailure {
        if (commands.isEmpty() || !commands.get(0).code.equals("LruCache")) {
            throw new RuntimeException("Expected LruCache as first command");
        }
        LruCache cache = new LruCache(commands.get(0).arg1);
        for (Op op : commands.subList(1, commands.size())) {
            int result;
            switch (op.code) {
                case "lookup":
                    result = cache.lookup(op.arg1);
                    if (result != op.arg2) {
                        throw new TestFailure("Lookup: expected " + String.valueOf(op.arg2) +
                                ", got " + String.valueOf(result));
                    }
                    break;
                case "insert":
                    cache.insert(op.arg1, op.arg2);
                    break;
                case "erase":
                    result = cache.erase(op.arg1) ? 1 : 0;
                    if (result != op.arg2) {
                        throw new TestFailure("Erase: expected " + String.valueOf(op.arg2) +
                                ", got " + String.valueOf(result));
                    }
                    break;
                default:
                    throw new RuntimeException("Unexpected command " + op.code);
            }
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "LruCache.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public Integer lookup(Integer key) {
        return sol.lookup(key);
    }

    public void insert(Integer key, Integer value) {
        sol.insert(key, value);
    }

    public Boolean erase(Object key) {
        return sol.erase(key);
    }

    /**
     * 思路二：使用 Java 自带的 LinkedHashMap 实现。
     * <p>
     * 时间复杂度：
     * 查找     插入      删除
     * O(n)     O(1)     O(1)
     * （上述结论待确认）
     * <p>
     * 空间复杂度：O(n)
     */
    private static class LruCacheSolTwo extends AbsLruCacheSol {
        private LinkedHashMap<Integer, Integer> isbnToPrice;

        public LruCacheSolTwo(final int capacity) {
            super(capacity);
            isbnToPrice = new LinkedHashMap<>(capacity, 1.0f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldestEntry) {
                    return size() > capacity;
                }
            };
        }

        @Override
        public Integer lookup(Integer key) {
            return isbnToPrice.getOrDefault(key, -1);
        }

        @Override
        public void insert(Integer key, Integer value) {
            // We add the value for key only if key is not present - we
            // don’t update existing values.
            // 注意：此处不可以用 containsKey 判断，因为其不会更新内部队列顺序，
            // 可以改用 get(key) == null 进行判断
            isbnToPrice.putIfAbsent(key, value);
        }

        @Override
        public Boolean erase(Object key) {
            return isbnToPrice.remove(key) != null;
        }
    }

    /**
     * 思路一：用一个队列和一个哈希表实现，队列用于实现 LRU 策略，哈希表用于存储
     * 数据。
     * <p>
     * 时间复杂度：查找     插入      删除
     * 队列：     O(n)     O(1)     O(1)
     * 哈希表：   O(1)     O(1)     O(1)
     * （上述结论待确认）
     * <p>
     * 空间复杂度：O(n)
     */
    private static class LruCacheSolOne extends AbsLruCacheSol {
        private final int capacity;
        private Queue<Integer> keyQueue;
        private Map<Integer, Integer> isbnToPrice;

        public LruCacheSolOne(final int capacity) {
            super(capacity);
            this.capacity = capacity;
            keyQueue = new LinkedList<>();
            isbnToPrice = new HashMap<>(capacity, 1.0f);
        }

        @Override
        public Integer lookup(Integer key) {
            if (keyQueue.contains(key)) {
                keyQueue.remove(key);
                keyQueue.add(key);
                return isbnToPrice.get(key);
            } else {
                return -1;
            }
        }

        @Override
        public void insert(Integer key, Integer value) {
            if (isbnToPrice.containsKey(key)) {
                keyQueue.remove(key);
                keyQueue.add(key);
                return;
            }

            keyQueue.add(key);
            isbnToPrice.put(key, value);
            if (keyQueue.size() > capacity) {
                Integer eldestKey = keyQueue.remove();
                isbnToPrice.remove(eldestKey);
            }
        }

        @Override
        public Boolean erase(Object key) {
            return keyQueue.remove(key)
                    && isbnToPrice.remove(key) != null;
        }
    }

    private static abstract class AbsLruCacheSol {
        public AbsLruCacheSol(final int capacity) {

        }

        public abstract Integer lookup(Integer key);

        public abstract void insert(Integer key, Integer value);

        public abstract Boolean erase(Object key);
    }

    @EpiUserType(ctorParams = {String.class, int.class, int.class})
    public static class Op {
        String code;
        int arg1;
        int arg2;

        public Op(String code, int arg1, int arg2) {
            this.code = code;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }
}
