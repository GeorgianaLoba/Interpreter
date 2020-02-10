package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;

public class VariableDeclarationStatement implements IStatement {
    private String name;
    private Type type ;


    public VariableDeclarationStatement(String name, Type type){
        this.name = name;
        this.type = type;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        //this is simply saying int a or boolean v
        //when the interpreter receives "int a" it will put in the symbol table
        // a tuple("a", 0) <- a is considered the id and 0 is the default value
        //for an integer
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        if (symbolTable.isDefined(this.name)){
            throw new MyException("already exists");
        }
        symbolTable.add(this.name, this.type.defaultValue());
        return null;
    }

    @Override
    public String toString() {
        return this.name + " "+ this.type.toString();
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException { //omit
        VariableDeclarationStatement clone = (VariableDeclarationStatement) super.clone();
        clone.name = name;
        clone.type = (Type)this.type.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        typeEnvironment.put(this.name, this.type);
        return typeEnvironment;
    }
}
