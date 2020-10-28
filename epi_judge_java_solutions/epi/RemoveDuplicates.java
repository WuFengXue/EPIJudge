package epi;

import epi.test_framework.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 14.3 REMOVE FIRST-NAME DUPLICATES
 * <p>
 * Design an efficient algorithm for removing all first-name duplicates
 * from an array. For example, if the input is ((Ian,Botham),(David,Gower)
 * ,(Ian,Bell),(Ian,Chappell)), one result could be ((Ian,Bell),(David,Gower));
 * ((David,Gower),(Ian,Botham)) would also be acceptable.
 * <p>
 * Hint: Bring equal items close together.
 */
public class RemoveDuplicates {
    @EpiTestComparator
    public static BiPredicate<List<String>, List<Name>> comp =
            (expected, result) -> {
                if (result == null) {
                    return false;
                }
                Collections.sort(expected);
                Collections.sort(result);
                if (expected.size() != result.size()) {
                    return false;
                }
                for (int i = 0; i < result.size(); i++) {
                    if (!expected.get(i).equals(result.get(i).firstName)) {
                        return false;
                    }
                }
                return true;
            };
    @EpiTestExpectedType
    public static List<String> expectedType;

    public static void eliminateDuplicate(List<Name> names) {
        solTwo(names);
        return;
    }

    /**
     * 思路二：实现 Comparable 接口，先对列表进行排序，然后借助两个指针将不重复
     * 的元素写到列表前面，最后再移除重复的元素
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solTwo(List<Name> names) {
        Collections.sort(names);
        int writeIdx = 1;
        for (int i = 1; i < names.size(); i++) {
            if (!names.get(i).firstName.equals(names.get(writeIdx - 1).firstName)) {
                names.set(writeIdx++, names.get(i));
            }
        }
        names.subList(writeIdx, names.size())
                .clear();
    }

    /**
     * 思路一：覆写 equals 方法，新建一个列表用于判断是否重复元素，遍历列表
     * 记录重复元素的下标，最后再依次移除重复元素（从下标最大的元素反向移除）
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(m)，m 为不重复元素的数量
     */
    private static void solOne(List<Name> names) {
        List<Name> candidateNames = new ArrayList<>();
        List<Integer> dupIdxList = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (candidateNames.contains(names.get(i))) {
                dupIdxList.add(i);
            } else {
                candidateNames.add(names.get(i));
            }
        }
        for (int i = dupIdxList.size() - 1; i >= 0; i--) {
            int index = dupIdxList.get(i);
            names.remove(index);
        }
    }

    @EpiTest(testDataFile = "remove_duplicates.tsv")
    public static List<Name> eliminateDuplicateWrapper(List<Name> names) {
        eliminateDuplicate(names);
        return names;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RemoveDuplicates.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @EpiUserType(ctorParams = {String.class, String.class})
    //@include
    public static class Name implements Comparable<Name> {
        String firstName;
        String lastName;

        public Name(String first, String last) {
            firstName = first;
            lastName = last;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Name)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            Name name = (Name) obj;
            return firstName.equals(name.firstName) && lastName.equals(name.lastName);
        }

        @Override
        public String toString() {
            return firstName;
        }

        @Override
        public int compareTo(Name name) {
            int cmpFirst = firstName.compareTo(name.firstName);
            if (cmpFirst != 0) {
                return cmpFirst;
            }
            return lastName.compareTo(name.lastName);
        }
    }
}
