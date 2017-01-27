import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class SudokuVar {
	
	//main method
	public static void main(String[] args) throws FileNotFoundException {
		 File file = new File(args[0]);
	     Scanner scan = new Scanner(file);
	       
	     int n = scan.nextInt();
	     int rows = scan.nextInt();
	     String[][] board = new String[n][rows*rows];
	       
	     for(int i = 0; i < n; i++){
	         String[] current = scan.next().split("");
	         for(int j = 0; j < n; j++){
	             board[i][j] = current[j];
	         }
	     }
	     scan.close();
	     solveCSP(board);
	     printBoard(board);
	}
	
	//board printing method
	 public static void printBoard(String[][] board){
	    	for(int i = 0; i < board.length; i++){
	    		for(int j = 0; j < board.length; j++){
	    			System.out.print(board[i][j]);
	    		}
	    		System.out.println();
	    	}
	  }
	
	 //gets the numbers in that number's box to eliminate from domain.
	public static HashMap<Integer, String> getSubGroup(String[][] board, int n, int i, int j){
       HashMap<Integer, String> subgroup = new HashMap<Integer, String>();
        if(n == 9){
        	subgroup.put(0, board[i][j]);
            subgroup.put(1, board[i][j-1]);
            subgroup.put(2, board[i][j+1]);
            subgroup.put(3, board[i-1][j-1]);
            subgroup.put(4, board[i-1][j]);
            subgroup.put(5, board[i-1][j+1]);
            subgroup.put(6, board[i+1][j+1]);
            subgroup.put(7, board[i+1][j-1]);
            subgroup.put(8, board[i+1][j]);
        }
        else if(n == 4){
        	subgroup.put(0, board[i][j]);
        	subgroup.put(1, board[i][j+1]);
        	subgroup.put(2, board[i+1][j]);
        	subgroup.put(3, board[i+1][j+1]);
        }
        
       return subgroup;
  }
	
	//checks if the given num is a possible solution for that space.
	//forward checking
	public static boolean isPossible(String[][] board, int n, int row, int col, String num){
		//checks column
		for(int i = 0; i < n; i++){
			if(board[i][col].equals(num))
				return false;
		}
		//checks row
		for(int j = 0; j < n; j++){
			if(board[row][j].equals(num))
				return false;
		}
		
		//removes subgrid nums (I know this code is badly replicated
		//and only works for sudokus of 9 or 4, but I didn't know
		//how else to do it without looping to save speed)
		if(n == 9){
			if((row >=0 && row <= 2 && col >= 0 && col <= 2)){
				if(getSubGroup(board, n, 1, 1).containsValue(num))
					return false;
			}
	        else if(row >=0 && row <= 2 && col >= 3 && col <= 5){
				if(getSubGroup(board, n, 1, 4).containsValue(num))
					return false;
	        }
	        else if(row >=0 && row <= 2 && col >= 6 && col <= 8){
				if(getSubGroup(board, n, 1, 7).containsValue(num))
					return false;
	        }
	        else if(row >=3 && row <= 5 && col >= 0 && col <= 2){
				if(getSubGroup(board, n, 4, 1).containsValue(num))
					return false;
	        }	
	        else if(row >=3 && row <= 5 && col >= 3 && col <= 5){
				if(getSubGroup(board, n, 4, 4).containsValue(num))
					return false;
	        }
	        else if(row >=3 && row <= 5 && col >= 6 && col <= 8){
	        	if(getSubGroup(board, n, 4, 7).containsValue(num))
	        		return false;
	        }
	        else if(row >=6 && row <= 8 && col >= 0 && col <= 2){
	        	if(getSubGroup(board, n, 7, 1).containsValue(num))
	        		return false;
	        }
	        else if(row >=6 && row <= 8 && col >= 3 && col <= 5){
	        	if(getSubGroup(board, n, 7, 4).containsValue(num))
					return false;	
	        }
	        else if(row >= 6 && row <= 8 && col >= 6 && col <= 8){
	        	if(getSubGroup(board, n, 7, 7).containsValue(num))
					return false;
	        }
		}
		else if(n == 4){
			if(row >= 0 && row <= 1 && col >= 0 && col <= 1){
				if(getSubGroup(board, n, 0, 0).containsValue(num))
					return false;
			}
			else if(row >= 0 && row <= 1 && col >= 2 && col <= 3){
				if(getSubGroup(board, n, 0, 2).containsValue(num))
					return false;			
			}
			else if(row >= 2 && row <= 3 && col >= 0 && col <= 1){
				if(getSubGroup(board, n, 2, 0).containsValue(num))
					return false;			
			}
			else if(row >= 2 && row <= 3 && col >= 2 && col <= 3){
				if(getSubGroup(board, n, 2, 2).containsValue(num))
					return false;		
			}
		}
		return true;
	}
	
	public static boolean solveCSP(String[][] board){
		
			int[] mins = mrvHeuristic(board);
			int minX = mins[0];
			int minY = mins[1];
			int sol = 1;
			for(int i = minX; i < board.length; i++){
				for(int j = minY; j < board.length; j++){
					if(!board[i][j].equals("-")){
						continue;
					}
					for(sol = 1; sol <= board.length; sol++){
						if(isPossible(board, board.length, i, j, Integer.toString(sol))){
							board[i][j] = Integer.toString(sol);
							if(solveCSP(board)){
								return true;
							}
							else {
								board[i][j] = "-";
							}
						}
					}
					return false;
				}
			}
		
		return true;
	}
	
	//gets domain size for mrv heuristic
	public static int getDomainSize(String[][] board, int i, int j, int n){
		HashMap<Integer, String> removeFromDomain = null;
		if(n == 9){
			if((i >=0 && i <= 2 && j >= 0 && j <= 2))
				removeFromDomain = getSubGroup(board, n, 1, 1);
	        else if(i >=0 && i <= 2 && j >= 3 && j <= 5)
	        	removeFromDomain = getSubGroup(board, n, 1, 4);
	        else if(i >=0 && i <= 2 && j >= 6 && j <= 8)
	        	removeFromDomain = getSubGroup(board, n, 1, 7);
	        else if(i >=3 && i <= 5 && j >= 0 && j <= 2)
	        	removeFromDomain = getSubGroup(board, n, 4, 1);
	        else if(i >=3 && i <= 5 && j >= 3 && j <= 5)
	        	removeFromDomain = getSubGroup(board, n, 4, 4);
	        else if(i >=3 && i <= 5 && j >= 6 && j <= 8)
	        	removeFromDomain = getSubGroup(board, n, 4, 7);
	        else if(i >=6 && i <= 8 && j >= 0 && j <= 2)
	        	removeFromDomain = getSubGroup(board, n, 7, 1);
	        else if(i >=6 && i <= 8 && j >= 3 && j <= 5)
	        	removeFromDomain = getSubGroup(board, n, 7, 4);
	        else if(i >= 6 && i <= 8 && j >= 6 && j <= 8)
	        	removeFromDomain = getSubGroup(board, n, 7, 7);
		}
		else if(n == 4){
			if((i >=0 && i <= 1 && j >= 0 && j <= 1))
				removeFromDomain = getSubGroup(board, n, 0, 0);
	        else if(i >=0 && i <= 1 && j >= 2 && j <= 3)
	        	removeFromDomain = getSubGroup(board, n, 0, 2);
	        else if(i >= 2 && i <= 3 && j >= 0 && j <= 1)
	        	removeFromDomain = getSubGroup(board, n, 2, 0);
	        else if(i >= 2 && i <= 3 && j >= 2 && j <= 3)
	        	removeFromDomain = getSubGroup(board, n, 2, 2);
		}
		//puts col nums into remove from domain
		for(int k = 0; k < n; k++){
			if(!removeFromDomain.containsValue(board[k][j])){
				removeFromDomain.put(removeFromDomain.size()+1, board[k][j]);
			}
		}
		//checks row
		for(int p = 0; p < n; p++){
			if(!removeFromDomain.containsValue(board[i][p])){
				removeFromDomain.put(removeFromDomain.size()+1, board[i][p]);
			}
		}
		return n - removeFromDomain.size();
	}
	
	//mrv heuristic method
	public static int[] mrvHeuristic(String[][] board){
		int minX = 0;
		int minY = 0;
		int min = board.length+1;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board.length; j++){
				int domSize = getDomainSize(board, i, j, board.length);
				if(board[i][j].equals("-") && domSize < min){
					min = domSize;
					minX = i;
					minY = j;
				}
			}
		}
		return new int[]{minX, minY};
		
	}

}
