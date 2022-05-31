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
                System.out.println(Arrays.toString(allNumbersSort[i][j]));
            }
        }
        Implies[] test = new Implies[9];
        Not[] allColumnFirstLine = new Not[8];


        for (int n = 0; n < 9; n++) {
            for (int i = 1; i < 9; i++) {
                allColumnFirstLine[i-1] =  new Not(allNumbersSort[0][i][n]);
            }
            test[n] = new Implies(allNumbersSort[0][0][n],new And(allColumnFirstLine));
        }




        System.out.println(new Or(test));

    }

}
