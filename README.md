# 🗳️ Votely

![Platform](https://img.shields.io/badge/platform-Android-blue.svg)
![Tech](https://img.shields.io/badge/tech-Firebase-orange)
![Status](https://img.shields.io/badge/status-Completed-brightgreen)
[![LinkedIn – Kaleem Hassan](https://img.shields.io/badge/LinkedIn-Kaleem%20Hassan-blue?logo=linkedin)](https://www.linkedin.com/in/kaleem-hassan)
[![GitHub](https://img.shields.io/github/stars/kaleemhassan/votely?style=social)](https://github.com/kaleemhassan/votely)

---

## 📱 Project Overview

**Votely** is a modern and secure voting application built using **Android Studio** and **Firebase**. It enables users to sign in and cast votes for their preferred candidates, ensuring each user can vote only once. The system updates live statistics and displays real-time voting results.

This project was developed as a final year project to simulate a real-world digital voting system, focusing on accessibility, transparency, and usability.

---

## 🎯 Features

- 🔐 **Google Sign-In & Firebase Authentication**
- 👤 **Profile Management** (Name, Email, Language, Logout)
- 🗳️ **Live Voting Panel** – Select and vote for candidates
- 📊 **Real-Time Stats** – Display of total votes, vote percentages
- 🔁 **24-Hour Countdown Timer** – Daily voting reset mechanism
- ✨ **Vote Confirmation** – Bottom sheet with confirmation overlay and animation
- ✅ **Firestore-Backed Vote Tracking** – Each user can vote once

---

## 📷 Screenshots

> Save images in `screenshots/` folder and update links below

- **Home Screen**  
  ![](screenshots/home.png)

- **Voting Screen**  
  ![](screenshots/voting.png)

- **Vote Confirmation Overlay**  
  ![](screenshots/confirm_vote.png)

- **Stats Screen**  
  ![](screenshots/stats.png)

---

## ⚙️ Tech Stack

| Layer       | Technology            |
|-------------|------------------------|
| Language    | Java                   |
| UI Design   | XML, Material Design   |
| Backend     | Firebase Firestore     |
| Auth        | Firebase Authentication|
| Animations  | Lottie + Custom XML    |

---

## 🚀 How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/kaleemhassan/votely.git
   
Open the project in Android Studio.

Add your own google-services.json file inside the app/ directory.

Sync Gradle.

Run the app on an emulator or real device.

📂 Project Structure
Copy
Edit
com.example.votely/
├── activities/
│   ├── LoginActivity.java
│   ├── SignupActivity.java
│   ├── OtpAuthenticationActivity.java
├── fragments/
│   ├── HomeFragment.java
│   ├── VotingFragment.java
│   ├── StatsFragment.java
│   ├── ProfileFragment.java
│   └── ConfirmVoteFragment.java
├── models/
│   └── Candidate.java
├── utils/
│   └── TimerUtils.java

This app was developed as part of our academic project.


🤝 Connect With Me

📫 Email: kaleemhassan.dev@gmail.com
