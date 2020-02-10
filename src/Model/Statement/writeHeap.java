package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.Expression.Expression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.ReferenceType;
import Model.Type.Type;
import Model.Value.ReferenceValue;
import Model.Value.Value;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class writeHeap implements IStatement {
    private String variable_name;
    private Expression expression;

    public writeHeap(String variable_name, Expression expression){
        this.variable_name=variable_name;
        this.expression=expression;
    }

    @Override
    public String toString() {
        return "writeHeap (" + variable_name +","+expression.toString()+")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();

        Lock lock = new ReentrantLock();
        lock.lock();
        if (!(symbolTable.isDefined(this.variable_name))){
            throw new MyException("not defined in symbol table\n");
        }

        Value val = symbolTable.lookup(this.variable_name);

        if (!(val.getType() instanceof ReferenceType)){
            throw  new MyException("not reference type\n");
        }

        ReferenceValue refVal = (ReferenceValue) val;

        if (!(heap.isDefined(refVal.getAddress()))){
            throw new MyException("not a valid address in heap\n");
        }

        Value evalution = this.expression.evaluate(symbolTable, heap);
        ReferenceType refType = (ReferenceType)refVal.getType();
        if (!(refType.getInner().equals(evalution.getType()))){
            throw new MyException("the types are not equal\n");
        }

        heap.update(refVal.getAddress(), evalution);
        lock.unlock();
        return null;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        writeHeap clone = (writeHeap) super.clone();
        clone.expression = this.expression.clone();
        clone.variable_name = variable_name;
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeVariable = typeEnvironment.get(this.variable_name);
        Type typeExpression=this.expression.typecheck(typeEnvironment);
        System.out.println("Here");
        if (!(typeVariable.equals(new ReferenceType(typeExpression))))
            throw new MyException("WriteHeap: types don't match\n");
        return typeEnvironment;
    }
}
