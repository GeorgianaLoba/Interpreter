package Model.Statement;

import Model.MyException;
import Model.State.ProgramState;
import Model.Type.Type;

import java.util.Map;

public class nop implements IStatement {
    public nop(){
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        return null;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        return null;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        return typeEnvironment;
    }
}
