package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.7 BUY AND SELL A STOCK TWICE
 * <p>
 * The max difference problem, introduced on Page 1, formalizes the maximum profit
 * that can be made by buying and then selling a single share over a given day range.
 * <p>
 * Write a program that computes the maximum profit that can be made by buying and
 * selling a share at most twice. The second buy must be made on another date after the
 * first sale.
 * <p>
 * Hint: What do you need to know about the first i elements when processing the
 * (i + 1)th element?
 */
public class BuyAndSellStockTwice {
    @EpiTest(testDataFile = "buy_and_sell_stock_twice.tsv")
    public static double buyAndSellStockTwice(List<Double> prices) {
        return solTwo(prices);
    }

    /**
     * 思路一：将数组拆分成两部分，分别计算卖一次的最大收益，然后相加，结果就是卖两次的最大收益
     * 遍历所有的组合，最后计算出最大收益
     * <p>
     * 备注：需要将只买卖一次的场景考虑进去
     * <p>
     * 时间复杂度：O(n ^ 2)
     * 空间复杂度：O(n ^ 2)
     */
    private static double solOne(List<Double> prices) {
        double maxProfitSellOnce = buyAndSellStockOnce(prices);
        double maxProfitSellTwice = 0.0;
        for (int i = 1; i < prices.size(); i++) {
            List<Double> pricesOne = prices.subList(0, i);
            List<Double> pricesTwo = prices.subList(i, prices.size());
            double profitOne = buyAndSellStockOnce(pricesOne);
            double profitTwo = buyAndSellStockOnce(pricesTwo);
            maxProfitSellTwice = Math.max(maxProfitSellTwice, profitOne + profitTwo);
        }
        return Math.max(maxProfitSellOnce, maxProfitSellTwice);
    }

    private static double buyAndSellStockOnce(List<Double> prices) {
        double minPrice = Double.MAX_VALUE, maxProfit = 0.0;
        for (int i = 0; i < prices.size(); i++) {
            minPrice = Math.min(minPrice, prices.get(i));
            maxProfit = Math.max(maxProfit, prices.get(i) - minPrice);
        }
        return maxProfit;
    }

    /**
     * 思路二：将数组分成两部分，分两步完成：
     * <p>
     * 第一步，正向遍历，计算并存储买卖一次的最大收益组合
     * <p>
     * 第二步，反向遍历，计算买卖一次的最大收益，然后和第一步的结果相加，即为总的最大收益
     * <p>
     * M[i] = F[i - 1] + B[i], F = forward, B = backward, F[-1] = 0
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    private static double solTwo(List<Double> prices) {
        double maxTotalProfit = 0.0;
        List<Double> firstBuySellProfits = new ArrayList<>();
        double minPriceSoFar = Double.MAX_VALUE;

        // Forward phase. For each day, we record maximum profit if we
        // sell on that day.
        for (int i = 0; i < prices.size(); i++) {
            minPriceSoFar = Math.min(minPriceSoFar, prices.get(i));
            maxTotalProfit = Math.max(maxTotalProfit, prices.get(i) - minPriceSoFar);
            firstBuySellProfits.add(maxTotalProfit);
        }

        // Backward phase. For each day, find the maximum profit if we make
        // the second buy on that day.
        double maxPriceSoFar = Double.MIN_VALUE;
        for (int i = prices.size() - 1; i > 0; i--) {
            maxPriceSoFar = Math.max(maxPriceSoFar, prices.get(i));
            maxTotalProfit = Math.max(maxTotalProfit, firstBuySellProfits.get(i - 1)
                    + maxPriceSoFar - prices.get(i));
        }
        return maxTotalProfit;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "BuyAndSellStockTwice.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
