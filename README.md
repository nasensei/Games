# Sudoku Solver (Backtracking) — Java

A simple Sudoku **solvability checker + solver** written in Java using **backtracking**.  
Empty cells are represented using `null`. The program checks whether a given Sudoku board is valid and solvable, then fills in the solution.

---

## Features

- ✅ Validates board format:
  - Board must be **non-null**
  - Board must be **square** (N×N)
  - Board size must be **divisible by 3**
- ✅ Validates existing entries:
  - Values must be `1` to `9` (or `null`)
  - No repeats in the same **row**, **column**, or **3×3 box**
- ✅ Uses **backtracking** to try numbers `1..9` in empty cells
- ✅ Prints the solved board when a solution is found

---

## How it works

### `isSolvable(Integer[][] board)`
Main entry point:
1. Checks the board dimensions and format.
2. Checks the initial numbers obey Sudoku rules.
3. Runs a recursive backtracking solver.

Returns:
- `true` if the board can be solved
- `false` if the board is invalid or unsolvable

### `gamePlay(...)`
A rule-checking helper:
- Temporarily clears the current cell to avoid comparing it with itself
- Checks:
  - Same row
  - Same column
  - Same 3×3 box

### `solver(...)`
Backtracking algorithm:
- Finds the next `null` cell
- Tries values `1..9`
- If a value is valid, places it and continues recursively
- If it leads to a dead end, removes it (**backtracks**) and tries the next value

---

## How to run

### Compile
```bash
javac SudokuSolve.java
