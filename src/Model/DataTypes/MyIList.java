package Model.DataTypes;

import Model.MyException;

public interface MyIList<T> { //interface for the OUT
    void add(T elem); //append to end of the list
    void add(int pos, T elem) throws MyException;
    void remove(T elem) throws MyException;
    void remove(int pos) throws MyException;
    T get(int pos) throws MyException;
    int size();
    boolean isEmpty();
}
