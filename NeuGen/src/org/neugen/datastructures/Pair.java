package org.neugen.datastructures;

/**
 * This class couples together a pair of values,
 * which may be of different types (FirstClass and SecondClass).
 *
 * @author Alexander Wanner
 * @param <FirstClass> First template parameter
 * @param <SecondClass> Second template parameter
 */
public class Pair<FirstClass, SecondClass> {

    /** First template parameter */
    public FirstClass first;
    /** Second template parameter */
    public SecondClass second;

    public Pair() {
        first = null;
        second = null;
    }

    /** pair objects are constructed from a pair of values */
    public Pair(FirstClass first, SecondClass second) {
        this.first = first;
        this.second = second;
    }
}
