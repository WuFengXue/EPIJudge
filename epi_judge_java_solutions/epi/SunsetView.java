package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;

/**
 * 9.6 COMPUTE BUILDINGS WITH A SUNSET VIEW
 * <p>
 * You are given with a series of buildings that have windows facing west. The buildings
 * are in a straight line, and any building which is to the east of a building of equal
 * or greater height cannot view the sunset.
 * <p>
 * Design an algorithm that processes buildings in east-to-west order and returns the
 * set of buildings which view the sunset. Each building is specified by its height.
 * <p>
 * Hint: When does a building not have a sunset view?
 */
public class SunsetView {
    public static List<Integer>
    examineBuildingsWithSunset(Iterator<Integer> sequence) {
        return solTwo(sequence);
    }

    /**
     * 思路一：先将全部元素添加到数组中，再依照从西到东的顺序进行遍历
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static List<Integer> solOne(Iterator<Integer> sequence) {
        if (sequence == null || !sequence.hasNext()) {
            return Collections.emptyList();
        }

        List<Integer> buildingHeights = new ArrayList<>();
        while (sequence.hasNext()) {
            buildingHeights.add(sequence.next());
        }
        Integer tallestHeight = buildingHeights.get(buildingHeights.size() - 1);
        List<Integer> result = new ArrayList<>();
        result.add(buildingHeights.size() - 1);
        for (int i = buildingHeights.size() - 2; i >= 0; i--) {
            if (buildingHeights.get(i).compareTo(tallestHeight) > 0) {
                tallestHeight = buildingHeights.get(i);
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 思路二：不改变顺序（从东到西），借助堆栈和一个自定义类（记录建筑编号和高度），遍历元素：
     * <p>
     * 先将会被当前建筑遮挡的元素（高度小于或等于当前元素）从堆栈中移除，再将当前元素压入堆栈
     * <p>
     * 遍历完成后，再将堆栈转换成对应的编号列表并返回
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：<= O(n)
     */
    private static List<Integer> solTwo(Iterator<Integer> sequence) {
        int buildingIdx = 0;
        Deque<BuildingWithHeight> candidates = new LinkedList<>();
        while (sequence.hasNext()) {
            Integer buildingHeight = sequence.next();
            while (!candidates.isEmpty()
                    && buildingHeight.compareTo(candidates.peekFirst().height) >= 0) {
                candidates.removeFirst();
            }
            candidates.addFirst(new BuildingWithHeight(buildingIdx++, buildingHeight));
        }

        List<Integer> result = new ArrayList<>();
        for (BuildingWithHeight building : candidates) {
            result.add(building.id);
        }
        return result;
    }

    @EpiTest(testDataFile = "sunset_view.tsv")
    public static List<Integer>
    examineBuildingsWithSunsetWrapper(List<Integer> sequence) {
        return examineBuildingsWithSunset(sequence.iterator());
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SunsetView.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class BuildingWithHeight {
        public Integer id;
        public Integer height;

        public BuildingWithHeight(Integer id, Integer height) {
            this.id = id;
            this.height = height;
        }
    }
}
