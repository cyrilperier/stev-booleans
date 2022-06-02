import stev.booleans.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class ModelisationBoolean {

    private static PropositionalVariable[] getPropositionOfSudoku(char[][] sudoku) {
        List<PropositionalVariable> res = new ArrayList<>();

        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[0].length; j++) {
                if (sudoku[i][j] != ' ') {
                    res.add(new PropositionalVariable("n" + (i+1) + (j+1) + sudoku[i][j]));
                }
            }
        }

        //Transform list into array
        PropositionalVariable[] propositionInArray = new PropositionalVariable[res.size()];
        res.toArray(propositionInArray);

        return propositionInArray;
    }

    public static BooleanFormula modelisationStevBoolean(char[][] sudoku) {
        And propNumberTrue = new And(getPropositionOfSudoku(sudoku));
        PropositionalVariable[][][] allNumbersSort = createAllProp();

//        System.out.println("Proposition existante :  \n" + propNumberTrue);

        And prop1=getProp1(allNumbersSort);
//        System.out.println("Proposition 1 : Chaque case ne peut contenir qu’un seul chiffre \n" + prop1);

        And prop2 = getProp("prop2",allNumbersSort);
//        System.out.println("\nProposition 2 : Chaque chiffre doit apparaître exactement une fois dans chaque ligne de la grille \n" + prop2);

        And prop3 = getProp("prop3",allNumbersSort);
//        System.out.println("\nProposition 3 : Chaque chiffre doit apparaître exactement une fois dans chaque colonne de la grille \n" + prop3);

        And prop4 = getProp4(allNumbersSort);
//        System.out.println("\nProposition 4 : Chaque chiffre doit apparaître exactement une fois dans chacune des neuf sous-grilles de taille 3×3 \n" + prop4);

//        System.out.println(propNumberTrue +" \n" + prop1);
//        And propTotal = new And(prop1,prop2,prop3,prop4, propNumberTrue);
        And propTest = new And(getNotOfBox(allNumbersSort,0,0,0)[0]);
        System.out.println(propNumberTrue);
        System.out.println(propTest+"\n");

        And propTotal = new And(propTest,propNumberTrue);

        return BooleanFormula.toCnf(propTotal);

    }

    /**
     * Method to create all proposition of a sudoku
     * @return PropositionalVariable[][][] all proposition
     */
    private static PropositionalVariable[][][] createAllProp() {
        PropositionalVariable[][][] allNumbersSort = new  PropositionalVariable[9][9][9];
        for (int l = 0; l < 9; l++) {
            for (int c = 0; c < 9; c++) {
                for (int n = 0; n < 9; n++) {
                    allNumbersSort[l][c][n] = new PropositionalVariable("n"+(l+1)+(c+1)+(n+1));
                }
            }
        }
        return allNumbersSort;
    }

    /**
     * Create the four proposition
     * @param allNumbersSort
     * @return
     */
    private static And getProp4(PropositionalVariable[][][] allNumbersSort){
        And[] allSudoku = new And[3];
        Or[] firstColumnOfSquare;
        //Pour chaque Grande colonne (premier indice : 0,0 ; 0,3 ; 0,6)
        for (int c = 0; c < 7; c += 3) {
            firstColumnOfSquare = getAllSquareOfOneColumnFOrEachLine(allNumbersSort, c);
            allSudoku[(c/3)] = new And(firstColumnOfSquare);
        }
        return new And(allSudoku);
    }

    /**
     * Method to get all implies of all square (3) in one big column (line : 0-8)
     * @param allNumbersSort
     * @param c number of first indice of big column (0,3,6)
     * @return
     */
    private static Or[] getAllSquareOfOneColumnFOrEachLine(PropositionalVariable[][][] allNumbersSort, int c) {
        Or[] firstColumnOfSquare = new Or[3];
        Or[] allNumberInCase;
        //Pour chaque Grande ligne (premier indice : 0,0 ; 3,0 ; 6,0)
        for (int l = 0; l < 7; l += 3) {
            allNumberInCase = getOnSquarre(allNumbersSort, c, l);
            Or oneSquare = new Or(allNumberInCase);
            firstColumnOfSquare[(l/3)] = oneSquare;
        }
        return  firstColumnOfSquare;
    }

    /**
     * Method to get all implies of one square (line : 0-8)
     * @param allNumbersSort
     * @param c number of first indice of big column (0,3,6)
     * @param l number of first indice of big line (0,3,6)
     */
    private static Or[] getOnSquarre(PropositionalVariable[][][] allNumbersSort, int c, int l) {
        Or[] allNumberInCase = new Or[9];
        for (int n = 0; n < 9; n++) {
            allNumberInCase[n] = new Or(getNotOfBox(allNumbersSort, l, c,n));
        }
        return allNumberInCase;
    }

    /**
     * Method to get all implies of one boxsquarre
     * @param allNumbersSort
     * @param l l'indice de ligne de quel case on se situe
     * @param c l'indice de colonne de quel case on se situe
     * @param n le nombre qu'on est en train de faire
     * @return
     */
    private static Implies[] getNotOfBox(PropositionalVariable[][][] allNumbersSort, int l,int c, int n) {
        Implies[] eachCaseInSquare = new Implies[9];
        //Pour chaque case du carré
        for (int caseInSquare = 0; caseInSquare < 9; caseInSquare++) {
            getImpliesForOneCase(allNumbersSort, l, c, n, eachCaseInSquare, caseInSquare);
        }


        return eachCaseInSquare;
    }

    /**
     * Method to get all implies for one case in squarre
     * @param allNumbersSort
     * @param l
     * @param c
     * @param n
     * @param eachCaseInSquare
     * @param caseInSquare
     */
    private static void getImpliesForOneCase(PropositionalVariable[][][] allNumbersSort, int l, int c, int n, Implies[] eachCaseInSquare, int caseInSquare) {
        PropositionalVariable numberSelected = null;
        //Permet de choisir le nombre qui sera a gauche de l'implication
        if( caseInSquare < 3){
            numberSelected = allNumbersSort[l][(c + caseInSquare)][n];
        } else if (caseInSquare < 6) {
            int j = caseInSquare % 3;
            numberSelected = allNumbersSort[(l +1)][(c + j)][n];
        } else {
            int j = caseInSquare % 6;
            numberSelected = allNumbersSort[(l +2)][(c + j)][n];
        }

        Not[] notTheSameNumberInBox = new Not[8];
        PropositionalVariable toKeep;
        int increment = 0;
        //Pour chaque autre nombre du carre, permet de faire la partie droite de l'implication
        for (int i = 0; i < 10; i++) {
            if( i < 3 ){
                toKeep = allNumbersSort[l][i+ c][n];
                increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment);
            }
            else if(i < 6){
                int j = i % 3;
                toKeep = allNumbersSort[(1+ l)][j+ c][n];
                increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment);
            }else if(i < 9){
                int j = i % 6;
                toKeep = allNumbersSort[(2+ l)][j+ c][n];
                increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment);
            }

        }
        eachCaseInSquare[caseInSquare] = new Implies(numberSelected,new And(notTheSameNumberInBox));
    }

    private static int incrementIfNotSameProp(PropositionalVariable numberSelected, Not[] notTheSameNumberInBox, PropositionalVariable toKeep, int increment) {
        if( toKeep != numberSelected){
            notTheSameNumberInBox[increment] =  new Not(toKeep);
            increment++;
        }
        return increment;
    }


    private static And getProp(String prop,PropositionalVariable[][][] allNumbersSort){
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



    private static And getProp1(PropositionalVariable[][][] allNumbersSort) {
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

}
