package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIStack;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class unlock implements IStatement {
    private String variable;

    public  unlock(String variable){
        this.variable=variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Lock lock = new ReentrantLock();
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Integer> lockTable = state.getLockTable();
        MyIStack<IStatement> exeStack = state.getExecutionStack();
        lock.lock();
        //critical section
        if (symbolTable.isDefined(variable)) {
            Value variableValue = symbolTable.lookup(variable);
            Type variableType = (symbolTable.lookup(variable)).getType();
            if (variableType.equals(new IntType())){
                Value found = symbolTable.lookup(variable);
                Integer index = (Integer) found.getValue();
                if (lockTable.isDefined(index)){
                    Integer identifier = lockTable.lookup(index);
                    if (identifier==state.getId()){
                        lockTable.update(index, -1);
                    }
                    else{
                        //do nothing
                    }
                }
                else{
                    throw new MyException("not in locktable\n");
                }
            }
            else{
                throw new MyException("not int\n");
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
        return null;
        //TODO
    }

    @Override
    public String toString() {
        return "unlock("+variable+")";
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeVariable = typeEnvironment.get(variable);
        if (!(typeVariable.equals(new IntType())))
            throw new MyException("not int\n");
        return typeEnvironment;
    }
}
