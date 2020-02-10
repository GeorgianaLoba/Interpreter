package Model.DataTypes;

import Model.MyException;

import java.util.LinkedList;

public class MyOut<T> implements MyIList<T> {
    private LinkedList<T> list;

    public MyOut(){
        this.list=new LinkedList<T>();
    }

    @Override
    public void add(T elem) {
        this.list.add(elem);
    }

    @Override
    public void add(int pos, T elem) throws MyException {
        try {
            this.list.add(pos, elem);
        }
        catch (Exception e){
            throw new MyException("Invalid position");
        }
    }

    @Override
    public void remove(T elem) throws MyException{
        try {
            this.list.remove(elem);
        }
        catch (Exception e){
            throw new MyException("The specified element is not present in the list");
        }
    }

    @Override
    public void remove(int pos) throws MyException {
        try {
            this.list.remove(pos);
        }
        catch (Exception e){
            throw new MyException("The position is invalid");
        }
    }

    @Override
    public T get(int pos) throws MyException{
        try {
            return this.list.get(pos);
        }
        catch (Exception e){
            throw new MyException("The position is invalid");
        }
    }

    @Override
    public int size() {
        return this.list.size();
    }


    @Override
    public boolean isEmpty() {
        if (this.list.size()==0)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder tipar = new StringBuilder();
        for (T elem: this.list){
            tipar.append("Printed: ").append(elem.toString()).append("; ");
        }
        return tipar.toString();
    }
}
