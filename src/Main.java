import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
 
public class Main {
    public static int subGridSize;
    public static int boardSize;
 
    public static void main(String[] args) throws FileNotFoundException {
 
        File file = new File("9x9a.txt");
        Scanner scan = new Scanner(file);
        String s = scan.nextLine();
        String[] ints = s.split(" ");
 
        boardSize = Integer.parseInt(ints[0]);
        subGridSize = Integer.parseInt(ints[1]);
        // subGridSize = (subGridSize * 2) + 1;
 
        Node[][] gameBoard = new Node[boardSize][boardSize];
 
        // Read in board
        for (int x = 0; x < gameBoard.length; x++) {
            String curLine = scan.nextLine();
            for (int y = 0; y < gameBoard[x].length; y++) {
                String current = Character.toString(curLine.charAt(y));
 
                if (current.equals("-")) {
                    gameBoard[x][y] = new Node(current, x, y, true,
                            boardSize + 1);
                } else {
                    gameBoard[x][y] = new Node(current, x, y, false);
                }
            }
        }
 scan.close();
       
 
        solveBoard(gameBoard, boardSize);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(gameBoard[i][j].value);
            }
            System.out.println();
        }
 
    }
 
    public static boolean solveBoard(Node[][] grid, int boardSize) {
 
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
 
                if (!grid[i][j].value.equals("-")) {
                    continue;
                }
                for (int num = 1; num <= boardSize; num++) {
                    if (validMove(grid, boardSize, i, j, num) == true) {
                        // if (grid[i][j].curMoves.containsValue(num)) {
                        // Attempted to check using the above, but it ended up
                        // inputting 1s into every blank spot. I found
                        // that using the a validMoves method and "continuing"
                        // upon finding a curMove to be efficient and correct.
                        grid[i][j].value = Integer.toString(num);
 
                        if (solveBoard(grid, boardSize)) {
                            return true;
                        } else {
                            grid[i][j].value = "-";
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }
 
    public static boolean validMove(Node[][] gameBoard, int boardSize, int row,
            int col, int num) {
 
        // Traverse the rows to see what to remove as options
        for (int i = 0; i < boardSize; i++) {
            Node cur = gameBoard[i][col];
            if (cur.curMoves.containsKey(i)) {
                continue;
            }
            if (cur.value.equals(Integer.toString(num))) {
                return false;
 
            }
        } // end rows for
 
        // Traverse the columns to see what to remove as options
        for (int j = 0; j < boardSize; j++) {
            Node cur = gameBoard[row][j];
            if (cur.curMoves.containsKey(j)) {
                continue;
            }
            if (cur.value.equals(Integer.toString(num))) {
                return false;
 
            }
 
        }// end columns for
 
        // Traverse subgrids
        int rowOffSet = (row / subGridSize) * subGridSize;
        int colOffset = (col / subGridSize) * subGridSize;
 
        for (int k = 0; k < subGridSize; k++) {
            for (int m = 0; m < subGridSize; m++) {
 
                if (gameBoard[rowOffSet + k][colOffset + m].value
                        .equals(Integer.toString(num))) {
                    return false;
                }
            }
        }
 
        return true;
 
    }
}
 
class Node {
    String value;
    int xPos;
    int yPos;
    boolean bool;
    HashMap<Integer, String> curMoves = new HashMap<Integer, String>();
 
    public Node(String v, int x, int y, boolean b) {
        value = v;
        xPos = x;
        yPos = y;
        bool = b;
    }
 
    public Node(String v, int x, int y, boolean b, int subGridSize) {
        value = v;
        xPos = x;
        yPos = y;
        bool = b;
 
        for (int i = 1; i < subGridSize; i++) {
            curMoves.put(i, Integer.toString(i));
        }
    }
}