import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * StudentTableModel.java
 * Custom JTable model for displaying student data.
 * CodeAlpha — Task 1: Student Grade Tracker
 */
public class StudentTableModel extends AbstractTableModel {

    private final String[] COLUMNS = {
        "#", "Name", "Math", "Science", "English", "History", "Comp Sci", "Average", "Grade", "Status"
    };

    private ArrayList<Student> students;

    public StudentTableModel(ArrayList<Student> students) {
        this.students = students;
    }

    @Override public int getRowCount()    { return students.size(); }
    @Override public int getColumnCount() { return COLUMNS.length; }
    @Override public String getColumnName(int col) { return COLUMNS[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        Student s = students.get(row);
        return switch (col) {
            case 0 -> row + 1;
            case 1 -> s.getName();
            case 2 -> s.getMath();
            case 3 -> s.getScience();
            case 4 -> s.getEnglish();
            case 5 -> s.getHistory();
            case 6 -> s.getComputerScience();
            case 7 -> String.format("%.1f", s.getAverage());
            case 8 -> s.getGrade();
            case 9 -> s.getStatus();
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int col) {
        if (col == 0 || (col >= 2 && col <= 6)) return Integer.class;
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) { return false; }

    public void refresh() { fireTableDataChanged(); }
}
