package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.Expression.Expression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.IntType;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class readFile implements IStatement {
    private Expression expression;
    private String var_name;

    public readFile(Expression expression, String var_name){
        this.expression=expression;
        this.var_name=var_name;
    }

    @Override
    public String toString() {
        return "readFile: (" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        if (symbolTable.isDefined(this.var_name) &&
                symbolTable.lookup(this.var_name).getType().equals(new IntType())){
                Value val = this.expression.evaluate(symbolTable, heap);
                if (val.getType().equals(new StringType())){
                    if (fileTable.isDefined((StringValue)val)){
                        BufferedReader buff = fileTable.lookup((StringValue) val);
                        try{
                            String line = buff.readLine();
                            IntValue value;
                            if (line.equals("")){
                                value=new IntValue(0);
                            }
                            else{
                                value = new IntValue(Integer.parseInt(line));
                            }
                            symbolTable.update(this.var_name, value);
                            return null;
                        }
                        catch (Exception e){
                            throw new MyException("Unable to read, foll error occured: " + e.toString());
                        }
                    }
                    else{
                        throw new MyException("the file not defined in filetable \n");
                    }
                }
                else{
                    throw new MyException("not string type\n");
                }
        }
        else{
            throw new MyException("var_name not in symbol table or/and not int type\n");
        }
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        readFile clone = (readFile)super.clone();
        clone.expression = this.expression.clone();
        clone.var_name = var_name;
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeExpression = this.expression.typecheck(typeEnvironment);
        if (!(typeExpression.equals(new StringType())))
            throw new MyException("ReadFile: not string type\n");
        return typeEnvironment;
    }
}
