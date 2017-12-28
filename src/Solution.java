import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    static Scanner in;
    static int width = 3;
    static int height = 3;
    static char[][] field = {{'2', '.', 'X'}, {'X', '.', 'H'}, {'.', 'H', '1'}};

    public static void main(String args[]) {
//        in = new Scanner(System.in);

//        width = in.nextInt();
//        height = in.nextInt();

//        field = new int[height][width];
//
//        for (int i = 0; i < height; i++) {
//            String row = in.next();
//            int j = 0;
//            for (char c : row.toCharArray()) {
//                switch (c) {
//                    case 'H':
//                        field[i][j] = -1;
//                        break;
//                    case '.':
//                        field[i][j] = -2;
//                        break;
//                    case 'X':
//                        field[i][j] = -3;
//                        break;
//                    default:
//                        field[i][j] = Integer.parseInt("" + c);
//                }
//                j++;
//            }
//        }
        System.out.println(Arrays.deepToString(field));
    }

    Map<Point, List<Path>> getReachableHoles(Point ball, int shots) {
        field[ball.h, ball.w + shots]
    }

    List<Point> getReachablePositions(Point ball, int shots) {
        List<Point> list = new LinkedList<Point>();

        if (ball.w - shots >= 0 && isAllowedShot(ball, '<', shots)) {
            list.add(new Point(ball.h, ball.w - shots));
        }

        if (ball.w + shots < width) {
            list.add(new Point(ball.h, ball.w + shots));
        }

        if (ball.h - shots >= 0) {
            list.add(new Point(ball.h - shots, ball.w));
        }

        if (ball.h + shots < 0) {
            list.add(new Point(ball.h + shots, ball.w));
        }

        return list;
    }

    boolean isAllowedShot(Point ball, char direction, int shots) {
        Point nextPos = null;
        switch (direction) {
            case '<':
                nextPos = new Point(ball.h, ball.w - 1);
                break;
            case '^':
                nextPos = new Point(ball.h - 1, ball.w);
                break;
            case '>':
                nextPos = new Point(ball.h, ball.w + 1);
                break;
            case 'v':
                nextPos = new Point(ball.h + 1, ball.w);
                break;
        }
        int valueOnNextPos = field[nextPos.h][nextPos.w];
        if (shots > 1) {
            return ((valueOnNextPos == '.') || (valueOnNextPos == 'X')) && isAllowedShot(nextPos, direction, shots - 1);
        }
        return valueOnNextPos == 'H' || valueOnNextPos == '.';
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
        int h;
        int w;

        public Point(int h, int w) {
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