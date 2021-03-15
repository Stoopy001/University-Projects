package vmtranslator;

/**
 * VM command types.
 * @author djb
 */
public enum CommandType {
    C_ARITHMETIC,
    C_PUSH, C_POP;
    
    /**
     * Convert from a VM instruction to the associated command type.
     * @param instr The VM instruction.
     * @return The associated command type.
     */
    public static CommandType identify(String instr)
    {
        switch(instr) {
            case "add" : 
            case "sub" : 
            case "neg" : 
            case "eq" : 
            case "gt" : 
            case "lt" : 
            case "and" : 
            case "or" : 
            case "not" : 
                return C_ARITHMETIC;
            case "pop" :
                return C_POP;
            case "push" :
                return C_PUSH;
            default:
                return null;
        }
    }
}
