package Model.Statement;

import Model.DataTypes.MyIStack;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.Type;

import java.util.Map;

public class CompoundStatement implements IStatement {
    private IStatement first;
    private IStatement second;

    public CompoundStatement(IStatement first, IStatement second){
        this.first=first;
        this.second=second;
    }

    @Override
    public String toString(){
        return "(" + first.toString() + ";" + second.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> stack = state.getExecutionStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException{
        CompoundStatement clone = (CompoundStatement) super.clone();
        clone.first=this.first.clone();
        clone.second=this.second.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        return second.typecheck(first.typecheck(typeEnvironment));
    }
}
