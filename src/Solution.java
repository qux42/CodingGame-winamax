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
//    static int width; // = 3;
//    static int height; // = 3;
//    static char[][] field;// = {{'2', '.', 'X'}, {'X', '.', 'H'}, {'.', 'H', '1'}};
    static Map<Point, Integer> balls = new HashMap<Point, Integer>();

    static {
        balls.put(new Point(0,0) , 2);
        balls.put(new Point(2,2) , 1);
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
        balls.forEach((ball, shots) -> {
            Map<Point, Set<Path>> reachableHoles = getReachableHoles(new HashMap<>(), new Path(new ArrayList<>()), ball, shots);

            System.out.println(ball +"  "+reachableHoles);
        });


        System.out.println(Arrays.deepToString(field));
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
    static Map<Point, Set<Path>> getReachableHoles(Map<Point, Set<Path>> closed, Path current, Point ball, int shots) {
        if (shots == 0) {
            return closed;
        }

        getReachablePositions(ball, shots).forEach((direction, nextPos) -> {
            if (field[nextPos.h][nextPos.w] == 'H') {
                insertOrUpdate(closed, nextPos, current.add(direction));
            } else {
                merge(closed, getReachableHoles(closed, current.add(direction), nextPos, shots - 1));
            }
        });

        return closed;
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
        public final Map<Point, Set<Path>> reachableHoles;

        public Ball(int h, int w, int shots, Map<Point, Set<Path>> reachableHoles) {
            super(h, w);
            this.shots = shots;
            this.reachableHoles = reachableHoles;
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
}