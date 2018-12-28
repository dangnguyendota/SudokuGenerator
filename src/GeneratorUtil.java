import java.util.Random;

public class GeneratorUtil {

    public void genBoard(SudokuLevel level){
        ResultUtil.start();
        int min = 0;
        switch (level){
            case VERYEASY:
                Engine.ENABLE_BASIC_REDUCTION = true;
                Engine.ENABLE_ADVANCE_REDUCTION = false;
                Engine.ENABLE_PROFESSIONAL_REDUCTION = false;
                min = 55;
                break;
            case EASY:
                Engine.ENABLE_BASIC_REDUCTION = true;
                Engine.ENABLE_ADVANCE_REDUCTION = false;
                Engine.ENABLE_PROFESSIONAL_REDUCTION = false;
                min = 45;
                break;
            case NORMAL:
                Engine.ENABLE_BASIC_REDUCTION = true;
                Engine.ENABLE_ADVANCE_REDUCTION = true;
                Engine.ENABLE_PROFESSIONAL_REDUCTION = false;
                min = 35;
                break;
            case HARD:
                Engine.ENABLE_BASIC_REDUCTION = true;
                Engine.ENABLE_ADVANCE_REDUCTION = true;
                Engine.ENABLE_PROFESSIONAL_REDUCTION = false;
                min = 25;
                break;
            case VERYHARD:
                Engine.ENABLE_BASIC_REDUCTION = true;
                Engine.ENABLE_ADVANCE_REDUCTION = true;
                Engine.ENABLE_PROFESSIONAL_REDUCTION = true;
                break;
        }
        FirstNDNGen firstNDNGen = new FirstNDNGen();
        genBoard(firstNDNGen.randomFullBoard(), min);
        ResultUtil.stop();
    }

    private void genBoard(SudokuBoard board, int minNumbers){
        ResultUtil.setAnswer(board.clone());
        Random rd = new Random();
        Loop: while (true){
            boolean[][] visited = new boolean[9][9];
            int count = 0;
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    if(board.empty(i, j)){
                        visited[i][j] = true;
                    }else{
                        count++;
                    }
                }
            }

            if(count <= minNumbers) break;

            while (true){
                if(count == 0){
                    break Loop;
                }
                int row = rd.nextInt(9);
                int col = rd.nextInt(9);
                while (visited[row][col]){
                    row = rd.nextInt(9);
                    col = rd.nextInt(9);
                }
                visited[row][col] = true;
                count--;
                int Tmp = board.reset(row, col);
                PseudoBoard pseudoBoard = new PseudoBoard(board.board);
                pseudoBoard.solve();
                if(pseudoBoard.solved()){
                    break;
                }else{
                    board.set(row, col, Tmp);
                }
            }

        }
        ResultUtil.set(board);
    }

}
