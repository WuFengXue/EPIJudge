package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

/**
 * 5.11 RECTANGLE INTERSECTION
 * <p>
 * This problem is concerned with rectangles whose sides are parallel to the X-axis and
 * Y-axis. See Figure 5.2 for examples.
 * <p>
 * Write a program which tests if two rectangles have a nonempty intersection. If the
 * intersection is nonempty, return the rectangle formed by their intersection.
 * <p>
 * Hint: Think of the X and Y dimensions independently.
 */
public class RectangleIntersection {
    @EpiTest(testDataFile = "rectangle_intersection.tsv")
    public static Rectangle intersectRectangle(Rectangle R1, Rectangle R2) {
        return solOne(R1, R2);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RectangleIntersection.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    /**
     * 思路一：先判断是否重叠，如果重叠则返回重叠的矩形
     * <p>
     * 时间复杂度：0(1)
     */
    private static Rectangle solOne(Rectangle R1, Rectangle R2) {
        if (!isIntersect(R1, R2)) {
            // 没有重叠的矩形
            return new Rectangle(0, 0, -1, -1);
        }
        return new Rectangle(
                Math.max(R1.x, R2.x), Math.max(R1.y, R2.y),
                Math.min(R1.x + R1.width, R2.x + R2.width) - Math.max(R1.x, R2.x),
                Math.min(R1.y + R1.height, R2.y + R2.height) - Math.max(R1.y, R2.y));
    }

    /**
     * 判断两个矩形是否存在重叠部分（说明：只有一个边重叠也算）
     */
    private static boolean isIntersect(Rectangle R1, Rectangle R2) {
        boolean r2LeftNoGreaterThanR1Right = R2.x <= R1.x + R1.width;
        boolean r2RightNoLessThanR1Left = R2.x + R2.width >= R1.x;
        boolean r2TopNoGreaterThanR1Bottom = R2.y <= R1.y + R1.height;
        boolean r2BottomNoLessThanR1Top = R2.y + R2.height >= R1.y;
        return r2LeftNoGreaterThanR1Right && r2RightNoLessThanR1Left
                && r2TopNoGreaterThanR1Bottom && r2BottomNoLessThanR1Top;
    }

    @EpiUserType(ctorParams = {int.class, int.class, int.class, int.class})
    public static class Rectangle {
        int x, y, width, height;

        public Rectangle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Rectangle rectangle = (Rectangle) o;

            if (x != rectangle.x) {
                return false;
            }
            if (y != rectangle.y) {
                return false;
            }
            if (width != rectangle.width) {
                return false;
            }
            return height == rectangle.height;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + width;
            result = 31 * result + height;
            return result;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + ", " + width + ", " + height + "]";
        }
    }
}
