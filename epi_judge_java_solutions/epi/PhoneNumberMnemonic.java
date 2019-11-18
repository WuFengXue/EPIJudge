package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiTestComparator;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 7.7 COMPUTE ALL MNEMONICS FOR A PHONE NUMBER
 * <p>
 * Each digit, apart from 0 and 1, in a phone keypad corresponds to one of three or
 * four letters of the alphabet, as shown in Figure 7.1 on the next page. Since words
 * are easier to remember than numbers, it is natural to ask if a 7 or 10-digit phone
 * number can be represented by a word. For example, "2276696" corresponds to
 * "ACRONYM" as well as "ABPOMZN".
 * <p>
 * Write a program which takes as input a phone number, specified as a string of
 * digits, and returns all possible character sequences that correspond to the phone
 * number. The cell phone keypad is specified by a mapping that takes a digit and
 * returns the corresponding set of characters. The character sequences do not have
 * to be legal words or phrases.
 * <p>
 * Hint: Use recursion.
 */
public class PhoneNumberMnemonic {
    // The mapping from digit to corresponding characters.
    private static final String[] MAPPING = {"0", "1", "ABC", "DEF", "GHI",
            "JKL", "MNO", "PQRS", "TUV", "WXYZ"};

    @EpiTestComparator
    public static BiPredicate<List<String>, List<String>> comp =
            (expected, result) -> {
                if (result == null) {
                    return false;
                }
                Collections.sort(expected);
                Collections.sort(result);
                return expected.equals(result);
            };

    @EpiTest(testDataFile = "phone_number_mnemonic.tsv")

    public static List<String> phoneMnemonic(String phoneNumber) {
        return solOne(phoneNumber);
    }

    /**
     * 思路零：直接嵌套多个 for 循环，一位电话号码一层，写死层数，因为测试用例存在多种号码数
     * 组合，无法通过测试
     * <p>
     * 时间复杂度：O((4 ^ n) * n)
     * <p>
     * 空间复杂度：O(4 ^ n)
     */
    private static List<String> solZero(String phoneNumber) {
        List<String> mnemonics = new ArrayList<>();
        for (int i = 0; i < MAPPING[phoneNumber.charAt(0) - '0'].length(); i++) {
            for (int j = 0; j < MAPPING[phoneNumber.charAt(1) - '0'].length(); j++) {
                for (int k = 0; k < MAPPING[phoneNumber.charAt(2) - '0'].length(); k++) {
                    for (int l = 0; l < MAPPING[phoneNumber.charAt(3) - '0'].length(); l++) {
                        for (int m = 0; m < MAPPING[phoneNumber.charAt(4) - '0'].length(); m++) {
                            for (int n = 0; n < MAPPING[phoneNumber.charAt(5) - '0'].length(); n++) {
                                for (int o = 0; o < MAPPING[phoneNumber.charAt(6) - '0'].length(); o++) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(MAPPING[phoneNumber.charAt(0) - '0'].charAt(i));
                                    sb.append(MAPPING[phoneNumber.charAt(1) - '0'].charAt(j));
                                    sb.append(MAPPING[phoneNumber.charAt(2) - '0'].charAt(k));
                                    sb.append(MAPPING[phoneNumber.charAt(3) - '0'].charAt(l));
                                    sb.append(MAPPING[phoneNumber.charAt(4) - '0'].charAt(m));
                                    sb.append(MAPPING[phoneNumber.charAt(5) - '0'].charAt(n));
                                    sb.append(MAPPING[phoneNumber.charAt(6) - '0'].charAt(o));
                                    mnemonics.add(sb.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        return mnemonics;
    }

    /**
     * 思路一：使用递归的方式实现，利用一个字符数组缓存字符串（便于修改）
     * <p>
     * 时间复杂度：O((4 ^ n) * n)
     * <p>
     * 空间复杂度：O(4 ^ n)
     */
    private static List<String> solOne(String phoneNumber) {
        char[] partialMnemonic = new char[phoneNumber.length()];
        List<String> mnemonics = new ArrayList<>();
        phoneMnemonicHelper(phoneNumber, 0, partialMnemonic, mnemonics);
        return mnemonics;
    }

    private static void phoneMnemonicHelper(String phoneNumber, int digit,
                                            char[] partialMnemonic,
                                            List<String> mnemonics) {
        if (digit == phoneNumber.length()) {
            // All digits are processed, so add partialMnemonic to mnemonics.
            // (We add a copy since subsequent calls modify partialMnemonic.)
            mnemonics.add(new String(partialMnemonic));
        } else {
            // Try all possible characters for this digit.
            String mappingStr = MAPPING[phoneNumber.charAt(digit) - '0'];
            for (int i = 0; i < mappingStr.length(); i++) {
                partialMnemonic[digit] = mappingStr.charAt(i);
                phoneMnemonicHelper(phoneNumber, digit + 1, partialMnemonic,
                        mnemonics);
            }
        }
    }

    /**
     * 思路二：使用迭代的方式实现，先计算出总的组合数并先填充空字符串，然后再进行迭代，每次写入一个字符
     * <p>
     * 时间复杂度：O(4 ^ 2n)
     * <p>
     * 空间复杂度：O(4 ^ n)
     */
    private static List<String> solTwo(String phoneNumber) {
        List<String> mnemonics = new ArrayList<>();
        int count = 1;
        for (int i = 0; i < phoneNumber.length(); i++) {
            count *= MAPPING[phoneNumber.charAt(i) - '0'].length();
        }
        for (int i = 0; i < count; i++) {
            mnemonics.add(new String(""));
        }
        for (int i = 0; i < phoneNumber.length(); i++) {
            String mappingStr = MAPPING[phoneNumber.charAt(i) - '0'];
            count /= mappingStr.length();
            for (int j = 0; j < mappingStr.length(); j++) {
                for (int k = 0; k < mnemonics.size(); k++) {
                    if ((k / count) % mappingStr.length() == j) {
                        String partialMnemonic = mnemonics.get(k).concat(
                                String.valueOf(mappingStr.charAt(j)));
                        mnemonics.set(k, partialMnemonic);
                    }
                }
            }
        }
        return mnemonics;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PhoneNumberMnemonic.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
