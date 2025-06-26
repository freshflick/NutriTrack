# 🥗 NutriTrack

**NutriTrack** is a personal nutrition companion app built with **Kotlin** and **Jetpack Compose**. The app empowers users to monitor their dietary habits, receive personalized insights, and explore smart, AI-enhanced suggestions — all backed by clean UI, secure authentication, and modern Android architecture.

---

## 📱 App Overview

NutriTrack helps individuals track what they eat, understand their nutritional quality, and make informed improvements. It uses HEIFA-based scoring to evaluate food quality, integrates an AI tip engine, and leverages a public fruit data API for contextual recommendations. Everything is wrapped in a sleek, responsive interface designed for real-world use.

---

## 🔑 Key Features

### 🧩 Core Capabilities
- 📋 **Personalized food intake questionnaire**
- 🔐 **Secure login and account claim** using unique IDs and validation
- 📊 **HEIFA-based food scoring** with gender-based breakdowns
- 📈 **Visual insights** for categories like fruits, vegetables, grains, and more
- 🧠 **Food quality education** through interactive breakdowns
- 📁 **Local Room database** for fast and secure data storage

### 🤖 Smart Enhancements
- 🧠 **NutriCoach AI Assistant**:
  - Uses [FruityVice API](https://www.fruityvice.com/) to suggest fruits based on user scores
  - Integrates **Google's Gemini API (GenAI)** to generate motivational food advice
  - Stores all AI responses in a history log for user review
- 🔐 **Strong password enforcement** and password visibility toggles
- 🌗 **Dark/Light Mode** for accessibility and eye comfort
- 🔄 **Quick logout**, **post-registration login redirection**, and **privacy-aware questionnaire saving**
- 🧼 **Modern UI design**: rounded corners, transitions, clean layouts
- 📊 **Admin View**: advanced data analytics, AI-powered insight summaries

---

## 🧠 NutriCoach (AI + Fruit Intelligence)

### 🍌 Fruit Suggestions (FruityVice API)
When a user's fruit intake is below optimal, the NutriCoach screen allows them to:
- Enter a fruit name
- View live fruit facts, stats, and nutrient content from the FruityVice API

If the user's score is already optimal, a fun random image is shown via [https://picsum.photos/](https://picsum.photos/).

### 🤖 Motivational AI Tips (GenAI)
- NutriTrack uses **Google Gemini API** to generate tailored encouragement and food tips
- Tips are enhanced using user intake data and food scores
- All tips are saved in a **NutriCoachTips** database table and can be reviewed via a history modal

---

## 🛠️ Architecture & Tech Stack

- **Jetpack Compose** — declarative UI
- **MVVM architecture** — ViewModel + LiveData
- **Room** — local SQLite database with DAO & entity models
- **DataStore** — for persisting login sessions and theme preference
- **Retrofit + Coroutines** — for network operations
- **Custom ViewModels & Repositories** — for clean separation of concerns

---

## 🧮 Database Schema

### Tables:
- **Patient**: Stores user ID, phone number, name, sex, and all HEIFA scores
- **FoodIntake**: Stores questionnaire responses with foreign key to Patient
- **NutriCoachTips**: Stores all AI-generated tips linked to a user

---

## 📋 Screens Summary

| Screen          | Purpose                                                       |
|-----------------|----------------------------------------------------------------|
| Welcome         | App introduction, disclaimer, and link to Monash Nutrition    |
| Login/Register  | Secure account claim and login via DB                         |
| Questionnaire   | Food categories, meal timing, persona selection               |
| Home            | Personalized greeting and overall HEIFA score                 |
| Insights        | Category-wise breakdown and call-to-action for improvement    |
| NutriCoach      | AI + API-driven fruit suggestions and motivational tips       |
| Settings        | View user info, toggle theme, and logout                      |
| Admin View      | Overview screen for analytics, pattern recognition using AI   |

---

## 🎯 Admin View & Analytics

Accessible by entering a special key (`dollar-entry-apples`), the admin dashboard includes:

- Average HEIFA scores by gender
- AI-generated insights (e.g., data patterns, common deficiencies)
- Historical food intake trends across users

---

## ⚙️ CSV Integration

On first launch, user and score data is imported from a CSV file and inserted into the Room database. From then on, the app operates entirely on local storage. 


---

## 🌐 APIs Used

| API          | Purpose                            | Link                                    |
|--------------|-------------------------------------|-----------------------------------------|
| FruityVice   | Fruit facts and nutrients           | https://www.fruityvice.com              |
| Gemini (GenAI) | Personalized motivational messages | https://aistudio.google.com             |
| Picsum       | Fun random images                   | https://picsum.photos                   |

---

## 🌗 Accessibility & UX Features

- Toggleable light/dark mode
- Immediate validation for password strength
- Show/hide password icons
- Accessible UI with large tap targets and clear labels
- Intentional privacy logic (questionnaire only saves on confirmation)

---

## 🧪 How to Run

1. Clone the repo
2. Open in Android Studio
3. Run on emulator/device
4. Place your `patients.csv` file in the device's internal storage as specified in the code
5. First launch will load the CSV → DB
6. Register or log in and start tracking!

---

## 👤 Author

**[Shayan Nadeem]**  
Email: [shayannadeem1x@gmail.com]  

---

## 📜 License

This project is released under the **MIT License**.  
All API use complies with public usage terms.

---

## 🙌 Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [FruityVice API](https://www.fruityvice.com/)
- [Gemini AI](https://aistudio.google.com/)
- All design inspirations and nutrition frameworks based on HEIFA scoring logic
