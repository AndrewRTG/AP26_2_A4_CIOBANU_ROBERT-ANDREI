package org.example;

import org.example.entity.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public static List<Question> loadQuestions(String fileName) {
        List<Question> questions = new ArrayList<>();

        InputStream inputStream = QuestionLoader.class
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (inputStream == null) {
            System.err.println("File not found: " + fileName);
            return questions;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            String text = null;
            String a = null;
            String b = null;
            String c = null;
            String d = null;
            String answer = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    if (text != null) {
                        questions.add(new Question(text, a, b, c, d, answer));
                    }

                    text = null;
                    a = null;
                    b = null;
                    c = null;
                    d = null;
                    answer = null;
                    continue;
                }

                if (line.startsWith("QUESTION:")) {
                    text = line.substring("QUESTION:".length()).trim();
                } else if (line.startsWith("A:")) {
                    a = line.substring("A:".length()).trim();
                } else if (line.startsWith("B:")) {
                    b = line.substring("B:".length()).trim();
                } else if (line.startsWith("C:")) {
                    c = line.substring("C:".length()).trim();
                } else if (line.startsWith("D:")) {
                    d = line.substring("D:".length()).trim();
                } else if (line.startsWith("ANSWER:")) {
                    answer = line.substring("ANSWER:".length()).trim();
                }
            }

            if (text != null) {
                questions.add(new Question(text, a, b, c, d, answer));
            }

        } catch (IOException e) {
            System.err.println("Error reading questions: " + e.getMessage());
        }

        return questions;
    }
}