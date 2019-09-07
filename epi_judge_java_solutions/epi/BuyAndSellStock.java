package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;


/**
 * 6.6 BUY AND SELL A STOCK ONCE
 * <p>
 * This problem is concerned with the problem of optimally buying and selling a stock
 * once, as described on Page 2. As an example, consider the following sequence of
 * stock prices: (310,315,275,295,260,270,290,230,255,250). The maximum profit that
 * can be made with one buy and one sell is 30—buy at 260 and sell at 290. Note that
 * 260 is not the lowest price, nor 290 the highest price.
 * <p>
 * Write a program that takes an array denoting the daily stock price, and returns the
 * maximum profit that could be made by buying and then selling one share of that stock.
 * <p>
 * Hint: Identifying the minimum and maximum is not enough since the minimum may appear
 * after the maximum height. Focus on valid differences.
 */
public class BuyAndSellStock {
    @EpiTest(testDataFile = "buy_and_sell_stock.tsv")
    public static double computeMaxProfit(List<Double> prices) {
        return solOne(prices);
    }

    /**
     * 思路一：遍历数组获取历史最大收益（当天卖出的最大收益 = 当日价格 - 历史最低价）
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static double solOne(List<Double> prices) {
        double minPrice = Double.MAX_VALUE, maxProfit = 0.0;
        for (Double price : prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "BuyAndSellStock.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
