package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 13.2 Is AN ANONYMOUS LETTER CONSTRUCTIBLE?
 * <p>
 * Write a program which takes text for an anonymous letter and text
 * for a magazine and determines if it is possible to write the anonymous
 * letter using the magazine. The anonymous letter can be written using
 * the magazine if for each character in the anonymous letter, the number
 * of times it appears in the anonymous letter is no more than the number
 * of times it appears in the magazine.
 * <p>
 * Hint: Count the number of distinct characters appearing in the letter.
 */
public class IsAnonymousLetterConstructible {
    @EpiTest(testDataFile = "is_anonymous_letter_constructible.tsv")

    public static boolean isLetterConstructibleFromMagazine(String letterText,
                                                            String magazineText) {
        return solTwo(letterText, magazineText);
    }

    /**
     * 思路二：遍历信文本，用一个哈希表记录每种字符出现的次数，然后再遍历杂志文本：
     * <p>
     * 如果字符不在哈希表中，则检查下个字符
     * <p>
     * 如果字符在哈希表中，则将字符出现次数减 1，如果次数为 0，则将其移除，
     * 如果哈希表为空，则结束遍历
     * <p>
     * 遍历结束后，如果哈希表为空，表明匹配成功，返回 true，否则返回 false
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：O(L)，L 为信文本的字符种类数
     */
    private static boolean solTwo(String letterText,
                                  String magazineText) {
        Map<Character, Integer> charFrequencyForLetter = new HashMap<>();
        // Compute the frequencies for all chars in letterText.
        for (int i = 0; i < letterText.length(); i++) {
            char c = letterText.charAt(i);
            if (charFrequencyForLetter.containsKey(c)) {
                charFrequencyForLetter.put(c, charFrequencyForLetter.get(c) + 1);
            } else {
                charFrequencyForLetter.put(c, 1);
            }
        }

        // Check if the characters in magazineText can cover characters in
        // letterText.
        for (int i = 0; i < magazineText.length(); i++) {
            char c = magazineText.charAt(i);
            if (charFrequencyForLetter.containsKey(c)) {
                charFrequencyForLetter.put(c, charFrequencyForLetter.get(c) - 1);
                if (charFrequencyForLetter.get(c) == 0) {
                    charFrequencyForLetter.remove(c);
                    // All characters for letterText are matched.
                    if (charFrequencyForLetter.isEmpty()) {
                        break;
                    }
                }
            }
        }
        // Empty charFrequencyForLetter means every char in letterText
        // can be covered by a character in magazineText.
        return charFrequencyForLetter.isEmpty();
    }

    /**
     * 思路一：先遍历杂志文本，用一个哈希表记录每种字符出现的次数，之后再遍历信文本：
     * <p>
     * 如果信文本的字符不在哈希表中，返回 false
     * <p>
     * 如果信文本的字符在哈希表中，则字符的出现次数减 1，如果次数为 0，则将其移除
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：O(M)，M 为杂志文本的字符种类数
     */
    private static boolean solOne(String letterText,
                                  String magazineText) {
        Map<Character, Integer> charFrequencyForMagazine = new HashMap<>();
        for (int i = 0; i < magazineText.length(); i++) {
            char c = magazineText.charAt(i);
            if (charFrequencyForMagazine.containsKey(c)) {
                charFrequencyForMagazine.put(c, charFrequencyForMagazine.get(c) + 1);
            } else {
                charFrequencyForMagazine.put(c, 1);
            }
        }

        for (int i = 0; i < letterText.length(); i++) {
            char c = letterText.charAt(i);
            if (charFrequencyForMagazine.containsKey(c)) {
                charFrequencyForMagazine.put(c, charFrequencyForMagazine.get(c) - 1);
                if (charFrequencyForMagazine.get(c) == 0) {
                    charFrequencyForMagazine.remove(c);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsAnonymousLetterConstructible.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
