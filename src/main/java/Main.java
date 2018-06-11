package main.java;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("/Users/gfedorenko/Uni/SPOS2/input/test2.txt");
        ArrayList<Token> tokens = lexer.getTokens();
        System.out.println(tokens);
        for (Token token : tokens){
            System.out.println(token.getTokenType() + "   :   " + token.getContent());
        }
    }
}