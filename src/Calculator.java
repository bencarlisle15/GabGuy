import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
public class Calculator {
    // private instance variables
    private String error = "An error has occured";
    private String[] operatorChars = {"^", "x", "*", "/", "+", "-"};

    public boolean checkEq(String eq) {
        // uses definition of one side of an equation and regex to check it
        String oper = "(\\^|\\+|\\-|\\*|x|\\/)";
        String num = "(([0-9]+)(\\.?)([0-9]*))";
        if (Pattern.compile("(" + num + "(" + oper + num + ")*)")
                        .matcher(eq)
                        .matches())
            return true;
        return false;
    }

    public String equationSolver(String equat) {
        // if the equation is only one number
        if (Pattern.compile("([0-9]+)(\\.?)([0-9]*)").matcher(equat).matches())
            return equat;
        // ArrayLists of parts of the equation
        ArrayList<Double> terms = new ArrayList<Double>();
        ArrayList<String> operators = new ArrayList<String>();
        ArrayList<Integer> positions = new ArrayList<Integer>();
        // searches for operators and adds their positions to the ArrayList
        for (int t = 0; t < operatorChars.length; t++) {
            for (int i = 0; i < equat.length(); i++) {
                if (equat.substring(i, i + 1).equals(operatorChars[t]))
                    positions.add(i);
            }
        }
        int endPos = 0;
        int startPos = -1;
        // puts the positions ArrayList in order of position
        Collections.sort(positions);
        // adds the chars from the last pos to the position to the terms ArrayList
        // and the next char to the terms one
        for (int i = 0; i < positions.size(); i++) {
            endPos = positions.get(i);
            terms.add(cStr(equat.substring(startPos + 1, endPos)));
            operators.add(equat.substring(endPos, endPos + 1));
            startPos = endPos;
        }
        // the rest of the chars must be a term
        terms.add(cStr(equat.substring(endPos + 1)));
        // loops through the operatorChars array to insure PEMDAS is used
        for (int i = 0; i < operatorChars.length; i++)
            for (int t = 0; t < operators.size(); t++)
                if (operatorChars[i].equals(operators.get(t))) {
                    // sets the term at the same pos as the operator equal to the
                    // operation between that and the second term
                    terms.set(t,
                                        twoTerms(operators.get(t), terms.get(t), terms.get(t + 1)));
                    // removes the next term and removes the operator at the current pos
                    // and subsequently t by one
                    terms.remove(t + 1);
                    operators.remove(t);
                    t--;
                }
        // only one term will be left and it is the answer
        double ans = terms.get(0);
        // changes the format to be more pleasing
        return changeFormat(cDub(ans));
    }

    private String changeFormat(String ans) {
        String fin = ans;
        // returns an error if the answer is infinity
        if (fin.equals("Infinity"))
            fin = error;
        // if the answer has no decimals
        else if (cStr(ans) == Math.ceil(cStr(ans)))
            // removes the decimal added as a result if being a double
            fin = new DecimalFormat("#").format(cStr(ans));
        return fin;
    }

    private double twoTerms(String type, double term1, double term2) {
        // find the operator and does the operation
        double ans = 0;
        if (type.equals("^"))
            ans = Math.pow(term1, term2);
        else if (type.equals("*") || type.equals("x"))
            ans = term1 * term2;
        else if (type.equals("/"))
            ans = term1 / term2;
        else if (type.equals("+"))
            ans = term1 + term2;
        else if (type.equals("-"))
            ans = term1 - term2;
        return ans;
    }

    private double cStr(String a) {
        // returns the String in double format
        return Double.parseDouble(a);
    }

    private String cDub(Double a) {
        // returns the double in String format
        return String.valueOf(a);
    }
}