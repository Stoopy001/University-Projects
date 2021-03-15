    // push constant 5
    @5
D=A

@SP
A=M
M=D
@SP
M=M+1
    // push constant 3
    @3
D=A

@SP
A=M
M=D
@SP
M=M+1
    // add
    @SP
M=M-1
A=M
D=M
A=A-1
D=M+D
M=D
    @THATS_ALL_FOLKS
    (THATS_ALL_FOLKS)
    0;JMP
