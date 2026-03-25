package FrontEnd.Tanmay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Dashboard extends JFrame {
    private final Color PRIMARY_BLUE = new Color(0, 102, 204);
    private final Color D_BG = new Color(18, 18, 24);
    private final Color D_SIDEBAR = new Color(28, 28, 36);
    private final Color D_CARD = new Color(40, 40, 50);
    private final Color TEXT_GRAY = new Color(150, 150, 150);

    private JPanel itineraryContainer;
    private JPanel destinationListPanel;
    private JTextField searchField;
    private JLabel totalExpenseLabel;
    private boolean isDarkMode;
    private List<ItineraryCard> itineraryCards = new ArrayList<>();

    private final List<String> ALL_DESTINATIONS = Arrays.asList(
        "Paris, France", "Tokyo, Japan", "New York, USA", "London, UK", "Rome, Italy",
        "Bali, Indonesia", "Sydney, Australia", "Dubai, UAE", "Barcelona, Spain", "Swiss Alps",
        "Santorini, Greece", "Kyoto, Japan", "Singapore", "Amsterdam, Netherlands", "Prague, Czech Republic",
        "Cape Town, South Africa", "Rio de Janeiro, Brazil", "Istanbul, Turkey", "Seoul, South Korea",
        "Venice, Italy", "Machu Picchu, Peru", "Marrakech, Morocco", "Cairo, Egypt", "Mumbai, India"
    );

    public Dashboard(boolean darkMode) {
        this.isDarkMode = darkMode;
        setTitle("TripManager Dashboard - Advanced Expense Tracking");
        setSize(1250, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(isDarkMode ? D_BG : Color.WHITE);

        mainContainer.add(createNavbar(), BorderLayout.NORTH);
        mainContainer.add(createSidebar(), BorderLayout.WEST);
        mainContainer.add(createTimelineArea(), BorderLayout.CENTER);

        add(mainContainer);
        applyTheme();
    }

    private JPanel navbar, sidebar, timelineArea;
    private JLabel logo;
    private List<JButton> navButtons = new ArrayList<>();

    private JPanel createNavbar() {
        navbar = new JPanel(new BorderLayout());
        navbar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        logo = new JLabel("TripManager");
        logo.setFont(new Font("SansSerif", Font.BOLD, 26));
        logo.setForeground(PRIMARY_BLUE);
        navbar.add(logo, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        rightPanel.setOpaque(false);

        String[] links = {"Home", "Destinations", "My Trips", "Logout"};
        for (String link : links) {
            JButton btn = new JButton(link);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setOpaque(true);

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setContentAreaFilled(true);
                    btn.setBackground(PRIMARY_BLUE);
                    btn.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e) {
                    btn.setContentAreaFilled(false);
                    btn.setBackground(null);
                    btn.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
                }
            });

            if (link.equals("Logout") || link.equals("Home")) {
                btn.addActionListener(e -> {
                    new LandingPage(isDarkMode).setVisible(true);
                    this.dispose();
                });
            }
            navButtons.add(btn);
            rightPanel.add(btn);
        }
        rightPanel.add(new ThemeSwitch());
        navbar.add(rightPanel, BorderLayout.EAST);
        return navbar;
    }

    private JPanel createSidebar() {
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(320, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel title = new JLabel("Explore World");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        searchField = new JTextField();
        styleSearchField(searchField);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateDestinationList(searchField.getText());
            }
        });

        header.add(title);
        header.add(Box.createVerticalStrut(20));
        header.add(searchField);
        sidebar.add(header, BorderLayout.NORTH);

        destinationListPanel = new JPanel();
        destinationListPanel.setLayout(new BoxLayout(destinationListPanel, BoxLayout.Y_AXIS));
        destinationListPanel.setOpaque(false);
        updateDestinationList(""); 

        JScrollPane scrollPane = new JScrollPane(destinationListPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        sidebar.add(scrollPane, BorderLayout.CENTER);
        
        return sidebar;
    }

    private void updateDestinationList(String query) {
        destinationListPanel.removeAll();
        List<String> filtered = ALL_DESTINATIONS.stream()
            .filter(d -> d.toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());

        for (String dest : filtered) {
            destinationListPanel.add(createSourceCard(dest));
            destinationListPanel.add(Box.createVerticalStrut(12));
        }
        destinationListPanel.revalidate();
        destinationListPanel.repaint();
    }

    private JPanel createSourceCard(String name) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(280, 50));
        card.setBackground(isDarkMode ? D_CARD : Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(60, 60, 70) : new Color(235, 235, 235), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel label = new JLabel(name);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
        card.add(label, BorderLayout.WEST);

        JLabel dragIcon = new JLabel("⋮⋮");
        dragIcon.setForeground(TEXT_GRAY);
        card.add(dragIcon, BorderLayout.EAST);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(card, DnDConstants.ACTION_COPY, dge -> {
            dge.startDrag(DragSource.DefaultCopyDrop, new StringSelection(name));
        });

        return card;
    }

    private JPanel createTimelineArea() {
        timelineArea = new JPanel(new BorderLayout()) {
            private Image bgImage;
            {
                try {
                    java.net.URL url = new java.net.URI("https://images.unsplash.com/photo-1526772662000-3f88f10405ff?q=80&w=2000&auto=format&fit=crop").toURL();
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
                g2.setColor(isDarkMode ? new Color(18, 18, 24, 225) : new Color(255, 255, 255, 210));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        timelineArea.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel title = new JLabel("Journey Timeline");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
        
        totalExpenseLabel = new JLabel("Total Trip cost: $0");
        totalExpenseLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalExpenseLabel.setForeground(PRIMARY_BLUE);
        
        header.add(title, BorderLayout.WEST);
        header.add(totalExpenseLabel, BorderLayout.EAST);
        timelineArea.add(header, BorderLayout.NORTH);

        itineraryContainer = new JPanel();
        itineraryContainer.setLayout(new BoxLayout(itineraryContainer, BoxLayout.Y_AXIS));
        itineraryContainer.setOpaque(false);
        
        JScrollPane scroll = new JScrollPane(itineraryContainer);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        
        timelineArea.add(scroll, BorderLayout.CENTER);

        new DropTarget(scroll, new DropTargetAdapter() {
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable t = dtde.getTransferable();
                    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                        addItineraryItem(s);
                        dtde.dropComplete(true);
                    } else { dtde.rejectDrop(); }
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        return timelineArea;
    }

    private void addItineraryItem(String destination) {
        ItineraryCard card = new ItineraryCard(destination);
        itineraryCards.add(card);
        itineraryContainer.add(card);
        itineraryContainer.add(Box.createVerticalStrut(20));
        
        itineraryContainer.revalidate();
        itineraryContainer.repaint();
        calculateOverallTotal();
    }

    private void calculateOverallTotal() {
        double total = 0;
        for (ItineraryCard card : itineraryCards) {
            total += card.getDestinationTotal();
        }
        totalExpenseLabel.setText("Total Trip cost: $" + (int)total);
    }

    // Inner Class for Destination Card with detailed expenses
    private class ItineraryCard extends JPanel {
        private String destination;
        private List<ExpenseItem> items = new ArrayList<>();
        private JPanel itemsPanel;
        private JLabel subtotalLabel;
        private JLabel dayLabel;

        public ItineraryCard(String dest) {
            this.destination = dest;
            setLayout(new BorderLayout());
            setOpaque(false);
            setMaximumSize(new Dimension(900, 350));
            setBorder(new EmptyBorder(10, 0, 10, 0));

            JPanel mainCard = new JPanel(new BorderLayout());
            mainCard.setBackground(isDarkMode ? D_CARD : Color.WHITE);
            mainCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 10, 0, 0, PRIMARY_BLUE),
                new EmptyBorder(20, 25, 20, 25)
            ));

            // Header: Day + Dest + Delete
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            
            dayLabel = new JLabel("DAY " + (itineraryCards.size() + 1));
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dayLabel.setForeground(PRIMARY_BLUE);
            
            JLabel name = new JLabel("📍 " + destination);
            name.setFont(new Font("SansSerif", Font.BOLD, 22));
            name.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);

            JPanel titleGroup = new JPanel();
            titlePanelGroup(titleGroup, dayLabel, name);
            header.add(titleGroup, BorderLayout.WEST);

            JButton del = new JButton("✕");
            del.setForeground(new Color(200, 50, 50));
            styleGhostButton(del);
            del.addActionListener(e -> removeThis());
            header.add(del, BorderLayout.EAST);

            // Expense List Panel
            itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setOpaque(false);
            itemsPanel.setBorder(new EmptyBorder(15, 10, 15, 0));

            // Add Expense Input Row
            JPanel inputRow = createInputRow();

            // Subtotal Area
            subtotalLabel = new JLabel("Destination Total: $0");
            subtotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            subtotalLabel.setForeground(isDarkMode ? new Color(150, 200, 255) : PRIMARY_BLUE);
            subtotalLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

            mainCard.add(header, BorderLayout.NORTH);
            mainCard.add(itemsPanel, BorderLayout.CENTER);
            
            JPanel footer = new JPanel(new BorderLayout());
            footer.setOpaque(false);
            footer.add(inputRow, BorderLayout.NORTH);
            footer.add(subtotalLabel, BorderLayout.SOUTH);
            mainCard.add(footer, BorderLayout.SOUTH);

            add(mainCard, BorderLayout.CENTER);
        }

        private void titlePanelGroup(JPanel p, JLabel d, JLabel n) {
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setOpaque(false);
            p.add(d);
            p.add(n);
        }

        private JPanel createInputRow() {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            row.setOpaque(false);
            row.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200, 50)));

            JTextField descF = new JTextField("Item (e.g. Hotel)");
            descF.setPreferredSize(new Dimension(150, 30));
            JTextField amtF = new JTextField("0");
            amtF.setPreferredSize(new Dimension(70, 30));
            
            JButton addBtn = new JButton("+ Add");
            stylePrimaryButton(addBtn);
            addBtn.addActionListener(e -> {
                try {
                    double amt = Double.parseDouble(amtF.getText());
                    addExpense(descF.getText(), amt);
                    amtF.setText("0");
                    descF.setText("");
                } catch (Exception ex) {}
            });

            row.add(new JLabel("Quick Add:"));
            row.add(descF);
            row.add(new JLabel("$"));
            row.add(amtF);
            row.add(addBtn);
            return row;
        }

        private void addExpense(String desc, double amt) {
            ExpenseItem item = new ExpenseItem(desc, amt);
            items.add(item);
            
            JPanel itemRow = new JPanel(new BorderLayout());
            itemRow.setOpaque(false);
            itemRow.setMaximumSize(new Dimension(800, 30));
            
            JLabel l = new JLabel("• " + desc + ": $" + (int)amt);
            l.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
            l.setFont(new Font("SansSerif", Font.PLAIN, 15));
            
            itemRow.add(l, BorderLayout.WEST);
            itemsPanel.add(itemRow);
            
            updateSubtotal();
            itemsPanel.revalidate();
            itemsPanel.repaint();
        }

        private void updateSubtotal() {
            double sum = getDestinationTotal();
            subtotalLabel.setText("Destination Total: $" + (int)sum);
            calculateOverallTotal();
        }

        public double getDestinationTotal() {
            return items.stream().mapToDouble(i -> i.amount).sum();
        }

        private void removeThis() {
            itineraryCards.remove(this);
            itineraryContainer.remove(this.getParent()); // Remove the vertical strut too if possible, simpler to just remove this
            itineraryContainer.remove(this);
            reorderDays();
            calculateOverallTotal();
            itineraryContainer.revalidate();
            itineraryContainer.repaint();
        }

        public void updateDay(int d) {
            dayLabel.setText("DAY " + d);
        }
    }

    private static class ExpenseItem {
        String desc; double amount;
        ExpenseItem(String d, double a) { this.desc = d; this.amount = a; }
    }

    private void reorderDays() {
        for (int i = 0; i < itineraryCards.size(); i++) {
            itineraryCards.get(i).updateDay(i + 1);
        }
    }

    private void styleSearchField(JTextField f) {
        f.setPreferredSize(new Dimension(280, 45));
        f.setMaximumSize(new Dimension(280, 45));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        f.setText("Search Global Cities...");
    }

    private void stylePrimaryButton(JButton b) {
        b.setBackground(PRIMARY_BLUE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleGhostButton(JButton b) {
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void applyTheme() {
        Color bgMain = isDarkMode ? D_BG : Color.WHITE;
        Color bgSidebar = isDarkMode ? D_SIDEBAR : new Color(250, 250, 252);
        Color textPrimary = isDarkMode ? Color.WHITE : Color.BLACK;
        Color border = isDarkMode ? new Color(50, 50, 60) : new Color(230, 230, 230);

        navbar.setBackground(bgMain);
        for (JButton btn : navButtons) btn.setForeground(textPrimary);
        sidebar.setBackground(bgSidebar);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, border));
        searchField.setBackground(isDarkMode ? new Color(45, 45, 55) : Color.WHITE);
        searchField.setForeground(textPrimary);
        timelineArea.repaint();
        repaint();
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

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new Dashboard(true).setVisible(true));
    }
}
