# Trie vs Robin Hood Hashing (Java)

A Java project comparing **static Trie implementation** and **Robin Hood Hashing Trie** in terms of **memory efficiency** and **scalability**.  
Experiments were conducted with generated dictionaries of up to 500k words, measuring space utilization across multiple word lengths.

---

## Project Explanation

### Background
- A **Trie (prefix tree)** stores characters in nodes with children arrays.
- A **Robin Hood Hashing table** uses open addressing with displacement-based collision resolution, minimizing clustering.

### Experiment
- **Datasets:** Randomly generated dictionaries of sizes `1k, 5k, 10k, 100k, 250k, 500k`.
- **Word lengths tested:**
  - Fixed length: 3, 5, 7, 10
  - Variable length: 3–12 (weighted towards shorter words)
- **Implementations:**
  - `TrieNode` with **26-length static array** of children
  - `TrieNode` with **Robin Hood Hashing table** (rehashes at 90%, grows 5→11→19→29)

### Findings
- **Static Trie:** wastes memory, since every node allocates space for 26 children regardless of usage.
- **Robin Hood Hashing:** dynamically resizes, leading to far more efficient memory usage.
- After ~17,576 words (all possible length-3 words), static utilization levels off, while hashing adapts efficiently.

---

## Repository Structure
Trie-Hashing-Comparison/
┣ src/
┃ ┣ Element.java
┃ ┣ HashingMain.java
┃ ┣ Heap.java
┃ ┣ RobinHoodHashing.java
┃ ┣ RobinHoodTrie.java
┃ ┣ Trie.java
┃ ┗ TrieNode.java
┣ docs/
┃ ┗ report.pdf
┣ LICENSE
┣ README.md

---

## Features
- TrieNode with **static array** children
- TrieNode with **Robin Hood Hashing** children
- Dictionary generation for fixed & variable word lengths
- Experimental framework for comparison
- Output for graphing memory usage trends

---

## Technologies
- Language: Java
- Concepts: Tries, Hashing, Collision Resolution (Robin Hood), Data Structure Benchmarking

---

## Authors
- Nicolas Constantinou
- 2024

---

## Usage

### Compile
```bash
javac src/*.java -d bin
java -cp bin HashingMain

