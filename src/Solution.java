import java.util.Arrays;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();

        int[][] field = new int[height][width];
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
}