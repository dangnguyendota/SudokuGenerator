import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.Random;

public class SudokuBoard {
    int[][] board;

    public SudokuBoard(int[][] board){
        //random board
        if(board.length != 9 || board[0].length != 9) throw new ValueException("Sai kích thước bàn sudoku.");
        this.board = board;
    }

    public SudokuBoard(PseudoBoard pseudoBoard){
        this.board = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(pseudoBoard.done(i, j)){
                    board[i][j] = pseudoBoard.get(i, j, 0) + 1;
                }
            }
        }
    }

    public boolean empty(int row, int column){
        return board[row][column] == 0;
    }

    public int reset(int row, int column){
        int tmp = board[row][column];
        board[row][column] = 0;
        return tmp;
    }

    public void set(int row, int column, int value){
        board[row][column] = value;
    }

    public SudokuBoard clone(){
        int[][] copy = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                copy[i][j] = board[i][j];
            }
        }
        return new SudokuBoard(copy);
    }

    public String encode(){
        StringBuilder st = new StringBuilder();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                st.append(Integer.toString(board[i][j]));
            }
        }
        return st.toString();
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 9; i++){
            if(i % 3 != 0) s.append("|           |           |           |\r\n");
            else s.append("+-----------------------------------+\r\n");
            s.append("| ");
            for(int j = 0; j < 9; j++){
                if(board[i][j] != 0) s.append(board[i][j]);
                else s.append(".");
                if(j % 3 != 2) s.append("   ");
                else s.append(" | ");
            }
            s.append("\r\n");
        }
        s.append("+-----------------------------------+\r\n");
        return s.toString();
    }

}
