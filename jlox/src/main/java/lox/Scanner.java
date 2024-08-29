package lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lox.TokenType.*;

public class Scanner {
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("and", AND);
        KEYWORDS.put("class", CLASS);
        KEYWORDS.put("else", ELSE);
        KEYWORDS.put("false", FALSE);
        KEYWORDS.put("for", FOR);
        KEYWORDS.put("fun", FUN);
        KEYWORDS.put("if", IF);
        KEYWORDS.put("nil", NIL);
        KEYWORDS.put("or", OR);
        KEYWORDS.put("print", PRINT);
        KEYWORDS.put("return", RETURN);
        KEYWORDS.put("super", SUPER);
        KEYWORDS.put("this", THIS);
        KEYWORDS.put("true", TRUE);
        KEYWORDS.put("var", VAR);
        KEYWORDS.put("while", WHILE);
    }

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start;
    private int current;
    private int line;

    public Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            // 只有一个字符
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case ';': addToken(SEMICOLON); break;
            case '+': addToken(PLUS); break;
            case '-': addToken(MINUS); break;
            case '*': addToken(STAR); break;
            // 一个或者两个字符
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GTREATER_EQUAL : GREATER);
                break;
            // 可能是除，也可能是注释
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } if (match('*')) {
                    multiComment();
                } else {
                    addToken(SLASH);
                }
                break;
                // skip white space
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            // string
            case '"':
                string();
                break;

            default:
                if (isDigit(c)) {
                   number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Unexpected character.");
                    break;
                }
        }
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    private char peekNext() {
        return current + 1 >= source.length() ? '\0' : source.charAt(current + 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * @param expected
     * @return 下一个字符是否为 expected，如果是的消费它
     */
    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }

        if (source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    private void multiComment() {
        while (!(peek() == '*' && peekNext() == '/')) {
            if (isAtEnd()) {
                Lox.error(line, "Unterminated block comment.");
                break;
            } else {
                char skip = advance();
                if (skip == '\n') {
                    line++;
                }
            }
        }
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') { // 支持换行, 为了处理简单
                line++;
            }
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        // skip closing "
        advance();
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = KEYWORDS.getOrDefault(text, IDENTIFIER);
        addToken(type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
