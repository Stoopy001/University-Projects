package vmtranslator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The infrastructure for a translator from VM code to Hack assembler.
 * Only the parsing elements are present.
 * The translation of the arithmetic, push and pop instructions
 * remain to be added to the CodeWriter.
 * 
 * @author djb
 */
public class Translator {
    // Input and output file suffixes.
    public static final String VM_SUFFIX = ".vm";
    public static final String ASM_SUFFIX = ".asm";
    // The code writer.
    private final CodeWriter coder;
    // List of input files.
    private final List<File> asmFiles;
    
    /**
     * Create a translator for the list of VM input files.
     * @param outfile The name of the output file.
     * @param vmFiles The list of input files.
     * @throws IOException 
     */
    public Translator(String outfile, List<File> vmFiles)
            throws IOException
    {
        FileWriter writer = new FileWriter(outfile);
        coder = new CodeWriter(writer);
        this.asmFiles = vmFiles;            
    }
    
    /**
     * Perform the translation of all the input files.
     * @throws IOException 
     */
    public void translate()
            throws IOException
    {
        for(File f : asmFiles) {
            try(BufferedReader reader =
                    new BufferedReader(new FileReader(f))) {
                String filename = f.getName();
                coder.setFilename(filename.substring(0, filename.lastIndexOf(VM_SUFFIX)));
                Parser parser = new Parser(reader);
                while(parser.hasMoreCommands()) {
                    parser.advance();
                    
                    // For debugging purposes, write a comment containing
                    // the source line into the output.
                    coder.writeComment(parser.getCurrentLine());
                    
                    CommandType command = parser.getCommandType();
                    String arg1 = parser.getArg1();
                    String arg2 = parser.getArg2();
                    switch(command) {
                        case C_ARITHMETIC:
                            coder.writeArithmetic(arg1);
                            break;
                        case C_POP:
                        case C_PUSH:
                            coder.writePushPop(command, arg1, Integer.parseInt(arg2));
                            break;
                        default:
                            throw new IllegalStateException("Unrecognised command type: " + command);
                    }
                }
            }
            catch(IOException ex) {
                System.err.println("Exception reading from " + f);
            }
        }
        coder.close();
    }

}
