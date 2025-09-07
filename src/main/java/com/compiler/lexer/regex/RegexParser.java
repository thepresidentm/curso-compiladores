package com.compiler.lexer.regex;

import java.util.Stack;

import com.compiler.lexer.nfa.NFA;
import com.compiler.lexer.nfa.State;

/**
 * RegexParser
 * -----------
 * This class provides functionality to convert infix regular expressions into nondeterministic finite automata (NFA)
 * using Thompson's construction algorithm. It supports standard regex operators: concatenation (·), union (|),
 * Kleene star (*), optional (?), and plus (+). The conversion process uses the Shunting Yard algorithm to transform
 * infix regex into postfix notation, then builds the corresponding NFA.
 *
 * Features:
 * - Parses infix regular expressions and converts them to NFA.
 * - Supports regex operators: concatenation, union, Kleene star, optional, plus.
 * - Implements Thompson's construction rules for NFA generation.
 *
 * Example usage:
 * <pre>
 *     RegexParser parser = new RegexParser();
 *     NFA nfa = parser.parse("a(b|c)*");
 * </pre>
 */
/**
 * Parses regular expressions and constructs NFAs using Thompson's construction.
 */
public class RegexParser {
    /**
     * Default constructor for RegexParser.
     */
        public RegexParser() {
            // TODO: Implement constructor if needed
        }

    /**
     * Converts an infix regular expression to an NFA.
     *
     * @param infixRegex The regular expression in infix notation.
     * @return The constructed NFA.
     */
    public NFA parse(String infixRegex) {
        // Pseudocode: Convert infix to postfix, then build NFA from postfix

        if (infixRegex.length() == 0) return handleEmpty();

        return this.buildNfaFromPostfix(ShuntingYard.toPostfix(infixRegex));
    }

    /**
     * Builds an NFA from a postfix regular expression.
     *
     * @param postfixRegex The regular expression in postfix notation.
     * @return The constructed NFA.
     */
    private NFA buildNfaFromPostfix(String postfixRegex) {
        // Pseudocode: For each char in postfix, handle operators and operands using a stack

        Stack<NFA> stack = new Stack<>();

        for(int i = 0; i < postfixRegex.length(); i++){

            Character currentCharacter = postfixRegex.charAt(i);

            switch (currentCharacter) {
                case '?':
                    this.handleOptional(stack);
                    break;
                case '+':
                    this.handlePlus(stack);
                    break;
                case '·':
                    this.handleConcatenation(stack);
                    break;
                case '|':
                    this.handleUnion(stack);
                    break;
                case '*':
                    this.handleKleeneStar(stack);
                    break;
                default:
                    this.createNfaForCharacter(stack, currentCharacter);
                    break;
            }
        }
        return stack.pop();
    }

    private NFA handleEmpty(){
        State a = new State();
        return new NFA(a, a);
    }

    /**
     * Handles the '?' operator (zero or one occurrence).
     * Pops an NFA from the stack and creates a new NFA that accepts zero or one occurrence.
     * @param stack The NFA stack.
     */
    private void handleOptional(Stack<NFA> stack) {
        // Pseudocode: Pop NFA, create new start/end, add epsilon transitions for zero/one occurrence
        NFA nfa = stack.pop();
        State start = new State();
        State end = new State();

        start.addTransition(null, nfa.startState);
        nfa.endState.addTransition(null, end);
        start.addTransition(null, end);

        stack.push(new NFA(start, end));
    }

    /**
     * Handles the '+' operator (one or more occurrences).
     * Pops an NFA from the stack and creates a new NFA that accepts one or more occurrences.
     * @param stack The NFA stack.
     */
    private void handlePlus(Stack<NFA> stack) {
        // Pseudocode: Pop NFA, create new start/end, add transitions for one or more occurrence
        NFA nfa = stack.pop();
        State start = new State();
        State end = new State();

        start.addTransition(null, nfa.startState);
        nfa.endState.addTransition(null, end);
        end.addTransition(null, start);

        stack.push(new NFA(start, end));
    }
    
    /**
     * Creates an NFA for a single character.
     * @param c The character to create an NFA for.
     * @param stack The NFA stack.
     */
    private void createNfaForCharacter(Stack<NFA> stack, char c) {
        // Pseudocode: Create start/end state, add transition for character
        State start = new State();
        State end = new State();

        start.addTransition(c, end);

        stack.push(new NFA(start, end));
    }

    /**
     * Handles the concatenation operator (·).
     * Pops two NFAs from the stack and connects them in sequence.
     * @param stack The NFA stack.
     */
    private void handleConcatenation(Stack<NFA> stack) {
        // Pseudocode: Pop two NFAs, connect end of first to start of second
        NFA second = stack.pop();
        NFA first = stack.pop();

        first.endState.addTransition(null, second.startState);

        stack.push(new NFA(first.startState, second.endState));
    }

    /**
     * Handles the union operator (|).
     * Pops two NFAs from the stack and creates a new NFA that accepts either.
     * @param stack The NFA stack.
     */
    private void handleUnion(Stack<NFA> stack) {
        // Pseudocode: Pop two NFAs, create new start/end, add epsilon transitions for union
        NFA first = stack.pop();
        NFA second = stack.pop();
        
        State start = new State();
        State end = new State();

        start.addTransition(null, first.startState);
        start.addTransition(null, second.startState);
        first.endState.addTransition(null, end);
        second.endState.addTransition(null, end);

        stack.push(new NFA(start, end));
    }

    /**
     * Handles the Kleene star operator (*).
     * Pops an NFA from the stack and creates a new NFA that accepts zero or more repetitions.
     * @param stack The NFA stack.
     */
    private void handleKleeneStar(Stack<NFA> stack) {
        // Pseudocode: Pop NFA, create new start/end, add transitions for zero or more repetitions
        NFA nfa = stack.pop();
        State start = new State();
        State end = new State();

        start.addTransition(null, end);
        end.addTransition(null, start);
        start.addTransition(null, nfa.startState);
        nfa.endState.addTransition(null, end);

        stack.push(new NFA(start, end));
    }

    /**
     * Checks if a character is an operand (not an operator).
     * @param c The character to check.
     * @return True if the character is an operand, false if it is an operator.
     */
    private boolean isOperand(char c) {
        // Pseudocode: Return true if c is not an operator
        return
            c != '|' &&
            c != '*' &&
            c != '?' &&
            c != '+' &&
            c != '(' &&
            c != ')' &&
            c != '·';
    }
}