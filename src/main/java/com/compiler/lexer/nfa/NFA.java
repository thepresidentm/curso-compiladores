package com.compiler.lexer.nfa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a Non-deterministic Finite Automaton (NFA) with a start and end state.
 * <p>
 * An NFA is used in lexical analysis to model regular expressions and pattern matching.
 * This class encapsulates the start and end states of the automaton.
 */

public class NFA {
    /**
     * The initial (start) state of the NFA.
     */
    public  State startState;

    /**
     * The final (accepting) state of the NFA.
     */
    public  State endState;

    /**
     * Constructs a new NFA with the given start and end states.
     * @param start The initial state.
     * @param end The final (accepting) state.
     */
    public NFA(State start, State end) {
        this.startState = start;
        this.endState = end;
    }

    /**
     * Returns the initial (start) state of the NFA.
     * @return the start state
     */
    public State getStartState() {
        return this.startState;
    }

    /**
     * Returns the final (end) state of the NFA.
     * @return the end state
     */
    public State getEndState() {
        return this.startState;
    }

    @Override
    public String toString(){
        String output = "";
        Stack<State> stack = new Stack<>();
        Set<State> visited = new HashSet<>();
        stack.push(this.startState);
        while(!stack.empty()){
            State current = stack.pop();
            visited.add(current);
            output += current.toString() + ", ";
            for (Transition transition : current.transitions)
                if (!visited.contains(transition.toState))stack.add(transition.toState);
        }
        return output;
    }
}