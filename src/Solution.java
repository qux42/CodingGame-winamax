import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {
    private static int width;
    private static int height;
    static double cnt = 0;
    static long ms1 = 0;

    public static void main(String args[]) {
        Map<Point, Integer> ballsPos = new HashMap<>();
//        String input = "40 10\n" +
//                "5.....X..3...H................HX.....4XH\n" +
//                "......X....XXXXX..............XX..2..HXX\n" +
//                "......4H........X..4H.H...3..H....4.....\n" +
//                ".HH.........H5XX.....H................5.\n" +
//                "X............XXXX....X.244.2.X..H.5.....\n" +
//                "X.H..........XXXX.......44...X.........5\n" +
//                "..............XX4.......3...H.........3.\n" +
//                "...3......3..X........X....H.H..........\n" +
//                ".......HH.....XXXXX.H.X.......XX....H.XX\n" +
//                "3........5....H.H.....X.......HX......XH\n";
String input = "2 1\n" +
        "1H";
        Scanner in = new Scanner(input);
//        Scanner in = new Scanner(System.in);
//
        width = in.nextInt();
        height = in.nextInt();

        char[] field = new char[height * width];

        for (int h = 0; h < height; h++) {
            char[] row = in.next().toCharArray();

            for (int w = 0; w < row.length; w++) {
                char c = row[w];

                if (c == 'H' || c == '.' || c == 'X') {
                    field[h * width + w] = c;
                } else {
                    ballsPos.put(new Point(h, w), Integer.parseInt("" + c));
                    field[h * width + w] = c;
                }
            }
        }

//        System.out.println(height);
//        System.out.println(width);
//        System.out.println(Arrays.toString(field));
//        System.out.println(ballsPos);

        List<Ball> balls = ballsPos.entrySet().stream().map((ball_shot) -> {
//            final Tree reachableHoles = getReachableHoles(ball_shot.getKey(), ball_shot.getValue(),);
            return new Ball(ball_shot.getKey().h, ball_shot.getKey().w, ball_shot.getValue(), false);
        }).collect(Collectors.toList());

//        System.out.println("Balls: " + balls);
//        System.out.println(Arrays.deepToString(field));


//        System.out.println(move);
        long b = System.currentTimeMillis();
        Set<Move> moves = doMagic(Collections.singleton(new Move(field, balls)), field);
        ms1 += System.currentTimeMillis() - b;
        cnt += 1;
        System.out.println(moves.toArray()[0]);
//        System.err.println(ms1 / cnt);
//        for (int i = 0; i < 1000; i++) {
//            b = System.currentTimeMillis();

//            doMagic(Collections.singleton(new Move(field, balls)), field);
//            ms1 += System.currentTimeMillis() - b;
//            cnt += 1;
//            System.err.println(ms1 / cnt);

//        }
    }


    private static Set<Move> doMagic(Set<Move> moves, char[] field) {
        Set<Move> s = new HashSet<>();


        if (moves.size() <= 1) {

            int size = moves.stream().flatMap(m -> m.balls.stream()).filter(b -> !b.finished).collect(Collectors.toList()).size();
//            System.out.println(size);
            if (size == 0) {
                return moves;
            }
        }
        for (Move m : moves) {
            if (!m.balls.isEmpty()) {

                if (m.balls.stream().filter(b -> b.shots != 0).collect(Collectors.toList()).size() == 0) {
                    System.err.println(moves);
                    s.addAll(moves);
                    return s;
                }
                char[] fieldCopy = deepCopy(m.field);

                Set<Move> move1 = move(m.balls.get(0), m.balls.subList(1, m.balls.size()), fieldCopy, field);
                s.addAll(move1);
            }

        }
        return doMagic(s, field);

    }


    private static Set<Move> move(Ball ball, List<Ball> restBalls, char[] field, char[] field2) {
        final Map<Ball, char[]> m = ball.move(field, field2);
        Set<Move> move = new HashSet<>();
        m.forEach((nextBall, f) -> {
            if (restBalls.isEmpty()) {
                move.add(new Move(f, Stream.of(nextBall).collect(Collectors.toList())
                ));
            } else {
                Set<Move> move1 = move(restBalls.get(0), restBalls.subList(1, restBalls.size()), f, field2);
                move1.forEach(t -> t.balls.add(nextBall));
                move.addAll(move1);
            }
        });


        return move;
    }

    static class Move {
        char[] field;
        List<Ball> balls;

        Move(char[] field, List<Ball> balls) {
            this.field = field;
            this.balls = balls;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            for (int h = 0; h < height; h++) {
                int r = h * width;
                for (int w = 0; w < width; w++) {
//            System.err.println(h +" " + w);
                    char c = field[r + w];
                    if (c != '<' && c != '>' && c != '^' && c != 'v') c = '.';
                    s.append(c);
                }
                s.append("\n");
            }
            return s.toString();
        }
    }


    static class Ball extends Point {
        final int shots;
        final boolean finished;
        Tree cache = null;

        Ball(int h, int w, int shots, boolean finished) {
            super(h, w);

            this.shots = shots;
            this.finished = finished;
        }

        Ball(int h, int w, int shots, char[] field) {
            this(h, w, shots, field[h * width + w] == 'H');
        }

        Tree getReachableHoles(char[] field) {
            if(cache != null){
                return cache;
            }
            Map<DIRECTION, Tree> branches = new HashMap<>();
            getReachablePositions(field).forEach((direction, nextPos) -> {
                if (nextPos.finished) {
                    branches.put(direction, new Tree(Collections.emptyMap()));
                } else if (shots > 1) {
                    branches.put(direction, nextPos.getReachableHoles(field));
                }
            });
            cache = new Tree(branches);
            return cache;
        }

        private Map<DIRECTION, Ball> getReachablePositions(char[] field) {
            Map<DIRECTION, Ball> map = new HashMap<>();
            if (w - shots >= 0 && isAllowedShot(DIRECTION.LEFT, field)) {
                map.put(DIRECTION.LEFT, new Ball(h, w - shots, shots - 1, field));
            }
            if (w + shots < width && isAllowedShot(DIRECTION.RIGHT, field)) {
                map.put(DIRECTION.RIGHT, new Ball(h, w + shots, shots - 1, field));
            }
            if (h + shots < height && isAllowedShot(DIRECTION.DOWN, field)) {
                map.put(DIRECTION.DOWN, new Ball(h + shots, w, shots - 1, field));
            }
            if (h - shots >= 0 && isAllowedShot(DIRECTION.UP, field)) {
                map.put(DIRECTION.UP, new Ball(h - shots, w, shots - 1, field));
            }
            return map;
        }

        private boolean isAllowedShot(DIRECTION direction, char[] field) {
            return isAllowedShot(this, direction, shots, field);
        }

        private boolean isAllowedShot(Point ball, DIRECTION direction, int shots, char[] field) {
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
            int valueOnNextPos = field[nextPos.h * width + nextPos.w];
            if (shots > 1) {
                return ((valueOnNextPos == '.') || (valueOnNextPos == 'X')) && isAllowedShot(nextPos, direction, shots - 1, field);
            }
            return valueOnNextPos == 'H' || valueOnNextPos == '.';
        }

        Map<Ball, char[]> move(char[] field, char[] field2) {
            if (finished) {
                return Collections.singletonMap(this, field);
            }


            Map<Ball, char[]> map = new HashMap<>();

            getReachableHoles(field2).childs.forEach((direction, children) -> {
                Point nextPos = null;
                char[] fieldCopy = deepCopy(field);
                boolean isAllowed = isAllowedShot(this, direction, shots, field);
                if (!isAllowed) {
                    return;
                }
                switch (direction) {
                    case LEFT:
                        nextPos = new Point(h, w - shots);
                        for (int i = 0; i < shots; i++) {
                            fieldCopy[h * width + (w - i)] = direction.value;
                        }
                        break;
                    case UP:
                        nextPos = new Point(h - shots, w);
                        for (int i = 0; i < shots; i++) {
                            fieldCopy[(h - i) * width + w] = direction.value;
                        }
                        break;
                    case RIGHT:
                        nextPos = new Point(h, w + shots);
                        for (int i = 0; i < shots; i++) {
                            fieldCopy[h * width + (w + i)] = direction.value;
                        }
                        break;
                    case DOWN:
                        nextPos = new Point(h + shots, w);
                        for (int i = 0; i < shots; i++) {
                            fieldCopy[(h + i) * width + w] = direction.value;
                        }
                        break;
                }
                boolean finished = fieldCopy[nextPos.h * width + nextPos.w] == 'H';
                fieldCopy[nextPos.h * width + nextPos.w] = Character.forDigit(shots - 1, 10);
                map.put(new Ball(nextPos.h, nextPos.w, shots - 1, finished), fieldCopy);

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

    static char[] deepCopy(char[] field) {
        return field.clone();
    }

    enum DIRECTION {
        RIGHT('>'),
        LEFT('<'),
        UP('^'),
        DOWN('v');

        private final char value;

        DIRECTION(char c) {
            this.value = c;
        }
    }

}