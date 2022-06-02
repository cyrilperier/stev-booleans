import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.PropositionalVariable;

import java.util.Arrays;

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
}