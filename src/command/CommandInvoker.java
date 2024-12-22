package command;

import javax.swing.*;
import java.util.Stack;

public class CommandInvoker {
    private Stack<Command> commandHistory = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo(); // Son komutu geri al
        } else {
            JOptionPane.showMessageDialog(null, "Undo operation completed. Nothing more to undo."); // Daha kullanıcı dostu mesaj
        }
    }
}
