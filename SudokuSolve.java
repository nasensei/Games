public class SudokuSolve {
	
    public static boolean isSolvable(Integer[][] board) {

        //check format
        //check if null
        if (board == null || board.length == 0) {
            return false;
        }

        //check if not a square
        int numCol = board[0].length;
        int numRow = board.length;
        if (numCol != numRow) {
            return false;
        }

        //check if not divisible by 3
        if (numCol % 3 != 0) {
            return false;
        }

        //check existing numbers, have to be in between 1 and 9 or null and the gameplay
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                Integer existNum = board[i][j];
                if (existNum != null) {
                    if (existNum < 1 || existNum > 9) {
                        return false;
                    }
                    if (!gamePlay(board, i, j, existNum)) {
                        return false;
                    }
                }
            }
        }

        return solver(board);
    }
    
    private static boolean gamePlay(Integer[][] board, int row, int col, Integer number) {
        Integer old = board[row][col];
        board[row][col] = null;
        int size = board.length;
        if (number == null) {
            board[row][col] = old;
            return true;
        }

        //checking for same number within the same row
        for (int i = 0; i < size; i++) {
            if (number == board[row][i]) {
                board[row][col] = old;
                return false;
            }
        }

        //checking for same number within the same column
        for (int i = 0; i < size; i++) {
            if (number == board[i][col]) {
                board[row][col] = old;
                return false;
            }
        }

        //checking for 3 x 3 grid
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == number) {
                    board[row][col] = old;
                    return false;
                }
            }
        }

        //passed all rules
        board[row][col] = number;
        return true;
    }

    //find any empty value in the board to fill out
    private static int[] findNull(Integer[][] board) {
        int size = board.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }
	

    //solve sudoku
    private static boolean solver(Integer[][] board) {
        int size = board.length;

        //the board is filled
        if (findNull(board) == null) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
            return true;
        }

        //board is not filled, using backtracking
        int[] pos = findNull(board);
        int rowNeedNum = pos[0];
        int colNeedNum = pos[1];

        for (int val = 1; val <= 9; val++) {
            if (gamePlay(board, rowNeedNum, colNeedNum, val)) {
                board[rowNeedNum][colNeedNum] = val;   // try
                if (solver(board)) {
                    return true;
                }
                board[rowNeedNum][colNeedNum] = null;  // backtrack
            }
        }
        return false;
    }


    
	public static void main(String[] args) {
		//Write some tests if you want check here or change b
        Integer[][] b = {
            {5, 3, null, null, 7, null, null, null, null},
            {6, null, null, 1, 9, 5, null, null, null},
            {null, 9, 8, null, null, null, null, 6, null},

            {8, null, null, null, 6, null, null, null, 3},
            {4, null, null, 8, null, 3, null, null, 1},
            {7, null, null, null, 2, null, null, null, 6},

            {null, 6, null, null, null, null, 2, 8, null},
            {null, null, null, 4, 1, 9, null, null, 5},
            {null, null, null, null, 8, null, null, 7, 9}
        };
		if(isSolvable(b)) {
			System.out.println("Yes this is solvable!");
		}
	}
	
}