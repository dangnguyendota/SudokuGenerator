public class GenHelper {

    public static int[] start_i = {0, 0, 0, 3, 3, 3, 6, 6, 6};
    public static int[] start_j = {0, 3, 6, 0, 3, 6, 0, 3, 6};
    public static int[][] nIndex = new int[9][9];
    static {
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                int[] neighbor = neighborPos(i, j);
                nIndex[i][j] = neighbor[0] * 3 + neighbor[1];
            }
        }
    }

    public static int[][][][] neighborhoob = new int[9][9][9][2];
    static {
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                int count = 0;
                for(int r = start_i[nIndex[i][j]]; r < start_i[nIndex[i][j]] + 3; r++){
                    for(int c = start_j[nIndex[i][j]]; c < start_j[nIndex[i][j]] + 3; c++){
                        neighborhoob[i][j][count] = new int[]{r, c};
                        count++;
                    }
                }
            }
        }
    }

    public static int[] neighborPos(int x, int y){
        return new int[]{x / 3, y / 3};
    }

    public static int neighborIndex(int x, int y){
        return nIndex[x][y];
    }

    public static boolean checkValidBoard(int[][] board){
        boolean[][] neighbors = new boolean[9][9];
        boolean[][] rows      = new boolean[9][9];
        boolean[][] columns   = new boolean[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(board[i][j] == 0) return false;
                int neighbor = GenHelper.neighborIndex(i, j);
                int value = board[i][j] - 1;
                if(neighbors[neighbor][value] || rows[i][value] || columns[j][value]) return false;
                neighbors[neighbor][value] = true;
                rows[i][value] = true;
                columns[j][value] = true;
            }
        }

        return true;
    }
}
