# 🎓 Student Grade Tracker — CodeAlpha Java Internship | Task 3

<img width="1920" height="872" alt="Dashboard" src="https://github.com/user-attachments/assets/33b0829f-0cef-4130-9cf3-195f0a38d209" />


<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-6366f1?style=for-the-badge)
![CodeAlpha](https://img.shields.io/badge/Internship-CodeAlpha-10d9a0?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)

A fully-featured **Java Swing GUI** application to input, manage, and analyze student grades — built as **Task 3** of the CodeAlpha Java Programming Internship.

</div>

---

## 📸 Features

### 🖥️ Dashboard Tab
- Total number of students
- Class average score
- Highest & lowest average in class
- Passing vs failing student count

### 📋 Students Tab
- Full grade table with all 5 subjects per student
- Color-coded **Grade** (A / B / C / D / F) and **Status** (Pass / Fail) columns
- **Add**, **Edit**, and **Delete** student records
- Input validation (names required, scores must be 0–100)

### 📊 Analytics Tab
- Per-subject class averages (Math, Science, English, History, Computer Science)
- Full class breakdown table with total score and deviation
- Auto-generated **Summary Report** showing:
  - Class average
  - Top & lowest performing students
  - Best & weakest subject

### 💾 Export
- Export all student data to a **CSV file** via file chooser

---

## ✅ CodeAlpha Task Requirements — All Met

| Requirement | How It's Implemented |
|---|---|
| Input and manage student grades | Add / Edit / Delete dialogs with validation |
| Calculate average, highest, lowest scores | Computed in `Student.java` + displayed in Dashboard |
| Use ArrayLists to store and manage data | `ArrayList<Student>` in `GradeTrackerApp.java` |
| Display a summary report of all students | Analytics tab — formatted class summary report |
| GUI-based interface | Java Swing — dark themed, 3-tab layout |

---

## 🗂️ Project Structure

```
CodeAlpha_StudentGradeTracker/
├── src/
│   ├── GradeTrackerApp.java      ← Main window, tabs, dashboard, analytics
│   ├── Student.java              ← Model: scores, average, grade, status
│   ├── StudentTableModel.java    ← Custom AbstractTableModel for JTable
│   └── StudentDialog.java        ← Add / Edit modal dialog with validation
├── README.md
└── .idea/                        ← IntelliJ IDEA configuration
```

---

## 🚀 How to Run

### Prerequisites
- Java JDK **17 or higher** — [Download here](https://adoptium.net)
- IntelliJ IDEA (Community or Ultimate)

### Steps
```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/CodeAlpha_StudentGradeTracker.git

# Open in IntelliJ IDEA
File → Open → Select the project folder

# Run
Open GradeTrackerApp.java → Click the ▶ Run button
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| GUI Framework | Java Swing |
| Data Storage | `ArrayList<Student>` |
| Architecture | OOP — Model / View separation |
| IDE | IntelliJ IDEA |
| Build Tool | None (plain Java) |

---

## 📊 Grade Scale

| Grade | Average Range | Status |
|---|---|---|
| **A** | 90 – 100 | Pass ✅ |
| **B** | 75 – 89  | Pass ✅ |
| **C** | 60 – 74  | Pass ✅ |
| **D** | 45 – 59  | Fail ❌ |
| **F** | 0  – 44  | Fail ❌ |

---

## 👤 Author

**Your Name**
- 🌐 LinkedIn: [linkedin.com/in/nihahawas45](https://linkedin.com/in/nihahawas45)
- 💻 GitHub: [github.com/nihahawas](https://github.com/nihahawas)
- 🏢 Internship: [CodeAlpha](https://www.codealpha.tech)

---

<div align="center">
  Made with ❤️ as part of the <strong>CodeAlpha Java Programming Internship</strong>
</div>
