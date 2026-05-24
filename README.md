# StudentGradeTracker — Java | CodeAlpha Task 1

A fully-featured **Java Swing GUI** Student Grade Tracker built for the CodeAlpha Java Programming Internship.

---

## ✅ CodeAlpha Requirements Covered

| Requirement | Implementation |
|---|---|
| Input and manage student grades | Add / Edit / Delete via dialog |
| Calculate average, highest, lowest | Live computed per student + dashboard |
| Arrays / ArrayLists to store data | `ArrayList<Student>` in `GradeTrackerApp` |
| Display summary report | Analytics tab with full class report |
| GUI-based interface | Java Swing dark-themed GUI |

---

## 🚀 How to Run in IntelliJ IDEA

1. **File → Open** → Select the `StudentGradeTracker_Java` folder
2. IntelliJ auto-detects it as a Java project
3. Set JDK to **Java 17+** if prompted *(File → Project Structure → SDK)*
4. Open `src/GradeTrackerApp.java`
5. Click the **▶ Run** button (or `Shift + F10`)

---

## 📁 Project Structure

```
StudentGradeTracker_Java/
├── src/
│   ├── GradeTrackerApp.java      ← Main window & entry point
│   ├── Student.java              ← Model: grade logic, avg, grade, status
│   ├── StudentTableModel.java    ← Custom JTable model
│   └── StudentDialog.java        ← Add / Edit modal dialog
├── README.md
└── .idea/                        ← IntelliJ project config
```

---

## ✨ Features

- **Dashboard Tab** — 6 live stat cards: Total Students, Class Average, Highest Avg, Lowest Avg, Passing, Failing
- **Students Tab** — Sortable JTable with all grades, color-coded Grade & Status columns; Edit & Delete buttons
- **Analytics Tab** — Per-subject averages, class breakdown table, and a formatted summary report
- **Add/Edit Modal** — Validated input dialog for name + 5 subject scores (0–100)
- **Delete with Confirmation** — Prevents accidental deletion
- **Export CSV** — Save all records to a `.csv` file via file chooser

---

## 🛠 Tech Stack

| Layer       | Technology              |
|-------------|-------------------------|
| Language    | Java 17+                |
| GUI         | Java Swing              |
| Data Store  | `ArrayList<Student>`    |
| OOP Design  | Model / View separation |
| Build       | IntelliJ IDEA (no Maven)|

---

## 📊 Grade Scale

| Grade | Average |
|-------|---------|
| A     | 90–100  |
| B     | 75–89   |
| C     | 60–74   |
| D     | 45–59   |
| F     | 0–44    |

Pass = Average ≥ 60
