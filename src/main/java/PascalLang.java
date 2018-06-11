package main.java;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class PascalLang {

    public static final String[] KEYWORDS =  new String[]{"and", "array", "begin", "case", "const", "div", "do", "downto", "else", "end", "file", "for", "function", "goto", "if",
        "in", "label", "mod", "nil", "not", "of", "or", "packed", "procedure", "program", "record", "repeat", "set", "then", "to",
        "type", "uses", "until", "var", "while", "with", "integer", "real", "boolean", "char", "string", "writeln", "write", "read", "readln" };

    public static final Character[] PUNCTUATION = new Character[] {'-','+','=','|','~','[',']','\\',
            ';','\'',':','"','<','>', ',','/','#', '\r', '\n', '\t', '!','%','^','&','*','(',')'};


    private static Set<String> keywords = new HashSet<String>(Arrays.asList(KEYWORDS));
    private static Set<Character> punctuation = new HashSet<Character>(Arrays.asList(PUNCTUATION));

    public static boolean isKeyword(String word)
    {
        return keywords.contains(word);
    }

    public static boolean isPunctuation(char word)
    {
        return punctuation.contains(word);
    }
}

