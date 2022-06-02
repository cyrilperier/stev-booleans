import stev.booleans.*;

import java.util.Arrays;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class ModelisationBoolean {


    public static BooleanFormula modelisationStevBoolean() {
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
//        System.out.println("Proposition 1 : Chaque case ne peut contenir qu’un seul chiffre \n" + prop1);

        And prop2 = getProp("prop2",allNumbersSort);
//        System.out.println("\nProposition 2 : Chaque chiffre doit apparaître exactement une fois dans chaque ligne de la grille \n" + prop2);

        And prop3 = getProp("prop3",allNumbersSort);
//        System.out.println("\nProposition 3 : Chaque chiffre doit apparaître exactement une fois dans chaque colonne de la grille \n" + prop3);

        And prop4 = getProp4(allNumbersSort);
//        System.out.println("\nProposition 4 : Chaque chiffre doit apparaître exactement une fois dans chacune des neuf sous-grilles de taille 3×3 \n" + prop4);

        And propTotal = new And(prop1,prop2,prop3,prop4);

        return BooleanFormula.toCnf(propTotal);

    }

    /**
     * Create the four proposition
     * @param allNumbersSort
     * @return
     */
    private static And getProp4(PropositionalVariable[][][] allNumbersSort){
        And[] allSudoku = new And[3];
        Or[] firstColumnOfSquare = new Or[3];
        Or[] allNumberInCase = new Or[9];

        //Pour chaque Grande colonne (premier indice : 0,0 ; 0,3 ; 0,6)
        for (int c = 0; c < 7; c += 3) {
            //Pour chaque Grande ligne (premier indice : 0,0 ; 3,0 ; 6,0)
            for (int l = 0; l < 7; l += 3) {
                //Pour chaque numero
                for (int n = 0; n < 9; n++) {
                    allNumberInCase[n] = new Or(getNotOfBox(allNumbersSort,l,c,n));
                }
                Or oneSquare = new Or(allNumberInCase);

                firstColumnOfSquare[(l/3)] = oneSquare;
            }
            allSudoku[(c/3)] = new And(firstColumnOfSquare);
        }

        return new And(allSudoku);


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

    /**
     * Permet de faire chaque la proposition pour chaque carre de sudoku
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
            PropositionalVariable numberSelected = null;
            //Permet de choisir le nombre qui sera a gauche de l'implication
            if( caseInSquare < 3){
                numberSelected = allNumbersSort[l][(c + caseInSquare)][n];
            } else if (caseInSquare < 6) {
                int j = caseInSquare % 3;
                numberSelected = allNumbersSort[(l+1)][(c + j)][n];
            } else {
                int j = caseInSquare % 6;
                numberSelected = allNumbersSort[(l+2)][(c + j)][n];
            }

            Not[] notTheSameNumberInBox = new Not[8];
            PropositionalVariable toKeep;
            int increment = 0;
            //Pour chaque autre nombre du carre, permet de faire la partie droite de l'implication
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
            eachCaseInSquare[caseInSquare] = new Implies(numberSelected,new And(notTheSameNumberInBox));
        }


        return eachCaseInSquare;
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

}
