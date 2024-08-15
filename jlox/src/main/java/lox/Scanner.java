package lox;

import java.util.ArrayList;
import java.util.List;

import static lox.TokenType.BANG_EQUAL;
import static lox.TokenType.COMMA;
import static lox.TokenType.DOT;
import static lox.TokenType.EOF;
import static lox.TokenType.EQUAL;
import static lox.TokenType.EQUAL_EQUAL;
import static lox.TokenType.GREATER;
import static lox.TokenType.GTREATER_EQUAL;
import static lox.TokenType.LEFT_BRACE;
import static lox.TokenType.LEFT_PAREN;
import static lox.TokenType.LESS;
import static lox.TokenType.LESS_EQUAL;
import static lox.TokenType.MINUS;
import static lox.TokenType.NUMBER;
import static lox.TokenType.PLUS;
import static lox.TokenType.RIGHT_BRACE;
import static lox.TokenType.RIGHT_PAREN;
import static lox.TokenType.SEMICOLON;
import static lox.TokenType.SLASH;
import static lox.TokenType.STAR;
import static lox.TokenType.STRING;

public class Scanner {
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
                addToken(match('=') ? BANG_EQUAL : BANG_EQUAL);
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

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
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


}
