# Omotesando Adventure

A text-based dungeon crawler game developed for the University of Tsukuba Object-Oriented Programming course. Navigate the maze, battle monsters, and find the Goddess to escape Omotesando.

## Prerequisites

* **Java 25** (Temurin or Oracle)
* **Maven 3.x**

## How to Build & Test

This project uses **JUnit 5** for automated testing. To compile the code and run the 15-test suite:

```bash
mvn clean package
```

Expected Output: `Tests run: 15, Failures: 0, Errors: 0, Skipped: 0`

# How to Run

Since this project uses Java 25 Preview Features, you must include the `--enable-preview` flag when running the compiled class files.

## Option 1: Using Maven (Recommended)

```bash
mvn exec:java -Dexec.mainClass="jp.ac.tsukuba.adventure.Main"
```

## Option 2: Using Raw Java

First build the project, then run:

```bash
java --enable-preview -cp target/classes jp.ac.tsukuba.adventure.Main
```

# Project Structure

- `src/main/java`: Core game logic (Game loop, Map parsing, Entity polymorphism).

- `src/test/java`: Comprehensive test suite (Map logic, Combat math, Healing).

- `src/main/resources`: Game assets (map.txt).

- `target/`: Compiled bytecode and build artifacts.