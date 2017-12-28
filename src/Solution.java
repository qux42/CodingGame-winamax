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

    Map<Point, List<Path>> getReachableHoles(Map<Point, List<Path>> closed, List<Character> current, Point ball, int shots) {
        if (shots == 0) {
            return closed;
        } else {
            Map<Character, Point> reachablePositions = getReachablePositions(ball, shots);

            for (Map.Entry<Character, Point> pos: reachablePositions.entrySet()) {
                if (field[pos.getValue().h][pos.getValue().w] == 'H') {

                } else {

                }
            }
        }

        return null;
    }

    Map<Character, Point> getReachablePositions(Point ball, int shots) {
        Map<Character, Point> map = new HashMap<Character, Point>();

        if (ball.w - shots >= 0 && isAllowedShot(ball, '<', shots)) {
            map.put('<', new Point(ball.h, ball.w - shots));
        }

        if (ball.w + shots < width && isAllowedShot(ball, '>', shots)) {
            map.put('>', new Point(ball.h, ball.w + shots));
        }

        if (ball.h + shots < height && isAllowedShot(ball, 'v', shots)) {
            map.put('v', new Point(ball.h + shots, ball.w));
        }

        if (ball.h - shots >= 0 && isAllowedShot(ball, '^', shots)) {
            map.put('^', new Point(ball.h - shots, ball.w));
        }

        return map;
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
        public final List<Character> waypoints;

        public Path(List<Character> waypoints) {
            this.waypoints = waypoints;
        }

        public int length() {
            return waypoints.size();
        }
    }

    static class Point {
        public final int h;
        public final int w;

        public Point(int h, int w) {
            this.w = w;
            this.h = h;
        }
    }
}