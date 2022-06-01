import stev.booleans.*;

import java.util.Arrays;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class Sudoku {

    public static void main(String[] args) {
        BooleanFormula cnf = modelisationStevBoolean();
        System.out.println(cnf);

        int[][] clauses = cnf.getClauses();

    }

    private static BooleanFormula modelisationStevBoolean() {
        PropositionalVariable[] allNumbers = new  PropositionalVariable[(9*9*9)];
        PropositionalVariable[][][] allNumbersSort = new  PropositionalVariable[9][9][9];


        for (int l = 0; l < 9; l++) {
            for (int c = 0; c < 9; c++) {
                for (int n = 0; n < 9; n++) {
                    allNumbers[(l*81)+(c*9)+n] = new PropositionalVariable("n"+(l+1)+(c+1)+(n+1));
                    allNumbersSort[l][c][n] = new PropositionalVariable("n"+(l+1)+(c+1)+(n+1));
                }
            }
        }


        And prop1=getProp1(allNumbersSort);
        And prop2 = getProp("prop2",allNumbersSort);
        And prop3 = getProp("prop3",allNumbersSort);
        And prop4 = getProp4(allNumbersSort);

        And propTotal = new And(prop1,prop2,prop3,prop4);

        return BooleanFormula.toCnf(propTotal);

    }

    public static And getProp4(PropositionalVariable[][][] allNumbersSort){
        And[] allSudoku = new And[3];
        Or[] firstColumnOfSquarre = new Or[3];
        Or[] allNumberInCase = new Or[9];

        for (int c = 0; c < 7; c += 3) {
            for (int l = 0; l < 7; l += 3) {
                for (int n = 0; n < 9; n++) {
                    allNumberInCase[n] = new Or(getNotOfBox(allNumbersSort,l,c,n));
                }
                Or oneSquarre = new Or(allNumberInCase);

                firstColumnOfSquarre[(l/3)] = oneSquarre;
            }
            allSudoku[(c/3)] = new And(firstColumnOfSquarre);
        }

        return new And(allSudoku);


    }

    public static And getProp1(PropositionalVariable[][][] allNumbersSort) {
        Implies[] tabImplic = new Implies[9];
        Not[] allColumnFirstLine = new Not[8];
        And[] tabAndImplic = new And[9];
        Or[] tabOrImplicCase = new Or[9];

        int increment = 0;

            for (int l = 0; l < 9; l++) {
                for (int c = 0; c < 9; c++) {
                    for (int m = 0; m < 9; m++) {
                        for (int n = 0; n < 9; n++) {
                            if (allNumbersSort[l][c][n] != allNumbersSort[l][c][m]) {

                                allColumnFirstLine[increment] = new Not(allNumbersSort[l][c][n]);
                                increment++;
                            }
                        }
                        increment = 0;

                        tabImplic[m] = new Implies(allNumbersSort[l][c][m], new Or(allColumnFirstLine));

                    }

                    tabOrImplicCase[c] = new Or(tabImplic);
                }

                tabAndImplic[l] = new And(tabOrImplicCase);

            }


        return new And(tabAndImplic);
    }

    private static Implies[] getNotOfBox(PropositionalVariable[][][] allNumbersSort, int l,int c, int n) {
        Implies[] eachCaseInSquarre = new Implies[9];
        for (int caseInSquarre = 0; caseInSquarre < 9; caseInSquarre++) {
            PropositionalVariable numberSelected = null;
            if( caseInSquarre < 3){
                numberSelected = allNumbersSort[l][(c + caseInSquarre)][n];
            } else if (caseInSquarre < 6) {
                int j = caseInSquarre % 3;
                numberSelected = allNumbersSort[(l+1)][(c + j)][n];
            } else {
                int j = caseInSquarre % 6;
                numberSelected = allNumbersSort[(l+2)][(c + j)][n];
            }

            Not[] notTheSameNumberInBox = new Not[8];
            PropositionalVariable toKeep;
            int increment = 0;
            for (int i = 0; i < 10; i++) {
                if( i < 3 ){
                    toKeep = allNumbersSort[(l)][i+c][n];
                    increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment);
                }
                else if(i < 6){
                    int j = i % 3;
                    toKeep = allNumbersSort[(1+l)][j+c][n];
                    increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment);
                }else if(i < 9){
                    int j = i % 6;
                    toKeep = allNumbersSort[(2+l)][j+c][n];
                    increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment);
                }

            }
            eachCaseInSquarre[caseInSquarre] = new Implies(numberSelected,new And(notTheSameNumberInBox));
        }


        return eachCaseInSquarre;
    }

    private static int incrementIfNotSameProp(PropositionalVariable numberSelected, Not[] notTheSameNumberInBox, PropositionalVariable toKeep, int increment) {
        if( toKeep != numberSelected){
            notTheSameNumberInBox[increment] =  new Not(toKeep);
            increment++;
        }
        return increment;
    }


    public static And getProp(String prop,PropositionalVariable[][][] allNumbersSort){
        Or[] tabOrImplic  = new Or[9];
        Implies[] tabImplic = new Implies[9];
        Not[] allColumnFirstLine = new Not[8];

        for (int l = 0; l < 9; l++) {
            for (int n = 0; n < 9; n++) {
                for (int c = 1; c < 9; c++) {
                    if(prop.equals("prop2")){
                        allColumnFirstLine[c-1] =  new Not(allNumbersSort[l][c][n]);
                    } else if (prop.equals("prop3")) {
                        allColumnFirstLine[c-1] =  new Not(allNumbersSort[c][l][n]);

                    }
                }
                if(prop.equals("prop2")){
                    tabImplic[n] = new Implies(allNumbersSort[l][0][n],new And(allColumnFirstLine));
                }else if(prop.equals("prop3")){
                    tabImplic[n] = new Implies(allNumbersSort[0][l][n],new And(allColumnFirstLine));
                }
            }
            tabOrImplic[l]=new Or(tabImplic);
        }
        return new And(tabOrImplic);
    }





}
