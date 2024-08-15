package lox;

public enum TokenType {
    // single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    //                        分号
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // one or two character tokens,
    // !,  !=
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GTREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // keywords
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF
}
