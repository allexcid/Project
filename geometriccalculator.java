package gui_activity;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class geometriccalculator 
{
	public static void main(String[] args) 
	{
        FrameLayout start = new FrameLayout();
        start.frame1();
    }
}

class FrameLayout 
{
    public Shapes2D part1;
    public Shapes3D part2;

    public void frame1() 
    {
        JFrame frame = new JFrame("Geometric Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: 2D Shapes
        part1 = new Shapes2D();
        tabbedPane.addTab("2D Shapes", part1.getPanel1());

        // Tab 2: 3D Shapes
        part2 = new Shapes3D();
        tabbedPane.addTab("3D Shapes", part2.getPanel2());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}

class Shapes2D 
{
    private JPanel panel1;
    private JComboBox<String> shapeSelector;
    private JLabel dimensionLabel1, dimensionLabel2;
    private JTextField inputField1, inputField2;
    private JTextField resultField;
    private JLabel helpLabel;

    public Shapes2D() 
    {
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(new Color(245, 245, 250)); // Soft light background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Select a 2D shape:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(50, 50, 80));

        shapeSelector = new JComboBox<>(new String[]{"Circle", "Square", "Rectangle"});
        shapeSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dimensionLabel1 = new JLabel("Dimension 1:");
        dimensionLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dimensionLabel1.setForeground(new Color(80, 80, 120));
        inputField1 = new JTextField(10);
        inputField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dimensionLabel2 = new JLabel("Dimension 2:");
        dimensionLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dimensionLabel2.setForeground(new Color(80, 80, 120));
        inputField2 = new JTextField(10);
        inputField2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton button = new JButton("Calculate");
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        resultField = new JTextField(15);
        resultField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultField.setEditable(false);
        resultField.setBackground(new Color(230, 230, 250));
        resultField.setForeground(new Color(30, 30, 60));
        resultField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2));

        // Help label for presentation
        helpLabel = new JLabel("Enter dimensions and click Calculate to find area or diagonal.");
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        helpLabel.setForeground(new Color(100, 100, 140));
        
        // Layout design using GridBagConstraint Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(label, gbc);

        gbc.gridx = 1;
        panel1.add(shapeSelector, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(dimensionLabel1, gbc);

        gbc.gridx = 1;
        panel1.add(inputField1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(dimensionLabel2, gbc);

        gbc.gridx = 1;
        panel1.add(inputField2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel1.add(button, gbc);

        gbc.gridy = 4;
        panel1.add(helpLabel, gbc);

        gbc.gridy = 5;
        panel1.add(resultField, gbc);

        updateInputFields();

        shapeSelector.addActionListener(e -> updateInputFields());
        button.addActionListener(e -> calculate());
    }

    private void updateInputFields() 
    {
        String shape = (String) shapeSelector.getSelectedItem();

        inputField1.setText("");
        inputField2.setText("");
        resultField.setText("");

        switch (shape) 
        {
            case "Circle":
                dimensionLabel1.setText("Radius:");
                dimensionLabel2.setText("Angle (degrees, optional for arc length):");
                dimensionLabel1.setVisible(true);
                inputField1.setVisible(true);
                dimensionLabel2.setVisible(true);
                inputField2.setVisible(true);
                break;

            case "Square":
                dimensionLabel1.setText("Side:");
                dimensionLabel1.setVisible(true);
                inputField1.setVisible(true);
                dimensionLabel2.setVisible(false);
                inputField2.setVisible(false);
                break;

            case "Rectangle":
                dimensionLabel1.setText("Length:");
                dimensionLabel2.setText("Width:");
                dimensionLabel1.setVisible(true);
                inputField1.setVisible(true);
                dimensionLabel2.setVisible(true);
                inputField2.setVisible(true);
                break;
        }
        panel1.revalidate();
        panel1.repaint();
    }

    private void calculate()
    {
        try {
            String shape = (String) shapeSelector.getSelectedItem();
            double result;

            switch (shape) {
                case "Circle":
                    double radius = Double.parseDouble(inputField1.getText());
                    if (radius <= 0) {
                        resultField.setText("Error: Radius must be > 0");
                        return;
                    }
                    String angleText = inputField2.getText().trim();
                    if (!angleText.isEmpty()) {
                        double angleDeg = Double.parseDouble(angleText);
                        if (angleDeg > 0) {
                            double angleRad = Math.toRadians(angleDeg);
                            result = radius * angleRad;
                            resultField.setText(String.format("Arc Length: %.2f", result));
                            return;
                        }
                    }
                    result = Math.PI * radius * radius;
                    resultField.setText(String.format("Area: %.2f", result));
                    break;

                case "Square":
                    double side = Double.parseDouble(inputField1.getText());
                    if (side <= 0) {
                        resultField.setText("Error: Side must be > 0");
                        return;
                    }
                    result = side * side;
                    resultField.setText(String.format("Area: %.2f", result));
                    break;

                case "Rectangle":
                    double length = Double.parseDouble(inputField1.getText());
                    double width = Double.parseDouble(inputField2.getText());
                    if (length <= 0 || width <= 0) {
                        resultField.setText("Error: Length and Width must be > 0");
                        return;
                    }
                    result = Math.sqrt(length * length + width * width);
                    resultField.setText(String.format("Diagonal: %.2f", result));
                    break;
            }
        } catch (NumberFormatException ex) {
            resultField.setText("Error: Enter valid numbers!");
        }
    }

    public JPanel getPanel1() 
    {
        return panel1;
    }
}

class Shapes3D 
{
    private JPanel panel2;
    private JComboBox<String> shapeSelector;
    private JLabel dimensionLabel1, dimensionLabel2;
    private JTextField inputField1, inputField2;
    private JTextField resultField;
    private JLabel volumeLabel;

    public Shapes3D() 
    {
        panel2 = new JPanel(new GridBagLayout());
        panel2.setBackground(new Color(245, 250, 245)); // Soft greenish background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Select a 3D shape:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(50, 80, 50));

        shapeSelector = new JComboBox<>(new String[]{"Cylinder", "Sphere", "Cube"});
        shapeSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dimensionLabel1 = new JLabel("Dimension 1:");
        dimensionLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dimensionLabel1.setForeground(new Color(80, 120, 80));
        inputField1 = new JTextField(10);
        inputField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dimensionLabel2 = new JLabel("Dimension 2:");
        dimensionLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dimensionLabel2.setForeground(new Color(80, 120, 80));
        inputField2 = new JTextField(10);
        inputField2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton button = new JButton("Calculate");
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        resultField = new JTextField(15);
        resultField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultField.setEditable(false);
        resultField.setBackground(new Color(220, 255, 220));
        resultField.setForeground(new Color(30, 70, 30));
        resultField.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));

        // Volume label for presentation
        volumeLabel = new JLabel("Volume is being calculated.");
        volumeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        volumeLabel.setForeground(new Color(60, 100, 60));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel2.add(label, gbc);

        gbc.gridx = 1;
        panel2.add(shapeSelector, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel2.add(dimensionLabel1, gbc);

        gbc.gridx = 1;
        panel2.add(inputField1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel2.add(dimensionLabel2, gbc);

        gbc.gridx = 1;
        panel2.add(inputField2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel2.add(button, gbc);

        gbc.gridy = 4;
        panel2.add(volumeLabel, gbc);

        gbc.gridy = 5;
        panel2.add(resultField, gbc);

        updateInputFields();

        shapeSelector.addActionListener(e -> updateInputFields());
        button.addActionListener(e -> calculateVolume());
    }

    private void updateInputFields() 
    {
        String shape = (String) shapeSelector.getSelectedItem();

        inputField1.setText("");
        inputField2.setText("");
        resultField.setText("");

        switch (shape)
        {
            case "Cylinder":
                dimensionLabel1.setText("Radius:");
                dimensionLabel2.setText("Height:");
                dimensionLabel1.setVisible(true);
                inputField1.setVisible(true);
                dimensionLabel2.setVisible(true);
                inputField2.setVisible(true);
                break;

            case "Sphere":
                dimensionLabel1.setText("Radius:");
                dimensionLabel1.setVisible(true);
                inputField1.setVisible(true);
                dimensionLabel2.setVisible(false);
                inputField2.setVisible(false);
                break;

            case "Cube":
                dimensionLabel1.setText("Side:");
                dimensionLabel1.setVisible(true);
                inputField1.setVisible(true);
                dimensionLabel2.setVisible(false);
                inputField2.setVisible(false);
                break;
        }
        panel2.revalidate();
        panel2.repaint();
    }

    private void calculateVolume() 
    {
        try {
            String shape = (String) shapeSelector.getSelectedItem();
            double result = 0;

            switch (shape) 
            {
                case "Cylinder":
                    double radius = Double.parseDouble(inputField1.getText());
                    double height = Double.parseDouble(inputField2.getText());
                    if (radius <= 0 || height <= 0) {
                        resultField.setText("Error: Radius and Height must be > 0");
                        return;
                    }
                    result = Math.PI * radius * radius * height;
                    break;

                case "Sphere":
                    double r = Double.parseDouble(inputField1.getText());
                    if (r <= 0) {
                        resultField.setText("Error: Radius must be > 0");
                        return;
                    }
                    result = (4.0 / 3.0) * Math.PI * Math.pow(r, 3);
                    break;

                case "Cube":
                    double side = Double.parseDouble(inputField1.getText());
                    if (side <= 0) {
                        resultField.setText("Error: Side must be > 0");
                        return;
                    }
                    result = Math.pow(side, 3);
                    break;
            }

            resultField.setText(String.format("Volume: %.2f", result));
        } catch (NumberFormatException ex) {
            resultField.setText("Error: Enter valid numbers!");
        }
    }

    public JPanel getPanel2() 
    {
        return panel2;
    }
}
