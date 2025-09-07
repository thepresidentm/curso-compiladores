package com.compiler.lexer.nfa;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a state in a Non-deterministic Finite Automaton (NFA).
 * Each state has a unique identifier, a list of transitions to other states,
 * and a flag indicating whether it is a final (accepting) state.
 *
 * <p>
 * Fields:
 * <ul>
 *   <li>{@code id} - Unique identifier for the state.</li>
 *   <li>{@code transitions} - List of transitions from this state to others.</li>
 *   <li>{@code isFinal} - Indicates if this state is an accepting state.</li>
 * </ul>
 *
 *
 * <p>
 * The {@code nextId} static field is used to assign unique IDs to each state.
 * </p>
 */
public class State {
    private static int nextId = 0;
    /**
     * Unique identifier for this state.
     */
    public final int id;

    /**
     * List of transitions from this state to other states.
     */
    public List<Transition> transitions;

    /**
     * Indicates if this state is a final (accepting) state.
     */
    public boolean isFinal;

    /**
     * Constructs a new state with a unique identifier and no transitions.
     * The state is not final by default.
     */
    public State() {
        this.id = nextId++; // Increments nextId after setting the this.id.
        this.transitions = new LinkedList<>();
        this.isFinal = false;
    }

    /**
     * Checks if this state is a final (accepting) state.
     * @return true if this state is final, false otherwise
     */
    public boolean isFinal() {
        // return this.transitions.isEmpty(); 
        return this.isFinal;
    }

    /**
     * Returns the states reachable from this state via epsilon transitions (symbol == null).
     * @return a list of states reachable by epsilon transitions
     */
    public List<State> getEpsilonTransitions() {
    // Pseudocode: Iterate over transitions, if symbol is null, add to result list
        LinkedList<State> result = new LinkedList<>();

        // Do we need the complete epsilon enclosure ???
        for (Transition transition : this.transitions)
            if(transition.symbol == null) result.add(transition.toState);
        
        return result;
    }

    /**
     * Returns the states reachable from this state via a transition with the given symbol.
     * @param symbol the symbol for the transition
     * @return a list of states reachable by the given symbol
     */
    public List<State> getTransitions(char symbol) {
    // Pseudocode: Iterate over transitions, if symbol matches, add to result list
        LinkedList<State> result = new LinkedList<>();


        // Do we need the complete enclosure ???
        for (Transition transition : this.transitions)
            if(transition.symbol == symbol) result.add(transition.toState);

        return result;
    }

    /**
     * Adds a new transition to the state.
     * @param symbol The symbol for the transition (null for epsilon).
     * @param toState The destination state.
     */
    public void addTransition(Character symbol, State toState){
        this.transitions.add(new Transition(symbol, toState));
    }

    @Override
    public String toString(){
        String transitions = "";
        for (Transition transition : this.transitions){
            transitions += transition.toString() + ", ";
        }
        return "{" + this.id + ": [" + transitions + "]}";
    }
}