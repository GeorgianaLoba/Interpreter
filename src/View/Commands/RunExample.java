package View.Commands;

import Controller.Controller;
import Model.MyException;


public class RunExample extends Command {
    private Controller controller;
    public RunExample(String key, String description, Controller controller){
        super(key, description);
        this.controller=controller;
    }
    @Override
    public void execute() throws MyException {
        try{
            this.controller.allStep();
        }
        catch(Exception e){
            throw new MyException("Error occur during run example command : " + e.toString());
        }
    }

    public Controller getController() {
        return controller;
    }
}
