package vmtranslator;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Parser of VM code.
 * @author djb
 */
public class Parser {
    // Input source.
    private final BufferedReader input;
    // The current line being processed.
    private String currentLine;
    // The next input line, if any.
    private String nextLine;
    // Text introducing a comment.
    private static final String COMMENT = "//";
    // The current command type.
    private CommandType commandType = null;
    // The current command arguments.
    private String arg1, arg2;

    /**
     * Parse the given source of VM input input.
     * @param input The input file.
     */
    public Parser(BufferedReader input) {
        this.input = input;
        currentLine = null;
        // Cue up the first input line.
        getNextLine();
    }

    /**
     * Is there at least one more command in the input?
     * @return true if there is another command.
     */
    public boolean hasMoreCommands() {
        return nextLine != null;
    }

    /**
     * Advance to the next command line.
     */
    public void advance() {
        currentLine = nextLine;
        getNextLine();
        decompose();
    }

    /**
     * Return the current command type.
     * @return the command type.
     */
    public CommandType getCommandType()
    {
        return commandType;
    }

    /**
     * Return the first argument of the current command.
     * @return the first argument.
     */
    public String getArg1()
    {
        return arg1;
    }

    /**
     * Return the second argument of the current command.
     * @return the second argument.
     */
    public String getArg2()
    {
        return arg2;
    }

    /**
     * Return the text of the current line.
     * @return the current line.
     */
    public String getCurrentLine()
    {
        return currentLine;
    }

    /**
     * Identify the components of the current command.
     */
    private void decompose()
    {
        String[] parts = currentLine.split("\\s+");
        assert parts.length > 0 && parts.length <= 3;
        if(parts.length < 1) {
            throw new RuntimeException("Unrecognised input: " + currentLine);
        }
        commandType = CommandType.identify(parts[0]);
        if(commandType == null) {
            throw new RuntimeException("Unrecognised command: " + parts[0]);
        }
        switch(commandType) {
            case C_ARITHMETIC:
                assert parts.length == 1;
                arg1 = parts[0];
                arg2 = null;
                break;
            case C_PUSH:
            case C_POP:
                assert parts.length == 3;
                if(parts.length != 3) {
                    throw new RuntimeException("Malformed input: " + currentLine);
                }
                arg1 = parts[1];
                arg2 = parts[2];
                break;
            default:
                assert parts.length == 2;
                if(parts.length != 2) {
                    throw new RuntimeException("Malformed input: " + currentLine);
                }
                arg1 = parts[1];
                arg2 = null;
                break;
        }
    }

    /**
     * Get the next significant command line, if any.
     * Comment lines are discarded in the process.
     * The line is trimmed at both ends.
     */
    private void getNextLine()
    {
        try {
            String line;
            do {
                line = input.readLine();
                if(line != null) {
                    line = line.trim();
                    if(line.startsWith(COMMENT)) {
                        line = "";
                    }
                }
            } while(line != null && line.isEmpty());
            if(line != null) {
                // Delete any trailing comments.
                int commentIndex = line.indexOf(COMMENT);
                if(commentIndex > 0) {
                    nextLine = line.substring(0, commentIndex).trim();
                }
                else {
                    nextLine = line;
                }
            }
            else {
                nextLine = null;
            }
        } catch (IOException ex) {
            nextLine = null;
        }
    }
}
