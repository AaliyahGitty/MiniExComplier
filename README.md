Mini Expression Compiler – README
Author: Aaliyah Bradley
# 1. Project Summary
The Mini Expression Compiler is a simplified compiler that processes arithmetic expressions through four major phases: Lexical Analysis, Parsing, Abstract Syntax Tree (AST) Construction, and Evaluation. The program reads a user‑entered expression, tokenizes it, verifies its structure using a recursive‑descent parser, builds a syntax tree, and then computes the final result. Invalid expressions produce helpful error messages with exact positions.
# 2. Setup Instructions
To run this project, you will need:
- Java Development Kit (JDK) version 11 or later
- A terminal or command prompt  Make sure the file MiniExpressionCompiler.java is saved in your working directory.
# 3. How to Compile the Program
Open a terminal in the directory containing the Java file and run:
javac MiniExpressionCompiler.java
This will generate MiniExpressionCompiler.class.
# 4. How to Run the Program
After compiling, run the program using:  
java MiniExpressionCompiler  
You will be prompted to enter an arithmetic expression. 
# 5. Example Inputs and Outputs
# Example 1
Input:     (3 + 2) * 5 - 1  
Output:     
=== Lexical Analysis ===     [LPAREN('(' @0), NUMBER('3' @1), PLUS('+' @3), NUMBER('2' @5), RPAREN(')' @6), STAR('*' @8), NUMBER('5' @10), MINUS('-' @12), NUMBER('1' @14), EOF@15]      
=== Parsing ===     
Parse result: success     
=== Syntax Tree === 
-     
 *       
  +         
   3       
  2       
 5      
1    
Result: 24
# Example 2 (Invalid Expression)
Input:     3 + * 5
Output:     
=== Lexical Analysis ===  
[NUMBER('3' @0), PLUS('+' @2), STAR('*' @4), NUMBER('5' @6), EOF@7]      
Parse error at position 4: Unexpected token '*'
