import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.Arrays;

public class PseudoBoard {
    private boolean foundReduction = false;
    private int[][][] pseudo = new int[9][9][];
    private static int[][] boxIndex = new int[9][9];

    static {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boxIndex[i][j] = ((int) (i / 3)) * 3 + (int) (j / 3);
            }
        }
    }

    private static int[] root = new int[9];

    static {
        for (int i = 0; i < 9; i++) {
            root[i] = i - i % 3;
        }
    }

    PseudoBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    pseudo[i][j] = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
                } else {
                    pseudo[i][j] = new int[]{board[i][j] - 1};
                }
            }
        }
    }

    public void solve(){
        foundReduction = true;
        int loop = 0;
        while (foundReduction) {
            foundReduction = false;
            easyReduction();
            if(Engine.ENABLE_BASIC_REDUCTION) basicReduction();
            if(Engine.ENABLE_ADVANCE_REDUCTION) advanceReduction();
            if(Engine.ENABLE_PROFESSIONAL_REDUCTION) professionalReduction();
            find();
            loop++;
        }
//        if(solved()) System.out.println("PUZZLE IS SOLVED IN " + loop + " STEPS ! ^^");
    }

    boolean solved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!done(i, j)) return false;
            }
        }
        return true;
    }

    boolean done(int row, int column) {
        return pseudo[row][column].length == 1;
    }

    int get(int row, int column, int index) {
        return pseudo[row][column][index];
    }

    private int[] get(int row, int column) {
        return pseudo[row][column];
    }

    private int length(int row, int column) {
        return pseudo[row][column].length;
    }

    private boolean equals(int r1, int c1, int r2, int c2) {
        if (length(r1, c1) != length(r2, c2)) return false;
        for (int index = 0, len = length(r1, c1); index < len; index++) {
            if (get(r1, c1, index) != get(r2, c2, index)) return false;
        }
        return true;
    }

    private boolean contain(int row, int column, int value) {
        for (int p : pseudo[row][column]) {
            if (p == value) return true;
        }
        return false;
    }

    private boolean isPartOf(int rootRow, int rooCol, int childRow, int childCol) {
        if (length(childRow, childCol) > length(rootRow, rooCol)) return false;
        for (int c : pseudo[childRow][childCol]) {
            if (!contain(rootRow, rooCol, c)) return false;
        }
        return true;
    }

    private void change(int row, int column, ArrayList<Integer> buffer) {
        pseudo[row][column] = new int[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            pseudo[row][column][i] = buffer.get(i);
        }
    }

    private void deleteExisted(int row, int column, boolean[] existed) {
        if (this.done(row, column)) return;
        ArrayList<Integer> buffer = new ArrayList<>();
        for (int p : pseudo[row][column]) {
            if (existed[p]) {
                foundReduction = true;
                continue;
            }
            buffer.add(p);
        }
        this.change(row, column, buffer);
    }

    private void delete(int row, int column, int value) {
        boolean found = false;
        int[] buffer = new int[pseudo[row][column].length - 1];
        int count = 0;
        for (int i = 0; i < pseudo[row][column].length; i++) {
            if (pseudo[row][column][i] == value) {
                found = true;
                continue;
            }
            buffer[count] = pseudo[row][column][i];
            count++;
        }
        if (!found) throw new ValueException("Could not find value in pseudo board to delete.");
        pseudo[row][column] = buffer;
    }

    private void easyReduction() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (!done(row, column)) continue;
                boolean[] existed = new boolean[9];
                existed[get(row, column, 0)] = true;
                for (int c = 0; c < 9; c++) {
                    if (c == column) continue;
                    deleteExisted(row, c, existed);
                }
                for (int r = 0; r < 9; r++) {
                    if (r == row) continue;
                    deleteExisted(r, column, existed);
                }
                for (int r = root[row]; r < root[row] + 3; r++) {
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        if (r == row && c == column) continue;
                        deleteExisted(r, c, existed);
                    }
                }
            }
        }
    }

    private void basicReduction() {
        //row
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column += 3) {
                //Lưu các số có thể có ở các ô cùng hàng khác hộp.
                boolean[] existed = new boolean[9];
                Arrays.fill(existed, true);

                for (int c = 0; c < 9; c++) {
                    if (boxIndex[row][column] == boxIndex[row][c]) continue;
                    for (int p : pseudo[row][c]) existed[p] = false;
                }

                //Duyệt qua các ô cùng hộp khác hàng.
                for (int r = root[row]; r < root[row] + 3; r++) {
                    if (r == row) continue;
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        deleteExisted(r, c, existed);
                    }
                }
                //Lưu các số có thể có ở các ô cùng hộp khác hàng
                Arrays.fill(existed, true);

                for (int r = root[row]; r < root[row] + 3; r++) {
                    if (r == row) continue;
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        for (int p : pseudo[r][c]) existed[p] = false;
                    }
                }

                //Duyệt qua các ô cùng hàng khác hộp.
                for (int c = 0; c < 9; c++) {
                    if (boxIndex[row][column] == boxIndex[row][c]) continue;
                    deleteExisted(row, c, existed);
                }
            }
        }

        for (int row = 0; row < 9; row += 3) {
            for (int column = 0; column < 9; column++) {
                //Lưu các số có thể có ở các ô cùng cột khác hộp.
                boolean[] colExisted = new boolean[9];
                Arrays.fill(colExisted, true);

                for (int r = 0; r < 9; r++) {
                    if (boxIndex[row][column] == boxIndex[r][column]) continue;
                    for (int p : pseudo[r][column]) colExisted[p] = false;
                }

                //Duyệt qua các ô cùng hộp khác cột.
                for (int r = root[row]; r < root[row] + 3; r++) {
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        if (c == column) continue;
                        deleteExisted(r, c, colExisted);
                    }
                }
                //Lưu các số có thể có ở các ô cùng hộp khác cột
                boolean[] boxExisted = new boolean[9];
                Arrays.fill(boxExisted, true);

                for (int r = root[row]; r < root[row] + 3; r++) {
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        if (c == column) continue;
                        for (int p : pseudo[r][c]) boxExisted[p] = false;
                    }
                }

                //Duyệt qua các ô cùng cột khác hộp.
                for (int r = 0; r < 9; r++) {
                    if (boxIndex[row][column] == boxIndex[r][column]) continue;
                    deleteExisted(r, column, boxExisted);
                }
            }
        }
    }

    private void advanceReduction() {
        int count;
        boolean[] existed = new boolean[9];
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column += 3) {
                //row with box
                Arrays.fill(existed, true);
                count = 0;
                for (int c = 0; c < 9; c++) {
                    if (boxIndex[row][column] == boxIndex[row][c]) continue;
                    if (done(row, c)) {
                        existed[pseudo[row][c][0]] = false;
                        count++;
                    }
                }

                ArrayList<int[]> notDone = new ArrayList<>();
                for (int r = root[row]; r < root[row] + 3; r++) {
                    if (r == row) continue;
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        if (done(r, c) && !existed[pseudo[r][c][0]]) {
                            existed[pseudo[r][c][0]] = true;
                            count--;
                        }
                        if (!done(r, c)) notDone.add(new int[]{r, c});
                    }
                }

                if (count == notDone.size() && count > 0) {
                    for (int[] position : notDone) {
                        deleteExisted(position[0], position[1], existed);
                    }
                }

                //box with row
                Arrays.fill(existed, true);
                count = 0;
                for (int r = root[row]; r < root[row] + 3; r++) {
                    if (r == row) continue;
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        if (done(r, c)) {
                            existed[pseudo[r][c][0]] = false;
                            count++;
                        }
                    }
                }

                notDone = new ArrayList<>();
                for (int c = 0; c < 9; c++) {
                    if (boxIndex[row][column] == boxIndex[row][c]) continue;
                    if (done(row, c) && !existed[pseudo[row][c][0]]) {
                        existed[pseudo[row][c][0]] = true;
                        count--;
                    }
                    if (!done(row, c)) notDone.add(new int[]{row, c});
                }

                if (count == notDone.size() && column > 0) {
                    for (int[] position : notDone) {
                        deleteExisted(position[0], position[1], existed);
                    }
                }
            }
        }

        for (int row = 0; row < 9; row += 3) {
            for (int column = 0; column < 9; column++) {
                //row with box
                Arrays.fill(existed, true);
                count = 0;
                for (int r = 0; r < 9; r++) {
                    if (boxIndex[row][column] == boxIndex[r][column]) continue;
                    if (done(r, column)) {
                        existed[pseudo[r][column][0]] = false;
                        count++;
                    }
                }

                ArrayList<int[]> notDone = new ArrayList<>();
                for (int c = root[column]; c < root[column] + 3; c++) {
                    if (c == column) continue;
                    for (int r = root[row]; r < root[row] + 3; r++) {
                        if (done(r, c) && !existed[pseudo[r][c][0]]) {
                            existed[pseudo[r][c][0]] = true;
                            count--;
                        }
                        if (!done(r, c)) notDone.add(new int[]{r, c});
                    }
                }

                if (count == notDone.size() && count > 0) {
                    for (int[] position : notDone) {
                        deleteExisted(position[0], position[1], existed);
                    }
                }

                //box with row
                Arrays.fill(existed, true);
                count = 0;
                for (int c = root[column]; c < root[column] + 3; c++) {
                    if (c == column) continue;
                    for (int r = root[row]; r < root[row] + 3; r++) {
                        if (done(r, c)) {
                            existed[pseudo[r][c][0]] = false;
                            count++;
                        }
                    }
                }

                notDone = new ArrayList<>();
                for (int r = 0; r < 9; r++) {
                    if (boxIndex[row][column] == boxIndex[r][column]) continue;
                    if (done(r, column) && !existed[pseudo[r][column][0]]) {
                        existed[pseudo[r][column][0]] = true;
                        count--;
                    }
                    if (!done(r, column)) notDone.add(new int[]{r, column});
                }

                if (count == notDone.size() && count > 0) {
                    for (int[] position : notDone) {
                        deleteExisted(position[0], position[1], existed);
                    }
                }
            }
        }
    }

    private void professionalReduction() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (done(row, column)) continue;

                boolean[] equal = new boolean[9];
                boolean[] existed = new boolean[9];
                for (int p : pseudo[row][column]) existed[p] = true;
                int count = 1;
                //row
                for (int c = 0; c < 9; c++) {
                    if (c == column || done(row, c)) continue;
                    if (isPartOf(row, column, row, c)) {
                        equal[c] = true;
                        count++;
                    }
                }
                if (count == length(row, column)) {
                    for (int c = 0; c < 9; c++) {
                        if (c == column || equal[c] || done(row, c)) continue;
                        deleteExisted(row, c, existed);
                    }
                }
                //col
                Arrays.fill(equal, false);
                count = 1;
                for (int r = 0; r < 9; r++) {
                    if (r == row || done(r, column)) continue;
                    if (isPartOf(row, column, r, column)) {
                        equal[r] = true;
                        count++;
                    }
                }
                if (count == length(row, column)) {
                    for (int r = 0; r < 9; r++) {
                        if (r == row || equal[r] || done(r, column)) continue;
                        deleteExisted(r, column, existed);
                    }
                }
//                box
                Arrays.fill(equal, false);
                count = 1;
                int index = -1;
                for (int r = root[row]; r < root[row] + 3; r++) {
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        index++;
                        if (r == row && c == column) continue;
                        if (done(r, c)) continue;
                        if (isPartOf(row, column, r, c)) {
                            equal[index] = true;
                            count++;
                        }
                    }
                }
                index = -1;
                if (count == length(row, column)) {
                    for (int r = root[row]; r < root[row] + 3; r++) {
                        for (int c = root[column]; c < root[column]; c++) {
                            index++;
                            if (r == row && c == column) continue;
                            if (done(r, c) || equal[index]) continue;
                            deleteExisted(r, c, existed);
                        }
                    }
                }
            }
        }
    }

    private void find() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                boolean[] existed = new boolean[9];
                int count = 9;
                //row
                for (int r = 0; r < 9; r++) {
                    if (r == row) continue;
                    for (int p : pseudo[r][column]) {
                        if (!existed[p]) {
                            count--;
                            existed[p] = true;
                        }
                    }
                }
                if (count > 1) {
                    throw new ValueException("1 ô không thể có 2 giá trị. Hàm find trong PseudoBoard.<row>");
                }
                if (count == 1) {
                    deleteExisted(row, column, existed);
                    continue;
                }
                //column
                Arrays.fill(existed, false);
                count = 9;
                for (int c = 0; c < 9; c++) {
                    if (c == column) continue;
                    for (int p : pseudo[row][c]) {
                        if (!existed[p]) {
                            count--;
                            existed[p] = true;
                        }
                    }
                }
                if (count > 1)
                    throw new ValueException("1 ô không thể có 2 giá trị. Hàm find trong PseudoBoard.<column>");
                if (count == 1) {
                    deleteExisted(row, column, existed);
                    continue;
                }

                //box
                Arrays.fill(existed, false);
                count = 9;
                for (int r = root[row]; r < root[row] + 3; r++) {
                    for (int c = root[column]; c < root[column] + 3; c++) {
                        if (r == row && c == column) continue;
                        for (int p : pseudo[r][c]) {
                            if (!existed[p]) {
                                count--;
                                existed[p] = true;
                            }
                        }
                    }
                }
                if (count > 1) throw new ValueException("1 ô không thể có 2 giá trị. Hàm find trong PseudoBoard.<box>");
                if (count == 1) {
                    deleteExisted(row, column, existed);
                }
            }
        }
    }

    public String toString() {
        int max = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                max = Math.max(max, pseudo[i][j].length);
            }
        }

        StringBuilder st = new StringBuilder();
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < max; j++) {
                line.append("--");
            }
            line.append("--");
        }
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) st.append(line);
            st.append("\n");
            st.append("| ");
            for (int j = 0; j < 9; j++) {
                for (int t = 0; t < max; t++) {
                    if (t < pseudo[i][j].length) st.append(pseudo[i][j][t] + 1).append(" ");
                    else st.append("  ");
                }
                if (j % 3 == 2) st.append("| ");
                else st.append(". ");
            }
            st.append("\n");
        }
        st.append(line).append("\n");
        return st.toString();
    }
}
