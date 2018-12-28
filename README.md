# Sudoku Generator

#####Sudoku engine, generate and solve puzzle with difference levels.
#####Created by _Nguyen Dang Nguyen_

- Sử dụng hàm:
     
      Engine.start(SudokuLevel level, int nOfLessons, String link)

- **level**: 5 levels ứng với
    - **SudokuLevel**._VERYEASY_
    - **SudokuLevel**._EASY_
    - **SudokuLevel**._NORMAL_
    - **SudokuLevel**._HARD_
    - **SudokuLevel**._VERYHARD_
- **_nOfLessons_**: Là số bài muốn tạo.
- **_link_**: là đường dẫn tới file chứa kết quả.
- _`Ví dụ`_ :
    
    
    Engine.start(SudokuLevel.VERYEASY, 200, "C:\\VeryEasy.txt");