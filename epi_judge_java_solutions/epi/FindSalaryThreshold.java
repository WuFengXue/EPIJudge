package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Collections;
import java.util.List;

/**
 * 14.10 COMPUTE A SALARY THRESHOLD
 * <p>
 * You are working in the finance office for ABC corporation. ABC needs to
 * cut payroll expenses to a specified target. The chief executive officer
 * wants to do this by putting a cap on last year's salaries. Every employee
 * who earned more than the cap last year will be paid the cap this year;
 * employees who earned no more than the cap will see no change in their salary.
 * <p>
 * For example, if there were five employees with salaries last year were
 * $90,$30,$100,$40, and $20, and the target payroll this year is $210,
 * then 60 is a suitable salary cap, since 60 + 30 + 60 + 40 + 20 = 210.
 * <p>
 * Design an algorithm for computing the salary cap, given existing salaries
 * and the target payroll.
 * <p>
 * Hint: How does the payroll vary with the cap?
 */
public class FindSalaryThreshold {
    @EpiTest(testDataFile = "find_salary_threshold.tsv")

    public static double findSalaryCap(int targetPayroll,
                                       List<Integer> currentSalaries) {
        return solThree(targetPayroll, currentSalaries);
    }

    /**
     * 思路三：迭代法实现，将薪水分为未调整和调整两部分，先对集合继续排序，然后遍历集合：
     * <p>
     * 如果未调整部分 + 调整部分（当前薪水 * 未调整数） >= 目标薪酬支出，
     * 则返回 （目标薪酬支出 - 未调整部分） / 未调整数
     * <p>
     * 如果未调整部分 + 调整部分（当前薪水 * 未调整数） < 目标薪酬支出，
     * 则将当前薪水添加到未调整部分，遍历下个元素
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static double solThree(int targetPayroll,
                                   List<Integer> currentSalaries) {
        Collections.sort(currentSalaries);
        double unadjustedSalarySum = 0;
        for (int i = 0; i < currentSalaries.size(); i++) {
            double adjustedSalarySum
                    = currentSalaries.get(i) * (currentSalaries.size() - i);
            if (unadjustedSalarySum + adjustedSalarySum >= targetPayroll) {
                return (targetPayroll - unadjustedSalarySum) / (currentSalaries.size() - i);
            } else {
                unadjustedSalarySum += currentSalaries.get(i);
            }
        }
        // No solution, since targetPayroll > existing payroll.
        return -1.0;
    }

    /**
     * 思路二：递归法实现，在思路一的基础上进行优化，通过一个下标来分隔集合
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(log(n))
     */
    private static double solTwo(int targetPayroll,
                                 List<Integer> currentSalaries) {
        Collections.sort(currentSalaries);
        return findSalaryCapHelper(targetPayroll, currentSalaries, 0);
    }

    private static double findSalaryCapHelper(double targetPayroll,
                                              List<Integer> currentSalaries,
                                              int start) {
        if (start >= currentSalaries.size()) {
            return -1.0;
        }

        int adjustedSalarySum = currentSalaries.get(start) * (currentSalaries.size() - start);
        if (adjustedSalarySum >= targetPayroll) {
            return targetPayroll / (currentSalaries.size() - start);
        } else {
            return findSalaryCapHelper(targetPayroll - currentSalaries.get(start),
                    currentSalaries, start + 1);
        }
    }

    /**
     * 思路一：递归法实现，将薪资集合分为未调整和调整两部分，通过 subList 接口截取集合
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(log (n))
     */
    private static double solOne(int targetPayroll,
                                 List<Integer> currentSalaries) {
        Collections.sort(currentSalaries);
        return findSalaryCapHelper(targetPayroll, currentSalaries);
    }

    private static double findSalaryCapHelper(double targetPayroll,
                                              List<Integer> currentSalaries) {
        if (currentSalaries.isEmpty()) {
            return -1.0;
        }

        int adjustedSalarySum = currentSalaries.get(0) * currentSalaries.size();
        if (adjustedSalarySum >= targetPayroll) {
            return targetPayroll / currentSalaries.size();
        } else {
            return findSalaryCapHelper(targetPayroll - currentSalaries.get(0),
                    currentSalaries.subList(1, currentSalaries.size()));
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "FindSalaryThreshold.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
