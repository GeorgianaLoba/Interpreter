package Controller;

import Model.MyException;
import Model.State.ProgramState;
import Model.Value.Value;
import Repository.IRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IController {
    List<Integer>getAddressesFromSymbolTable(Collection<Value> symbolTableValues);
    Map<Integer, Value> unsafeGarbageCollector(List<Integer> symbolTableAddresses, Map<Integer, Value> heap);
    Map<Integer, Value> safeGarbageCollector(List<Integer> symbolTableAddresses, Map<Integer, Value> heap);
    List<ProgramState> removeCompletedPrograms(List<ProgramState> programList);
    void allStep() throws MyException, InterruptedException;
    void oneStepForAllPrograms(List<ProgramState> programList) throws MyException, InterruptedException;
    int size();
    IRepository getRepository();
}
