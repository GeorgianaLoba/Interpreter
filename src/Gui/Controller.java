package Gui;

import Model.DataTypes.MyFileTable;
import Model.MyException;
import View.Commands.Command;
import View.Commands.RunExample;
import View.Interpreter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import Model.Type.*;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //i wanted to reuse code (cause I'm lazy)
    //so I simply changed a bit the Interpretor class (which used to be my main)
    private Interpreter interpretor;
    public static Command command;

    @FXML
    private ListView<String> programsList;
    @FXML
    private Button startProgramButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        interpretor = new Interpreter();
        try {
            interpretor.interpret();
        } catch (MyException e) {
            System.out.println(e.toString());
        }
        Map<String, Command> commands = interpretor.getCommands();
        for (Map.Entry entry: commands.entrySet() ){
            if (!(entry.getKey().equals("0"))) {
                if (entry.getValue() instanceof RunExample){
                    Command command = (RunExample) entry.getValue();
                    programsList.getItems().add("Key: "+ command.getKey() + " Description: " + command.getDescription());
                }
            }
        }
        programsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void startProgram() throws IOException, MyException {
        //TODO: can be improved
        String program = programsList.getSelectionModel().getSelectedItem();
        Integer id;
        try {
            id = Integer.parseInt(program.substring(5, 7));
        }
        catch (Exception e){
            id = Integer.parseInt(program.substring(5,6));
        }
        String commandId = Integer.toString(id);
        interpretor = new Interpreter();

        try {
            interpretor.interpret();
        } catch (MyException e) {
            System.out.println(e.toString());
        }
        this.command = interpretor.getCommands().get(commandId);
        RunExample com;
        com=(RunExample)this.command;
        try {
            com.getController().getRepository().getProgramList().get(0).getProgram().typecheck(new HashMap<String, Type>());
            Parent loader = FXMLLoader.load(getClass().getResource("/Gui/main.fxml"));
            startProgramButton.getScene().setRoot(loader);
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }


    }
}
