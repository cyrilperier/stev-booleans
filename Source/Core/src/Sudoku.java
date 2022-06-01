import stev.booleans.*;

import java.util.Arrays;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class Sudoku {

    public static void main(String[] args) {
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

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
//                System.out.println(Arrays.toString(allNumbersSort[i][j]));
            }
        }
//        System.out.println(allNumbersSort[1][0][0]);

        And prop2 = getProp("prop2",allNumbersSort);
        And prop3 = getProp("prop3",allNumbersSort);
        And prop1=getProp1(allNumbersSort);
//        System.out.println(prop1);
        getProp4(allNumbersSort);



    }

    public static void getProp4(PropositionalVariable[][][] allNumbersSort){
        And[] allSudoku = new And[3];
        And[] firstColumnOfSquarre = new And[3];
        Implies[] allNumberInCase = new Implies[9];
        
        for (int l = 0; l < 7; l += 3) {
            for (int n = 0; n < 9; n++) {
                allNumberInCase[n] = getNotOfBox(allNumbersSort,l,n);
            }
            And oneSquarre = new And(allNumberInCase);

            firstColumnOfSquarre[(l/3)] = oneSquarre;
        }

        System.out.println(firstColumnOfSquarre[0]);
        System.out.println(firstColumnOfSquarre[1]);
        System.out.println(firstColumnOfSquarre[2]);


    }

    public static And getProp1(PropositionalVariable[][][] allNumbersSort) {
        Implies[] tabImplic = new Implies[9];
        Not[] allColumnFirstLine = new Not[8];
        Or[] tabOrImplic = new Or[9];
        int m=0;
        int increment = 0;

            for (int l = 0; l < 9; l++) {
                for (int c = 0; c < 9; c++) {
                    for (int n = 0; n < 9; n++) {
                        if(allNumbersSort[l][c][n] != allNumbersSort[l][c][m]) {

                            allColumnFirstLine[increment] = new Not(allNumbersSort[l][c][n]);
                            increment++;
                        }
                        }
                    increment=0;

                    tabImplic[c] = new Implies(allNumbersSort[l][c][m], new Or(allColumnFirstLine));
                    m++;
                }
                m=0;






                tabOrImplic[l] = new Or(tabImplic);


            }


        return new And(tabOrImplic);
    }

    private static Implies getNotOfBox(PropositionalVariable[][][] allNumbersSort, int l, int n) {
        PropositionalVariable numberSelected = allNumbersSort[l][0][n];
        Not[] notTheSameNumberInBox = new Not[8];
        PropositionalVariable toKeep;
        int increment = 0;
            for (int i = 0; i < 10; i++) {
                if( i < 3 ){
                    toKeep = allNumbersSort[(l)][i][n];
                    if( toKeep != numberSelected){
                        notTheSameNumberInBox[increment] =  new Not(toKeep);
                        increment++;

                    }
                }
                else if(i < 6){
                    int j = i % 3;
                    toKeep = allNumbersSort[(1+l)][j][n];
                    if( toKeep != numberSelected){
                        notTheSameNumberInBox[increment] =  new Not(toKeep);
                        increment++;

                    }
                }else if(i < 9){
                    int j = i % 6;
                    toKeep = allNumbersSort[(2+l)][j][n];
                    if( toKeep != numberSelected){
                        notTheSameNumberInBox[increment] =  new Not(toKeep);
                        increment++;
                    }

                }


            }

        return new Implies(numberSelected,new And(notTheSameNumberInBox));
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
