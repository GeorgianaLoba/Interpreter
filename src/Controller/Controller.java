package Controller;

import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIList;
import Model.DataTypes.MyIStack;
import Model.MyException;
import Model.State.ProgramState;
import Model.Statement.IStatement;
import Model.Value.ReferenceValue;
import Model.Value.Value;
import Repository.IRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller implements IController {
    private IRepository repo;
    private ExecutorService executor;

    public Controller(IRepository repo){
        this.repo=repo;
    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symbolTableAddresses, Map<Integer, Value> heap){
        Map<Integer, Value> newHeap = new HashMap<>();

        for (Integer k: heap.keySet()) {
            if (symbolTableAddresses.contains(k)) {
                newHeap.put(k, heap.get(k));
            }
        }

        Map<Integer, Value> mp = heap.entrySet().stream().filter(e->symbolTableAddresses.contains(e.getKey())).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

         for (Map.Entry<Integer, Value> entry: mp.entrySet())   {
                Integer key = entry.getKey();
                Value val = entry.getValue();
                while (val instanceof ReferenceValue){
                    ReferenceValue refVal = (ReferenceValue)val;
                    int addr = refVal.getAddress();
                    Value val2 = heap.get(addr);
                    newHeap.put(addr, val2);
                    val=val2;
                }
            }
        return newHeap;
    }

    @Override
    public Map<Integer, Value> unsafeGarbageCollector(List<Integer> symbolTableAddresses, Map<Integer, Value> heap){
        return heap.entrySet().stream().filter(e->symbolTableAddresses.contains(e.getKey())).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Integer>getAddressesFromSymbolTable(Collection<Value> symbolTableValues){
        return symbolTableValues.stream().filter(v->v instanceof ReferenceValue).map(
                v->{ReferenceValue v1 = (ReferenceValue)v; return v1.getAddress();}).collect(Collectors.toList());
    }

    @Override
    public List<ProgramState> removeCompletedPrograms(List<ProgramState> programList) {
        return programList.stream().filter(p->p.isNotCompleted()).collect(Collectors.toList());
    }

    @Override
    public void oneStepForAllPrograms(List<ProgramState> programList) throws MyException, InterruptedException {
        //print ProgramState list
        List<Callable<ProgramState>> callList = programList.stream().
                map((ProgramState program) -> (Callable<ProgramState>)(() -> {
                    return program.oneStep();
                })).collect(Collectors.toList());
        List<ProgramState> newProgramList = executor.invokeAll(callList).stream().map(
                future->{
                    try{
                        return future.get();
                    }
                    catch (InterruptedException | ExecutionException e) {
                        System.out.println("Exception occured: " + e.toString() + "\n");
                    }
                    return null;
                }).filter(p->p!=null).collect(Collectors.toList());

        programList.addAll(newProgramList);

        programList.forEach(program-> {
            try {
                repo.logPrgStateExec(program);
            } catch (MyException e) {
                e.printStackTrace();
            }
        });
        repo.setProgramList(programList);
    }

    @Override
    public void allStep() throws MyException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState>programsList = removeCompletedPrograms(repo.getProgramList());
        while (programsList.size()>0){
            //garbage
            oneStepForAllPrograms(programsList);
            programsList = removeCompletedPrograms(repo.getProgramList());
        }
        executor.shutdownNow();
        repo.setProgramList(programsList);
    }

    public List<ProgramState> allStepForGui() throws MyException, InterruptedException {
        //called on click button
        List<ProgramState>programsList = removeCompletedPrograms(repo.getProgramList());
        if (programsList.size()>0){
            oneStepForAllPrograms(programsList);
        }
        else{
            executor.shutdownNow();
            throw new MyException("Program is done\n");
        }
        repo.setProgramList(programsList);
        return this.repo.getProgramList();
    }

    public void wrapperAllStepForGui(){
        //this is called only once in the initializer
        executor = Executors.newFixedThreadPool(2);
    }

    @Override
    public int size(){
        return this.repo.size();
    }

    @Override
    public IRepository getRepository() {
        return this.repo;
    }
}
