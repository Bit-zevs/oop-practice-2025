package org.example;

import java.util.Scanner;

public class LogikOfDialogue {
    private void help() {
        System.out.println("This bot guesses a random number.");
        System.out.println("Again enter a number from 0 to 4");
    }

    private void questionForTheUser(Integer userNumber) {
        Questions q = new Questions();
        int randomNumber = q.expressionGenerator();

        if (userNumber == randomNumber) {
            System.out.println("Yes, you are right");
        } else {
            System.out.println("No, the correct answer is " + randomNumber);
        }
        System.out.println("Enter a number from 0 to 4");
    }

    public void enterFromConsole() {
        help();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            try {
                String userEnter = sc.nextLine();
                switch (userEnter) {
                    case "/help":
                    case "/start":
                        help();
                        break;
                    default:
                        questionForTheUser(Integer.parseInt(userEnter));
                }
            } catch (Exception e) {
                System.out.println("Unknown command");
                System.out.println("Again enter a number from 0 to 4");
            }
        }
    }
}