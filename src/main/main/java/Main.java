import services.BotLauncher;
import dialogue.DialogueManager;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите режим: telegram, console или both");
            return;
        }

        String mode = args[0].toLowerCase();
        DialogueManager manager = new DialogueManager();

        BotLauncher launcher = new BotLauncher();

        switch (mode) {
            case "telegram" -> launcher.runTelegram(manager);
            case "console" -> launcher.runConsole(manager);
            case "both" -> launcher.runBoth(manager);
            default -> System.out.println("Неверный режим. Используйте: telegram, console или both");
        }
    }
}
