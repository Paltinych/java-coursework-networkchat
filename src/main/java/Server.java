import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Server {

    public static ArrayList<Thread> connectionToServer = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        Logger logger = Logger.getInstance();

        Properties settings = new Properties();
        try {
            settings.load(new FileInputStream("settings.txt"));
        } catch (IOException e) {
            System.out.println("Настройки не прочитаны");
        }
        int port = Integer.parseInt(settings.getProperty("port"));
        System.out.println(port);

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println(Thread.currentThread().getName() + ": Сервер запущен");
        logger.log("file.log", "Сервер запущен\n");

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    connectionToServer.add(new ConnectToServer(socket));
                    System.out.println("Количество подключенных: " + connectionToServer.size());
                } catch (IOException ex) {
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}
