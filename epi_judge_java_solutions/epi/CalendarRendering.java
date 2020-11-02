package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 14.4 RENDER A CALENDAR
 * <p>
 * Consider the problem of designing an online calendaring application. One component of
 * the design is to render the calendar, i.e., display it visually.
 * <p>
 * Suppose each day consists of a number of events, where an event is specified as a start time
 * and a finish time. Individual events for a day are to be rendered as nonoverlapping
 * rectangular regions whose sides are parallel to the X- and Y-axes. Let the X-axis correspond
 * to time. If an event starts at time b and ends at time e, the upper and lower sides of its
 * corresponding rectangle must be at b and e, respectively. Figure 14.1 represents a set of events.
 * <p>
 * Suppose the Y-coordinates for each day's events must lie between 0 and L (a pre-specified
 * constant), and each event's rectangle must have the same "height" (distance between the sides
 * parallel to the X-axis). Your task is to compute the maximum height an event rectangle can have.
 * In essence, this is equivalent to the following problem.
 * <p>
 * Write a program that takes a set of events, and determines the maximum number of events that
 * take place concurrently.
 * <p>
 * Hint: Focus on endpoints.
 */
public class CalendarRendering {
    @EpiTest(testDataFile = "calendar_rendering.tsv")

    public static int findMaxSimultaneousEvents(List<Event> A) {
        return solTwo(A);
    }

    /**
     * 思路二：只关注事件的起止时间节点，遍历事件，记录全部的时间节点，然后对这些时间节点进行排序，
     * 最后遍历节点，遇到一个起点高度加一，遇到一个终点高度减一，记录并返回最大的高度
     * <p>
     * 在比较节点时，如果时间一致时，需要注意让起点排在终点前面
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static int solTwo(List<Event> A) {
        // Builds an array of all endpoints.
        List<Endpoint> E = new ArrayList<>();
        for (Event e : A) {
            E.add(new Endpoint(e.start, true));
            E.add(new Endpoint(e.finish, false));
        }
        // Sorts the endpoint array according to the time, breaking ties
        // by putting start times before end times.
        Collections.sort(E, new Comparator<Endpoint>() {
            @Override
            public int compare(Endpoint e1, Endpoint e2) {
                if (e1.time != e2.time) {
                    return Integer.compare(e1.time, e2.time);
                }

                // If times are equal, an endpoint that starts an interval comes first.
                return e1.isStart && !e2.isStart ? -1 : !e1.isStart && e2.isStart ? 1 : 0;
            }
        });
        
        // Track the number of simultaneous events, and record the maximum
        // number of simultaneous events.
        int maxNumSimultaneousEvents = 0, numSimultaneousEvents = 0;
        for (Endpoint e : E) {
            if (e.isStart) {
                numSimultaneousEvents++;
                maxNumSimultaneousEvents = Math.max(maxNumSimultaneousEvents, numSimultaneousEvents);
            } else {
                numSimultaneousEvents--;
            }
        }
        return maxNumSimultaneousEvents;
    }

    /**
     * 思路一：聚焦事件的起止时间节点，遍历事件以记录全部的节点，然后再遍历节点，计算节点最多落在几个事件
     * 的时间段内（即为高度），记录并返回最大的高度
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(n)
     */
    private static int solOne(List<Event> A) {
        List<Endpoint> E = new ArrayList<>();
        for (Event event : A) {
            E.add(new Endpoint(event.start, true));
            E.add(new Endpoint(event.finish, false));
        }
        int maxNumSimultaneousEvents = 0, numSimultaneousEvents = 0;
        for (Endpoint endpoint : E) {
            numSimultaneousEvents = 0;
            for (Event event : A) {
                if (endpoint.time >= event.start && endpoint.time <= event.finish) {
                    numSimultaneousEvents++;
                    maxNumSimultaneousEvents = Math.max(maxNumSimultaneousEvents, numSimultaneousEvents);
                }
            }
        }
        return maxNumSimultaneousEvents;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "CalendarRendering.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @EpiUserType(ctorParams = {int.class, int.class})

    public static class Event {
        public int start, finish;

        public Event(int start, int finish) {
            this.start = start;
            this.finish = finish;
        }
    }

    private static class Endpoint {
        public int time;
        public boolean isStart;

        Endpoint(int time, boolean isStart) {
            this.time = time;
            this.isStart = isStart;
        }
    }
}
