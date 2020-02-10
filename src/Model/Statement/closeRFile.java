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
import java.io.IOException;
import java.util.Map;

public class closeRFile implements IStatement {
    private Expression expression;

    public closeRFile(Expression expression){this.expression=expression;}

    @Override
    public String toString() {
        return "close_RFile (" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        Value val = this.expression.evaluate(symbolTable, heap);
        if (val.getType().equals(new StringType())) {
            StringValue sVal = (StringValue) val;
            if (fileTable.isDefined(sVal)) {
                BufferedReader buff = fileTable.lookup(sVal);
                try {
                    buff.close();
                    fileTable.remove(sVal);
                    return null;
                } catch (IOException e) {
                    throw new MyException("couldn't close file\n");
                }
            }
            else{
                throw new MyException("not defined in file table\n");
            }
        }
        else{
            throw new MyException("not string type\n");
        }
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        closeRFile clone = (closeRFile)super.clone();
        clone.expression = this.expression.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeExpression = this.expression.typecheck(typeEnvironment);
        if (!(typeExpression.equals(new StringType())))
            throw new MyException("CloseFile: not String\n");
        return typeEnvironment;
    }

}
