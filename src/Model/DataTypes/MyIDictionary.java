package Model.DataTypes;

import Model.MyException;

import java.util.Map;

public interface MyIDictionary<T1, T2> {
    void add(T1 key, T2 value) throws MyException;
    void update(T1 key, T2 value) throws MyException;
    T2 remove(T1 key) throws MyException;
    boolean isEmpty();
    T2 lookup(T1 key) throws MyException;
    boolean isDefined(T1 key); //true if the key is in dictionary
    Map<T1, T2> getContent();
    void setContent(Map<T1, T2> map);
    int size();
}
