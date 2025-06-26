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

## 📱 Screenshots preview

<table>
  <tr>
    <td>

<b>Welcome</b><br>
Purpose: App introduction, disclaimer, and Monash Nutrition link.

</td>
    <td align="right">
      <img width="180" alt="Welcome" src="https://github.com/user-attachments/assets/1241ffd3-38b2-4080-a6c7-321f452b62a3" />
    </td>
  </tr>
  <tr>
    <td>

<b>Login/Register</b><br>
Purpose: Secure account claim and login via database.

</td>
    <td align="right">
      <img width="180" alt="Login" src="https://github.com/user-attachments/assets/260232ec-62d0-40a3-8be3-242e9ef8bdcd" />
    </td>
  </tr>
  <tr>
    <td>

<b>Questionnaire</b><br>
Purpose: Food categories, persona, and meal timing input.

</td>
    <td align="right">
      <img width="180" alt="Questionnaire" src="https://github.com/user-attachments/assets/c5088963-e0c3-4c22-a360-35e958ca2e0f" />
    </td>
  </tr>
  <tr>
    <td>

<b>Home</b><br>
Purpose: Personalized HEIFA score display.

</td>
    <td align="right">
      <img width="180" alt="Home" src="https://github.com/user-attachments/assets/94446a61-bfdb-4e1c-a306-6bfad8811f68" />
    </td>
  </tr>
  <tr>
    <td>

<b>Insights</b><br>
Purpose: Nutrient breakdown and improvement suggestions.

</td>
    <td align="right">
      <img width="180" alt="Insights" src="https://github.com/user-attachments/assets/f114d49d-7c7b-43a8-8922-bcb49adae219" />
    </td>
  </tr>
  <tr>
    <td>

<b>NutriCoach</b><br>
Purpose: Show fruit suggestions and AI tips when fruit intake is low.

</td>
    <td align="right">
      <img width="180" alt="NutriCoach" src="https://github.com/user-attachments/assets/89846ba5-7dc6-42f9-a705-8f4239cf9fc9" />
    </td>
  </tr>
  <tr>
    <td>

<b>NutriCoach (AI)</b><br>
Purpose: GenAI tips based on user data and nutrition score.

</td>
    <td align="right">
      <img width="180" alt="NutriCoach AI" src="https://github.com/user-attachments/assets/9042e515-27c2-47b7-ade9-e2df9e43c25a" />
    </td>
  </tr>
  <tr>
    <td>

<b>Settings</b><br>
Purpose: Theme toggle, user info, and logout.

</td>
    <td align="right">
      <img width="180" alt="Settings" src="https://github.com/user-attachments/assets/113072d0-a527-4f3a-9fda-1682cdde16b3" />
    </td>
  </tr>
  <tr>
    <td>

<b>Admin View</b><br>
Purpose: Average scores + AI-based pattern recognition (secured access).

</td>
    <td align="right">
      <img width="180" alt="Admin View" src="https://github.com/user-attachments/assets/256dcbc0-2057-4b30-8b25-04b0b66ce323" />
    </td>
  </tr>
</table>


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
