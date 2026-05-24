import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * StudentDialog.java
 * Modal dialog for adding or editing a student record.
 * CodeAlpha — Task 1: Student Grade Tracker
 */
public class StudentDialog extends JDialog {

    // ─── Theme Colors ────────────────────────────────────────────────────────────
    private static final Color BG       = new Color(13, 13, 24);
    private static final Color SURFACE  = new Color(22, 22, 37);
    private static final Color BORDER   = new Color(50, 50, 80);
    private static final Color ACCENT   = new Color(99, 102, 241);
    private static final Color TEXT     = new Color(238, 238, 248);
    private static final Color TEXT2    = new Color(160, 160, 192);
    private static final Color RED      = new Color(244, 63, 94);
    private static final Color GREEN    = new Color(16, 217, 160);

    // ─── Fields ──────────────────────────────────────────────────────────────────
    private JTextField tfName, tfMath, tfScience, tfEnglish, tfHistory, tfCS;
    private boolean confirmed = false;
    private Student editTarget;

    public StudentDialog(Frame owner, Student student) {
        super(owner, student == null ? "Add New Student" : "Edit Student Record", true);
        this.editTarget = student;
        buildUI();
        if (student != null) populate(student);
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void buildUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(24, 28, 16, 28));

        JLabel title = new JLabel(editTarget == null ? "Add New Student" : "Modify Student Record");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ── Form Body ──
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(0, 28, 8, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 12);

        // Student Name (full width)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        body.add(makeLabel("STUDENT NAME"), gbc);
        gbc.gridy = 1;
        tfName = makeField("e.g. Ali Hassan");
        body.add(tfName, gbc);

        // Score fields row
        gbc.gridwidth = 1;
        String[] subjects = {"MATH", "SCIENCE", "ENGLISH", "HISTORY"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < subjects.length; i++) {
            gbc.gridx = i; gbc.gridy = 2;
            body.add(makeLabel(subjects[i]), gbc);
        }
        for (int i = 0; i < subjects.length; i++) {
            gbc.gridx = i; gbc.gridy = 3;
            fields[i] = makeField("0–100");
            body.add(fields[i], gbc);
        }
        tfMath    = fields[0];
        tfScience = fields[1];
        tfEnglish = fields[2];
        tfHistory = fields[3];

        // CS field (half width)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        body.add(makeLabel("COMPUTER SCIENCE"), gbc);
        gbc.gridy = 5;
        tfCS = makeField("0–100");
        body.add(tfCS, gbc);

        add(body, BorderLayout.CENTER);

        // ── Footer Buttons ──
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(12, 28, 24, 28));

        JButton btnCancel = makeBtn("Cancel", SURFACE, TEXT2);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = makeBtn(editTarget == null ? "Add Student" : "Save Changes", ACCENT, Color.WHITE);
        btnSave.addActionListener(e -> onSave());

        footer.add(btnCancel);
        footer.add(btnSave);
        add(footer, BorderLayout.SOUTH);

        // Enter key submits
        getRootPane().setDefaultButton(btnSave);
    }

    private void onSave() {
        String name = tfName.getText().trim();
        if (name.isEmpty()) { showError("Student name cannot be empty."); return; }

        int[] scores = new int[5];
        JTextField[] scoreFields = {tfMath, tfScience, tfEnglish, tfHistory, tfCS};
        String[] labels = {"Math", "Science", "English", "History", "Computer Science"};

        for (int i = 0; i < scoreFields.length; i++) {
            try {
                int v = Integer.parseInt(scoreFields[i].getText().trim());
                if (v < 0 || v > 100) throw new NumberFormatException();
                scores[i] = v;
            } catch (NumberFormatException ex) {
                showError(labels[i] + " must be a number between 0 and 100.");
                scoreFields[i].requestFocus();
                return;
            }
        }

        if (editTarget != null) {
            editTarget.setName(name);
            editTarget.setMath(scores[0]);
            editTarget.setScience(scores[1]);
            editTarget.setEnglish(scores[2]);
            editTarget.setHistory(scores[3]);
            editTarget.setComputerScience(scores[4]);
        } else {
            editTarget = new Student(name, scores[0], scores[1], scores[2], scores[3], scores[4]);
        }
        confirmed = true;
        dispose();
    }

    private void populate(Student s) {
        tfName.setText(s.getName());
        tfMath.setText(String.valueOf(s.getMath()));
        tfScience.setText(String.valueOf(s.getScience()));
        tfEnglish.setText(String.valueOf(s.getEnglish()));
        tfHistory.setText(String.valueOf(s.getHistory()));
        tfCS.setText(String.valueOf(s.getComputerScience()));
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT2);
        return lbl;
    }

    private JTextField makeField(String placeholder) {
        JTextField tf = new JTextField(12);
        tf.setBackground(SURFACE);
        tf.setForeground(TEXT);
        tf.setCaretColor(TEXT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        tf.setToolTipText(placeholder);
        return tf;
    }

    private JButton makeBtn(String label, Color bg, Color fg) {
        JButton btn = new JButton(label);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // ─── Result Getters ──────────────────────────────────────────────────────────
    public boolean isConfirmed() { return confirmed; }
    public Student getStudent()  { return editTarget; }
}
