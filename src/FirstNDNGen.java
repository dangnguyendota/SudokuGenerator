import java.util.Random;

public class FirstNDNGen {


    public SudokuBoard randomFullBoard() {
        boolean[][] column = new boolean[9][9];
        boolean[][] row = new boolean[9][9];
        boolean[][] neighbors = new boolean[9][9];
        int[][] board = new int[9][9];

        /* Random 3 first rows */
        for (int index = 0; index < 3; index++) {
            while (true) {
                /* clone */
                int[][] _board = new int[9][9];
                boolean[][] _neighbors = new boolean[9][9];
                boolean[][] _row = new boolean[9][9];
                boolean[][] _column = new boolean[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        _board[i][j] = board[i][j];
                        _neighbors[i][j] = neighbors[i][j];
                        _row[i][j] = row[i][j];
                        _column[i][j] = column[i][j];
                    }
                }

                /* if solved, it means random completely */
                boolean solved = random3X3(index, _board, _neighbors, _row, _column);
                if (solved) {
                    /* copy clones to real */
                    board = _board;
                    neighbors = _neighbors;
                    row = _row;
                    column = _column;
                    break;
                }
            }
        }

        /* permutation  of 6 rows left*/
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int[][][] permutationUp = new int[3][3][2];
            int[][][] permuationDown = new int[3][3][2];
            if (random.nextInt(2) == 0) {
                permutationUp = new int[][][]{
                        {{0, 1}, {0, 2}, {0, 0}},
                        {{1, 1}, {1, 2}, {1, 0}},
                        {{2, 1}, {2, 2}, {2, 0}},
                };
                permuationDown = new int[][][]{
                        {{0, 2}, {0, 0}, {0, 1}},
                        {{1, 2}, {1, 0}, {1, 1}},
                        {{2, 2}, {2, 0}, {2, 1}}
                };
            } else {
                permuationDown = new int[][][]{
                        {{0, 1}, {0, 2}, {0, 0}},
                        {{1, 1}, {1, 2}, {1, 0}},
                        {{2, 1}, {2, 2}, {2, 0}},
                };
                permutationUp = new int[][][]{
                        {{0, 2}, {0, 0}, {0, 1}},
                        {{1, 2}, {1, 0}, {1, 1}},
                        {{2, 2}, {2, 0}, {2, 1}}
                };
            }
            for (int col = i * 3; col < i * 3 + 3; col++) {
                for (int ro = 3; ro < 6; ro++) {
                    int[] per = permutationUp[ro - 3][col - i * 3];
                    board[ro][col] = board[per[0]][per[1] + i * 3];
                }
                for (int ro = 6; ro < 9; ro++) {
                    int[] per = permuationDown[ro - 6][col - i * 3];
                    board[ro][col] = board[per[0]][per[1] + i * 3];
                }
            }
        }

        int[][] listRd = {
                {0, 1, 2},
                {0, 2, 1},
                {1, 0, 2},
                {1, 2, 0},
                {2, 0, 1},
                {2, 1, 0}
        };
        int[][] newBoard = new int[9][9];
        for (int i = 0; i < 9; i += 3) {
            int rd = random.nextInt(6);
            for (int j = 0; j < 9; j++) {
                newBoard[i][j] = board[i + listRd[rd][0]][j];
                newBoard[i + 1][j] = board[i + listRd[rd][1]][j];
                newBoard[i + 2][j] = board[i + listRd[rd][2]][j];
            }
        }


        return new SudokuBoard(newBoard);
    }


    private boolean random3X3(int boxIndex, int[][] board, boolean[][] neighbors, boolean[][] row, boolean[][] column) {
        Random random = new Random();
        for (int i = GenHelper.start_i[boxIndex]; i < 3 + GenHelper.start_i[boxIndex]; i++) {
            for (int j = GenHelper.start_j[boxIndex]; j < 3 + GenHelper.start_j[boxIndex]; j++) {
                int neighbor = GenHelper.neighborIndex(i, j);
                boolean valid = false;
                for (int t = 0; t < 9; t++) {
                    if (!neighbors[neighbor][t] && !row[i][t]) {
                        valid = true;
                        break;
                    }
                }

                if (!valid) return false;
                int rd = random.nextInt(9);
                while (neighbors[neighbor][rd] || row[i][rd]) {
                    rd = random.nextInt(9);
                }
                board[i][j] = rd + 1;
                neighbors[neighbor][rd] = true;
                column[j][rd] = true;
                row[i][rd] = true;
            }
        }
        return true;
    }
}
