package me.kovel.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AppMain {
    private static final List<Character> OPERATORS_HIGH_PRIORITY = Collections.singletonList('^');
    private static final List<Character> OPERATORS_MIDDLE_PRIORITY = Arrays.asList('*', '/');
    private static final List<Character> OPERATORS_LOW_PRIORITY = Arrays.asList('+', '-');
    private static final List<Character> OPERATORS_LOWEST_PRIORITY = Arrays.asList('(', ')');
    private static final List<Character> OPERATORS = new ArrayList<>();
    private static final Map<Character, Integer> OPERATOR_PRIORITIES = new HashMap<>();

    static {
        OPERATORS.addAll(OPERATORS_HIGH_PRIORITY);
        OPERATORS.addAll(OPERATORS_MIDDLE_PRIORITY);
        OPERATORS.addAll(OPERATORS_LOW_PRIORITY);
        OPERATORS.addAll(OPERATORS_LOWEST_PRIORITY);

        OPERATOR_PRIORITIES.put('^', 4);
        OPERATOR_PRIORITIES.put('*', 3);
        OPERATOR_PRIORITIES.put('/', 3);
        OPERATOR_PRIORITIES.put('+', 2);
        OPERATOR_PRIORITIES.put('-', 2);
        OPERATOR_PRIORITIES.put('(', 1);
        OPERATOR_PRIORITIES.put(')', 1);
    }

    public static void main(String[] args) {
        List<String> expressions = Arrays.asList(
                "",
                "1 2 3",
                "1 2 3.5",
                "1 2 + 3.5 +",
                "1 3 +",
                "1 3 *",
                "1 3 -",
                "4 2 /",

                "1 2 + 4 * 3 +",  // 15
                "3 4 5 * + 7 + 4 6 - /",
                "5 1 2 + 4 * + 3 - ", // 14
                "1 2 3.5" // 3.5
        );
        expressions.forEach(AppMain::evaluate);

        evaluate(generateRPN("1 + 2 * 3"));
        evaluate(generateRPN("2 + 2 * 2"));
        evaluate(generateRPN("2 + 2 * 2 - 2 ^ 2"));
        evaluate(generateRPN("3 + 4 * 2 / ( 1 - 5 ) ^ 2"));
    }

    private static String generateRPN(String s) {
        StringBuilder output = new StringBuilder();
        StringBuilder token = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                token.append(c);
            } else if (c == ' ') {
                if (token.length() > 0)
                {
                    output.append(token);
                    output.append(' ');
                    token = new StringBuilder();
                }
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                char topToken = operators.pop();
                while (topToken != '(') {
                    output.append(topToken);
                    output.append(' ');
                    topToken = operators.pop();
                }
            } else if (OPERATORS.contains(c)) {
                while (!operators.isEmpty() && OPERATOR_PRIORITIES.get(operators.peek()) > OPERATOR_PRIORITIES.get(c)) {
                    output.append(operators.pop());
                    output.append(' ');
                }
                operators.push(c);
            } else {
                System.err.println("Wrong token: " + c);
                System.exit(1);
            }
        }

        if (token.length() > 0) {
            output.append(token);
            output.append(' ');
        }

        while (!operators.isEmpty()) {
            output.append(operators.pop());
            output.append(' ');
        }

        return output.toString().trim();
    }

    private static void evaluate(String e) {
        if (e == null || e.isEmpty()) {
            System.out.println(0);
            return;
        }

        System.out.print("'" + e + "': ");

        Stack<String> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < e.length(); i++) {
            char c = e.charAt(i);

            if (c == ' ') {
                if (builder.length() > 0) {
                    stack.add(builder.toString());
                    builder = new StringBuilder();
                }
                continue;
            }

            if (Character.isDigit(c) || c == '.') {
                builder.append(c);
                continue;
            }

            BigDecimal value;
            BigDecimal y = new BigDecimal(stack.pop());
            BigDecimal x = new BigDecimal(stack.pop());
            switch (c) {
                case '+':
                    value = x.add(y);
                    break;
                case '*':
                    value = x.multiply(y);
                    break;
                case '/':
                    value = x.divide(y);
                    break;
                case '-':
                    value = x.subtract(y);
                    break;
                case '^':
                    value = x.pow(y.intValue());
                    break;
                default:
                    continue;
            }
            stack.push(String.valueOf(value));
        }

        if (builder.length() > 0) {
            System.out.println(builder.toString());
        } else {
            System.out.println(stack.pop());
        }
    }
}
