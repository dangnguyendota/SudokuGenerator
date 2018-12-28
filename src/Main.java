import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        //Ví dụ về tạo puzzle sudoku.
        try {
            Engine.start(SudokuLevel.VERYEASY, 20, "C:\\Users\\EmChaoAnhNguyenA\\Desktop\\SudokuLessons\\VeryEasy.txt");
            Engine.start(SudokuLevel.EASY, 20, "C:\\Users\\EmChaoAnhNguyenA\\Desktop\\SudokuLessons\\Easy.txt");
            Engine.start(SudokuLevel.NORMAL, 20, "C:\\Users\\EmChaoAnhNguyenA\\Desktop\\SudokuLessons\\Normal.txt");
            Engine.start(SudokuLevel.HARD, 20, "C:\\Users\\EmChaoAnhNguyenA\\Desktop\\SudokuLessons\\Hard.txt");
            Engine.start(SudokuLevel.VERYHARD, 20, "C:\\Users\\EmChaoAnhNguyenA\\Desktop\\SudokuLessons\\VeryHard.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
