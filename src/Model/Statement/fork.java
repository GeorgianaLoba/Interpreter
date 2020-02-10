package Model.Statement;

import Model.DataTypes.*;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.Type;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class fork implements IStatement {
    private IStatement statement;

    public fork(IStatement statement){
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        //SymbolTable and ExecutionStack unique in every thread
        //Need to make deepcopy of SymbolTable and new ExecutionStack
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();

        //These are common between threads: Heap, FileTables, Out
        MyIDictionary<Integer, Value> heap = state.getHeap();
        MyIList<Value> out = state.getOut();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<Integer, Integer> lockTable = state.getLockTable();


        MyIDictionary<String, Value> newSymbolTable = new MySymbolTable<String, Value>();
        for (Map.Entry entry: symbolTable.getContent().entrySet()){
            newSymbolTable.add((String)entry.getKey(), (Value)entry.getValue());
        }
        MyIStack<IStatement> newExecutionStack = new MyExecutionStack<IStatement>();
        return new ProgramState(newExecutionStack, newSymbolTable, out,fileTable, heap,lockTable, statement, ProgramState.newId());
    }

    @Override
    public String toString() {
        return "Fork(*"+this.statement.toString()+"*)";
    }


    @Override
    public IStatement clone() throws CloneNotSupportedException {
        fork clone = (fork) super.clone();
        clone.statement = (IStatement)this.statement.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Map<String, Type> cloned = new HashMap<>(typeEnvironment);
        this.statement.typecheck(cloned);
        return typeEnvironment;
    }
}
