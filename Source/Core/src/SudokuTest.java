import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.PropositionalVariable;

import javax.management.ValueExp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Test(expected = Exception.class)
    public void testProb1() throws Exception {
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

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();



        Sudoku.solveProblem(clauses,inverteGrille);
    }

    @Test(expected = Exception.class)
    public void solveProblemTestOnSquarreNumberOnePresentMultipleTime() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("#11######111######111############################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getNotOfBox(allNumbersSort,0,0,0,"notAtRight"));
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);
    }

    @Test
    public void solveProblemTestShouldHaveOneNumberAtLeastInSquarre() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("#26493815315728946489651237852147693673985124941362758194836572567214389238579461");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getOnSquarre(allNumbersSort,0,0));
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        List<String> values =  Sudoku.solveProblem(clauses,inverteGrille);
        System.out.println(values);
        Assert.assertTrue(values.contains("n117"));
    }

    @Test
    public void solveProblemTestOnSquarreNumberOneNotExisting() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("1################################################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getNotOfBox(allNumbersSort,0,0,0,"notAtRight"));
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);
    }

    @Test(expected = Exception.class)
    public void solveProblemTestOnSquarreWithMultipleNumber() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("211######112######111############################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getOnSquarre(allNumbersSort,0,0));
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


       Sudoku.solveProblem(clauses,inverteGrille);
    }

    @Test(expected = Exception.class)
    public void testProb2() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("122##############################################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getProp("prop2",allNumbersSort));
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);

    }

    @Test()
    public void testProb2AvoidVide() throws Exception {
    sudoku = Sudoku.getSudokuByArgs("#26493815315728946489651237852147693673985124941362758194836572567214389238579461");


        List<PropositionalVariable> res = new ArrayList<>();

        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[0].length; j++) {
                if (sudoku[i][j] != ' ') {
                    res.add(new PropositionalVariable("n" + (i+1) + (j+1) + sudoku[i][j]));
                }
            }
        }
        //res.add(new PropositionalVariable("!n117"));

        //Transform list into array
        PropositionalVariable[] propositionInArray = new PropositionalVariable[res.size()];
        res.toArray(propositionInArray);


        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(propositionInArray);

        And propTest = new And(ModelisationBoolean.getPropAvoidVoid("prop2",allNumbersSort),ModelisationBoolean.getProp1(allNumbersSort));


        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        List<String> value=Sudoku.solveProblem(clauses,inverteGrille);
        Assert.assertTrue("n117",value.contains("n117"));

    }
    @Test(expected = Exception.class)
    public void testProb3_Exception() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("#2########2########1#############################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getProp("prop3",allNumbersSort));
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);

    }


    @Test
    public void solveProblemTestOnSquarreMultipleNumber() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("2########1##########8############################################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));
        And propTest = new And(ModelisationBoolean.getOnSquarre(allNumbersSort,0,0));
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);
    }

    @Test
    public void solveProblemTestOnThreeSquarres() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("1########2########3#########1#######2############################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getAllSquareOfOneColumnFOrEachLine(allNumbersSort,0));


        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


       Sudoku.solveProblem(clauses,inverteGrille);
    }

    @Test(expected = Exception.class)
    public void solveProblemTestOnThreeSquarres_ExcptionBecauseTwo1() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("1########2########3#########1#######1############################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getAllSquareOfOneColumnFOrEachLine(allNumbersSort,0));


        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);
    }


    @Test(expected = Exception.class)
    public void solveProblemTestOnAllSudoku_Two9InOneSquarre() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("1###5####2########3#########4##9####1###9########################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getProp4(allNumbersSort));


        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);
    }

    @Test
    public void solveProblemTestOnAllSudokuGood() throws Exception {
        sudoku = Sudoku.getSudokuByArgs("1###5####2########3#########4##2####1###9########################################");

        PropositionalVariable[][][] allNumbersSort = ModelisationBoolean.createAllProp();
        And propNumberTrue = new And(ModelisationBoolean.getPropositionOfSudoku(sudoku));

        And propTest = new And(ModelisationBoolean.getProp4(allNumbersSort));


        And propTotal = new And(propTest,propNumberTrue);
        BooleanFormula cnf = BooleanFormula.toCnf(propTotal);
        Map<Integer, String> inverteGrille = Sudoku.getInverteGrille(cnf);

        int[][] clauses = cnf.getClauses();


        Sudoku.solveProblem(clauses,inverteGrille);
    }

}