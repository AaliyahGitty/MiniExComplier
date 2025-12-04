
// Author: Aaliyah Bradley
// Mini Expression Compiler - Single File Implementation

import java.util.*;

public class MiniExpressionCompiler {

    enum TokenType { NUMBER, PLUS, MINUS, STAR, SLASH, LPAREN, RPAREN, EOF }

    static class Token {
        final TokenType type;
        final String lexeme;
        final int position;
        Token(TokenType type, String lexeme, int position) {
            this.type = type; this.lexeme = lexeme; this.position = position;
        }
        @Override
        public String toString() {
            if (type == TokenType.EOF) return "EOF@" + position;
            return type + "('" + lexeme + "' @" + position + ")";
        }
    }

    static class Lexer {
        private final String input;
        private int current = 0;
        Lexer(String input){ this.input = input; }

        List<Token> tokenize() {
            List<Token> tokens = new ArrayList<>();
            while (!isAtEnd()) {
                char c = peek();
                if (Character.isWhitespace(c)) { advance(); }
                else if (Character.isDigit(c)) { tokens.add(number()); }
                else {
                    int pos = current;
                    switch (c) {
                        case '+': tokens.add(new Token(TokenType.PLUS, "+", pos)); break;
                        case '-': tokens.add(new Token(TokenType.MINUS, "-", pos)); break;
                        case '*': tokens.add(new Token(TokenType.STAR, "*", pos)); break;
                        case '/': tokens.add(new Token(TokenType.SLASH, "/", pos)); break;
                        case '(': tokens.add(new Token(TokenType.LPAREN, "(", pos)); break;
                        case ')': tokens.add(new Token(TokenType.RPAREN, ")", pos)); break;
                        default: throw new RuntimeException("Unexpected character '" + c + "'");
                    }
                    advance();
                }
            }
            tokens.add(new Token(TokenType.EOF, "", current));
            return tokens;
        }

        private boolean isAtEnd() { return current >= input.length(); }
        private char peek() { return input.charAt(current); }
        private char advance() { return input.charAt(current++); }

        private Token number() {
            int start = current;
            while (!isAtEnd() && Character.isDigit(peek())) advance();
            return new Token(TokenType.NUMBER, input.substring(start, current), start);
        }
    }

    interface Expr {}
    static class Literal implements Expr { final double value; Literal(double v){ value = v; } }
    static class Unary implements Expr { final Token operator; final Expr right;
        Unary(Token op, Expr r){ operator = op; right = r; } }
    static class Binary implements Expr { final Expr left; final Token operator; final Expr right;
        Binary(Expr l, Token op, Expr r){ left = l; operator = op; right = r; } }

    static class ParseException extends RuntimeException {
        final int position;
        ParseException(String msg, int pos){ super(msg); position = pos; }
    }

    static class Parser {
        private final List<Token> tokens; private int current = 0;
        Parser(List<Token> t){ tokens = t; }

        Expr parse() {
            Expr expr = expression();
            if (!isAtEnd()) {
                Token t = peek();
                throw new ParseException("Unexpected token '" + t.lexeme + "'", t.position);
            }
            return expr;
        }

        private Expr expression() {
            Expr expr = term();
            while (match(TokenType.PLUS, TokenType.MINUS))
                expr = new Binary(expr, previous(), term());
            return expr;
        }

        private Expr term() {
            Expr expr = unary();
            while (match(TokenType.STAR, TokenType.SLASH))
                expr = new Binary(expr, previous(), unary());
            return expr;
        }

        private Expr unary() {
            if (match(TokenType.MINUS))
                return new Unary(previous(), unary());
            return primary();
        }

        private Expr primary() {
            if (match(TokenType.NUMBER))
                return new Literal(Double.parseDouble(previous().lexeme));
            if (match(TokenType.LPAREN)) {
                Expr expr = expression();
                consume(TokenType.RPAREN, "Expected ')'");
                return expr;
            }
            Token t = peek();
            throw new ParseException("Unexpected token '" + t.lexeme + "'", t.position);
        }

        private boolean match(TokenType... types){
            for (TokenType t : types){
                if (check(t)){ advance(); return true; }
            }
            return false;
        }

        private void consume(TokenType type, String msg){
            if (!check(type)) throw new ParseException(msg, peek().position);
            advance();
        }

        private boolean check(TokenType t){ return !isAtEnd() && peek().type == t; }
        private Token advance(){ return tokens.get(current++); }
        private boolean isAtEnd(){ return peek().type == TokenType.EOF; }
        private Token peek(){ return tokens.get(current); }
        private Token previous(){ return tokens.get(current - 1); }
    }

    static class Evaluator {
        double eval(Expr expr){
            if (expr instanceof Literal) return ((Literal)expr).value;
            if (expr instanceof Unary) {
                Unary u = (Unary)expr;
                return -eval(u.right);
            }
            if (expr instanceof Binary) {
                Binary b = (Binary)expr;
                double l = eval(b.left), r = eval(b.right);
                switch (b.operator.type){
                    case PLUS: return l + r;
                    case MINUS: return l - r;
                    case STAR: return l * r;
                    case SLASH:
                        if (r == 0) throw new RuntimeException("Division by zero");
                        return l / r;
                }
            }
            throw new RuntimeException("Unknown expression");
        }
    }

    static class TreePrinter {
        static void print(Expr e){ print(e,0); }
        private static void print(Expr e, int indent){
            String s = " ".repeat(indent);
            if (e instanceof Literal){
                System.out.println(s + ((Literal)e).value);
            } else if (e instanceof Unary){
                Unary u = (Unary)e;
                System.out.println(s + u.operator.lexeme);
                print(u.right, indent+2);
            } else if (e instanceof Binary){
                Binary b = (Binary)e;
                System.out.println(s + b.operator.lexeme);
                print(b.left, indent+2);
                print(b.right, indent+2);
            }
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Expression: ");
        String input = sc.nextLine();

        try {
            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.tokenize();

            System.out.println("=== Lexical Analysis ===");
            System.out.println(tokens);

            Parser parser = new Parser(tokens);
            Expr expr = parser.parse();
            System.out.println("Parse result: success");

            System.out.println("=== Syntax Tree ===");
            TreePrinter.print(expr);

            Evaluator eval = new Evaluator();
            System.out.println("Result: " + eval.eval(expr));

        } catch (ParseException e){
            System.out.println("Parse error at position " + e.position + ": " + e.getMessage());
        } catch (RuntimeException e){
            System.out.println("Runtime error: " + e.getMessage());
        }
    }
}
