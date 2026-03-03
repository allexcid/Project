package final_pt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class WaterBillPanel extends JPanel {
    private static final Color APP_BG = new Color(243, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(21, 93, 125);
    private static final Color PRIMARY_HOVER = new Color(16, 73, 99);
    private static final Color BORDER = new Color(214, 224, 232);
    private static final Color TEXT_DARK = new Color(37, 44, 53);

    private static final double RATE_PER_CUBIC_METER = 2.50;

    private User currentUser;
    private JLabel balanceLabel;
    private JTextField consumptionField;
    private JLabel amountLabel;
    private DecimalFormat df = new DecimalFormat("#0.00");

    public WaterBillPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(APP_BG);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(22, 22, 22, 22)
        ));
        contentPanel.setBackground(CARD_BG);

        JLabel titleLabel = new JLabel("Water Bill Payment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        balanceLabel = new JLabel("Current Balance: $0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        balanceLabel.setForeground(new Color(91, 100, 110));
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rateLabel = new JLabel("Rate: $" + RATE_PER_CUBIC_METER + " per cubic meter");
        rateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rateLabel.setForeground(new Color(91, 100, 110));
        rateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel consumptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        consumptionPanel.setOpaque(false);
        JLabel consumptionLabel = new JLabel("Water Consumption (m3): ");
        consumptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        consumptionField = new JTextField(12);
        styleTextField(consumptionField);
        consumptionPanel.add(consumptionLabel);
        consumptionPanel.add(consumptionField);

        amountLabel = new JLabel("Amount to Pay: $0.00");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        amountLabel.setForeground(TEXT_DARK);
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton calculateButton = new JButton("Calculate Bill");
        styleButton(calculateButton, false);
        calculateButton.addActionListener(e -> calculateBill());

        JButton payButton = new JButton("Pay Bill");
        styleButton(payButton, true);
        payButton.addActionListener(e -> payBill());

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        contentPanel.add(balanceLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(rateLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        contentPanel.add(consumptionPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(amountLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(calculateButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(payButton);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton button, boolean primary) {
        button.setMaximumSize(new Dimension(320, 38));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(primary ? PRIMARY : new Color(230, 238, 244));
        button.setForeground(primary ? Color.BLACK : PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(primary ? PRIMARY_HOVER : new Color(214, 227, 236));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primary ? PRIMARY : new Color(230, 238, 244));
            }
        });
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(180, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    public void setUser(User user) {
        this.currentUser = user;
        updateBalance();
    }

    private void updateBalance() {
        if (currentUser != null) {
            balanceLabel.setText("Current Balance: $" + df.format(currentUser.getBalance()));
        } else {
            balanceLabel.setText("Current Balance: $0.00");
        }
    }

    private void calculateBill() {
        try {
            double consumption = Double.parseDouble(consumptionField.getText().trim());
            if (consumption < 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid consumption value", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount = consumption * RATE_PER_CUBIC_METER;
            amountLabel.setText("Amount to Pay: $" + df.format(amount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void payBill() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double consumption = Double.parseDouble(consumptionField.getText().trim());
            if (consumption < 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid consumption value", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount = consumption * RATE_PER_CUBIC_METER;

            if (!currentUser.canPay(amount)) {
                JOptionPane.showMessageDialog(this, "Insufficient balance", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.addTransaction("Water Bill Payment", -amount);
            updateBalance();

            JOptionPane.showMessageDialog(this, "Payment successful!\nAmount paid: $" + df.format(amount), "Success", JOptionPane.INFORMATION_MESSAGE);

            consumptionField.setText("");
            amountLabel.setText("Amount to Pay: $0.00");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}
