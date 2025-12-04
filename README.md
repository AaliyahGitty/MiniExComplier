# MiniExComplier - READ ME
# Author: Aaliyah Bradley
This document describes the implementation and functionality of the Mini Expression Compiler project, including lexical analysis, parsing, AST construction, and evaluation.
# 1. Overview
This compiler evaluates arithmetic expressions by performing:
 1. Lexical Analysis
2. Parsing via Recursive Descent
 3. Abstract Syntax Tree Construction
4. Expression Evaluation 5. Error Reporting
# 2. Grammar
The following grammar is used:
expression → term ( ('+' | '-') term )*
term → unary ( ('*' | '/') unary )*
 unary → '-' unary | primary
 primary → NUMBER | '(' expression ')'
# 3. Lexical Analysis
The lexer tokenizes the input into recognizable symbols such as numbers, operators, and parentheses.
# 4. Parsing
The parser validates expression structure using recursive descent, producing a syntax tree or an error.
# 5. Abstract Syntax Tree
The AST represents the hierarchical structure of the expression. Nodes may be Literal, Unary, or Binary.
# 6. Evaluation
During evaluation, the AST is recursively processed to compute the final numeric result.
# 7. Error Handling
Invalid inputs produce detailed parsing errors or runtime errors such as division by zero.
