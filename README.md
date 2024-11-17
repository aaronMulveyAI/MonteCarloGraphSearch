# Monte Carlo Graph Search and Transpositions in Monte Carlo Tree Search

**Author:** Aaron Mulvey Izquierdo  
**Department:** Advanced Computing Sciences  
**Institution:** Maastricht University  

---

## 📜 Abstract

This thesis explores how to enhance the **Monte Carlo Tree Search (MCTS)** algorithm by utilizing **transposition tables** and transforming its structure into a **directed acyclic graph (DAG)**, creating the **Monte Carlo Graph Search (MCGS)**. The results demonstrate that managing transpositions significantly improves efficiency in strategy games like Omega, highlighting the potential of these techniques for broader applications.

---

## 📂 Structure

1. **Introduction:**
   - Motivation for using MCTS in board games.
   - Challenges with transpositions and their impact on efficiency.

2. **Explored Algorithms:**
   - Variants of MCTS (UCT0, UCT1, UCT2, UCT3).
   - Implementation of MCGS with configurations UCD1, UCD2, and UCD3.

3. **Test Game:**
   - Use of the Omega strategy game as the experimental domain.

4. **Results:**
   - Comparison of win rates between MCTS and MCGS.
   - Evaluation of search time and computational efficiency.

5. **Conclusions and Future Work:**
   - Benefits of managing transpositions with MCGS.
   - Possibilities for extending these techniques to other domains.

---

## 🔍 Research Objectives

1. How does integrating transposition tables impact the efficiency and accuracy of MCTS?
2. How does transitioning from a tree structure to a graph improve MCGS performance?
3. What advantages does MCGS have over MCTS in metrics such as win rates and search efficiency?

---

## ⚙️ Experimental Setup

1. **Environment:**  
   - MacBook Air (2020), Apple M1, 8GB RAM.
   - Implementation in **Java 17**.

2. **Parameters:**  
   - Exploration constant `C = 0.4`.
   - Random simulation strategy.

3. **Testing:**  
   - Round-robin tournament with 10,000 iterations and a time limit of 1000 ms.

---

## 📊 Key Results

- **Efficiency:**  
   UCD3 consistently outperformed other configurations, handling transpositions more efficiently.

- **Win Rate Comparison:**  
   UCD3 showed a significantly higher win rate compared to UCT0 and other basic MCTS variants.

- **Search Time:**  
   While UCT0 was faster for short searches, UCD3 optimized transposition handling for deeper searches.

---

## 🛠️ Potential Applications

- Complex games with large state spaces.
- Domains like automated planning, robotics, logistics, and healthcare.

---

## 🤝 Acknowledgments

Special thanks to **Prof. Dr. Mark Winands** and **Dr. Matuš Mihalák** for their supervision and support throughout this project.

---

## 🔗 Key References

1. Childs, B. E., Brodeur, J. H., & Kocsis, L. (2008). *Transpositions and Move Groups in Monte Carlo Tree Search*.  
2. Cazenave, T., Mehat, J., & Saffidine, A. (2012). *Upper Confidence Bound for Rooted Directed Acyclic Graphs*.  
3. Czech, J., Korus, P., & Kersting, K. (2020). *Monte Carlo Graph Search for AlphaZero*.  

For a full list, refer to the thesis document.

---

This file summarizes the key aspects of your thesis and is designed to capture the attention of both technical and non-technical readers. If you want to customize any section or add visuals, let me know! 😊
