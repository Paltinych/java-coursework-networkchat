import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getInstance();
        Properties settings = new Properties();
        try {
            settings.load(new FileInputStream("settings.txt"));
        } catch (IOException e) {
            System.out.println("Настройки не прочитаны");
        }
        String host = settings.getProperty("address");
        int port = Integer.parseInt(settings.getProperty("port"));

        logger.log("client.log", "Клиент запущен\n");
        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            System.out.println(in.readLine());
            Scanner input = new Scanner(System.in);
            String name = input.nextLine();
            out.println(name);

            String resp = in.readLine();
            System.out.println(resp);
            logger.log("client.log", String.format("- " + name + " подключился.\n"));

            while (true) {
                String message = input.nextLine();
                out.println(message);
                if (message.equalsIgnoreCase("выход")) {
                    logger.log("client.log", String.format("- " + name + " отключился.\n"));
                    break;
                }
                System.out.println(in.readLine());
                logger.log("client.log", String.format("- " + name + " отправил: " + message + "\n"));
            }
        } catch (IOException e) {
            System.out.println("Ошибка подключению к серверу.");
        }
    }
}
