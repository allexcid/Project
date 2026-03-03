package final_pt;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class UtilityPaymentSystem extends JFrame {
    private static final int MAX_USERS = 100;

    private static final Color APP_BG = new Color(243, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(21, 93, 125);
    private static final Color PRIMARY_HOVER = new Color(16, 73, 99);
    private static final Color BORDER = new Color(214, 224, 232);
    private static final Color TEXT_DARK = new Color(37, 44, 53);

    private final User[] users;
    private int userCount;
    private User currentUser;
    private JTabbedPane tabbedPane;
    private WaterBillPanel waterPanel;
    private ElectricityBillPanel electricityPanel;
    private RechargePanel rechargePanel;
    private TransactionPanel transactionPanel;
    private JPanel loginPanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JLabel userLabel;

    public UtilityPaymentSystem() {
        users = new User[MAX_USERS];
        userCount = 0;

        setTitle("Utility Payment & Recharge System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 650);
        setMinimumSize(new Dimension(880, 580));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);
        getContentPane().setBackground(APP_BG);

        createLoginPanel();
        createMainPanel();

        getContentPane().add(loginPanel, "LOGIN");
        getContentPane().add(mainPanel, "MAIN");

        cardLayout.show(getContentPane(), "LOGIN");
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(APP_BG);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(28, 30, 28, 30)
        ));
        formPanel.setPreferredSize(new Dimension(420, 320));

        JLabel title = createLabel("Welcome Back");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Sign in to pay bills and manage balance");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(92, 102, 112));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        styleTextField(usernameField, "Username");
        styleTextField(passwordField, "Password");
        styleButton(loginButton, true);
        styleButton(registerButton, false);

        formPanel.add(title);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        formPanel.add(subtitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(registerButton);

        loginButton.addActionListener(e -> login(usernameField.getText(), new String(passwordField.getPassword())));
        registerButton.addActionListener(e -> showRegisterDialog());

        loginPanel.add(formPanel);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(APP_BG);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PRIMARY);
        topPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        userLabel = new JLabel();
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, false);
        logoutButton.addActionListener(e -> logout());

        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(CARD_BG);
        tabbedPane.setForeground(TEXT_DARK);
        tabbedPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        tabbedPane.setFocusable(false);

        waterPanel = new WaterBillPanel();
        electricityPanel = new ElectricityBillPanel();
        rechargePanel = new RechargePanel();
        transactionPanel = new TransactionPanel();

        tabbedPane.addTab("Water Bill", new ImageIcon(), waterPanel, "Water Bill Payment");
        tabbedPane.addTab("Electricity Bill", new ImageIcon(), electricityPanel, "Electricity Bill Payment");
        tabbedPane.addTab("Recharge", new ImageIcon(), rechargePanel, "Add money to your account");
        tabbedPane.addTab("Transaction History", new ImageIcon(), transactionPanel, "View Transactions");
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == transactionPanel && currentUser != null) {
                transactionPanel.setUser(currentUser);
            }
        });

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(250, 38));
        field.setMaximumSize(new Dimension(420, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(6, 12, 6, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setToolTipText(placeholder);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(PRIMARY);
    }

    private void styleButton(JButton button, boolean primaryStyle) {
        button.setPreferredSize(new Dimension(220, 38));
        button.setMaximumSize(new Dimension(220, 38)); // smaller width
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // center in BoxLayout
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);

        if (primaryStyle) {
            button.setBackground(PRIMARY);
            button.setForeground(Color.BLACK);
            button.setOpaque(true);
        } else {
            button.setBackground(new Color(230, 238, 244));
            button.setForeground(PRIMARY);
            button.setOpaque(true);
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryStyle ? PRIMARY_HOVER : new Color(214, 227, 236));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryStyle ? PRIMARY : new Color(230, 238, 244));
            }
        });
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 30));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void login(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(username) && users[i].getPassword().equals(password)) {
                currentUser = users[i];
                cardLayout.show(getContentPane(), "MAIN");
                waterPanel.setUser(currentUser);
                electricityPanel.setUser(currentUser);
                rechargePanel.setUser(currentUser);
                transactionPanel.setUser(currentUser);
                userLabel.setText("Welcome, " + currentUser.getFullName());
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showRegisterDialog() {
        if (userCount >= MAX_USERS) {
            JOptionPane.showMessageDialog(this, "Maximum user limit reached", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField nameField = new JTextField();

        Object[] message = {
            "Username:", usernameField,
            "Password:", passwordField,
            "Full Name:", nameField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String name = nameField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < userCount; i++) {
                if (users[i].getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            users[userCount++] = new User(username, password, name);
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void logout() {
        currentUser = null;
        userLabel.setText("");
        cardLayout.show(getContentPane(), "LOGIN");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new UtilityPaymentSystem().setVisible(true));
    }
}
