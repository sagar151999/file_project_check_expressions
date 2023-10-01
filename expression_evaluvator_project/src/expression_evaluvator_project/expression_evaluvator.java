package expression_evaluvator_project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class expression_evaluvator {
	
	    public static boolean checkvalidoperator(char c) {
	        return c == '+' || c == '-' || c == '*' || c == '/';
	    }

	    public static boolean checkexpbalanced(String expression) {
	        Stack<Character> stack = new Stack<>();
	        for (int i = 0; i < expression.length(); i++) {
	            char c = expression.charAt(i);
	            if (c == '(') {
	                stack.push(c);
	            } else if (c == ')') {
	                if (stack.isEmpty()) {
	                    return false;
	                }
	                stack.pop();
	            }
	        }
	        return stack.isEmpty();
	    }

	    public static double evaluateExpression(String expression, Map<String, Double> variables) {
	        try {
	            String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");
	            Stack<Double> values = new Stack<>();
	            Stack<String> operators = new Stack<>();

	            for (String token : tokens) {
	                token = token.trim();

	                if (token.isEmpty()) {
	                    continue;
	                }

	                if (token.matches("[+-]?\\d+(\\.\\d+)?")) {
	                    values.push(Double.parseDouble(token));
	                } else if (variables.containsKey(token)) {
	                    values.push(variables.get(token));
	                } else if (token.equals("(")) {
	                    operators.push(token);
	                } else if (token.equals(")")) {
	                    while (!operators.isEmpty() && !operators.peek().equals("(")) {
	                        applyOperator(values, operators);
	                    }
	                    operators.pop(); // Pop the '('
	                } else if (checkvalidoperator(token.charAt(0))) {
	                    while (!operators.isEmpty() && hasHigherPrecedence(token, operators.peek())) {
	                        applyOperator(values, operators);
	                    }
	                    operators.push(token);
	                } else {
	                    throw new IllegalArgumentException("Invalid token: " + token);
	                }
	            }

	            while (!operators.isEmpty()) {
	                applyOperator(values, operators);
	            }

	            if (values.size() == 1) {
	                return values.pop();
	            } else {
	                throw new IllegalArgumentException("given expression is Invalid expression");
	            }
	        } catch (Exception e) {
	            return Double.NaN; // Return NaN if there's an error in evaluation
	        }
	    }

	    private static boolean hasHigherPrecedence(String op1, String op2) {
	        return (op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"));
	    }

	    private static void applyOperator(Stack<Double> values, Stack<String> operators) {
	        String operator = operators.pop();
	        double operand2 = values.pop();
	        double operand1 = values.pop();

	        switch (operator) {
	            case "+":
	                values.push(operand1 + operand2);
	                break;
	            case "-":
	                values.push(operand1 - operand2);
	                break;
	            case "*":
	                values.push(operand1 * operand2);
	                break;
	            case "/":
	                values.push(operand1 / operand2);
	                break;
	        }
	    }
	    


	    public static void main(String[] args) {
	        String fileName = "C:\\Users\\Sagar\\eclipse-workspace\\expression_evaluvator_project\\expressionFile";
	        String resultFileName = "C:\\Users\\Sagar\\eclipse-workspace\\expression_evaluvator_project\\expressionFileoutput";

	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(fileName));
	            String expression = reader.readLine();

	            if (expression == null) {
	            	try (PrintWriter writer = new PrintWriter(resultFileName)) {
	                    writer.println("The given file is empty.");
	                    System.out.println("The given file is empty.");
	                } catch (IOException e) {
	                    System.out.println("Error writing to result file: " + e.getMessage());
	                }
	               
	                return;
	            }

	            if (!checkexpbalanced(expression)) {
	            	try (PrintWriter writer = new PrintWriter(resultFileName)) {
	                    writer.println("Compile time error: Unbalanced parentheses");
	                    System.out.println("Compile time error: Unbalanced parentheses");
	                } catch (IOException e) {
	                    System.out.println("Error writing to result file: " + e.getMessage());
	                }
	                
	                return;
	            }

	            boolean hasValidOperator = false;
	            for (char c : expression.toCharArray()) {
	                if (checkvalidoperator(c)) {
	                    hasValidOperator = true;
	                    break;
	                }
	            }

	            if (!hasValidOperator) {
	            	try (PrintWriter writer = new PrintWriter(resultFileName)) {
	                    writer.println("Compile time error: No valid arithmetic operators");
	                    System.out.println("Compile time error: No valid arithmetic operators");
	                } catch (IOException e) {
	                    System.out.println("Error writing to result file: " + e.getMessage());
	                }
	                
	                return;
	            }

	            Scanner scanner = new Scanner(System.in);
	            Map<String, Double> variables = new HashMap<>();

	            
	            for (char c : expression.toCharArray()) {
	                if (Character.isLetter(c)) {
	                    String variableName = Character.toString(c);
	                    if (!variables.containsKey(variableName)) {
	                        System.out.println("Enter the value of '" + variableName + "':");
	                        double variableValue = scanner.nextDouble();
	                        variables.put(variableName, variableValue);
	                    }
	                }
	            }

	            double result = evaluateExpression(expression, variables);

	            try (PrintWriter writer = new PrintWriter(resultFileName)) {
	                writer.println("Result (" + expression + "): " + result);
	                System.out.println("Result (" + expression + "): " + result + "\nwritten to " + resultFileName);
	            } catch (IOException e) {
	                System.out.println("Error writing to result file: " + e.getMessage());
	            }
	        } catch (FileNotFoundException e) {
	            System.out.println("The file " + fileName + " does not exist.");
	        } catch (IOException e) {
	            System.out.println("An error occurred while reading the file: " + e.getMessage());
	        }
	    }
	}