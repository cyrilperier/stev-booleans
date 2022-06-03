import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import stev.booleans.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class Sudoku {

    public static void main(String[] args) throws ContradictionException, TimeoutException {

        char[][] sudoku = getSudokuByArgs(args[0]);

        BooleanFormula cnf = ModelisationBoolean.modelisationStevBoolean(sudoku); ////
        //System.out.println(cnf);

        int[][] clauses = cnf.getClauses();

//        System.out.println(Arrays.deepToString(sudoku));
        //System.out.println(Arrays.deepToString(clauses));
//        System.out.println(Arrays.deepToString(sudoku));
   //     System.out.println(Arrays.deepToString(clauses));

      //  boolean res = solveProblem(clauses);
        //WriteSudoku(sudoku);

    }



    public static boolean solveProblem(int[][] clauses) throws ContradictionException, TimeoutException {
        final int MAXVAR = 10000000;
        final int NBCLAUSES = clauses.length;

        ISolver solver= SolverFactory.newDefault();

        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);


        for (int[] clause : clauses) {

            solver.addClause(new VecInt(clause));
        }

        IProblem problem = solver;


        if (problem.isSatisfiable()){
            System.out.println("Solution");
            System.out.println(Arrays.toString(solver.modelWithInternalVariables()));
            return true;
        }else{
            System.out.println("Pas de de solution");
            return false;
        }
    }

    public static char[][] getSudokuByArgs(String gameString) {
        // Retrieve the string of game entries passed in args
        char[][] gameTable = new char[9][9];

        // Convert game string to a 2D Array of size 9x9
        int charIndex = 0;
        for (int i = 0; i < gameTable.length; i++) {
            for (int j = 0; j < gameTable[0].length; j++) {
                if (gameString.charAt(charIndex) == '#')
                gameTable[i][j] = ' ';
                else gameTable[i][j] = gameString.charAt(charIndex);
                charIndex++;
            }
        }
        return gameTable;
    }

    private static void WriteSudoku(char[][] sudoku) {
        for(int j=0;j<9;j++) {
            for (int i = 0; i < 9; i++) {
                if (i == 3 || i==6) {
                    System.out.print("|");
                    System.out.print(" ");
                }
                if (sudoku[j][i] == ' ') {
                    System.out.print("-");
                    System.out.print(" ");
                } else {
                    System.out.print(sudoku[j][i]);
                    System.out.print(" ");
                }


            }
            System.out.println("");
            if(j==2 || j==5){
                System.out.println("======|=======|======");}
        }
    }


}
