// ErrorHandler collects and reports errors
public class ErrorHandler {
    public static void error(String message) throws Exception {
        throw new Exception("Error: " + message);
    }
}
