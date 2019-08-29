package me.kovel.calculator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class AppMain
{
    public static void main(String[] args)
    {
        List<String> expressions = Arrays.asList(
                "1 2 + 4 * 3 +",  // 15
                "3 4 5 * + 7 + 4 6 - /",
                "5 1 2 + 4 * + 3 - ", // 14
                "1 2 3.5" // 3.5
        );
        expressions.forEach(AppMain::evaluate);
    }

    private static void evaluate(String e)
    {
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
