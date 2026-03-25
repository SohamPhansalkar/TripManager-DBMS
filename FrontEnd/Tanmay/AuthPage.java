package FrontEnd.Tanmay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AuthPage extends JFrame {

    // Light Theme
    private final Color L_BG = Color.WHITE;
    private final Color L_TEXT = new Color(50, 50, 50);
    private final Color L_INPUT_BG = new Color(245, 245, 245);
    private final Color PRIMARY_BLUE = new Color(0, 102, 204);

    // Dark Theme
    private final Color D_BG = new Color(18, 18, 18);
    private final Color D_TEXT = new Color(240, 240, 240);
    private final Color D_INPUT_BG = new Color(35, 35, 35);

    private boolean isDarkMode = false;
    private boolean isSignupMode = false;

    private JPanel mainPanel, cardPanel;
    private CardLayout cardLayout;
    private HttpClient client;

    private List<JButton> toggleButtons = new ArrayList<>();
    private List<JTextField> inputFields = new ArrayList<>();

    public AuthPage(boolean darkMode) {
        this.isDarkMode = darkMode;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        setTitle("TripManager - Authentication");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel(new BorderLayout());
        
        // Simple Navbar for AuthPage
        JPanel authNavbar = new JPanel(new BorderLayout());
        authNavbar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton homeBtn = new JButton("Home");
        homeBtn.setBorderPainted(false);
        homeBtn.setContentAreaFilled(false);
        homeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBtn.addActionListener(e -> {
            new LandingPage(isDarkMode).setVisible(true);
            this.dispose();
        });
        authNavbar.add(homeBtn, BorderLayout.WEST);
        toggleButtons.add(homeBtn);

        ThemeSwitch ts = new ThemeSwitch();
        authNavbar.add(ts, BorderLayout.EAST);
        
        mainPanel.add(authNavbar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createAuthPanel(false), "LOGIN");
        cardPanel.add(createAuthPanel(true), "SIGNUP");
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        applyTheme();
    }

    private JPanel createAuthPanel(boolean signup) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));
        p.setOpaque(false);

        JLabel title = new JLabel(signup ? "Create Account" : "Welcome Back");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(signup ? "Join the TripManager community" : "Sign in to continue planning");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(10));
        p.add(subtitle);
        p.add(Box.createVerticalStrut(30));

        // Fields
        JTextField emailField = createStyledTextField("Email");
        JPasswordField passField = createStyledPasswordField("Password");
        inputFields.add(emailField); inputFields.add(passField);

        if (signup) {
            JTextField firstNameField = createStyledTextField("First Name");
            JTextField lastNameField = createStyledTextField("Last Name");
            JTextField dobField = createStyledTextField("Date of Birth (YYYY-MM-DD)");
            inputFields.add(firstNameField); inputFields.add(lastNameField); inputFields.add(dobField);
            
            p.add(firstNameField); p.add(Box.createVerticalStrut(15));
            p.add(lastNameField); p.add(Box.createVerticalStrut(15));
            p.add(dobField); p.add(Box.createVerticalStrut(15));
            p.add(emailField); p.add(Box.createVerticalStrut(15));
            p.add(passField); p.add(Box.createVerticalStrut(30));

            JButton actionBtn = new JButton("Sign Up");
            stylePrimaryButton(actionBtn);
            actionBtn.addActionListener(e -> handleSignup(emailField.getText(), new String(passField.getPassword()), 
                             firstNameField.getText(), lastNameField.getText(), dobField.getText()));
            p.add(actionBtn);
        } else {
            p.add(emailField); p.add(Box.createVerticalStrut(15));
            p.add(passField); p.add(Box.createVerticalStrut(30));

            JButton actionBtn = new JButton("Sign In");
            stylePrimaryButton(actionBtn);
            actionBtn.addActionListener(e -> handleLogin(emailField.getText(), new String(passField.getPassword())));
            p.add(actionBtn);
        }

        p.add(Box.createVerticalStrut(20));

        JButton toggleBtn = new JButton(signup ? "Already have an account? Login" : "Don't have an account? Sign Up");
        toggleBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        toggleBtn.setBorderPainted(false);
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.addActionListener(e -> {
            isSignupMode = !isSignupMode;
            cardLayout.show(cardPanel, isSignupMode ? "SIGNUP" : "LOGIN");
        });
        toggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleButtons.add(toggleBtn);
        p.add(toggleBtn);

        return p;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        styleField(f);
        return f;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField f = new JPasswordField(placeholder);
        styleField(f);
        return f;
    }

    private void styleField(JTextField f) {
        f.setMaximumSize(new Dimension(400, 45));
        f.setPreferredSize(new Dimension(400, 45));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                    BorderFactory.createEmptyBorder(0, 15, 0, 15)
                ));
            }
            public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(0, 15, 0, 15)
                ));
            }
        });
    }

    private void stylePrimaryButton(JButton b) {
        b.setMaximumSize(new Dimension(400, 50));
        b.setPreferredSize(new Dimension(400, 50));
        b.setBackground(PRIMARY_BLUE);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void handleLogin(String email, String password) {
        String json = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        sendRequest("http://localhost:8080/login", json, "Login Successful!");
    }

    private void handleSignup(String email, String password, String first, String last, String dob) {
        String json = String.format(
            "{\"email\": \"%s\", \"password\": \"%s\", \"first_name\": \"%s\", \"last_name\": \"%s\", \"dob\": \"%s\"}",
            email, password, first, last, dob
        );
        sendRequest("http://localhost:8080/signup", json, "Signup Successful! Please login.");
    }

    private void sendRequest(String url, String json, String successMsg) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    if (res.statusCode() == 200) {
                        JOptionPane.showMessageDialog(this, successMsg);
                        if (successMsg.contains("Login")) {
                            SwingUtilities.invokeLater(() -> {
                                new Dashboard(isDarkMode).setVisible(true);
                                this.dispose();
                            });
                        } else if (successMsg.contains("Signup")) {
                            SwingUtilities.invokeLater(() -> {
                                isSignupMode = false;
                                cardLayout.show(cardPanel, "LOGIN");
                            });
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: " + res.body(), "Failed", JOptionPane.ERROR_MESSAGE);
                    }
                })
                .exceptionally(ex -> {
                    Object[] options = {"Try Again", "Enter Demo Mode (Bypass Server)"};
                    int n = JOptionPane.showOptionDialog(this,
                        "Server not reachable at localhost:8080.\nWould you like to enter Demo Mode to test the UI?",
                        "Connection Error",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null, options, options[0]);

                    if (n == 1) { // Demo Mode selected
                        SwingUtilities.invokeLater(() -> {
                            new Dashboard(isDarkMode).setVisible(true);
                            this.dispose();
                        });
                    }
                    return null;
                });
    }

    private void applyTheme() {
        Color bg = isDarkMode ? D_BG : L_BG;
        Color text = isDarkMode ? D_TEXT : L_TEXT;
        Color inputBg = isDarkMode ? D_INPUT_BG : L_INPUT_BG;

        mainPanel.setBackground(bg);
        mainPanel.getComponent(0).setBackground(bg); // Navbar
        cardPanel.setBackground(bg);
        
        for (JButton b : toggleButtons) b.setForeground(isDarkMode ? Color.WHITE : PRIMARY_BLUE);
        for (JTextField f : inputFields) {
            f.setBackground(inputBg);
            f.setForeground(text);
        }

        updateComponentTree(mainPanel, bg, text);
        repaint();
    }

    private void updateComponentTree(Container root, Color bg, Color text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JPanel) {
                ((JPanel) c).setBackground(bg);
                updateComponentTree((Container) c, bg, text);
            } else if (c instanceof JLabel) {
                if (((JLabel) c).getForeground() != Color.GRAY && ((JLabel) c).getForeground() != new Color(255, 180, 0)) {
                    c.setForeground(text);
                }
            }
        }
    }

    private class ThemeSwitch extends JPanel {
        private final int width = 50, height = 26;
        public ThemeSwitch() {
            setPreferredSize(new Dimension(width, height));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    isDarkMode = !isDarkMode;
                    applyTheme();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isDarkMode ? PRIMARY_BLUE : new Color(200, 200, 200));
            g2.fill(new java.awt.geom.RoundRectangle2D.Double(0, 0, width, height, height, height));
            g2.setColor(Color.WHITE);
            int padding = 3, thumbSize = height - (padding * 2);
            int x = isDarkMode ? (width - thumbSize - padding) : padding;
            g2.fillOval(x, padding, thumbSize, thumbSize);
            g2.dispose();
        }
    }
}
