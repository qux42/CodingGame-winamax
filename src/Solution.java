import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    static Scanner in;
    static int width;
    static int height;
    static int[][] field;

    public static void main(String args[]) {
        in = new Scanner(System.in);

        width = in.nextInt();
        height = in.nextInt();

        field = new int[height][width];

        for (int i = 0; i < height; i++) {
            String row = in.next();
            int j = 0;
            for (char c : row.toCharArray()) {
                switch (c) {
                    case 'H':
                        field[i][j] = -1;
                        break;
                    case '.':
                        field[i][j] = -2;
                        break;
                    case 'X':
                        field[i][j] = -3;
                        break;
                    default:
                        field[i][j] = Integer.parseInt("" + c);
                }
                j++;
            }
        }
        System.out.println(Arrays.deepToString(field));
    }

    Map<Point, List<Path>> getReachableHoles(Point ball, int shots) {
        field[ball.h, ball.w + shots]
    }

    List<Point> getReachablePositions(Point ball, int shots) {
        List<Point> list = new LinkedList<Point>();


        if (ball.w - shots >= 0) {

        }

        if (ball.w + shots < width) {

        }

        if (ball.h - shots >= 0) {

        }

        if (ball.h + shots < 0) {

        }
    }

    boolean isAllowedShot(Point ball, char direction, int shots) {
        if (shots > 1) {
            switch (direction) {
                case '<':
                    field[ball.h][ball.w -1]
            }
        } else if (shots == 1) {
            switch (direction) {
                case '<':
                    field[ball.h][ball.w - 1]
            }
        } else {

        }
    }

    static class Path {
        List<Point> waypoints;

        public Path(List<Point> waypoints) {
            this.waypoints = waypoints;
        }

        public int length() {
            return waypoints.size();
        }
    }

    static class Point {
        int w;
        int h;

        public Point(int w, int h) {
            this.w = w;
            this.h = h;
        }

        public int getW() {
            return w;
        }

        public int getH() {
            return h;
        }
    }
}