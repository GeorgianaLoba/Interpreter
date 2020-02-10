package View;

import Model.MyException;
import View.Commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private Map<String, Command> commands;

    public TextMenu(){
        this.commands=new HashMap<String, Command>();
    }
    public void addCommand(Command c){
        this.commands.put(c.getKey(), c);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    private void printMenu(){
        for (Command command: this.commands.values()){
            String line = String.format("%4s: %s", command.getKey(), command.getDescription());
            System.out.println(line);
        }
    }
    public void show() throws MyException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            printMenu();
            System.out.printf("Input the option: ");
            String key = scanner.nextLine();
            Command com = commands.get(key);
            if (com==null){
                System.out.printf("Invalid Option");
                continue;
            }
            com.execute();
        }
    }
}
