import stev.booleans.And;
import stev.booleans.Not;
import stev.booleans.Or;
import stev.booleans.PropositionalVariable;

/**
 * Project: stev-booleans
 * Package: PACKAGE_NAME
 */
public class Sudoku {

    public static void main(String[] args) {
        PropositionalVariable[] lines = new PropositionalVariable[9];
        PropositionalVariable[] columns = new PropositionalVariable[9];
        PropositionalVariable[] numbers = new PropositionalVariable[9];

        for (int i = 0; i < 9 ; i++) {
            int j = i+1;
            lines[i] = new PropositionalVariable("l"+j);
            columns[i] = new PropositionalVariable("c"+j);
            numbers[i] = new PropositionalVariable("r"+j);

        }

        And andLines = new And(lines) ;
        And andColumnss = new And(columns) ;
        And andNumbers = new And(numbers) ;
        Or orNumbers = new Or(numbers) ;
        Or orColumns = new Or(columns) ;
        Or orLines = new Or(lines) ;

        //First proposition
        And eachCaseShouldHaveOneAtLeastOneNumber = new And(andLines,andColumnss,orNumbers);


        //Third proposition
        And eachNumberShouldBeAtLeastOneTImeInLine = new And(andLines,andNumbers,orColumns);

        //Third proposition
        And eachNumberShouldBeAtLeastOneTImeInColumn = new And(andColumnss,andNumbers,orLines);


        System.out.println(eachCaseShouldHaveOneAtLeastOneNumber);
        System.out.println(eachNumberShouldBeAtLeastOneTImeInLine);
        System.out.println(eachNumberShouldBeAtLeastOneTImeInColumn);

    }


}
