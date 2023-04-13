import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    private static double dollarRate;
    private static double rubleRate;

    public static void main(String[] args) {
        loadRates();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the expression (e.g. $12 + 52.5R, or 'exit' to quit):");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            String[] tokens = input.split("\\s+");
            if (tokens.length != 3) {
                System.out.println("Invalid input, please try again.");
                continue;
            }
            try {
                double leftOperand = parseOperand(tokens[0]);
                double rightOperand = parseOperand(tokens[2]);
                char operator = tokens[1].charAt(0);
                double result = calculate(leftOperand, operator, rightOperand);
                System.out.println(formatAmount(result));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format, please try again.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void loadRates() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
            dollarRate = Double.parseDouble(properties.getProperty("dollar.rate"));
            rubleRate = Double.parseDouble(properties.getProperty("ruble.rate"));
        } catch (IOException e) {
            System.out.println("Failed to load rates from configuration file.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid rate format in configuration file.");
            System.exit(1);
        }
    }

    private static double parseOperand(String token) {
        if (token.startsWith("$")) {
            return Double.parseDouble(token.substring(1));
        } else if (token.endsWith("R")) {
            return Double.parseDouble(token.substring(0, token.length() - 1));
        } else {
            throw new IllegalArgumentException("Invalid operand format: " + token);
        }
    }

    private static double calculate(double leftOperand, char operator, double rightOperand) {
        switch (operator) {
            case '+':
                return leftOperand + rightOperand;
            case '-':
                return leftOperand - rightOperand;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private static String formatAmount(double amount) {
        if (amount >= 0) {
            return "$" + String.format("%.2f", amount );
        } else {
            return String.format("%.2f", -amount ) + "R";
        }
    }
/*
    public static double toDollar(double amount) {
        return amount / (rubleRate * dollarRate);
    }

    public static double toRub(double amount) {
        return (amount / dollarRate * rubleRate);
    }
    
 */
}