package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiTestComparator;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 7.10 COMPUTE ALL VALID IP ADDRESSES
 * <p>
 * A decimal string is a string consisting of digits between 0 and 9. Internet
 * Protocol (IP) addresses can be written as four decimal strings separated by
 * periods, e.g., 192.168.1.201. A careless programmer mangles a string representing
 * an IP address in such a way that all the periods vanish.
 * <p>
 * Write a program that determines where to add periods to a decimal string so that
 * the resulting string is a valid IP address. There may be more than one valid IP
 * address corresponding to a string, in which case you should print all possibilities.
 * <p>
 * For example, if the mangled string is "19216811" then two corresponding IP
 * addresses are 192.168.1.1 and 19.216.81.1. (There are seven other possible IP
 * addresses for this string.)
 * <p>
 * Hint: Use nested loops.
 */
public class ValidIpAddresses {
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

    @EpiTest(testDataFile = "valid_ip_addresses.tsv")

    public static List<String> getValidIpAddress(String s) {
        return solOne(s);
    }

    /**
     * 思路一：用 3 个 for 循环遍历所有可能的组合
     */
    private static List<String> solOne(String s) {
        List<String> result = new ArrayList<>();
        for (int i = 1; i < 4 && i < s.length(); i++) {
            String first = s.substring(0, i);
            if (isValidPart(first)) {
                for (int j = 1; j < 4 && i + j < s.length(); j++) {
                    String second = s.substring(i, i + j);
                    if (isValidPart(second)) {
                        for (int k = 1; k < 4 && i + j + k < s.length(); k++) {
                            String third = s.substring(i + j, i + j + k);
                            String fourth = s.substring(i + j + k);
                            if (isValidPart(third) && isValidPart(fourth)) {
                                result.add(first + "." + second + "." + third + "." + fourth);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 判断一个字符串是否合法的 IP 地址部分（0 ~ 255）
     */
    private static boolean isValidPart(String s) {
        if (s.length() > 3) {
            return false;
        }
        // ”00" , ”000” , ”01", etc. are not valid, but "0" is valid
        if (s.startsWith("0") && s.length() > 1) {
            return false;
        }
        return Integer.valueOf(s) <= 255;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ValidIpAddresses.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
