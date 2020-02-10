package Model.State;
import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIList;
import Model.DataTypes.MyIStack;
import Model.Statement.IStatement;
import Model.Value.StringValue;
import Model.Value.Value;
import Model.MyException;
import java.io.BufferedReader;

public class ProgramState {
    private MyIStack<IStatement> executionStack;
    private MyIDictionary<String, Value> symbolTable;
    private MyIList<Value> out;
    private MyIDictionary<StringValue, BufferedReader> fileTable;
    private MyIDictionary<Integer, Value> heap;
    private MyIDictionary<Integer, Integer> lockTable;
    private IStatement program;
    private int id;
    static int currentId =1;

    public ProgramState(MyIStack<IStatement> executionStack,
                        MyIDictionary<String, Value> symbolTable, MyIList<Value> out,
                        MyIDictionary<StringValue, BufferedReader> fileTable,
                        MyIDictionary<Integer, Value> heap, MyIDictionary<Integer, Integer> lockTable, IStatement program, int id){
        this.executionStack=executionStack;
        this.symbolTable=symbolTable;
        this.out=out;
        this.fileTable=fileTable;
        this.heap = heap;
        this.lockTable=lockTable;
        this.program = program;
        this.executionStack.push(program);
        this.id=id;
    }

    public String toString(){
        StringBuilder tipar = new StringBuilder();
        tipar.append("ID: " + getId() + "\n");
        tipar.append("Execution Stack: {" );
        tipar.append(this.executionStack.toString());
        tipar.append("} \nSymbol Table: {" );
        tipar.append(this.symbolTable.toString() );
        tipar.append("} \nOut: {");
        tipar.append(this.out.toString());
        tipar.append("} \nFile Table: {");
        tipar.append(this.fileTable.toString());
        tipar.append("} \nHeap: {");
        tipar.append(this.heap.toString());
        tipar.append("} \n");
        return tipar.toString();
    }

    public IStatement getProgram() {
        return program;
    }

    public boolean isNotCompleted(){
        return !(this.executionStack.isEmpty());
    }

    public ProgramState oneStep() throws MyException{
        if (this.executionStack.isEmpty()){
            throw new MyException("program state is empty \n");
        }
        IStatement currentStatement = this.executionStack.pop();
        return currentStatement.execute(this);
    }

    public MyIStack<IStatement> getExecutionStack(){
        return this.executionStack;
    }

    public MyIDictionary<String, Value> getSymbolTable(){
        return this.symbolTable;
    }

    public MyIList<Value> getOut(){
        return this.out;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable(){
        return this.fileTable;
    }

    public MyIDictionary<Integer, Value> getHeap() {
        return this.heap;
    }

    public int getId() {
        return id;
    }

    public MyIDictionary<Integer, Integer> getLockTable() {
        return lockTable;
    }

    public synchronized static int newId(){
        currentId++;
        return currentId;
    }
}
