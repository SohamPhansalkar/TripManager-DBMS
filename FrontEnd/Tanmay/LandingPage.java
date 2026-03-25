package FrontEnd.Tanmay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LandingPage extends JFrame {

    // Light Theme Colors
    private final Color L_PRIMARY_BLUE = new Color(0, 102, 204);
    private final Color L_BG_MAIN = Color.WHITE;
    private final Color L_BG_HERO = new Color(245, 245, 245);
    private final Color L_TEXT_PRIMARY = new Color(51, 51, 51);
    private final Color L_TEXT_SECONDARY = Color.GRAY;
    private final Color L_CARD_BORDER = new Color(230, 230, 230);

    // Dark Theme Colors
    private final Color D_PRIMARY_BLUE = new Color(30, 144, 255);
    private final Color D_BG_MAIN = new Color(18, 18, 18);
    private final Color D_BG_HERO = new Color(30, 30, 30);
    private final Color D_TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color D_TEXT_SECONDARY = new Color(170, 170, 170);
    private final Color D_CARD_BORDER = new Color(60, 60, 60);

    private boolean isDarkMode = false;
    
    // Components to update
    private JPanel navbar, mainContent, hero, featuresSection, footer;
    private JLabel logo, headline, subheadline, copyright;
    private JLabel itineraryTitle, reviewsTitle;
    private List<JButton> navButtons = new ArrayList<>();
    private List<JPanel> featureCards = new ArrayList<>();
    private List<JLabel> cardTitles = new ArrayList<>();
    private List<JTextArea> cardDescs = new ArrayList<>();
    private ThemeSwitch themeSwitch;

    public LandingPage() {
        this(false);
    }

    public LandingPage(boolean startInDarkMode) {
        this.isDarkMode = startInDarkMode;
        setTitle("TripManager - Simplify Your Journey");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Navigation Bar
        navbar = createNavbar();
        add(navbar, BorderLayout.NORTH);

        // Main Content Panel
        mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(L_BG_MAIN);

        // Hero Section
        hero = createHeroSection();
        mainContent.add(hero);
        mainContent.add(Box.createVerticalStrut(40));

        // Features Section
        featuresSection = createFeaturesSection();
        mainContent.add(featuresSection);
        mainContent.add(Box.createVerticalStrut(40));

        // Itineraries Section
        mainContent.add(createItinerariesSection());
        mainContent.add(Box.createVerticalStrut(40));

        // Reviews Section
        mainContent.add(createReviewsSection());
        mainContent.add(Box.createVerticalStrut(40));

        // Footer
        mainContent.add(Box.createVerticalGlue());
        footer = createFooter();
        mainContent.add(footer);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25); // Faster, smoother scroll
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        add(scrollPane, BorderLayout.CENTER);

        applyTheme();
    }

    private JPanel createNavbar() {
        JPanel nb = new JPanel(new BorderLayout());
        nb.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        logo = new JLabel("TripManager");
        logo.setFont(new Font("SansSerif", Font.BOLD, 24));
        nb.add(logo, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        rightPanel.setOpaque(false);

        String[] links = {"Home", "Destinations", "My Trips", "Login"};
        for (String link : links) {
            JButton btn = new JButton(link);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setOpaque(true); // Required for background color visibility

            // Hover Effects
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setContentAreaFilled(true);
                    btn.setBackground(isDarkMode ? D_PRIMARY_BLUE : L_PRIMARY_BLUE);
                    btn.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e) {
                    btn.setContentAreaFilled(false);
                    btn.setBackground(null);
                    btn.setForeground(isDarkMode ? D_TEXT_PRIMARY : L_TEXT_PRIMARY);
                }
            });

            if (link.equals("Login")) {
                btn.addActionListener(e -> {
                    new AuthPage(isDarkMode).setVisible(true);
                    this.dispose();
                });
            } else if (link.equals("Home")) {
                btn.addActionListener(e -> {
                    new LandingPage().setVisible(true);
                    this.dispose();
                });
            } else if (link.equals("Destinations")) {
                btn.addActionListener(e -> {
                    new Dashboard(isDarkMode).setVisible(true);
                    this.dispose();
                });
            }
            navButtons.add(btn);
            rightPanel.add(btn);
        }

        // Custom Theme Switch
        themeSwitch = new ThemeSwitch();
        rightPanel.add(themeSwitch);

        nb.add(rightPanel, BorderLayout.EAST);
        return nb;
    }

    private JPanel createHeroSection() {
        JPanel h = new JPanel(new GridBagLayout()) {
            private Image bgImage;
            {
                try {
                    java.net.URL url = new java.net.URI("https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?q=80&w=2000&auto=format&fit=crop").toURL();
                    bgImage = new ImageIcon(url).getImage();
                } catch (Exception e) {}
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                if (bgImage != null) {
                    double scale = Math.max((double) getWidth() / bgImage.getWidth(null), (double) getHeight() / bgImage.getHeight(null));
                    int w = (int) (bgImage.getWidth(null) * scale);
                    int h = (int) (bgImage.getHeight(null) * scale);
                    g2.drawImage(bgImage, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
                }
                g2.setColor(new Color(0, 0, 0, 130)); // Dark overlay
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        h.setPreferredSize(new Dimension(1000, 500));
        h.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        headline = new JLabel("TripManager - Simplify Your Journey");
        headline.setFont(new Font("SansSerif", Font.BOLD, 58));
        headline.setForeground(Color.WHITE); // Force white for background contrast
        h.add(headline, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 30, 0);
        subheadline = new JLabel("The ultimate tool for effortless trip planning and management.");
        subheadline.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subheadline.setForeground(new Color(220, 220, 220)); // Force light grey for background contrast
        h.add(subheadline, gbc);

        gbc.gridy = 2;
        JButton startBtn = new JButton("Get Started Free");
        startBtn.setBackground(new Color(0, 102, 204));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.setMargin(new Insets(15, 45, 15, 45));
        startBtn.setOpaque(true);
        startBtn.setBorderPainted(false);
        startBtn.addActionListener(e -> new AuthPage(isDarkMode).setVisible(true));
        h.add(startBtn, gbc);

        return h;
    }

    private JPanel createFeaturesSection() {
        JPanel fs = new JPanel(new GridLayout(1, 3, 30, 0));
        fs.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));

        fs.add(createFeatureCard("Plan Anywhere", "Access your itineraries on any device, anywhere."));
        fs.add(createFeatureCard("Manage Expenses", "Keep track of your budget and spending seamlessly."));
        fs.add(createFeatureCard("Collaborate", "Invite friends and family to plan the perfect group trip."));

        return fs;
    }

    private JPanel createFeatureCard(String title, String desc) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(L_CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea descArea = new JTextArea(desc);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setMaximumSize(new Dimension(200, 100));

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(15));
        card.add(descArea);

        featureCards.add(card);
        cardTitles.add(titleLbl);
        cardDescs.add(descArea);

        return card;
    }

    private JPanel createItinerariesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        itineraryTitle = new JLabel("Popular Itineraries");
        itineraryTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        itineraryTitle.setHorizontalAlignment(SwingConstants.CENTER);
        section.add(itineraryTitle, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        cards.add(createItineraryCard("Summer in Greece", "7 Days • Athens, Santorini, Mykonos", "View Details", 
            "https://images.unsplash.com/photo-1533105079780-92b9be482077?q=80&w=500&auto=format&fit=crop"));
        cards.add(createItineraryCard("Japan Discovery", "10 Days • Tokyo, Kyoto, Osaka", "View Details", 
            "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?q=80&w=500&auto=format&fit=crop"));
        cards.add(createItineraryCard("Europe Express", "14 Days • Paris, Rome, Amsterdam", "View Details", 
            "https://images.unsplash.com/photo-1467269204594-9661b134dd2b?q=80&w=500&auto=format&fit=crop"));

        section.add(cards, BorderLayout.CENTER);
        return section;
    }

    private JPanel createItineraryCard(String title, String desc, String btnText, String imageUrl) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(isDarkMode ? new Color(35, 35, 45) : Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 75) : new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(0, 0, 15, 0)
        ));

        // Thumbnail with Image
        JPanel thumb = new JPanel(new BorderLayout()) {
            private Image img;
            {
                try {
                    img = new ImageIcon(new java.net.URI(imageUrl).toURL()).getImage();
                } catch (Exception e) {}
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    double scale = Math.max((double) getWidth() / img.getWidth(null), (double) getHeight() / img.getHeight(null));
                    int w = (int) (img.getWidth(null) * scale);
                    int h = (int) (img.getHeight(null) * scale);
                    g2.drawImage(img, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
                    g2.dispose();
                }
            }
        };
        thumb.setPreferredSize(new Dimension(300, 160));
        thumb.setBackground(isDarkMode ? new Color(50, 50, 65) : new Color(240, 245, 255));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 20));
        t.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
        cardTitles.add(t); // Track for theme updates

        JLabel d = new JLabel(desc);
        d.setFont(new Font("SansSerif", Font.PLAIN, 13));
        d.setForeground(Color.GRAY);
        // Track as secondary text if needed, but Gray works for both

        JButton b = new JButton(btnText);
        stylePrimaryButton(b);
        b.addActionListener(e -> new AuthPage(isDarkMode).setVisible(true));

        info.add(t);
        info.add(Box.createVerticalStrut(8));
        info.add(d);
        info.add(Box.createVerticalStrut(15));
        info.add(b);

        card.add(thumb, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    private JPanel createReviewsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        reviewsTitle = new JLabel("Trusted by Thousands of Travelers");
        reviewsTitle.setFont(new Font("SansSerif", Font.BOLD, 36));
        reviewsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Real stories from real explorers around the world");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(reviewsTitle);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subtitle);
        
        section.add(titlePanel, BorderLayout.NORTH);

        JPanel reviews = new JPanel(new GridLayout(1, 4, 20, 0));
        reviews.setOpaque(false);
        reviews.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        reviews.add(createReviewCard("Tanmay", "⭐⭐⭐⭐⭐", "TripManager transformed how I travel. The drag-and-drop itinerary builder saved me hours of planning!", new Color(0, 150, 136)));
        reviews.add(createReviewCard("Soham", "⭐⭐⭐⭐⭐", "The interface is stunningly clean. I love how I can switch to dark mode at night while planning my Japan trip.", new Color(103, 58, 183)));
        reviews.add(createReviewCard("JiruBoy", "⭐⭐⭐⭐⭐", "Collaborating with my family for our summer vacation was so seamless. Highly recommended for group trips!", new Color(233, 30, 99)));
        reviews.add(createReviewCard("Artist", "⭐⭐⭐⭐⭐", "The aesthetics of this app are inspiring! It's like painting my own journey with a digital brush.", new Color(255, 152, 0)));

        section.add(reviews, BorderLayout.CENTER);
        return section;
    }

    private JPanel createReviewCard(String user, String stars, String text, Color avatarColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("Serif", Font.BOLD, 100));
                g2.setColor(isDarkMode ? new Color(255, 255, 255, 10) : new Color(0, 0, 0, 5));
                g2.drawString("“", getWidth() - 50, 80);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setBackground(isDarkMode ? new Color(30, 30, 35) : Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(50, 50, 60) : new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        header.setOpaque(false);
        
        JLabel avatar = new JLabel(user.substring(0, 1), SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(45, 45));
        avatar.setOpaque(true);
        avatar.setBackground(avatarColor);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel userMeta = new JPanel();
        userMeta.setLayout(new BoxLayout(userMeta, BoxLayout.Y_AXIS));
        userMeta.setOpaque(false);
        
        JLabel u = new JLabel(user);
        u.setFont(new Font("SansSerif", Font.BOLD, 16));
        u.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
        cardTitles.add(u);

        JLabel s = new JLabel(stars);
        s.setForeground(new Color(255, 180, 0));
        
        userMeta.add(u);
        userMeta.add(s);
        
        header.add(avatar);
        header.add(userMeta);

        JTextArea t = new JTextArea(text);
        t.setFont(new Font("SansSerif", Font.ITALIC, 15));
        t.setLineWrap(true);
        t.setWrapStyleWord(true);
        t.setEditable(false);
        t.setOpaque(false);
        t.setForeground(isDarkMode ? new Color(200, 200, 200) : new Color(80, 80, 80));
        t.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        cardDescs.add(t);

        card.add(header, BorderLayout.NORTH);
        card.add(t, BorderLayout.CENTER);

        return card;
    }

    private void stylePrimaryButton(JButton b) {
        b.setBackground(new Color(0, 102, 204));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorderPainted(false);
        b.setOpaque(true);
    }

    private JPanel createFooter() {
        JPanel f = new JPanel();
        f.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        copyright = new JLabel("© 2026 TripManager Inc. All rights reserved.");
        copyright.setFont(new Font("SansSerif", Font.PLAIN, 12));
        f.add(copyright);

        return f;
    }

    private void applyTheme() {
        Color bgMain = isDarkMode ? D_BG_MAIN : L_BG_MAIN;
        Color bgHero = isDarkMode ? D_BG_HERO : L_BG_HERO;
        Color textPrimary = isDarkMode ? D_TEXT_PRIMARY : L_TEXT_PRIMARY;
        Color textSecondary = isDarkMode ? D_TEXT_SECONDARY : L_TEXT_SECONDARY;
        Color primaryBlue = isDarkMode ? D_PRIMARY_BLUE : L_PRIMARY_BLUE;
        Color cardBorder = isDarkMode ? D_CARD_BORDER : L_CARD_BORDER;

        // Navbar
        navbar.setBackground(bgMain);
        logo.setForeground(primaryBlue);
        for (JButton btn : navButtons) {
            btn.setForeground(textPrimary);
        }

        // Main Content
        mainContent.setBackground(bgMain);
        
        // Hero
        hero.setBackground(bgHero);
        // Hero
        headline.setForeground(Color.WHITE);
        subheadline.setForeground(new Color(220, 220, 220));

        // Section Titles
        if (itineraryTitle != null) itineraryTitle.setForeground(textPrimary);
        if (reviewsTitle != null) reviewsTitle.setForeground(textPrimary);

        // Features
        featuresSection.setBackground(bgMain);
        for (int i = 0; i < featureCards.size(); i++) {
            JPanel card = featureCards.get(i);
            card.setBackground(isDarkMode ? D_BG_HERO : L_BG_MAIN);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
            ));
            cardTitles.get(i).setForeground(textPrimary);
            cardDescs.get(i).setForeground(textSecondary);
        }

        // Footer
        footer.setBackground(isDarkMode ? new Color(10, 10, 10) : new Color(51, 51, 51));
        copyright.setForeground(Color.WHITE);

        repaint();
    }

    // Custom Switch Component
    private class ThemeSwitch extends JPanel {
        private final int width = 50;
        private final int height = 26;

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

            // Draw Track
            Color trackColor = isDarkMode ? D_PRIMARY_BLUE : new Color(200, 200, 200);
            g2.setColor(trackColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, width, height, height, height));

            // Draw Thumb (Switch Circle)
            g2.setColor(Color.WHITE);
            int padding = 3;
            int thumbSize = height - (padding * 2);
            int x = isDarkMode ? (width - thumbSize - padding) : padding;
            g2.fillOval(x, padding, thumbSize, thumbSize);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LandingPage().setVisible(true);
        });
    }
}
