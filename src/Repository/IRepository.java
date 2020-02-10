package Repository;
import Model.MyException;

import Model.State.ProgramState;

import java.util.List;

public interface IRepository {
    void addProgram(ProgramState program);
    void logPrgStateExec(ProgramState program) throws MyException;
    List<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> newPrograms);
    int size();
    ProgramState get(Integer id);
}
