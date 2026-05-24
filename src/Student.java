/**
 * Student.java
 * Model class representing a student with subject scores.
 * CodeAlpha — Task 1: Student Grade Tracker
 */
public class Student {

    private static int idCounter = 1;

    private int id;
    private String name;
    private int math;
    private int science;
    private int english;
    private int history;
    private int computerScience;

    // ─── Constructor ────────────────────────────────────────────────────────────
    public Student(String name, int math, int science, int english, int history, int computerScience) {
        this.id             = idCounter++;
        this.name           = name;
        this.math           = math;
        this.science        = science;
        this.english        = english;
        this.history        = history;
        this.computerScience = computerScience;
    }

    // ─── Computed Metrics ────────────────────────────────────────────────────────
    public double getAverage() {
        return (math + science + english + history + computerScience) / 5.0;
    }

    public int getTotal() {
        return math + science + english + history + computerScience;
    }

    public int getHighestScore() {
        return Math.max(math, Math.max(science, Math.max(english, Math.max(history, computerScience))));
    }

    public int getLowestScore() {
        return Math.min(math, Math.min(science, Math.min(english, Math.min(history, computerScience))));
    }

    public String getGrade() {
        double avg = getAverage();
        if (avg >= 90) return "A";
        if (avg >= 75) return "B";
        if (avg >= 60) return "C";
        if (avg >= 45) return "D";
        return "F";
    }

    public String getStatus() {
        return getAverage() >= 60 ? "Pass" : "Fail";
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────
    public int    getId()            { return id; }
    public String getName()          { return name; }
    public int    getMath()          { return math; }
    public int    getScience()       { return science; }
    public int    getEnglish()       { return english; }
    public int    getHistory()       { return history; }
    public int    getComputerScience(){ return computerScience; }

    public void setName(String name)                  { this.name = name; }
    public void setMath(int math)                     { this.math = math; }
    public void setScience(int science)               { this.science = science; }
    public void setEnglish(int english)               { this.english = english; }
    public void setHistory(int history)               { this.history = history; }
    public void setComputerScience(int cs)            { this.computerScience = cs; }

    // Used by JTable / toString
    @Override
    public String toString() {
        return String.format("%-20s | Math:%-4d Sci:%-4d Eng:%-4d His:%-4d CS:%-4d | Avg: %.1f | Grade: %s",
                name, math, science, english, history, computerScience, getAverage(), getGrade());
    }
}
