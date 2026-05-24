import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * GradeTrackerApp.java
 * Main application window — Student Grade Tracker
 * CodeAlpha Internship — Task 1
 *
 * Features:
 *  - Add / Edit / Delete students
 *  - View all grades in a sortable JTable
 *  - Dashboard stats: total students, class average, highest avg, lowest avg
 *  - Analytics tab: per-subject averages, pass/fail breakdown, class summary report
 *  - Export to CSV
 */
public class GradeTrackerApp extends JFrame {

    // ─── Theme ───────────────────────────────────────────────────────────────────
    static final Color BG        = new Color(3,  3,  10);
    static final Color BG2       = new Color(7,  7,  16);
    static final Color SURFACE   = new Color(15, 15, 28);
    static final Color SURFACE2  = new Color(22, 22, 37);
    static final Color SURFACE3  = new Color(28, 28, 46);
    static final Color BORDER    = new Color(40, 40, 65);
    static final Color BORDER2   = new Color(55, 55, 85);
    static final Color TEXT      = new Color(238, 238, 248);
    static final Color TEXT2     = new Color(160, 160, 192);
    static final Color TEXT3     = new Color(85,  85, 112);
    static final Color ACCENT    = new Color(99,  102, 241);
    static final Color ACCENT2   = new Color(129, 140, 248);
    static final Color GREEN     = new Color(16,  217, 160);
    static final Color RED       = new Color(244, 63,  94);
    static final Color GOLD      = new Color(240, 180, 41);
    static final Color BLUE      = new Color(56,  189, 248);
    static final Color VIOLET    = new Color(192, 132, 252);
    static final Color ORANGE    = new Color(251, 146, 60);

    // ─── Data ────────────────────────────────────────────────────────────────────
    private ArrayList<Student> students = new ArrayList<>();
    private StudentTableModel  tableModel;
    private JTable             table;

    // ─── Stat Labels ─────────────────────────────────────────────────────────────
    private JLabel lblTotal, lblAvg, lblHighest, lblLowest, lblPass, lblFail;

    // ─── Analytics Labels ────────────────────────────────────────────────────────
    private JLabel lblAvgMath, lblAvgSci, lblAvgEng, lblAvgHis, lblAvgCS;
    private JTextArea taReport;
    private JTable    analyticsTable;
    private DefaultTableModel analyticsModel;

    // ─── Constructor ─────────────────────────────────────────────────────────────
    public GradeTrackerApp() {
        super("StudentGradeTracker — CodeAlpha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 680));
        setPreferredSize(new Dimension(1200, 720));
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);

        loadSampleData();
        refreshAll();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HEADER
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(BG2);
        hdr.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            new EmptyBorder(14, 28, 14, 28)
        ));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        JLabel icon = new JLabel("⬡");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 22));
        icon.setForeground(ACCENT);

        JLabel name = new JLabel("StudentGradeTracker");
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(TEXT);

        JLabel sep = new JLabel("  |  Student Performance Dashboard");
        sep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sep.setForeground(TEXT3);

        brand.add(icon); brand.add(name); brand.add(sep);
        hdr.add(brand, BorderLayout.WEST);

        // Right actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton btnExport = makeBtn("⬇  Export CSV", SURFACE2, TEXT2);
        btnExport.addActionListener(e -> exportCSV());

        JButton btnAdd = makeBtn("＋  Add Student", ACCENT, Color.WHITE);
        btnAdd.addActionListener(e -> addStudent());

        actions.add(btnExport); actions.add(btnAdd);
        hdr.add(actions, BorderLayout.EAST);
        return hdr;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TABS
    // ═══════════════════════════════════════════════════════════════════════════
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG);
        tabs.setForeground(TEXT2);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBorder(new EmptyBorder(0, 0, 0, 0));

        tabs.addTab("  Dashboard  ", buildDashboardTab());
        tabs.addTab("  Students   ", buildStudentsTab());
        tabs.addTab("  Analytics  ", buildAnalyticsTab());

        // Refresh analytics when tab is switched
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 2) refreshAnalytics();
        });
        return tabs;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DASHBOARD TAB
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildDashboardTab() {
        JPanel p = new JPanel(new BorderLayout(0, 24));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(32, 32, 32, 32));

        // Eyebrow
        p.add(makeSectionHead("Overview", "Student Performance Dashboard", "Real-time summary of class performance metrics"), BorderLayout.NORTH);

        // Stats grid
        JPanel grid = new JPanel(new GridLayout(1, 6, 14, 0));
        grid.setOpaque(false);

        lblTotal   = new JLabel("0");
        lblAvg     = new JLabel("0.0");
        lblHighest = new JLabel("0.0");
        lblLowest  = new JLabel("0.0");
        lblPass    = new JLabel("0");
        lblFail    = new JLabel("0");

        grid.add(makeStatCard("Total Students",  lblTotal,   "👥", ACCENT2));
        grid.add(makeStatCard("Class Average",   lblAvg,     "📊", GOLD));
        grid.add(makeStatCard("Highest Average", lblHighest, "🏆", GREEN));
        grid.add(makeStatCard("Lowest Average",  lblLowest,  "📉", RED));
        grid.add(makeStatCard("Passing",         lblPass,    "✅", GREEN));
        grid.add(makeStatCard("Failing",         lblFail,    "❌", RED));

        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // STUDENTS TAB
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildStudentsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(28, 32, 32, 32));

        // Header
        p.add(makeSectionHead("Records", "Student Grade Records", "Add, edit, search and delete student entries"), BorderLayout.NORTH);

        // Table
        tableModel = new StudentTableModel(students);
        table = new JTable(tableModel);
        styleTable(table);

        // Column widths
        int[] widths = {40, 160, 65, 75, 75, 75, 80, 75, 60, 65};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Custom renderer for Grade and Status columns
        table.getColumnModel().getColumn(8).setCellRenderer(new GradeCellRenderer());
        table.getColumnModel().getColumn(9).setCellRenderer(new StatusCellRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(SURFACE);
        scroll.getViewport().setBackground(SURFACE);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        p.add(scroll, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        toolbar.setBorder(new EmptyBorder(0, 0, 8, 0));

        JButton btnEdit = makeBtn("✏  Edit", SURFACE2, TEXT2);
        btnEdit.addActionListener(e -> editStudent());

        JButton btnDelete = makeBtn("🗑  Delete", new Color(60, 20, 30), RED);
        btnDelete.addActionListener(e -> deleteStudent());

        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        p.add(toolbar, BorderLayout.SOUTH);

        return p;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ANALYTICS TAB
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildAnalyticsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(28, 32, 32, 32));

        p.add(makeSectionHead("Analytics", "Performance Analytics", "Subject averages, grade distribution and class report"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.weightx = 1; g.insets = new Insets(0, 0, 20, 0);

        // ── Subject Averages ──
        JPanel subGrid = new JPanel(new GridLayout(1, 5, 12, 0));
        subGrid.setOpaque(false);

        lblAvgMath = new JLabel("0.0"); lblAvgSci = new JLabel("0.0");
        lblAvgEng  = new JLabel("0.0"); lblAvgHis = new JLabel("0.0");
        lblAvgCS   = new JLabel("0.0");

        subGrid.add(makeStatCard("Math Avg",      lblAvgMath, "M", ACCENT2));
        subGrid.add(makeStatCard("Science Avg",   lblAvgSci,  "S", GOLD));
        subGrid.add(makeStatCard("English Avg",   lblAvgEng,  "E", GREEN));
        subGrid.add(makeStatCard("History Avg",   lblAvgHis,  "H", ORANGE));
        subGrid.add(makeStatCard("Comp Sci Avg",  lblAvgCS,   "C", BLUE));

        g.gridx = 0; g.gridy = 0; g.weighty = 0;
        center.add(subGrid, g);

        // ── Class Breakdown Table ──
        analyticsModel = new DefaultTableModel(
            new String[]{"Student", "Total / 500", "Grade", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        analyticsTable = new JTable(analyticsModel);
        styleTable(analyticsTable);
        analyticsTable.getColumnModel().getColumn(2).setCellRenderer(new GradeCellRenderer());
        analyticsTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        JScrollPane scroll = new JScrollPane(analyticsTable);
        scroll.getViewport().setBackground(SURFACE);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.setPreferredSize(new Dimension(0, 200));

        g.gridy = 1; g.weighty = 1;
        center.add(scroll, g);

        // ── Summary Report Text Area ──
        taReport = new JTextArea(5, 0);
        taReport.setEditable(false);
        taReport.setBackground(SURFACE);
        taReport.setForeground(TEXT2);
        taReport.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        taReport.setBorder(new EmptyBorder(14, 16, 14, 16));
        taReport.setLineWrap(true);
        taReport.setWrapStyleWord(true);

        JScrollPane reportScroll = new JScrollPane(taReport);
        reportScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        reportScroll.getViewport().setBackground(SURFACE);

        g.gridy = 2; g.weighty = 0.4;
        center.add(reportScroll, g);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DATA ACTIONS
    // ═══════════════════════════════════════════════════════════════════════════
    private void addStudent() {
        StudentDialog dlg = new StudentDialog(this, null);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            students.add(dlg.getStudent());
            refreshAll();
            toast("Student added successfully!", GREEN);
        }
    }

    private void editStudent() {
        int row = table.getSelectedRow();
        if (row < 0) { toast("Please select a student to edit.", GOLD); return; }
        Student s = students.get(row);
        StudentDialog dlg = new StudentDialog(this, s);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            refreshAll();
            toast("Student record updated.", GREEN);
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) { toast("Please select a student to delete.", GOLD); return; }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete \"" + students.get(row).getName() + "\" from records?\nThis action cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(row);
            refreshAll();
            toast("Student record deleted.", RED);
        }
    }

    private void exportCSV() {
        if (students.isEmpty()) { toast("No records to export.", GOLD); return; }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("GradeTracker_Records.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(chooser.getSelectedFile()))) {
            pw.println("Name,Math,Science,English,History,CompSci,Average,Grade,Status");
            for (Student s : students) {
                pw.printf("\"%s\",%d,%d,%d,%d,%d,%.1f,%s,%s%n",
                    s.getName(), s.getMath(), s.getScience(), s.getEnglish(),
                    s.getHistory(), s.getComputerScience(), s.getAverage(),
                    s.getGrade(), s.getStatus());
            }
            toast("Exported to " + chooser.getSelectedFile().getName(), GREEN);
        } catch (IOException ex) {
            toast("Export failed: " + ex.getMessage(), RED);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // REFRESH LOGIC
    // ═══════════════════════════════════════════════════════════════════════════
    private void refreshAll() {
        tableModel.refresh();
        refreshDashboard();
    }

    private void refreshDashboard() {
        int total = students.size();
        lblTotal.setText(String.valueOf(total));

        if (total == 0) {
            lblAvg.setText("—"); lblHighest.setText("—");
            lblLowest.setText("—"); lblPass.setText("0"); lblFail.setText("0");
            return;
        }

        double sum = 0, high = 0, low = 100;
        int pass = 0, fail = 0;
        for (Student s : students) {
            double a = s.getAverage();
            sum += a;
            if (a > high) high = a;
            if (a < low)  low  = a;
            if (s.getStatus().equals("Pass")) pass++; else fail++;
        }

        lblAvg.setText(String.format("%.1f", sum / total));
        lblHighest.setText(String.format("%.1f", high));
        lblLowest.setText(String.format("%.1f", low));
        lblPass.setText(String.valueOf(pass));
        lblFail.setText(String.valueOf(fail));
    }

    private void refreshAnalytics() {
        // Subject averages
        if (students.isEmpty()) {
            for (JLabel l : new JLabel[]{lblAvgMath, lblAvgSci, lblAvgEng, lblAvgHis, lblAvgCS})
                l.setText("—");
            analyticsModel.setRowCount(0);
            taReport.setText("Add students to generate the performance report.");
            return;
        }

        int n = students.size();
        double sumM=0, sumS=0, sumE=0, sumH=0, sumC=0;
        for (Student s : students) {
            sumM += s.getMath(); sumS += s.getScience(); sumE += s.getEnglish();
            sumH += s.getHistory(); sumC += s.getComputerScience();
        }
        lblAvgMath.setText(String.format("%.1f", sumM/n));
        lblAvgSci .setText(String.format("%.1f", sumS/n));
        lblAvgEng .setText(String.format("%.1f", sumE/n));
        lblAvgHis .setText(String.format("%.1f", sumH/n));
        lblAvgCS  .setText(String.format("%.1f", sumC/n));

        // Analytics table
        analyticsModel.setRowCount(0);
        for (Student s : students)
            analyticsModel.addRow(new Object[]{s.getName(), s.getTotal() + " / 500", s.getGrade(), s.getStatus()});

        // Summary report text
        double classAvg = students.stream().mapToDouble(Student::getAverage).average().orElse(0);
        long passCount  = students.stream().filter(s -> s.getStatus().equals("Pass")).count();
        String best  = students.stream().max((a,b) -> Double.compare(a.getAverage(), b.getAverage())).map(Student::getName).orElse("—");
        String worst = students.stream().min((a,b) -> Double.compare(a.getAverage(), b.getAverage())).map(Student::getName).orElse("—");

        // Highest/lowest subject
        double[] avgs = {sumM/n, sumS/n, sumE/n, sumH/n, sumC/n};
        String[] subs = {"Math", "Science", "English", "History", "Computer Science"};
        int hiIdx = 0, loIdx = 0;
        for (int i = 1; i < 5; i++) {
            if (avgs[i] > avgs[hiIdx]) hiIdx = i;
            if (avgs[i] < avgs[loIdx]) loIdx = i;
        }

        String report = String.format(
            "══════════════════════════════════════════════════════════\n" +
            "  CLASS SUMMARY REPORT\n" +
            "══════════════════════════════════════════════════════════\n" +
            "  Total Students     : %d\n" +
            "  Class Average      : %.1f / 100\n" +
            "  Highest Average    : %s\n" +
            "  Lowest Average     : %s\n" +
            "  Passing Students   : %d  |  Failing: %d\n" +
            "  Best Subject       : %s (avg %.1f)\n" +
            "  Needs Improvement  : %s (avg %.1f)\n" +
            "══════════════════════════════════════════════════════════",
            n, classAvg, best, worst, passCount, n - passCount,
            subs[hiIdx], avgs[hiIdx], subs[loIdx], avgs[loIdx]
        );
        taReport.setText(report);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SAMPLE DATA
    // ═══════════════════════════════════════════════════════════════════════════
    private void loadSampleData() {
        students.add(new Student("Ali Hassan",      92, 88, 95, 78, 90));
        students.add(new Student("Fatima Khan",     75, 82, 68, 90, 77));
        students.add(new Student("Umar Farooq",     55, 62, 70, 58, 65));
        students.add(new Student("Zainab Malik",    88, 91, 85, 92, 94));
        students.add(new Student("Bilal Ahmed",     40, 35, 50, 42, 38));
        students.add(new Student("Sara Qureshi",    78, 74, 82, 80, 76));
        students.add(new Student("Hamza Raza",      65, 70, 60, 55, 68));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // UI HELPERS
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel makeSectionHead(String eyebrow, String title, String sub) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel eye = new JLabel("▪  " + eyebrow.toUpperCase());
        eye.setFont(new Font("Segoe UI", Font.BOLD, 10));
        eye.setForeground(ACCENT2);

        JLabel ttl = new JLabel(title);
        ttl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        ttl.setForeground(TEXT);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        s.setForeground(TEXT2);

        p.add(eye); p.add(Box.createVerticalStrut(6));
        p.add(ttl); p.add(Box.createVerticalStrut(4)); p.add(s);
        return p;
    }

    private JPanel makeStatCard(String label, JLabel valueLabel, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        ico.setForeground(color);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(TEXT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT3);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(ico, BorderLayout.WEST);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);
        bottom.add(valueLabel);
        bottom.add(Box.createVerticalStrut(2));
        bottom.add(lbl);

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.CENTER);
        return card;
    }

    private void styleTable(JTable t) {
        t.setBackground(SURFACE);
        t.setForeground(TEXT);
        t.setSelectionBackground(new Color(99, 102, 241, 60));
        t.setSelectionForeground(TEXT);
        t.setGridColor(BORDER);
        t.setRowHeight(38);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(SURFACE2);
        t.getTableHeader().setForeground(TEXT2);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        t.setFillsViewportHeight(true);
    }

    private JButton makeBtn(String label, Color bg, Color fg) {
        JButton btn = new JButton(label);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private void toast(String msg, Color color) {
        JOptionPane pane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        JDialog dlg = pane.createDialog(this, "Notice");
        dlg.setBackground(SURFACE);
        Timer t = new Timer(2200, e -> dlg.dispose());
        t.setRepeats(false); t.start();
        dlg.setVisible(true);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CUSTOM CELL RENDERERS
    // ═══════════════════════════════════════════════════════════════════════════

    static class GradeCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            lbl.setHorizontalAlignment(CENTER);
            lbl.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
            lbl.setBackground(sel ? new Color(99,102,241,60) : SURFACE);
            String g = v != null ? v.toString() : "";
            lbl.setForeground(switch (g) {
                case "A"  -> GREEN;
                case "B"  -> BLUE;
                case "C"  -> GOLD;
                case "D"  -> ORANGE;
                default   -> RED;
            });
            return lbl;
        }
    }

    static class StatusCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            lbl.setHorizontalAlignment(CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setBackground(sel ? new Color(99,102,241,60) : SURFACE);
            boolean pass = "Pass".equals(v != null ? v.toString() : "");
            lbl.setForeground(pass ? GREEN : RED);
            return lbl;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ENTRY POINT
    // ═══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        // Use system look and feel as base, then override
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}

        UIManager.put("TabbedPane.background",        BG2);
        UIManager.put("TabbedPane.foreground",        TEXT2);
        UIManager.put("TabbedPane.selected",          SURFACE);
        UIManager.put("TabbedPane.contentAreaColor",  BG);
        UIManager.put("TabbedPane.tabAreaBackground", BG2);
        UIManager.put("Panel.background",             BG);
        UIManager.put("OptionPane.background",        SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT);

        SwingUtilities.invokeLater(GradeTrackerApp::new);
    }
}
