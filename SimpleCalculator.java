import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleCalculator {
    private JFrame frame;
    private JTextField display;
    private String currentInput = "";

    public SimpleCalculator() {
        frame = new JFrame("Simple Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // Display area (text field)
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 30));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(display, BorderLayout.NORTH);

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));

        // Create buttons
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "(", ")", "√"
        };

        // Add buttons to panel
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setBackground(new Color(200, 200, 255));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);

        // Keyboard Input Support
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode());
                if (key.matches("[0-9]") || key.matches("[+\\-*/=()√.]")) {
                    currentInput += key;
                    display.setText(currentInput);
                } else if (key.equals("Backspace")) {
                    if (currentInput.length() > 0) {
                        currentInput = currentInput.substring(0, currentInput.length() - 1);
                        display.setText(currentInput);
                    }
                }
            }
        });
        frame.setFocusable(true); // Ensure frame can focus for key events

        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String text = source.getText();

            if (text.equals("=")) {
                try {
                    currentInput = evaluateExpression(currentInput);
                } catch (Exception ex) {
                    currentInput = "Error";
                }
            } else if (text.equals("C")) {
                currentInput = "";
            } else {
                currentInput += text;
            }

            display.setText(currentInput);
        }
    }

    // Simple arithmetic parser function
    private String evaluateExpression(String expression) {
        try {
            // Handle square root symbol '√'
            if (expression.contains("√")) {
                expression = expression.replace("√", "Math.sqrt");
            }

            // Ensure expression is properly formatted with only valid characters
            expression = expression.replaceAll("[^0-9+\\-*/().sqrt]", "");

            // Calculate the result
            double result = evalExpression(expression);

            return String.valueOf(result);

        } catch (Exception e) {
            return "Error";
        }
    }

    // Function to evaluate simple arithmetic expressions
    private double evalExpression(String expression) {
        if (expression == null || expression.isEmpty()) {
            return 0;
        }

        // First, handle parenthesis by recursively evaluating them
        while (expression.contains("(")) {
            int start = expression.lastIndexOf("(");
            int end = expression.indexOf(")", start);
            String subExpr = expression.substring(start + 1, end);
            double subResult = evalExpression(subExpr);
            expression = expression.substring(0, start) + subResult + expression.substring(end + 1);
        }

        // Handle the basic arithmetic operations: +, -, *, /
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            return evalExpression(parts[0]) + evalExpression(parts[1]);
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-");
            return evalExpression(parts[0]) - evalExpression(parts[1]);
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            return evalExpression(parts[0]) * evalExpression(parts[1]);
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            return evalExpression(parts[0]) / evalExpression(parts[1]);
        }

        // If it's a number, return it
        return Double.parseDouble(expression);
    }

    public static void main(String[] args) {
        new SimpleCalculator();
    }
}
