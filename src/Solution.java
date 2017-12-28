import java.util.*;
import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    static Scanner in;
    static int width = 3;
    static int height = 3;
    static char[][] field = {{'2', '.', 'X'}, {'X', '.', 'H'}, {'.', 'H', '1'}};
    //    static int width; // = 3;
//    static int height; // = 3;
//    static char[][] field;// = {{'2', '.', 'X'}, {'X', '.', 'H'}, {'.', 'H', '1'}};
    static Map<Point, Integer> ballsPos = new HashMap<Point, Integer>();

    static {
        ballsPos.put(new Point(0, 0), 2);
        ballsPos.put(new Point(2, 2), 1);
    }

    public static void main(String args[]) {
//        in = new Scanner(System.in);
//
//        width = in.nextInt();
//        height = in.nextInt();
//
//        field = new char[height][width];
//
//        for (int i = 0; i < height; i++) {
//            field[i] = in.next().toCharArray();
//        }
//
//        for (int h = 0; h < height; h++) {
//            char[] row = in.next().toCharArray();
//
//            for (int w = 0; w < row.length; w++) {
//                char c = row[w];
//
//                if (c == 'H' || c == '.' || c == 'X') {
//                    field[h][w] = c;
//                } else {
//                    balls.put(new Point(h, w), Integer.parseInt("" + c));
//                    field[h][w] = c;
//                }
//            }
//        }

        // TODO: the magic code here:
        List<Ball> balls = ballsPos.entrySet().stream().map((ball_shot) -> {
            final Tree reachableHoles = getReachableHoles(ball_shot.getKey(), ball_shot.getValue());
            return new Ball(ball_shot.getKey().h, ball_shot.getKey().w, ball_shot.getValue(), reachableHoles);
        }).collect(Collectors.toList());

        System.out.println(balls);
        System.out.println(Arrays.deepToString(field));
    }


    static Set<Move> move(List<Ball> balls, char[][] field) {


        return null;
    }

    static class Move {
        char[][] field;
        List<Ball> balls;

        public Move(char[][] field, List<Ball> balls) {
            this.field = field;
            this.balls = balls;
        }
    }

    static void insertOrUpdate(Map<Point, Set<Path>> map, Point p, Path path) {
        if (map.containsKey(p)) {

            System.out.println("test:");
            Set<Path> paths = map.get(p);
            paths.add(path);
        } else {
            Set<Path> points = new HashSet();
            points.add(path);
            map.put(p, points);
        }
    }

    static void merge(Map<Point, Set<Path>> map1, Map<Point, Set<Path>> map2) {
        map2.forEach((point, listOfPaths) -> {
            if (map1.containsKey(point)) {
//                listOfPaths.forEach(l -> insertOrUpdate(map1, point, l));
                map1.get(point).addAll(listOfPaths);
            } else {
                map1.put(point, listOfPaths);
            }
        });
    }

    // TODO: invalide pfade (sich selbst überschneidend) herausfiltern
    // TODO 2: nicht immer alle pfade für einen ball berechnen, sondern weitere pfade nur auf bedarf berechnen
    static Tree getReachableHoles(Point ball, int shots) {
        return new Tree(getReachableHoles2(ball, shots));
    }

    static Map<Character, AbstractTree> getReachableHoles2(Point ball, int shots) {
        Map<Character, AbstractTree> branches = new HashMap<>();
        getReachablePositions(ball, shots).forEach((direction, nextPos) -> {

            if (field[nextPos.h][nextPos.w] == 'H') {
                branches.put(direction, null);
            } else {
                branches.put(direction, new Tree(getReachableHoles2(nextPos, shots - 1)));
            }
        });
        return branches;
    }

    static Map<Character, Point> getReachablePositions(Point ball, int shots) {
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

    static boolean isAllowedShot(Point ball, char direction, int shots) {
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
        @Override
        public String toString() {
            return "Path {" +
                    "waypoints=" + waypoints +
                    '}';
        }

        public final List<Character> waypoints;

        public Path(List<Character> waypoints) {
            this.waypoints = waypoints;
        }

        public int length() {
            return waypoints.size();
        }

        public Path add(char c) {
            final ArrayList<Character> copy = new ArrayList<>(waypoints);
            copy.add(c);
            return new Path(copy);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path = (Path) o;
            return Objects.equals(waypoints, path.waypoints);
        }

        @Override
        public int hashCode() {

            return Objects.hash(waypoints);
        }
    }

    static class Ball extends Point {
        public final int shots;
        public final Tree reachableHoles;

        public Ball(int h, int w, int shots, Tree reachableHoles) {
            super(h, w);

            this.shots = shots;
            this.reachableHoles = reachableHoles;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Ball{");
            sb.append("shots=").append(shots);
            sb.append(", reachableHoles=").append(reachableHoles);
            sb.append('}');
            return sb.toString();
        }

        public void move(char[][] field) {


        }

    }

    static class Point {

        @Override
        public String toString() {
            return "Point {" + "h=" + h + ", w=" + w + '}';
        }

        public final int h;
        public final int w;

        public Point(int h, int w) {
            this.w = w;
            this.h = h;
        }
    }

    interface AbstractTree {}

    static class Tree implements AbstractTree {
        private final Map<Character, AbstractTree> childs;

        public Tree(Map<Character, AbstractTree> childs) {
            this.childs = childs;
        }
    }

//    static class Leaf implements AbstractTree {
//        private final char value;
//
//        public Leaf(char value) {
//            this.value = value;
//        }
//    }


}