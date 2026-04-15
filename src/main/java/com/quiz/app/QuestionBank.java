package com.quiz.app;

import java.util.List;

public final class QuestionBank {
    private QuestionBank() {
    }

    public static List<QuizQuestion> allQuestions() {
        return List.of(
                new QuizQuestion(
                        "Calculus",
                        "What is the derivative of ln(x^2 + 1) with respect to x?",
                        List.of("2x/(x^2 + 1)", "1/(x^2 + 1)", "2/(x + 1)", "ln(2x + 1)"),
                        "2x/(x^2 + 1)"
                ),
                new QuizQuestion(
                        "Linear Algebra",
                        "If A is a 2x2 matrix with determinant 0, what can be concluded?",
                        List.of("A is singular", "A is orthogonal", "A is always symmetric", "A has trace 0"),
                        "A is singular"
                ),
                new QuizQuestion(
                        "Physics",
                        "Which law states that total energy in an isolated system remains constant?",
                        List.of("Conservation of Energy", "Hooke's Law", "Gauss's Law", "Snell's Law"),
                        "Conservation of Energy"
                ),
                new QuizQuestion(
                        "Quantum Mechanics",
                        "In the Schrödinger equation, the wave function squared represents:",
                        List.of("Probability density", "Potential energy", "Momentum", "Electric field strength"),
                        "Probability density"
                ),
                new QuizQuestion(
                        "Chemistry",
                        "A solution with pH = 3 is how many times more acidic than pH = 5?",
                        List.of("100 times", "2 times", "20 times", "1000 times"),
                        "100 times"
                ),
                new QuizQuestion(
                        "Organic Chemistry",
                        "A tertiary alkyl halide most commonly undergoes which substitution mechanism?",
                        List.of("SN1", "SN2", "E2 only", "No reaction"),
                        "SN1"
                ),
                new QuizQuestion(
                        "Biology",
                        "Where does the electron transport chain primarily occur in eukaryotic cells?",
                        List.of("Inner mitochondrial membrane", "Ribosome", "Nucleus", "Golgi apparatus"),
                        "Inner mitochondrial membrane"
                ),
                new QuizQuestion(
                        "Genetics",
                        "In Hardy-Weinberg equilibrium, allele frequencies remain constant if:",
                        List.of("No evolution occurs", "Mutation rate is high", "Population is tiny", "Natural selection is strong"),
                        "No evolution occurs"
                ),
                new QuizQuestion(
                        "Computer Science",
                        "What is the average-case time complexity of quicksort?",
                        List.of("O(n log n)", "O(n^2)", "O(log n)", "O(n)"),
                        "O(n log n)"
                ),
                new QuizQuestion(
                        "Data Structures",
                        "Which data structure gives FIFO behavior?",
                        List.of("Queue", "Stack", "Binary Heap", "Hash Set"),
                        "Queue"
                ),
                new QuizQuestion(
                        "Operating Systems",
                        "Which is NOT one of Coffman's deadlock conditions?",
                        List.of("Preemptive scheduling", "Mutual exclusion", "Hold and wait", "Circular wait"),
                        "Preemptive scheduling"
                ),
                new QuizQuestion(
                        "Databases",
                        "Third Normal Form (3NF) is primarily used to reduce:",
                        List.of("Data redundancy", "Query speed", "Index size", "Disk partitioning"),
                        "Data redundancy"
                ),
                new QuizQuestion(
                        "Statistics",
                        "By the Central Limit Theorem, the sampling distribution of the mean tends toward:",
                        List.of("Normal distribution", "Uniform distribution", "Poisson distribution", "Binomial distribution"),
                        "Normal distribution"
                ),
                new QuizQuestion(
                        "Economics",
                        "Price elasticity of demand greater than 1 indicates demand is:",
                        List.of("Elastic", "Inelastic", "Perfectly inelastic", "Unitary impossible"),
                        "Elastic"
                ),
                new QuizQuestion(
                        "Finance",
                        "Net Present Value (NPV) discounts future cash flows to account for:",
                        List.of("Time value of money", "Tax compliance", "Inventory turnover", "Market capitalization"),
                        "Time value of money"
                ),
                new QuizQuestion(
                        "Psychology",
                        "Classical conditioning is most associated with which researcher?",
                        List.of("Ivan Pavlov", "B.F. Skinner", "Jean Piaget", "Carl Rogers"),
                        "Ivan Pavlov"
                ),
                new QuizQuestion(
                        "Sociology",
                        "A social role expected behavior tied to a status is called:",
                        List.of("Role expectation", "Social drift", "Cultural lag", "Anomie"),
                        "Role expectation"
                ),
                new QuizQuestion(
                        "Literature",
                        "Iambic pentameter typically consists of how many iambs per line?",
                        List.of("5", "4", "6", "10"),
                        "5"
                ),
                new QuizQuestion(
                        "Philosophy",
                        "Utilitarian ethics is most strongly focused on:",
                        List.of("Maximizing overall happiness", "Duty regardless of outcomes", "Virtue as habit only", "Divine command"),
                        "Maximizing overall happiness"
                ),
                new QuizQuestion(
                        "Electrical Engineering",
                        "The Nyquist criterion in digital communication relates to:",
                        List.of("Minimum sampling frequency", "Battery efficiency", "Antenna gain", "Circuit resistance"),
                        "Minimum sampling frequency"
                ),
                new QuizQuestion(
                        "Civil Engineering",
                        "In a stress-strain curve, the slope in the elastic region is:",
                        List.of("Young's modulus", "Poisson ratio", "Shear force", "Buckling factor"),
                        "Young's modulus"
                ),
                new QuizQuestion(
                        "Mechanical Engineering",
                        "Which thermodynamic law introduces the concept of entropy?",
                        List.of("Second law", "First law", "Zeroth law", "Third law"),
                        "Second law"
                ),
                new QuizQuestion(
                        "Political Science",
                        "In many democratic systems, separation of powers is intended to:",
                        List.of("Prevent concentration of authority", "Increase inflation", "Eliminate elections", "Centralize media control"),
                        "Prevent concentration of authority"
                ),
                new QuizQuestion(
                        "Environmental Science",
                        "Primary producers in an ecosystem are mostly:",
                        List.of("Autotrophs", "Carnivores", "Detritivores", "Omnivores"),
                        "Autotrophs"
                ),
                new QuizQuestion(
                        "Machine Learning",
                        "High bias and low variance usually indicates:",
                        List.of("Underfitting", "Overfitting", "Perfect generalization", "Data leakage"),
                        "Underfitting"
                )
        );
    }
}
