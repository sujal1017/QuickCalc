package com.example.additionicon;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    // This shows the numbers and expression on screen
    TextView display;

    // This stores the full expression like: 52 + 2 - 5 * 2
    StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect display TextView from XML
        display = findViewById(R.id.display);
    }

    // ================= BUTTON HANDLERS =================

    // Called when number buttons (0–9, .) are pressed
    public void onDigit(View view) {
        Button button = (Button) view;

        // Add the digit to expression
        expression.append(button.getText());

        // Show updated expression
        display.setText(expression.toString());
    }

    // Called when + − × ÷ buttons are pressed
    public void onOperator(View view) {

        // Do nothing if expression is empty
        if (expression.length() == 0) return;

        // Prevent two operators in a row
        char lastChar = expression.charAt(expression.length() - 1);
        if ("+-×−÷".contains(String.valueOf(lastChar))) return;

        Button button = (Button) view;

        // Add operator with spaces (important for calculation)
        expression.append(" ").append(button.getText()).append(" ");
        display.setText(expression.toString());
    }

    // Called when = button is pressed
    public void onEqual(View view) {
        try {
            double result = calculate(expression.toString());

            // Show result
            display.setText(String.valueOf(result));

            // Clear expression for next calculation
            expression.setLength(0);

        } catch (Exception e) {
            display.setText("Error");
            expression.setLength(0);
        }
    }

    // Called when C button is pressed
    public void onClear(View view) {
        expression.setLength(0);
        display.setText("0");
    }

    // ================= CALCULATION LOGIC =================

    // This method evaluates the expression
    private double calculate(String exp) {

        // Convert UI symbols to Java symbols
        exp = exp.replace("×", "*")
                .replace("÷", "/")
                .replace("−", "-");

        // Stack for numbers and operators
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        // Split expression by space
        String[] parts = exp.split(" ");

        for (String part : parts) {

            // If number → push to number stack
            if (isNumber(part)) {
                numbers.push(Double.parseDouble(part));
            }
            // If operator → handle precedence
            else {
                char op = part.charAt(0);

                while (!operators.isEmpty() &&
                        getPriority(operators.peek()) >= getPriority(op)) {

                    double b = numbers.pop();
                    double a = numbers.pop();
                    char operator = operators.pop();

                    numbers.push(doOperation(a, b, operator));
                }

                operators.push(op);
            }
        }

        // Final calculation
        while (!operators.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char operator = operators.pop();

            numbers.push(doOperation(a, b, operator));
        }

        return numbers.pop();
    }

    // Checks if string is a number
    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Operator priority
    private int getPriority(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    // Performs calculation
    private double doOperation(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return b == 0 ? 0 : a / b;
        }
        return 0;
    }
}
