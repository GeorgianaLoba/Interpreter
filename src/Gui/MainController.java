package Gui;

import Model.DataTypes.MyExecutionStack;
import Model.DataTypes.MyIList;
import Model.DataTypes.MyIStack;
import Model.MyException;
import Model.State.ProgramState;
import Model.Statement.IStatement;
import Model.TableViewData.HeapData;
import Model.TableViewData.LockData;
import Model.TableViewData.SymbolData;
import Model.Value.StringValue;
import Model.Value.Value;
import View.Commands.RunExample;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    private RunExample com;
    //list of program states
    private List<ProgramState> states;
    //current state is the one clicked by the user of 1 (default program state) if not clicked
    private ProgramState currentState;

    @FXML
    private ListView<String> outList;
    @FXML
    private ListView<String> fileList;
    @FXML
    private ListView<String> programsList;
    @FXML
    private Label numberProgramLabel;
    @FXML
    private TableView<HeapData> heapTable;
    @FXML
    private TableView<SymbolData> symbolTable;
    @FXML
    private TableView<LockData> lockTable;
    @FXML
    private ListView<String> executionList;
    @FXML
    private Label programDescription;
    @FXML
    private Button exitButton;
    @FXML
    private Button restartButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //this is called first
        com=(RunExample)Controller.command;

        this.states=com.getController().getRepository().getProgramList();
        currentState = this.states.get(0);
        this.com.getController().wrapperAllStepForGui();
        initTables();
        programsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        programDescription.setText(com.getDescription());
    }

    private void initTables(){
        //function to initialize tables with the right columns
        TableColumn<HeapData, Integer> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<HeapData, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setMinWidth(100);
        heapTable.getColumns().addAll(addressColumn, valueColumn);


        TableColumn<SymbolData, String> variableNameColumn = new TableColumn<>("Variable Name");
        variableNameColumn.setCellValueFactory(new PropertyValueFactory<>("variableName"));
        variableNameColumn.setMinWidth(100);
        TableColumn<SymbolData, String> valColumn = new TableColumn<>("Value");
        valColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        symbolTable.getColumns().addAll(variableNameColumn, valColumn);



        TableColumn<LockData, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.setMinWidth(100);
        TableColumn<LockData, String> vColumn = new TableColumn<>("Value");
        vColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        lockTable.getColumns().addAll(locationColumn, vColumn);

    }


    public void runOneStep(){
        numberProgramLabel.setText(Integer.toString(com.getController().size()));
        try {
            populateProgramStates();
            populateSymbol(currentState);
            populateExecutionStack(currentState);
            if (!states.isEmpty()) {
                populateOut(states.get(0)); //since out, file, heap are common to all states, we can pick one randomly
                populateFile(states.get(0));
                populateHeap(states.get(0));
                populateLock(states.get(0));
            }
        } catch (MyException | CloneNotSupportedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Program executed completely. No more steps.", ButtonType.OK);
            alert.showAndWait();
        }
        try {
            this.states=com.getController().allStepForGui();
        } catch (MyException | InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Program executed completely. No more steps.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void populateOut(ProgramState state) throws MyException {
        this.outList.getItems().clear();
        MyIList<Value>out = state.getOut();
        int i=0;
        while(i<out.size()){
            this.outList.getItems().add(out.get(i).toString());
            i++;
        }
    }

    private void populateFile(ProgramState state) throws MyException{
        this.fileList.getItems().clear();
        Map<StringValue, BufferedReader> fileTable = state.getFileTable().getContent();
        Iterator dictIterator = fileTable.entrySet().iterator();
        while (dictIterator.hasNext()){
            Map.Entry pair = (Map.Entry)dictIterator.next();
            this.fileList.getItems().add(pair.getKey().toString()+ " -> "+ pair.getValue().toString() + "; " );
        }
    }

    private void populateProgramStates() throws MyException{
        this.programsList.getItems().clear();
        for(ProgramState program: com.getController().getRepository().getProgramList()){
            this.programsList.getItems().add(Integer.toString(program.getId()));
        }
    }

    private void populateHeap(ProgramState state){
        ObservableList<HeapData> heapData = FXCollections.observableArrayList();
        Map<Integer, Value>heap=state.getHeap().getContent();
        Iterator dictIterator = heap.entrySet().iterator();
        while (dictIterator.hasNext()){
            Map.Entry pair = (Map.Entry)dictIterator.next();
            heapData.add(new HeapData((Integer) pair.getKey(), pair.getValue().toString()));
        }
        heapTable.setItems(heapData);
    }

    private void populateLock(ProgramState state){
        ObservableList<LockData> lockData = FXCollections.observableArrayList();
        Map<Integer, Integer> lock = state.getLockTable().getContent();
        Iterator dictIterator = lock.entrySet().iterator();
        while (dictIterator.hasNext()){
            Map.Entry pair = (Map.Entry)dictIterator.next();
            lockData.add(new LockData((Integer)pair.getKey(), (Integer) pair.getValue()));
        }
        lockTable.setItems(lockData);
    }





    private void populateSymbol(ProgramState state){
        symbolTable.getItems().clear();
        ObservableList<SymbolData> symbolData = FXCollections.observableArrayList();
        Map<String, Value> symbol = state.getSymbolTable().getContent();
        Iterator dictIterator = symbol.entrySet().iterator();
        while (dictIterator.hasNext()){
            Map.Entry pair = (Map.Entry)dictIterator.next();
            symbolData.add(new SymbolData((String) pair.getKey(), pair.getValue().toString()));
        }
        symbolTable.setItems(symbolData);
    }

    private void populateExecutionStack(ProgramState state) throws MyException, CloneNotSupportedException{
        executionList.getItems().clear();
        MyExecutionStack<IStatement> stack = (MyExecutionStack<IStatement>) state.getExecutionStack();
        Stack<?> clonedStack = stack.clone();
        while (!(clonedStack.isEmpty())){
            executionList.getItems().add(clonedStack.pop().toString());
        }

    }

    public void populateSpecific() throws MyException, CloneNotSupportedException{
        //this function is called when we click on a state in the list of programs states
        //it simply changes my current program, so that i know to change
        //symboltable and executionlist accordingly
        String stateId = programsList.getSelectionModel().getSelectedItem();
        ProgramState current = com.getController().getRepository().get(Integer.parseInt(stateId));
        this.currentState = current;
        populateSymbol(this.currentState);
        populateExecutionStack(this.currentState);
    }

    public void restart(){
        //copy pasted the main class start function
        Stage primaryStage = (Stage) restartButton.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong during restart.", ButtonType.OK);
            alert.showAndWait();
        }
        primaryStage.setTitle("Interpreter");
        Scene scene1 = new Scene(root,700, 500);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }


    public void exit(){ //called when clicking on the exit button
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
