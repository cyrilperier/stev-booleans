/**
 * LAUR08069900, PERC12109605,
 */

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import stev.booleans.*;

import java.util.*;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class Sudoku {

    public static void main(String[] args) throws Exception {

        char[][] sudoku = getSudokuByArgs(args[0]);

        BooleanFormula cnf = ModelisationBoolean.modelisationStevBoolean(sudoku);
        Map<Integer, String> inverteGrille = getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();

        List<String> res = solveProblem(clauses,inverteGrille);
        System.out.println(res);

    }

    public static Map<Integer, String> getInverteGrille(BooleanFormula cnf) {
        Map<String, Integer> grilleIndiceProp = cnf.getVariablesMap();

        Map<Integer, String> inverteGrille= new HashMap<>();

        for (Map.Entry<String, Integer> entry : grilleIndiceProp.entrySet()){
            inverteGrille.put(entry.getValue(), entry.getKey());
        }
        return inverteGrille;
    }


    public static List<String> solveProblem(int[][] clauses, Map<Integer, String> invertGrille) throws Exception {
        final int MAXVAR = 729;
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
            int[] model = problem.model();

            int[] justPostive = Arrays.stream(model).filter(n -> n >=0).toArray();

            List<String> numberSearch = new ArrayList<>() ;
            for (int i :
                    justPostive) {
                numberSearch.add(invertGrille.get(i));
            }
            System.out.println(numberSearch.size());
            return numberSearch;
        }else{
            throw new Exception("Pas de solution");
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
