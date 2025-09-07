package com.compiler.lexer.regex;

import java.util.Stack;

/**
 * Utility class for regular expression parsing using the Shunting Yard
 * algorithm.
 * <p>
 * Provides methods to preprocess regular expressions by inserting explicit
 * concatenation operators, and to convert infix regular expressions to postfix
 * notation for easier parsing and NFA construction.
 */
/**
 * Utility class for regular expression parsing using the Shunting Yard
 * algorithm.
 */
public class ShuntingYard {

    /**
     * Default constructor for ShuntingYard.
     */
    public ShuntingYard() {
        // TODO: Implement constructor if needed
    }

    /**
     * Inserts the explicit concatenation operator ('·') into the regular
     * expression according to standard rules. This makes implicit
     * concatenations explicit, simplifying later parsing.
     *
     * @param regex Input regular expression (may have implicit concatenation).
     * @return Regular expression with explicit concatenation operators.
     */
    public static String insertConcatenationOperator(String regex) {
        /*
            Pseudocode:
            For each character in regex:
                - Append current character to output
                - If not at end of string:
                        - Check if current and next character form an implicit concatenation
                        - If so, append '·' to output
            Return output as string
         */
        String output = "";
        for (int i = 0; i < regex.length(); i++){
            Character current = regex.charAt(i);
            output += current;
            if (i == regex.length() - 1) continue; // Iterator at the end of string
            Character next = regex.charAt(i + 1);

            // In these cases current and next do not form implicit concat
            if(
                current == '(' ||
                current == '|' ||
                current == '·' ||
                next == ')' ||
                next == '*' ||
                next == '+' ||
                next == '?' ||
                next == '|' ||
                next == '·'
            ) continue;

            output += '·';
        }
        return output;
    }


    /**
     * Determines if the given character is an operand (not an operator or
     * parenthesis).
     *
     * @param c Character to evaluate.
     * @return true if it is an operand, false otherwise.
     */
    private static boolean isOperand(char c) {
        /*
        Pseudocode:
        Return true if c is not one of: '|', '*', '?', '+', '(', ')', '·'
         */
        return
            c != '|' &&
            c != '*' &&
            c != '?' &&
            c != '+' &&
            c != '(' &&
            c != ')' &&
            c != '·';
    }

    /**
     * Converts an infix regular expression to postfix notation using the
     * Shunting Yard algorithm. This is useful for constructing NFAs from
     * regular expressions.
     *
     * @param infixRegex Regular expression in infix notation.
     * @return Regular expression in postfix notation.
     */
    public static String toPostfix(String infixRegex) {
        /*
        Pseudocode: Wikipedias pseudocode.
         */
        Stack<Character> stack = new Stack<>();
        String output = "";
        String infix = ShuntingYard.insertConcatenationOperator(infixRegex);

        for (int i = 0; i < infix.length(); i++){

            Character currentCharacter = infix.charAt(i);

            switch (currentCharacter) {

                case '(':
                    stack.push(currentCharacter);
                    break;

                case ')':
                    for (Character operator = stack.pop(); operator != '('; operator = stack.pop())
                        output += operator;
                    break;
                
                case '*':
                case '+':
                case '?':
                case '|':
                case '·':
                    while (!stack.isEmpty()) {
                        if(
                            stack.peek() == '(' ||
                            ShuntingYard.getPrecedence(stack.peek()) < ShuntingYard.getPrecedence(currentCharacter))
                            break;
                        output += stack.pop();
                    }
                    stack.push(currentCharacter);
                    break;

                default: // Didnt match previous cases implies is operand
                    output += currentCharacter;
                    break;
            }

        }

        while (!stack.isEmpty()) output += stack.pop();

        return output;
    }

    private static int getPrecedence(Character c){
        if(c == '*' || c == '?' || c == '+' ) return 2;
        if (c == '·') return 1;
        return 0;
    }

}
