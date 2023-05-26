// Imports for user input handling
import java.util.Scanner;

// Imports for data handling
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

// Imports for error handling
import java.lang.Exception;

// Imports for testing
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Main {
    public static void main(String[] args) {
        try {
            // Scanner object to handle user input
            Scanner scanner = new Scanner(System.in);

            // Prompt user to input their program and read it as a String
            System.out.println("Enter your program:");
            String text = scanner.nextLine();

            // LEXICAL ANALYSIS
            // Lexer object with input text
            Lexer lexer = new Lexer(text);

            // PARSING
            // Parser object with Lexer object
            Parser parser = new Parser(lexer);
            // Call parse method to generate Abstract Syntax Tree (AST)
            AST tree = parser.parse();

            // SEMANTIC ANALYSIS
            // SemanticAnalyzer object
            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            // Pass AST to SemanticAnalyzer
            analyzer.visit(tree);

            // INTERPRETATION
            // Create Interpreter object with AST and SemanticAnalyzer
            Interpreter interpreter = new Interpreter(tree, analyzer);
            // Interpret program by traversing AST and performing operations it represents
            interpreter.interpret();

            // OUTPUT
            // Print values of all variables in symbol table
            for (Map.Entry<String, Integer> var : interpreter.symbolTable.symbols.entrySet()) {
                System.out.println(var.getKey() + " = " + var.getValue());
            }

            // Close scanner
            scanner.close();

        } catch (LexerException e) {
            // Print Lexer error message to console
            System.err.println(e.getMessage());
        } catch (ParserException e) {
            // Print Parser error message to console
            System.err.println(e.getMessage());
        } catch (SemanticAnalyzerException e) {
            // Print Semantic Analyzer error message to console
            System.err.println(e.getMessage());
        } catch (InterpreterException e) {
            // Print Interpreter error message to console
            System.err.println(e.getMessage());
        } catch (Exception e) {
            // Print general error message to console
            System.err.println(e.getMessage());
        }
    }
}
