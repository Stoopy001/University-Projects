package vmtranslator;

import java.io.IOException;
import java.io.Writer;

/**
 * Output Hack ASM for VM code.
 *
 * @author djb, Costi Flavius Lacatusu
 */
public class CodeWriter {
    // Temporary registers for intermediate calculations,
    // should they be required by the translator.
    private static final String R13 = "R13", R14 = "R14", R15 = "R15";

    // Where the translation is written.
    private final Writer writer;
    // Current file being processed.
    private String currentFilename;
    // Asm to push D
    private final String pushD = "\n@SP\nA=M\nM=D\n@SP\nM=M+1";
    // Asm to pop D
    private final String popD = "@SP\nM=M-1\nA=M\nD=M";
    private int jmpId = 0;

    /**
     * Create a CodeWriter to output to the given file.
     * 
     * @param writer Where to write the code.
     */
    public CodeWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Translation of a new file.
     * 
     * @param filename The input file name.
     */
    public void setFilename(String filename) {
        this.currentFilename = filename;
    }

    /**
     * Translate the given arithmetic command.
     * 
     * @param command The command to be translated.
     * @throws java.io.IOException
     */
    public void writeArithmetic(String command) throws IOException {
        switch (command) {
        // Binary arithmetic operators.
        case "add":
            arithmeticsSubAdd(1);
            break;
        case "and":
            arithmeticsAndOr(1);
            break;
        case "or":
            arithmeticsAndOr(2);
            break;
        case "sub":
            arithmeticsSubAdd(2);
            break;

        // Unary operators.
        case "neg":
            unary(true);
            break;
        case "not":
            unary(false);
            break;

        // Relational operators.
        case "eq":
            relation("JEQ");
            break;
        case "lt":
            relation("JLT");
            break;
        case "gt":
            relation("JGT");
            break;
        default:
            throw new IllegalStateException("Unrecognised arithmetic command: " + command);
        }
    }

    /**
     * Translate the given push or pop command.
     * 
     * @param command The command to be translated.
     * @param segment The segment to be accessed.
     * @param index   The index within segment.
     * @throws java.io.IOException
     */
    public void writePushPop(CommandType command, String segment, int index) throws IOException {
        if (null == command) {
            throw new IllegalStateException("Invalid command in writePushPop: " + command);
        } else {
            switch (command) {
            case C_PUSH:
                push(segment, index);
                break;
            case C_POP:
                pop(segment, index);
                break;
            default:
                throw new IllegalStateException("Invalid command in writePushPop: " + command);
            }
        }
    }

    /**
     * Write the given text as a comment.
     * 
     * @param text the text to output.
     * @throws IOException
     */
    public void writeComment(String text) throws IOException {
        output("// " + text);
    }

    /**
     * Close the output file.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        try (writer) {
            String endOfProgram = "THATS_ALL_FOLKS";
            output("@" + endOfProgram);
            output(String.format("(%s)", endOfProgram));
            output("0;JMP");
        }
    }

    /**
     * Output the given string with an indent and terminate the current line.
     * 
     * @param s The string to output.
     * @throws IOException
     */
    private void output(String s) throws IOException {
        writer.write("    ");
        writer.write(s);
        writer.write('\n');
    }
    /**
     * Push the given index (constant or memory pointer) into the given segment
     * 
     * @param segment The segment to push to
     * @param index The index of data to push
     */
    private void push(String segment, int index) {
        String s = "";

        if (segment.equals("constant")) {

            s = "@" + index + "\nD=A\n" + pushD;

        } else if (segment.equals("local")) {
            s = "@LCL\nD=M\n@" + index + "\nD=D+A\nA=D\nD=M\n" + pushD;
        } else if (segment.equals("argument")) {
            s = "@ARG\nD=M\n@" + index + "\nD=D+A\nA=D\nD=M\n" + pushD;
        } else if (segment.equals("this")) {
            s = "@THIS\nD=M\n@" + index + "\nD=D+A\nA=D\nD=M\n" + pushD;
        } else if (segment.equals("that")) {
            s = "@THAT\nD=M\n@" + index + "\nD=D+A\nA=D\nD=M\n" + pushD;
        } else if (segment.equals("pointer")) {
            s = "@3\nD=A\n@" + index + "\nD=D+A\nA=D\nD=M\n" + pushD;
        } else if (segment.equals("temp")) {
            s = "@5\nD=A\n@" + index + "\nD=D+A\nA=D\nD=M\n" + pushD;
        } else if (segment.equals("static")) {
            s = "@" + currentFilename + "." + index + "\nD=M\n" + pushD;
        }
        try {
            output(s);
        } catch (IOException e) {
            System.out.println("Something went wrong when pushing: " + e.getMessage());
        }
    }
    /***
     * Output Hack ASM to pop the given index from the given segment
     * 
     * @param segment   Segment to pop from
     * @param index Index of data to pop
     */
    private void pop(String segment, int index) {
        String s = "";
        if (segment.equals("local")) {
            s = "@LCL\nD=M\n@" + index + "\nD=D+A\n@" + R13 + "\nM=D\n" + popD + "\n@" + R13 + "\nA=M\nM=D\n";
        } else if (segment.equals("argument")) {
            s = "@ARG\nD=M\n@" + index + "\nD=D+A\n@" + R13 + "\nM=D\n" + popD + "\n@" + R13 + "\nA=M\nM=D\n";
        } else if (segment.equals("this")) {
            s = "@THIS\nD=M\n@" + index + "\nD=D+A\n@" + R14 + "\nM=D\n" + popD + "\n@" + R14 + "\nA=M\nM=D\n";
        } else if (segment.equals("that")) {
            s = "@THAT\nD=M\n@" + index + "\nD=D+A\n@" + R14 + "\nM=D\n" + popD + "\n@" + R14 + "\nA=M\nM=D\n";
        } else if (segment.equals("pointer")) {
            s = "@3\nD=A\n@" + index + "\nD=D+A\n@" + R15 + "\nM=D\n" + popD + "\n@" + R15 + "\nA=M\nM=D\n";
        } else if (segment.equals("temp")) {
            s = "@5\nD=A\n@" + index + "\nD=D+A\n@" + R15 + "\nM=D\n" + popD + "\n@" + R15 + "\nA=M\nM=D\n";
        } else if (segment.equals("static")) {
            s = popD + "\n@" + currentFilename + "." + index + "\nM=D\n";
        }
        try {
            output(s);
        } catch (IOException e) {
            System.out.println("Something went wrong when popping: " + e.getMessage());
        }
    }
    /**
     * Output Hack ASM to perform an arithmetic operation on the last two items in the stack
     * 
     * @param sel The arithmetic operation to perform (add / substract)
     */
    private void arithmeticsSubAdd(int sel) {
        String op = "";
        switch (sel) {
        case 1:
            op = "+";
            break;
        case 2:
            op = "-";
            break;
        }
        String s = popD + "\nA=A-1\nD=M" + op + "D\nM=D";
        try {
            output(s);
        } catch (IOException e) {
            System.out.println("Something went wrong when adding: " + e.getMessage());
        }
    }

    /**
     * Output Hack ASM to perform an arithmetic operation on the last two items in the stack
     * 
     * @param sel The arithmetic operation to perform (And / OR)
     */
    private void arithmeticsAndOr(int sel) {
        String op = "";
        switch (sel) {
        case 1:
            op = "&";
            break;
        case 2:
            op = "|";
            break;
        }
        String s = popD + "\nA=A-1\nM=M" + op + "D";
        try {
            output(s);
        } catch (IOException e) {
            System.out.println("Something went wrong when adding: " + e.getMessage());
        }
    }
    /**
     * Output Hack ASM to perform a unary operation on the last two items in the stack
     * 
     * @param sel The unary operation to perform (True = negate / False = not)
     */
    private void unary(boolean sel) {
        String op = sel ? "-" : "!";

        String s = "@SP\nA=M-1\nM=" + op + "M\n";
        try {
            output(s);
        } catch (IOException e) {
            System.out.println("Something went wrong when performing Neg: " + e.getMessage());
        }
    }
    /**
     * Output Hack ASM to perform a relation (if statement in Hack)
     * 
     * @param sel The operation to perform
     */
    private void relation(String operation) {
        String s = popD + "\nA=A-1\nD=M-D\n@TRUE" + jmpId + "\nD;" + operation + "\n@SP\nA=M-1\nM=0\n@THEN" + jmpId
                + "\n0;JMP\n(TRUE" + jmpId + ")\n@SP\nA=M-1\nM=-1\n(THEN" + jmpId + ")\n";
        jmpId++;
        try {
            output(s);
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
