import stev.booleans.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class ModelisationBoolean {

    public static PropositionalVariable[] getPropositionOfSudoku(char[][] sudoku) {
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
//       System.out.println("\nProposition 2 : Chaque chiffre doit apparaître exactement une fois dans chaque ligne de la grille \n" + prop2);

        And prop3 = getProp("prop3",allNumbersSort);
//        System.out.println("\nProposition 3 : Chaque chiffre doit apparaître exactement une fois dans chaque colonne de la grille \n" + prop3);

        And prop4 = getProp4(allNumbersSort);
//        System.out.println("\nProposition 4 : Chaque chiffre doit apparaître exactement une fois dans chacune des neuf sous-grilles de taille 3×3 \n" + prop4);

//        System.out.println(propNumberTrue +" \n" + prop1);
        And propTotal = new And(prop1,prop2,prop3,prop4, propNumberTrue);



        return BooleanFormula.toCnf(propTotal);

    }

    /**
     * Method to create all proposition of a sudoku
     * @return PropositionalVariable[][][] all proposition
     */
    public static PropositionalVariable[][][] createAllProp() {
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
    public static And getProp4(PropositionalVariable[][][] allNumbersSort){
        And[] allSudoku = new And[3];
        And[] firstColumnOfSquare;
        //Pour chaque Grande colonne (premier indice : 0,0 ; 0,3 ; 0,6)
        for (int c = 0; c < 7; c += 3) {
            //Pour chaque Grande ligne (premier indice : 0,0 ; 3,0 ; 6,0)
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
    public static And[] getAllSquareOfOneColumnFOrEachLine(PropositionalVariable[][][] allNumbersSort, int c) {
        And[] firstColumnOfSquare = new And[3];
        And[] allNumberInCase;
        //Pour chaque Grande ligne (premier indice : 0,0 ; 3,0 ; 6,0)
        for (int l = 0; l < 7; l += 3) {
            allNumberInCase = getOnSquarre(allNumbersSort, c, l);
            And oneSquare = new And(allNumberInCase);
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
    public static And[] getOnSquarre(PropositionalVariable[][][] allNumbersSort, int c, int l) {
        And[] allNumberInCase = new And[9*2];
        for (int n = 0; n < 18; n++) {
            if( n < 9 ){
                allNumberInCase[n] = new And(getNotOfBox(allNumbersSort, l, c,n,"notAtRight"));
            }else{

                allNumberInCase[n] = new And(getNotOfBox(allNumbersSort, l, c,n%9,"notAtLeft"));
            }
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
    static Implies[] getNotOfBox(PropositionalVariable[][][] allNumbersSort, int l, int c, int n,String type) {
        Implies[] eachCaseInSquare = new Implies[9];
        //Pour chaque case du carré

        for (int caseInSquare = 0; caseInSquare < 9; caseInSquare++) {
            getImpliesForOneCase(allNumbersSort, l, c, n, eachCaseInSquare, caseInSquare,type);
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
    private static void getImpliesForOneCase(PropositionalVariable[][][] allNumbersSort, int l, int c, int n, Implies[] eachCaseInSquare, int caseInSquare,String type) {
        BooleanFormula numberSelected = null;
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



        BooleanFormula[] notTheSameNumberInBox = new BooleanFormula[8];
        PropositionalVariable toKeep;
        int increment = 0;
        //Pour chaque autre nombre du carre, permet de faire la partie droite de l'implication
        for (int i = 0; i < 10; i++) {
            if( i < 3 ){
                toKeep = allNumbersSort[l][i+ c][n];
                increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment,type);
            }
            else if(i < 6){
                int j = i % 3;
                toKeep = allNumbersSort[(1+ l)][j+ c][n];
                increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment,type);
            }else if(i < 9){
                int j = i % 6;
                toKeep = allNumbersSort[(2+ l)][j+ c][n];
                increment = incrementIfNotSameProp(numberSelected, notTheSameNumberInBox, toKeep, increment,type);
            }

        }

        if(type.equals("notAtRight")){
            eachCaseInSquare[caseInSquare] = new Implies(numberSelected,new And(notTheSameNumberInBox));
        }else{
            eachCaseInSquare[caseInSquare] = new Implies(new Not(numberSelected),new Or(notTheSameNumberInBox));
        }

    }

    private static int incrementIfNotSameProp(BooleanFormula numberSelected, BooleanFormula[] notTheSameNumberInBox, BooleanFormula toKeep, int increment,String type) {
        if(numberSelected != toKeep){
            if(type.equals("notAtRight")){
                notTheSameNumberInBox[increment] =  new Not(toKeep);
            }else{
                notTheSameNumberInBox[increment] =  toKeep;
            }
            increment++;
        }
        return increment;
    }


    public static And getProp(String prop,PropositionalVariable[][][] allNumbersSort){
        And[] tabOrImplic  = new And[9];
        Implies[] tabImplic = new Implies[9];
        Not[] allColumnFirstLine = new Not[8];
        And[]tabAndTotal = new And[9];
        int increment = 0;
        for(int m = 0;m<9;m++) {
            for (int l = 0; l < 9; l++) {
                for (int n = 0; n < 9; n++) {
                    for (int c = 0; c < 9; c++) {
                        if (allNumbersSort[l][m][n] != allNumbersSort[l][c][n]) {


                            if (prop.equals("prop2")) {
                                allColumnFirstLine[increment] = new Not(allNumbersSort[l][c][n]);
                                increment++;
                            } else if (prop.equals("prop3")) {
                                allColumnFirstLine[increment] = new Not(allNumbersSort[c][l][n]);
                                increment++;

                            }
                        }
                    }
                    increment = 0;
                    if (prop.equals("prop2")) {
                        tabImplic[n] = new Implies(allNumbersSort[l][m][n], new And(allColumnFirstLine));
                    } else if (prop.equals("prop3")) {
                        tabImplic[n] = new Implies(allNumbersSort[m][l][n], new And(allColumnFirstLine));
                    }
                }
                tabOrImplic[l] = new And(tabImplic);
            }
        tabAndTotal[m]= new And(tabOrImplic);
        }
        return new And(tabAndTotal);
    }





   public static And getProp1(PropositionalVariable[][][] allNumbersSort) {
        Implies[] tabImplic = new Implies[9];
        Not[] allColumnFirstLine = new Not[8];
        And[] tabAndImplic = new And[9];
        And[] tabOrImplicCase = new And[9];

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
                    tabImplic[m] = new Implies(allNumbersSort[l][c][m], new And(allColumnFirstLine));
                }
                tabOrImplicCase[c] = new And(tabImplic);
            }
            tabAndImplic[l] = new And(tabOrImplicCase);

        }
        return new And(tabAndImplic);
    }



}
