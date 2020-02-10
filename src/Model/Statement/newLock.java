package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyLockTable;
import Model.Expression.ValueExpression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class newLock implements IStatement {
    private String variable;

    public newLock(String variable){
        this.variable=variable;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Lock lock = new ReentrantLock();
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Integer> lockTable = state.getLockTable();
        lock.lock();
        //critical section
        if (symbolTable.isDefined(variable)){
            Value variableValue = symbolTable.lookup(variable);
            Type variableType = (symbolTable.lookup(variable)).getType();
            if (variableType.equals(new IntType())){
                lockTable.add(MyLockTable.getFreeLocation(), -1);
                symbolTable.update(variable, new IntValue(MyLockTable.getFreeLocation()));
                MyLockTable.moveFreeLocation();
            }
            else{
                throw new MyException("not of type int");
            }
        }
        else{
            throw new MyException("not in symbol table\n");
        }

        lock.unlock();
        return null;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        //todo
        return null;
    }

    @Override
    public String toString() {
        return "newLock("+variable+")";
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeVariable = typeEnvironment.get(variable);
        if (!(typeVariable.equals(new IntType())))
            throw new MyException("not int\n");
        return typeEnvironment;
    }
}
