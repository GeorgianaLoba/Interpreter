package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.Expression.Expression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

public class openRFile implements IStatement {
    private Expression expression;

    public openRFile(Expression expression){ this.expression=expression;}


    @Override
    public String toString() {
        return "open_RFile: (" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        Value value = this.expression.evaluate(symbolTable, heap);
        if (value.getType().equals(new StringType())){
            StringValue sVal = (StringValue) value;
            if (fileTable.isDefined(sVal)){
                throw new MyException("The filename is already defined\n");
            }
            else{
                try{
                    BufferedReader buff = new BufferedReader(new FileReader(sVal.getValue()));
                    fileTable.add(sVal, buff);
                    return null;
                }
                catch (Exception e){
                    throw new MyException("Error when opening the file: " + e.toString());
                }
            }
        }
        else{
            throw new MyException("not string\n");
        }
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        openRFile clone = (openRFile)super.clone();
        clone.expression = this.expression.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type type = this.expression.typecheck(typeEnvironment);
        if (!(type.equals(new StringType())))
            throw new MyException("OpenFile: not string type\n");
        return typeEnvironment;
    }
}
