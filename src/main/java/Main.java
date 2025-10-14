import dialogue.DialogueManager;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DialogueManager manager = new DialogueManager();
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        String userId = "console-user";

        System.out.println("Кино бот (консольная версия). Напиши /start, чтобы начать. /quit — выход.");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("/quit")) {
                System.out.println("До встречи");
                break;
            }
            String response = manager.handleMessage(userId, input);
            System.out.println(response);
        }
        scanner.close();
    }
}
