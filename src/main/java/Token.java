package main.java;

public class Token {
    private String content;
    private Type tokenType;

    public Token(Type tokenType, String content){
        this.content = content;

        if(tokenType.equals(Type.IDENTIFIER) || tokenType.equals(Type.ERROR)){
            if(PascalLang.isKeyword(content)){
                this.tokenType = Type.KEYWORD;
            }
            else
                this.tokenType = tokenType;
        }  else
            this.tokenType = tokenType;

         if(tokenType.equals(Type.PUNCTUATION)){
             switch (content) {
                 case "\r":
                     this.content = "\\" + "r";
                     break;
                 case "\n":
                     this.content = "\\" + "n";
                     break;
                 case "\t":
                     this.content = "\\" + "t";
                     break;
                 case " ":
                     this.content = " ";
                     break;
             }
        }
        if(tokenType.equals(Type.COMMENT)){
            this.content = this.content.replace("<","&lt;");
            this.content = this.content.replace(">","&gt;");
        }

    }

    public Type getTokenType() {
        return tokenType;
    }

    public String getContent() {
        return content;
    }
}
