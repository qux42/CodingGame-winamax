import java.util.*;
import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

//    static Scanner in;
//    static int width = 3;
//    static int height = 3;
//    static char[][] field = {{'2', '.', 'X'}, {'X', '.', 'H'}, {'.', 'H', '1'}};


    private static int width = 6;
    private static int height = 6;
    private static char[][] field = {{'3', '.', '.', 'H', '.', '2'}, {'.', '2', '.', '.', 'H', '.'}, {'.', '.', 'H', '.', '.', 'H'}, {'.', 'X', '.', '2', '.', 'X'}, {'.', '.', '.', '.', '.', '.'}, {'3', '.', '.', 'H', '.', '.'}};


    //    static int width; // = 3;
//    static int height; // = 3;
//    static char[][] field;// = {{'2', '.', 'X'}, {'X', '.', 'H'}, {'.', 'H', '1'}};
    private static Map<Point, Integer> ballsPos = new HashMap<>();

    static {
//        ballsPos.put(new Point(0, 0), 2);
//        ballsPos.put(new Point(2, 2), 1);

        ballsPos.put(new Point(5, 0), 3);
        ballsPos.put(new Point(0, 0), 3);
        ballsPos.put(new Point(1, 1), 2);
        ballsPos.put(new Point(0, 5), 2);
        ballsPos.put(new Point(3, 3), 2);


    }

    public static void main(String args[]) {
//        in = new Scanner(System.in);
//
//        width = in.nextInt();
//        height = in.nextInt();
//
//        field = new char[height][width];
//
////        for (int i = 0; i < height; i++) {
////            field[i] = in.next().toCharArray();
////        }
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
//                    ballsPos.put(new Point(h, w), Integer.parseInt("" + c));
//                    field[h][w] = c;
//                }
//            }
//        }

//        System.out.println(height);
//        System.out.println(width);
        System.out.println(Arrays.deepToString(field));
//        System.out.println(ballsPos);

        List<Ball> balls = ballsPos.entrySet().stream().map((ball_shot) -> {
//            final Tree reachableHoles = getReachableHoles(ball_shot.getKey(), ball_shot.getValue(),);
            return new Ball(ball_shot.getKey().h, ball_shot.getKey().w, ball_shot.getValue());
        }).collect(Collectors.toList());

        System.out.println("Balls: " + balls);
//        System.out.println(Arrays.deepToString(field));
        Set<Move> move = move(balls.get(0), balls.subList(1, balls.size()), field);
//        System.out.println(move);
        Set<Move> moves = doMagic(move);
        System.out.println(moves.toArray()[0]);
    }


    private static Set<Move> doMagic(Set<Move> moves) {
        Set<Move> s = new HashSet<>();
        if (moves.size() <= 1) {
            return moves;
        }
        for (Move m : moves) {
            if (!m.balls.isEmpty()) {

                if (m.balls.stream().filter(b -> b.shots != 0).collect(Collectors.toList()).size() == 0) {
                    System.err.println(moves);
                    s.addAll(moves);
                    return s;
                }
                char[][] fieldCopy = deepCopy(m.field);

                Set<Move> move1 = move(m.balls.get(0), m.balls.subList(1, m.balls.size()), fieldCopy);
                s.addAll(move1);
            }

        }
        return doMagic(s);

    }


    private static Set<Move> move(Ball ball, List<Ball> restBalls, char[][] field) {
        final Map<Ball, char[][]> m = ball.move(field);
        Set<Move> move = new HashSet<>();
        m.forEach((nextBall, f) -> {
            if (restBalls.isEmpty()) {
                ArrayList<Ball> objects = new ArrayList<>();
                objects.add(nextBall);
                move.add(new Move(f, objects));
            } else {
                Set<Move> move1 = move(restBalls.get(0), restBalls.subList(1, restBalls.size()), f);
                move1.forEach(t -> t.balls.add(nextBall));
                move.addAll(move1);
            }
        });


        return move;
    }

    static class Move {
        char[][] field;
        List<Ball> balls;

        Move(char[][] field, List<Ball> balls) {
            this.field = field;
            this.balls = balls;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            for (char[] aField : field) {
                for (char anAField : aField) {
                    char c = anAField;
                    if (c != '<' && c != '>' && c != '^' && c != 'v') c = '.';
                    s.append(c);
                }
                s.append("\n");
            }
            return s.toString();
        }
    }

    // TODO: invalide pfade (sich selbst überschneidend) herausfiltern
    // TODO 2: nicht immer alle pfade für einen ball berechnen, sondern weitere pfade nur auf bedarf berechnen


    private static boolean isAllowedShot(Point ball, DIRECTION direction, int shots) {
        Point nextPos = null;
        switch (direction) {
            case LEFT:
                nextPos = new Point(ball.h, ball.w - 1);
                break;
            case UP:
                nextPos = new Point(ball.h - 1, ball.w);
                break;
            case RIGHT:
                nextPos = new Point(ball.h, ball.w + 1);
                break;
            case DOWN:
                nextPos = new Point(ball.h + 1, ball.w);
                break;
        }
        int valueOnNextPos = field[nextPos.h][nextPos.w];
        if (shots > 1) {
            return ((valueOnNextPos == '.') || (valueOnNextPos == 'X')) && isAllowedShot(nextPos, direction, shots - 1);
        }
        return valueOnNextPos == 'H' || valueOnNextPos == '.';
    }


    static class Ball extends Point {
        final int shots;
        final boolean finished;


        Ball(int h, int w, int shots, boolean finished) {
            super(h, w);

            this.shots = shots;
            this.finished = finished;
        }

        Ball(Point p, int shots, boolean finished) {
            this(p.h, p.w, shots, finished);
        }

        Ball(int h, int w, int shots) {
            this(h, w, shots, false);
        }

        Ball(int h, int w, int shots, char[][] field) {
            this(h, w, shots, field[h][w] == 'H');
        }


        Tree getReachableHoles(char[][] field) {
            return getReachableHoles(this, shots, field);
        }

        Tree getReachableHoles(Point ball, int shots, char[][] field) {
            Map<DIRECTION, Tree> branches = new HashMap<>();

            getReachablePositions(ball, shots, field).forEach((direction, nextPos) -> {
                if (nextPos.finished) {
                    branches.put(direction, new Tree(Collections.emptyMap()));
                } else if (shots > 1) {
                    branches.put(direction, getReachableHoles(nextPos, shots - 1, field));
                }
            });

            return new Tree(branches);
        }

        private Map<DIRECTION, Ball> getReachablePositions(Point ball, int shots, char[][] field) {

            Map<DIRECTION, Ball> map = new HashMap<>();

            if (ball.w - shots >= 0 && isAllowedShot(ball, DIRECTION.LEFT, shots)) {

                map.put(DIRECTION.LEFT, new Ball(ball.h, ball.w - shots, shots - 1, field));
            }

            if (ball.w + shots < width && isAllowedShot(ball, DIRECTION.RIGHT, shots)) {
                map.put(DIRECTION.RIGHT, new Ball(ball.h, ball.w + shots, shots - 1, field));
            }

            if (ball.h + shots < height && isAllowedShot(ball, DIRECTION.DOWN, shots)) {
                map.put(DIRECTION.DOWN, new Ball(ball.h + shots, ball.w, shots - 1, field));
            }

            if (ball.h - shots >= 0 && isAllowedShot(ball, DIRECTION.UP, shots)) {
                map.put(DIRECTION.UP, new Ball(ball.h - shots, ball.w, shots - 1, field));
            }
            return map;
        }

        Map<Ball, char[][]> move(char[][] field) {
            Tree reachableHoles = getReachableHoles(field);
            Map<Ball, char[][]> map = new HashMap<>();
            if (finished) {
                map.put(this, field);
                return map;

            }
            reachableHoles.childs.forEach((direction, children) -> {
                Point nextPos = null;
                char[][] fieldCopy = deepCopy(field);
                boolean ok = true;
                char f = '0';
                switch (direction) {
                    case LEFT:
                        nextPos = new Point(h, w - shots);
                        for (int i = 0; i < shots; i++) {
                            char c = fieldCopy[h][w - i];
                            if (c == '.' || c == 'X' || c == 'H' || i == 0) {
                                fieldCopy[h][w - i] = '<';
                            } else {
                                ok = false;
                            }
                            f = fieldCopy[h][w - shots];
                            fieldCopy[h][w - shots] = Character.forDigit(shots - 1, 10);
                        }
                        break;
                    case UP:
                        nextPos = new Point(h - shots, w);
                        for (int i = 0; i < shots; i++) {
                            char c = fieldCopy[h - i][w];
                            if (c == '.' || c == 'X' || c == 'H' || i == 0) {
                                fieldCopy[h - i][w] = '^';
                            } else {
                                ok = false;
                            }
                        }
                        f = fieldCopy[h - shots][w];
                        fieldCopy[h - shots][w] = Character.forDigit(shots - 1, 10);
                        break;
                    case RIGHT:
                        nextPos = new Point(h, w + shots);
                        for (int i = 0; i < shots; i++) {
                            char c = fieldCopy[h][w + i];
                            if (c == '.' || c == 'X' || c == 'H' || i == 0) {
                                fieldCopy[h][w + i] = '>';
                            } else {
                                ok = false;
                            }
                        }
                        f = fieldCopy[h][w + shots];
                        fieldCopy[h][w + shots] = Character.forDigit(shots - 1, 10);
                        break;
                    case DOWN:
                        nextPos = new Point(h + shots, w);
                        for (int i = 0; i < shots; i++) {
                            char c = fieldCopy[h + i][w];
                            if (c == '.' || c == 'X' || c == 'H' || i == 0) {
                                fieldCopy[h + i][w] = 'v';
                            } else {
                                ok = false;
                            }
                        }
                        f = fieldCopy[h + shots][w];
                        fieldCopy[h + shots][w] = Character.forDigit(shots - 1, 10);
                        break;
                }
                if (ok && (f == '.' || f == 'H')) {
                    map.put(new Ball(nextPos.h, nextPos.w, shots - 1, f == 'H'), fieldCopy);
                }
            });

            return map;
        }

        @Override
        public String toString() {
            return "(" + super.toString() + "->" + shots + "->" + finished + ")";
        }
    }

    static class Point {

        @Override
        public String toString() {
            return "(" + h + ", " + w + ')';
        }

        public final int h;
        public final int w;

        Point(int h, int w) {
            this.w = w;
            this.h = h;
        }
    }


    static class Tree {
        private final Map<DIRECTION, Tree> childs;

        Tree(Map<DIRECTION, Tree> childs) {
            this.childs = childs;
        }

        @Override
        public String toString() {
            return "Tree{" + "childs=" + childs +
                    '}';
        }
    }

    static char[][] deepCopy(char[][] field) {
        return Arrays.stream(field).map(char[]::clone).toArray($ -> field.clone());
    }

    enum DIRECTION {
        RIGHT('>'),
        LEFT('<'),
        UP('^'),
        DOWN('v');

        private final char c;

//        public static DIRECTION fromString(char c) {
//            for (DIRECTION b : DIRECTION.values()) {
//                if (b.c == c) {
//                    return b;
//                }
//            }
//            throw new RuntimeException("cannot cast Direction");
//        }

        DIRECTION(char c) {
            this.c = c;
        }
    }

}