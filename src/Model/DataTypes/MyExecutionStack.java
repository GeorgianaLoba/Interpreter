package Model.DataTypes;

import Model.MyException;

import java.util.Collection;
import java.util.Stack;

public class MyExecutionStack<T> implements MyIStack<T> {
    private Stack<T> stack;

    public MyExecutionStack(){
        this.stack= new Stack<T>();
    }

    @Override
    public T pop() throws MyException {
        try{
            return this.stack.pop();
        }
        catch (Exception e){
            throw new MyException("The stack is empty.");
        }
    }


    @Override
    public void push(T v) {
        this.stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        if (this.stack.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int size() {
        return this.stack.size();
    }

    @Override
    public Stack<?> clone() {
        return (Stack<?>) this.stack.clone();
    }


    @Override
    public String toString() {
        StringBuilder tipar = new StringBuilder();
        for (T el: this.stack){
            tipar.append(el.toString()+"; ");
        }
        return tipar.toString();
    }

}
