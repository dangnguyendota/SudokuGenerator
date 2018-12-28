public class ResultUtil {
    private static SudokuBoard answer;
    private static SudokuBoard board;
    private static boolean running;
    static void start(){
        board = null;
        running = true;
    }

    static void stop(){
        running = false;
    }

    static boolean isRunning(){
        return running;
    }

    static void setAnswer(SudokuBoard b){
        answer = b;
    }

    static void set(SudokuBoard b){
        board = b;
    }

    static SudokuBoard getBoard(){
        if(running){
            System.out.println("Puzzle generator is running, please wait a minute.");
            return null;
        }
        return board;
    }
    static SudokuBoard getAnswer(){
        return answer;
    }
}
