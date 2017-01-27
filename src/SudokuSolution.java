import java.io.*;
import java.util.*;
 
 
public class SudokuSolution {
    List<Integer> domain;
    String[][] board;
    int mrvVal;
    int n, count;
   
    public static void main(String [] args) throws FileNotFoundException{
    	SudokuSolution sol = new SudokuSolution();
//    	sol.printBoard();
//    	System.out.println();
//    	System.out.println(sol.getSubGroup(sol.n, 7, 7));
    	SudokuCSP csp = new SudokuCSP();
    	sol.solveCSP(csp);
    }
 
    public void printBoard(){
    	for(int i = 0; i < board.length; i++){
    		for(int j = 0; j < board.length; j++){
    			System.out.print(board[i][j]);
    		}
    		System.out.println();
    	}
    }
    public SudokuSolution() throws FileNotFoundException{
         domain = new LinkedList<Integer>();
            board = createBoard();
         for(int i = 0; i < n; i++){
                domain.add(i);
            }
         mrvVal = -1;
         
    }
   
    public boolean spotIsSet(){
        return mrvVal != -1;
    }
   
    public boolean isInconsistent(){
        return domain.size() == 0;
    }
 
  public ArrayList<String> getSubGroup(int n, int i, int j){
        String[][] subgroup = new String[n][n];
        ArrayList<String> subGroupDom = new ArrayList<String>();
        if(n == 9){
            subgroup[i][j] = board[i][j];
            subgroup[i][j-1] = board[i][j-1];
            subgroup[i][j+1] = board[i][j+1];
            subgroup[i-1][j-1] = board[i-1][j-1];
            subgroup[i-1][j] = board[i-1][j];
            subgroup[i-1][j+1] = board[i-1][j+1];
            subgroup[i+1][j+1] = board[i+1][j+1];
            subgroup[i+1][j-1] = board[i+1][j-1];
            subgroup[i+1][j] = board[i+1][j];
           
            if(canParse(subgroup[i][j])){
            	subGroupDom.add(subgroup[i][j]);
            }
            if(canParse(subgroup[i][j-1])){
            	subGroupDom.add(subgroup[i][j-1]);
            }
            if(canParse(subgroup[i][j+1])){
            	subGroupDom.add(subgroup[i][j+1]);
            }
            if(canParse(subgroup[i-1][j-1])){
            	subGroupDom.add(subgroup[i-1][j-1]);
            }
            if(canParse(subgroup[i-1][j])){
            	subGroupDom.add(subgroup[i-1][j]);
            }
            if(canParse(subgroup[i-1][j+1])){
            	subGroupDom.add(subgroup[i-1][j+1]);
            }
            if(canParse(subgroup[i+1][j+1])){
            	subGroupDom.add(subgroup[i+1][j+1]);
            }
            if(canParse(subgroup[i+1][j-1])){
            	subGroupDom.add(subgroup[i+1][j-1]);
            }
            if(canParse(subgroup[i+1][j])){
            	subGroupDom.add(subgroup[i+1][j]);
            }
        }
       return subGroupDom;
  }
   
  public boolean canParse(String input){
	  boolean canParse = true;
	  try {
		  Integer.parseInt(input);
	  }
	  catch(NumberFormatException e){
		  canParse = false;
	  }
	  return canParse;
  }
    public void solveCSP(SudokuCSP sud){
        if(sud.set == n){
            count++;
        }
        else {
            int targetVar = sud.mrvHeuristic(false);
            int targetRow = targetVar;
            assert(targetVar != -1);
            domain = sud.variables[targetVar].domain;
            for(int i = 0; i < domain.size(); i++){
                SudokuCSP copy = sud;
                SudokuSolution var = copy.variables[targetVar];
                int col = i;
                var.mrvVal = col;
                copy.set++;
               
                for(int row = 0; row < n; row++){
                    if(row != targetRow && !copy.variables[row].spotIsSet()){
                        copy.variables[row].domain.remove(col);
                        copy.variables[row].domain.remove(row);
                        if((row >=0 && row <= 2 && col >= 0 && col <= 2)){
                       	 	copy.variables[row].domain.removeAll(var.getSubGroup(n, 1, 1));
                        }
                        else if(row >=0 && row <= 2 && col >= 3 && col <= 5){
                          	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 1, 4));
                        }
                        else if(row >=0 && row <= 2 && col >= 6 && col <= 8){
                         	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 1, 7));
                       }
                        else if(row >=3 && row <= 5 && col >= 0 && col <= 2){
                         	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 4, 1));
                       }
                        else if(row >=3 && row <= 5 && col >= 3 && col <= 5){
                         	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 4, 4));
                       }
                        else if(row >=3 && row <= 5 && col >= 6 && col <= 8){
                         	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 4, 7));
                       }
                        else if(row >=6 && row <= 8 && col >= 0 && col <= 2){
                         	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 7, 1));
                       }
                        else if(row >=6 && row <= 8 && col >= 3 && col <= 5){
                         	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 7, 4));
                       }
                        else if(row >= 6 && row <= 8 && col >= 6 && col <= 8){
                         	 copy.variables[row].domain.removeAll(var.getSubGroup(n, 7, 7));
                       }
                }
            }
                solveCSP(copy);
            }
        }
        
    }
   
    public String[][] createBoard() throws FileNotFoundException {
        File file = new File("9x9a.txt");
        Scanner scan = new Scanner(file);
       
        n = scan.nextInt();
        int rows = scan.nextInt();
        String[][] board = new String[n][rows*rows];
       
        for(int i = 0; i < n; i++){
            String[] current = scan.next().split("");
            for(int j = 0; j < n; j++){
                board[i][j] = current[j];
            }
        }
       
        scan.close();
        return board;
    }
   
   
}
 
class SudokuCSP {
   
    SudokuSolution[] variables;
    int set;
    int n;
    public SudokuCSP() throws FileNotFoundException{
        SudokuSolution sol = new SudokuSolution();
        variables = new SudokuSolution[sol.n];
        n = sol.n;
        set = 0;
    }
   
    public int mrvHeuristic(boolean useMRV){
        if(useMRV){
            int min = n+1;
            int which = -1;
            for(int i = 0; i < n; i++){
                SudokuSolution sol = variables[i];
                if(!sol.spotIsSet() && (int)sol.domain.size() < min){
                    min = sol.domain.size();
                    which = i;
                }
            }
            return which;
        }
         else{
             for(int j = 0; j < n; j++){
                 SudokuSolution noMRVSol = variables[j];
                 if(!noMRVSol.spotIsSet()){
                     return 1;
                 }
             }
             return -1;
         }
    }
}