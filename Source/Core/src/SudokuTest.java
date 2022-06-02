import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.PropositionalVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class SudokuTest {
    private char[][] sudoku;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProb1() throws ContradictionException, TimeoutException {
        sudoku = Sudoku.getSudokuByArgs("122##############################################################################");

            List<PropositionalVariable> res = new ArrayList<>();

            for (int i = 0; i < sudoku.length; i++) {
                for (int j = 0; j < sudoku[0].length; j++) {
                    if (sudoku[i][j] != ' ') {
                        res.add(new PropositionalVariable("n" + (i+1) + (j+1) + sudoku[i][j]));
                    }
                }
            }
            res.add(new PropositionalVariable("n112"));

            //Transform list into array
            PropositionalVariable[] propositionInArray = new PropositionalVariable[res.size()];
            res.toArray(propositionInArray);

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();

        And propNumberTrue = new And(propositionInArray);

        And propTest = new And(ModelisationBoolean.getProp1(allNumbersSort));
System.out.println(propTest);

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);

        int[][] clauses = cnf.getClauses();



        Assert.assertFalse(Sudoku.solveProblem(clauses));
    }

    @Test
    public void solveProblem() throws ContradictionException, TimeoutException {
        sudoku = Sudoku.getSudokuByArgs("111######111######111############################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getNotOfBox(allNumbersSort,0,0,0)[0]);
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);

        int[][] clauses = cnf.getClauses();

        System.out.println(Arrays.deepToString(clauses));

        Assert.assertFalse(Sudoku.solveProblem(clauses));
    }
    @Test
    public void testProb2() throws ContradictionException, TimeoutException {
        sudoku = Sudoku.getSudokuByArgs("122##############################################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getProp("prop2",allNumbersSort));
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);

        int[][] clauses = cnf.getClauses();

        System.out.println(Arrays.deepToString(clauses));

        Assert.assertFalse(Sudoku.solveProblem(clauses));
    }
    @Test
    public void testProb3_Exception() throws ContradictionException, TimeoutException {
        sudoku = Sudoku.getSudokuByArgs("#2########2########1#############################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getProp("prop3",allNumbersSort));
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);

        int[][] clauses = cnf.getClauses();

        System.out.println(Arrays.deepToString(clauses));

        Assert.assertFalse(Sudoku.solveProblem(clauses));
    }

}