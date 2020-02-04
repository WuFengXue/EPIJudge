package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 9.4 NORMALIZE PATHNAMES
 * <p>
 * A file or directory can be specified via a string called the pathname. This string
 * may specify an absolute path, starting from the root, e.g., /usr/bin/gcc, or a path
 * relative to the current working directory, e.g., scripts/awkscripts.
 * <p>
 * The same directory may be specified by multiple directory paths. For example,
 * /usr/lib/../bin/gcc and scripts//./,./scripts/awkscripts/././ specify equivalent
 * absolute and relative pathnames.
 * <p>
 * Write a program which takes a pathname, and returns the shortest equivalent
 * pathname. Assume individual directories and files have names that use only
 * alphanumeric characters. Subdirectory names may be combined using forward slashes (/),
 * the current directory (.), and parent directory (..).
 * <p>
 * Hint:Trace the cases.How should . and .. be handled? Watch for invalid paths.
 */
public class DirectoryPathNormalization {
    @EpiTest(testDataFile = "directory_path_normalization.tsv")

    public static String shortestEquivalentPath(String path) {
        return solOne(path);
    }

    /**
     * 思路一：使用堆栈存储路径名。遍历全部路径名，进行相应的处理：
     * <p>
     * 路径名为空字符串或"."：过滤
     * <p>
     * 路径名为".."：推出栈顶路径名
     * <p>
     * 路径名为正常文件名：压入堆栈
     * <p>
     * 时间复杂度：O(n)
     */
    private static String solOne(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Illegal path " + path);
        }

        Deque<String> pathNames = new LinkedList<>();
        for (String pathName : path.split("/")) {
            if ("..".equals(pathName)) {
                // Special case: starts with "/", which is an absolute path.
                if (path.startsWith("/") && pathNames.isEmpty()) {
                    throw new IllegalArgumentException("Path error, trying to go up root " + path);
                } else if (pathNames.isEmpty() || "..".equals(pathNames.peekFirst())) {
                    pathNames.addFirst(pathName);
                } else {
                    pathNames.removeFirst();
                }
            }
            // Must be a name.
            else if (!pathName.isEmpty() && !".".equals(pathName)) {
                pathNames.addFirst(pathName);
            }
        }

        StringBuilder result = new StringBuilder();
        if (path.startsWith("/")) {
            result.append("/");
        }
        Iterator<String> iterator = pathNames.descendingIterator();
        if (iterator.hasNext()) {
            result.append(iterator.next());
        }
        while (iterator.hasNext()) {
            result.append("/");
            result.append(iterator.next());
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DirectoryPathNormalization.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
