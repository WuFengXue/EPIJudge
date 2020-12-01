package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 14.8 TEAM PHOTO DAY—1
 * <p>
 * You are a photographer for a soccer meet. You will be taking pictures of
 * pairs of opposing teams. All teams have the same number of players. A team
 * photo consists of a front row of players and a back row of players. A player
 * in the back row must be taller than the player in front of him, as illustrated
 * in Figure 14.3. All players in a row must be from the same team.
 * <p>
 * Design an algorithm that takes as input two teams and the heights of the players
 * in the teams and checks if it is possible to place players to take the photo
 * subject to the placement constraint.
 * <p>
 * Hint: First try some concrete inputs, then make a general conclusion.
 */
class Team {
    private List<Player> players;

    public Team(List<Integer> height) {
        players =
                height.stream().map(h -> new Player(h)).collect(Collectors.toList());
    }

    // Checks if team0 can be placed in front of team1.
    public static boolean validPlacementExists(Team team0, Team team1) {
        return solOne(team0, team1);
    }

    /**
     * 思路二：在思路一上进行调整，在排序时先拷贝一份列表数据而不是直接修改原来的对象
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static boolean solTow(Team A, Team B) {
        List<Player> ASorted = A.sortPlayersByHeight();
        List<Player> BSorted = B.sortPlayersByHeight();
        for (int i = 0; i < ASorted.size(); i++) {
            if (ASorted.get(i).compareTo(BSorted.get(i)) >= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 观察归纳法，优先考虑最高的球员，后排最高的球员一定要比前排最高的球员更高，如果
     * 该条件满足，则将它们配对，然后考虑剩余的球员（一样的规则）
     * <p>
     * 可以先对球员按身高进行排序，然后依次比较（排序时，直接修改入参）
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static boolean solOne(Team A, Team B) {
        Collections.sort(A.players);
        Collections.sort(B.players);
        for (int i = 0; i < A.players.size(); i++) {
            if (A.players.get(i).compareTo(B.players.get(i)) >= 0) {
                return false;
            }
        }
        return true;
    }

    public List<Player> sortPlayersByHeight() {
        List<Player> result = new ArrayList<>(players);
        Collections.sort(result);
        return result;
    }

    private static class Player implements Comparable<Player> {
        public Integer height;

        public Player(Integer h) {
            height = h;
        }

        @Override
        public int compareTo(Player that) {
            return Integer.compare(height, that.height);
        }
    }
}

public class IsArrayDominated {
    @EpiTest(testDataFile = "is_array_dominated.tsv")
    public static void
    validPlacementExistsWrapper(TimedExecutor executor, List<Integer> team0,
                                List<Integer> team1, boolean expected01,
                                boolean expected10) throws Exception {
        Team t0 = new Team(team0), t1 = new Team(team1);

        boolean result01 = executor.run(() -> Team.validPlacementExists(t0, t1));
        boolean result10 = executor.run(() -> Team.validPlacementExists(t1, t0));
        if (result01 != expected01 || result10 != expected10) {
            throw new TestFailure("");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsArrayDominated.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
