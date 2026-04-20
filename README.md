# 🎓 Java Quiz Application

![Java](https://img.shields.io/badge/Java-17_LTS-orange?style=flat&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-17.0.2-blue?style=flat)
![Maven](https://img.shields.io/badge/Maven-3.9.15-red?style=flat&logo=apachemaven)
![Platform](https://img.shields.io/badge/Platform-Windows-lightgrey?style=flat)

> A JavaFX-based desktop quiz application featuring randomised
> multi-subject MCQs, real-time feedback, and score tracking.

---

## ✨ Features

- 🎯 10 randomised questions per session across multiple subjects
- ✅ Instant feedback — green for correct, red for wrong
- 🎉 Celebration animation on correct answers
- 📊 Final score screen with accuracy percentage
- 🔄 Play again without restarting the app

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java JDK | 17 LTS | Core language |
| JavaFX | 17.0.2 | GUI framework |
| Apache Maven | 3.9.15 | Build tool |
| Git | Latest | Version control |

---

## 🚀 How to Run

```bash
# 1. Clone the repository
git clone https://github.com/Anant-083/java-quiz-application.git

# 2. Navigate to project
cd java-quiz-application

# 3. Build
mvn clean install

# 4. Run
mvn javafx:run
```

---

## 📁 Project Structure

```
src/main/java/com/quiz/app/
├── QuizApp.java        # Main app + UI controller
├── QuizEngine.java     # Quiz logic + session management
├── QuizQuestion.java   # Question data model (Java record)
└── QuestionBank.java   # Question pool (25 questions)
```

---

## 📌 Prerequisites

- Java 17 JDK
- Apache Maven 3.9+
- Git

---

⭐ If you found this useful, give it a star!
