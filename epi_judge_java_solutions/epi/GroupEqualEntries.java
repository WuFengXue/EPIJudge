package epi;

import epi.test_framework.*;

import java.util.*;

/**
 * 14.7 PARTITIONING AND SORTING AN ARRAY WITH MANY REPEATED ENTRIES
 * Suppose you need to reorder the elements of a very large array so that
 * equal elements appear together. For example, if the array is {b,a,c,b,d,a,b,d)
 * then (a,a,b,b,b,c,d,d) is an acceptable reordering, as is (d, d,c,a,a,b,b,b).
 * <p>
 * If the entries are integers, this reordering can be achieved by sorting
 * the array. If the number of distinct integers is very small relative to the size
 * of the array, an efficient approach to sorting the array is to count the number
 * of occurrences of each distinct integer and write the appropriate number of
 * each integer, in sorted order, to the array. When array entries are objects,
 * with multiple fields, only one of which is to be used as a key, the problem
 * is harder to solve.
 * <p>
 * You are given an array of student objects. Each student has an integer-valued
 * age field that is to be treated as a key. Rearrange the elements of the array
 * so that students of equal age appear together. The order in which different
 * ages appear is not important. How would your solution change if ages have to
 * appear in sorted order?
 * <p>
 * Hint: Count the number of students for each age.
 */
public class GroupEqualEntries {
    public static void groupByAge(List<Person> people) {
        solTwo(people);
        return;
    }

    /**
     * 思路三：在思路二的基础上进行优化，直接在原集合进行元素交换，通过更新年龄-数量
     * 和年龄-偏移表标记已交换过的元素
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(m)，m为年龄数
     */
    private static void solThree(List<Person> people) {
        Map<Integer, Integer> ageToCount = new HashMap<>();
        for (Person p : people) {
            if (ageToCount.containsKey(p.age)) {
                ageToCount.put(p.age, ageToCount.get(p.age) + 1);
            } else {
                ageToCount.put(p.age, 1);
            }
        }

        Map<Integer, Integer> ageToOffset = new HashMap<>();
        int offset = 0;
        for (Map.Entry<Integer, Integer> entry : ageToCount.entrySet()) {
            ageToOffset.put(entry.getKey(), offset);
            offset += entry.getValue();
        }

        while (!ageToOffset.isEmpty()) {
            Map.Entry<Integer, Integer> entry = ageToOffset.entrySet().iterator().next();
            int fromOffset = entry.getValue();
            int age = people.get(fromOffset).age;
            int toOffset = ageToOffset.get(age);
            Collections.swap(people, fromOffset, toOffset);
            // Use ageToCount to see when we are finished with a particular age.
            int count = ageToCount.get(age) - 1;
            ageToCount.put(age, count);
            if (count > 0) {
                ageToOffset.put(age, ageToOffset.get(age) + 1);
            } else {
                ageToOffset.remove(age);
            }
        }
    }

    /**
     * 思路二：记录两张表，年龄-数量、年龄-偏移，然后遍历集合，将元素依次设置到新集合的对应位置，
     * 并同步更新偏移表，全部遍历结束后再将新集合的内容拷贝覆盖回旧的集合
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static void solTwo(List<Person> people) {
        List<Person> result = new ArrayList<>(Collections.nCopies(people.size(), null));

        Map<Integer, Integer> ageToCount = new HashMap<>();
        for (Person p : people) {
            if (ageToCount.containsKey(p.age)) {
                ageToCount.put(p.age, ageToCount.get(p.age) + 1);
            } else {
                ageToCount.put(p.age, 1);
            }
        }

        Map<Integer, Integer> ageToOffset = new HashMap<>();
        int offset = 0;
        for (Map.Entry<Integer, Integer> entry : ageToCount.entrySet()) {
            ageToOffset.put(entry.getKey(), offset);
            offset += entry.getValue();
        }

        for (Person p : people) {
            result.set(ageToOffset.get(p.age), p);
            ageToOffset.put(p.age, ageToOffset.get(p.age) + 1);
        }

        Collections.copy(people, result);
    }

    /**
     * 思路一：对集合做排序（按年龄）
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solOne(List<Person> people) {
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return Integer.compare(p1.age, p2.age);
            }
        });
    }

    private static Map<Person, Integer> buildMultiset(List<Person> people) {
        Map<Person, Integer> m = new HashMap<>();
        for (Person p : people) {
            m.put(p, m.getOrDefault(p, 0) + 1);
        }
        return m;
    }

    @EpiTest(testDataFile = "group_equal_entries.tsv")
    public static void groupByAgeWrapper(TimedExecutor executor,
                                         List<Person> people) throws Exception {
        if (people.isEmpty()) {
            return;
        }
        Map<Person, Integer> values = buildMultiset(people);

        executor.run(() -> groupByAge(people));

        Map<Person, Integer> newValues = buildMultiset(people);
        if (!values.equals(newValues)) {
            throw new TestFailure("Entry set changed");
        }
        int lastAge = people.get(0).age;
        Set<Integer> ages = new HashSet<>();

        for (Person p : people) {
            if (ages.contains(p.age)) {
                throw new TestFailure("Entries are not grouped by age");
            }
            if (p.age != lastAge) {
                ages.add(lastAge);
                lastAge = p.age;
            }
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "GroupEqualEntries.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @EpiUserType(ctorParams = {Integer.class, String.class})

    public static class Person {
        public Integer age;
        public String name;

        public Person(Integer k, String n) {
            age = k;
            name = n;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Person person = (Person) o;

            if (!age.equals(person.age))
                return false;
            return name.equals(person.name);
        }

        @Override
        public int hashCode() {
            int result = age.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }
}
