package lox;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 这个类的职责是什么?
 */
@Slf4j
public class Lox {
    static boolean hasError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            log.info("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(path));
        run(new String(data, StandardCharsets.UTF_8));
        if (hasError) {
            System.exit(65);
        }
    }

    /**
     * 启动 REPL
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hasError = false;
        }
    }

    private static void run(String source) {
        List<Token> tokens = new Scanner(source).scanTokens();
        System.out.println(tokens);
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message + message);
        hasError = true;
    }
}
