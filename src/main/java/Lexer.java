package main.java;

import java.io.*;
import java.util.ArrayList;

public class Lexer {

    ArrayList<Token> tokens = new ArrayList<>();
    String buffer = "";
    State currState = State.START;
    boolean fileEnded = false;

    public Lexer(String filePath) {

        try {
            BufferedReader is = new BufferedReader(new FileReader(filePath));
            int c;

            while ((c = is.read()) != -1) {
                genTokens((char) c);
            }

            is.close();
            fileEnded = true;
            genTokens('\n');
            tokens.remove(tokens.size() - 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }


    public void genTokens(Character character) {
        boolean analyze = true;
        System.out.println(currState);
        System.out.println(character);
        System.out.println(buffer);

        while (analyze) {

            switch (currState) {
                case START: {
                    if (character == '[' || character == ']' || character == '~')
                        currState = State.OPERATOR;
                    else if (character == ':')
                        currState = State.DOUBLE_ASSIGN;
                    else if (character == '>')
                        currState = State.LL;
                    else if (character == '<')
                        currState = State.RL;
                    else if (character >= '1' && character <= '9')
                        currState = State.NUMBER;
                    else if (character == '.')
                        currState = State.DOT;
                    else if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || (character >= 'а' && character <= 'я') || (character >= 'А' && character <= 'Я') || character == '_')
                        currState = State.IDENTIFIER;
                    else if (character == '0')
                        currState = State.ZERO;
                    else if (character == '/')
                        currState = State.COMMENT;
                    else if (character == '{')
                        currState = State.COMMENT2;
                    else if (character == '+' || character == '&' || character == '|')
                        currState = State.OPER_D;
                    else if (character == '-')
                        currState = State.MINUS;
                    else if (character == ':')
                        currState = State.SEMI_COL;
                    else if (character == '\"' || character == '\'')
                        currState = State.STRING_LIT;
                    else
                        checkTerminateSymbol(character);
                    analyze = false;
                    System.out.println("START:    " + currState);
                }
                break;

                case OPERATOR: {
                    tokens.add(new Token(Type.OPERATOR, buffer.isEmpty() ? Character.toString(character) : buffer));
                    reset();
                    analyze = true;
                }

                case IDENTIFIER: {
                    if (!((character >= 'а' && character <= 'я') || (character >= 'А' && character <= 'Я') || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || (character >= 'A' && character <= 'Z') || character == '_')) {
                        if (PascalLang.isPunctuation(character) || character == '.') {
                            tokens.add(new Token(Type.IDENTIFIER, buffer));
                            reset();
                            analyze = true;
                        } else {
                            tokens.add(new Token(Type.ERROR, buffer));
                            reset();
                            analyze = false;
                        }
                    } else {
                        analyze = false;
                    }
                }
                break;

                case DOUBLE_ASSIGN: {
                    if (character == '=') {
                        currState = State.OPERATOR;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case RL: {
                    if (character == '<' || character == '=') {
                        currState = State.DOUBLE_ASSIGN;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case LL: {
                    if (character == '>' || character == '=') {
                        currState = State.DOUBLE_ASSIGN;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case NUMBER: {

                    if (character == '.')
                        currState = State.NUMBER_D;
                    else if (!(character >= '0' && character <= '9')) {
                        numberCheck(character);
                        analyze = true;
                    }
                    analyze = false;
                }
                break;

                case EXPONENT: {
                    if (character == '+' || character == '-' || (character >= '0' && character <= '9')) {
                        currState = State.EXPONENT_SYMB;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.ERROR, buffer + character));
                        reset();
                        analyze = false;
                    }
                }
                break;

                case EXPONENT_SYMB: {
                    if (character >= '0' && character <= '9') {
                        currState = State.EXPONENT_SYMB;
                        analyze = false;
                    } else {
                        numberCheck(character);
                        analyze = true;
                        return;
                    }
                }
                break;

                case DOT: {
                    if (character >= '0' && character <= '9') {
                        currState = State.NUMBER_DOT;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case DOT_ERR: {
                    if (character == '\r' || character == ' ' || character == '\n') {
                        tokens.add(new Token(Type.ERROR, buffer));
                        analyze = true;
                    } else {
                        analyze = false;
                    }

                }
                break;

                case NUMBER_D: {
                    if (character >= '0' && character <= '9') {
                        currState = State.NUMBER_DOT;
                        analyze = false;
                    } else {
                        currState = State.DOT_ERR;
                        analyze = false;
                    }
                }
                break;

                case NUMBER_DOT: {

                    if (!(character >= '0' && character <= '9')) {
                        numberCheck(character);
                        analyze = true;
                    } else {
                        analyze = false;
                    }
                }
                break;

                case ZERO: {
                    if (character == '.') {
                        currState = State.NUMBER_D;
                        analyze = false;
                    } else if (character >= '0' && character <= '9') {
                        currState = State.NUMBER;
                        analyze = false;
                    } else {
                        numberCheck(character);
                        analyze = true;
                    }

                }
                break;

                case COMMENT: {
                    if (character == '/') {
                        currState = State.CLOOP1;
                        analyze = false;
                    } else if (character == '=') {
                        currState = State.OPERATOR;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case COMMENT2: {
                    if (character == '\r' || character == '\n') {
                        tokens.add(new Token(Type.COMMENT, buffer));
                        reset();
                        analyze = true;
                    } else {
                        analyze = false;
                    }

                }

                case CLOOP1: {
                    if (character == '\r' || character == '\n') {
                        tokens.add(new Token(Type.COMMENT, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case END_COMM: {
                    tokens.add(new Token(Type.COMMENT, buffer));
                    reset();
                    analyze = true;
                }

                case CLOOP2: {
                    if (character == '/') {
                        currState = State.END_COMM;
                        analyze = false;
                    } else {
                        currState = State.COMMENT2;
                        analyze = false;
                    }
                }
                break;

                case OPER_D: {
                    if (character == '=' || Character.toString(character).equals(buffer)) {
                        currState = State.OPERATOR;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case MINUS: {
                    if (character == '=' || character == '-' || character == '>') {
                        currState = State.OPERATOR;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case SEMI_COL: {
                    if (character == ':') {
                        currState = State.OPERATOR;
                        analyze = false;
                    } else {
                        tokens.add(new Token(Type.OPERATOR, buffer));
                        reset();
                        analyze = true;
                    }
                }
                break;

                case LITERAL: {
                    System.out.println("IN LITERAL CASE");
                    tokens.add(new Token(Type.LITERAL, buffer));
                    analyze = false;
                    reset();
                    System.out.println("AFTER RESET:  " + currState);

                }

                case ERR_COMM: {
                    if (character == '\r' || character == '\n') {
                        tokens.add(new Token(Type.ERROR, buffer));
                        reset();
                        analyze = false;
                    } else {
                        analyze = false;
                    }
                }

                case CHARACTER_T: {
                    if (character == 'r' || character == 'n' || character == 't' || character == '\\' || character == '\'' || character == '\"') {
                        currState = State.CHARACTER_LOOP;
                        analyze = false;
                    } else {
//                        currState = State.ERR_COMM;
                        analyze = false;
                    }
                }
                break;

                case CHARACTER_LOOP: {
                    if (character == '\'') {
                        currState = State.LITERAL;
                        analyze = false;
                    } else {
//                        currState = State.ERR_COMM;
                        analyze = false;
                    }
                }
                break;

                case CHARACTER_LIT: {
                    if (character == '\\') {
                        currState = State.CHARACTER_T;
                        analyze = false;
                    } else if (character == '\r' || character == '\n' || character == '\'') {
                        tokens.add(new Token(Type.ERROR, buffer));
                        reset();
                        analyze = true;
                    } else {
                        currState = State.CHARACTER_LOOP;
                        analyze = false;
                    }

                }
                break;

                case STRING_LIT: {
                    if (character == '\r' || character == '\n') {
                        tokens.add(new Token(Type.ERROR, buffer));
                        reset();
                        analyze = true;
                    } else if (character == '\"' || character == '\'') {
                        tokens.add(new Token(Type.LITERAL, buffer + character));
                        reset();
                        analyze = false;
                    } else {
                        currState = State.STRING_T;
                        analyze = false;
                    }

                }
                break;

                case STRING_T: {
                    if (character == '\r' || character == '\n') {
                        tokens.add(new Token(Type.ERROR, buffer));
                        reset();
                        analyze = true;
                    } else if (character == '\"' || character == '\'') {
                        currState = State.LITERAL;
                        analyze = false;
                    } else {
                        analyze = false;
                    }
                }
                break;

                case ERROR: {
                    if (PascalLang.isPunctuation(character)) {
                        tokens.add(new Token(Type.ERROR, buffer));
                        reset();
                        analyze = true;
                    } else {
                        analyze = false;
                    }
                }
                break;

                default: {
                    if (PascalLang.isPunctuation(character)) {
                        tokens.add(new Token(Type.ERROR, buffer));
                        reset();
                        analyze = false;
                    } else {
                        analyze = false;
                    }
                }
                break;

            }
        }

        if (currState != State.START) {
            buffer += character;
        }
    }

    public void reset() {
        buffer = "";
        currState = State.START;
    }

    public void numberCheck(Character character) {
        if (PascalLang.isPunctuation(character)) {
            tokens.add(new Token(Type.NUMBER, buffer));
            reset();
        } else {
            currState = State.ERROR;
            buffer += character;
        }

    }

    public void checkTerminateSymbol(char character) {
        if (PascalLang.isPunctuation(character))
            tokens.add(new Token(Type.PUNCTUATION, Character.toString(character)));
        else
            tokens.add(new Token(Type.ERROR, Character.toString(character)));
        reset();
    }
    
}