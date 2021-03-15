package vmtranslator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handle the translation from VM source files to a single
 * Hack ASM output file.
 *
 * @author djb
 * @version 2020.11.28
 */
public class Main {

    /**
     * @param args VM files or a directory of VM files.
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Usage: java Main file.vm OR dir");
        }
        else {
            File arg = new File(args[0]);
            if(arg.exists() && arg.canRead()) {
                String srcName = arg.getName();
                if(arg.isDirectory() || srcName.endsWith(Translator.VM_SUFFIX)) {
                    List<File> vmFiles = new ArrayList<>();
                    // Isolate the name to which the translation will be made.
                    String fullName = arg.getAbsolutePath();
                    String outName;
                    if(arg.isDirectory()) {
                        try {
                            outName = arg.getCanonicalPath() + Translator.ASM_SUFFIX;
                        } catch (IOException ex) {
                            outName = "Unknown" + Translator.ASM_SUFFIX;
                        }
                        File[] fileList = arg.listFiles();
                        for(File f : fileList) {
                            if(f.canRead() && f.getName().endsWith(Translator.VM_SUFFIX)) {
                                vmFiles.add(f);
                            }
                        }

                    }
                    else {
                        outName =
                                fullName.substring(0,
                                        fullName.lastIndexOf(Translator.VM_SUFFIX)) +
                                        Translator.ASM_SUFFIX;
                        vmFiles.add(arg);
                    }
                    if(!vmFiles.isEmpty()) {
                        try {
                            Translator trans = new Translator(outName, vmFiles);
                            trans.translate();
                        }
                        catch(IOException ex) {
                            System.err.println("Exception writing to " + outName);
                        }
                    }
                    else {
                        System.err.println("No VM files to translate.");
                    }
                }
                else {
                    System.err.println(arg.getName() + " is neither a VM file nor a directory.");
                }
            }
            else {
                System.err.println(arg.getName() + " not found.");
            }
        }
    }

}