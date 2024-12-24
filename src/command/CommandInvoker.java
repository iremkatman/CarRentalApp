package command;

import javax.swing.*;
import java.util.Stack;

public class CommandInvoker {
    private Stack<Command> commandHistory = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    public boolean canUndo() {
        return !commandHistory.isEmpty();
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo(); // Son komutu geri al
        } else {
            JOptionPane.showMessageDialog(null, "Nothing to undo."); // Daha kullanıcı dostu mesaj
        }
    }
}

