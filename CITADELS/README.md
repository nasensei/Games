# Citadels (Java – Command Line Game)

This project is a **command-line implementation of the board game *Citadels***, developed in **Java** using an object-oriented design.  
The game models the core mechanics of Citadels, including character selection, turn-based actions, special abilities, and district building.

The focus of this project is on **clean OOP structure**, clear game flow, and faithful rule implementation rather than graphical presentation.

---

## 🎮 Features

- Character selection phase (Assassin → Warlord)
- Turn-based gameplay following official Citadels rules
- Unique **character abilities** (e.g. Assassin, Thief, Architect)
- Gold and district card management
- District building and scoring
- Human and computer players
- Command-line user interaction
- Save and load game state (JSON)

---

## 🛠️ Technologies Used

- **Java**
- Object-Oriented Programming (OOP)
- Command-Line Interface (CLI)
- JSON (for save/load functionality)

---

## ▶️ How to Run

1. Compile all Java files:
   ```bash
   javac *.java
2. Run the game:
   ```bash
   java App
3. Follow the on-screen prompts to play the game using text-based commands.

---

## 📜 Game Rules

- At the start of each round, players **secretly choose a character**.
- Characters take turns acting in numerical order from **Assassin to Warlord**.
- On a character’s turn, the player may:
  - Use the character’s special ability
  - Collect gold or draw district cards
  - Build at most one district
- Each character has a **unique ability** that can affect other players or the game state.
- Players manage gold and district cards strategically to expand their city.
- The game ends when a player builds the required number of districts.
- Final scores are calculated based on district values and applicable bonuses.
