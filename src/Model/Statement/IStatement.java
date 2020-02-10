package Model.Statement;

import Model.MyException;

import Model.State.ProgramState;
import Model.Type.Type;

import java.util.Map;

public interface IStatement {
    ProgramState execute(ProgramState state) throws MyException;
    IStatement clone() throws CloneNotSupportedException;
    Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException;
}
