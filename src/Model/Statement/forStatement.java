package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIStack;
import Model.Expression.Expression;
import Model.Expression.RelationalExpression;
import Model.Expression.VariableExpression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.BooleanType;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.BooleanValue;
import Model.Value.Value;

import java.util.Map;

public class forStatement implements IStatement{
    private String variable;
    private Expression expr1, expr2, expr3;
    private IStatement statement;

    public forStatement(String variable, Expression expr1, Expression expr2, Expression expr3, IStatement statement){
        this.variable= variable;
        this.expr1=expr1;
        this.expr2=expr2;
        this.expr3=expr3;
        this.statement=statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack stack = state.getExecutionStack();

        IStatement newStatement = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(
                new AssignmentStatement("v", expr1),
                new While (new RelationalExpression(new VariableExpression("v"), expr2, 1),
                        new CompoundStatement(statement, new AssignmentStatement("v", expr3)))));
        stack.push(newStatement);
        return null;
    }


    @Override
    public String toString() {
        return "FOR(" + variable +"="+ this.expr1.toString()+";" + variable + "<" + this.expr2.toString()
                + ";" + variable+"="+this.expr3.toString()+") "+ this.statement.toString();
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        return null;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        typeEnvironment.put("v", new IntType());
        Type typeExpr1 = this.expr1.typecheck(typeEnvironment);
        Type typeExpr2= this.expr2.typecheck(typeEnvironment);
        Type typeExpr3 = this.expr3.typecheck(typeEnvironment);
        if (!(typeExpr1.equals(new IntType())))
            throw new MyException("Not int\n");
        if (!(typeExpr2.equals(new IntType())))
            throw new MyException("Not int\n");
        if (!(typeExpr3.equals(new IntType())))
            throw new MyException("Not int\n");

        return typeEnvironment;
    }
}
