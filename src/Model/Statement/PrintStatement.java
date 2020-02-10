package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIList;
import Model.Expression.Expression;
import Model.MyException;

import Model.State.ProgramState;
import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;

public class PrintStatement implements IStatement {
    private Expression expression;

    public PrintStatement(Expression expression){
        this.expression=expression;
    }

    @Override
    public String toString(){
        return "Print (" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIList<Value> out = state.getOut();
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        out.add(this.expression.evaluate(symbolTable, heap));
        return null;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        PrintStatement clone = (PrintStatement) super.clone();
        clone.expression = this.expression.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        this.expression.typecheck(typeEnvironment);
        return typeEnvironment;
    }
}
