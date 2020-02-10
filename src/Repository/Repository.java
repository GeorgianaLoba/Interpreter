package Repository;

import Model.MyException;
import Model.State.ProgramState;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<ProgramState> states;
    private String filePath;

    public Repository(String filePath){
        this.filePath=filePath;
        this.states = new ArrayList<ProgramState>();
    }

    @Override
    public void addProgram(ProgramState program) {
        this.states.add(program);
    }

    @Override
    public void logPrgStateExec(ProgramState program) throws MyException {
        try {

            //PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.filePath, true)));
            // for the hierarchy visualisation >
            FileWriter fw = new FileWriter(this.filePath, true);
            //BufferedWriter bw = new BufferedWriter(fw);
            //PrintWriter pw = new PrintWriter(bw);
            //pw.println(this.states.getFirst().toString());
            fw.write(program.toString());
            fw.close();
        }
        catch(Exception e) {
            throw new MyException(e.toString());
        }
    }

    @Override
    public List<ProgramState> getProgramList() {
        return this.states;
    }

    @Override
    public void setProgramList(List<ProgramState> newStates) {
        this.states=newStates;
    }

    @Override
    public int size(){
        return this.states.size();
    }

    @Override
    public ProgramState get(Integer id) {
        for (ProgramState program: this.states){
            if (program.getId() == id)
                return program;
        }
        return null;
    }
}
