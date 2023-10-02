import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectToServer extends Thread {
    private final Socket CONNECT_SOCKET;
    private final PrintWriter OUT;
    private final BufferedReader IN;

    public ConnectToServer(Socket connectSocket) throws IOException {
        CONNECT_SOCKET = connectSocket;
        OUT = new PrintWriter(connectSocket.getOutputStream(), true);
        IN = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
        start();
    }
    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        try {
            logger.log("file.log", String.format("Новое соединение. порт: %d%n", CONNECT_SOCKET.getPort()));
            OUT.println("Введите ваше имя:");
            final String NAME = IN.readLine().toUpperCase();
            logger.log("file.log", String.format("- " + NAME + " подключился.\n"));
            OUT.println(String.format("Привет %s, ваш порт: %d", NAME, CONNECT_SOCKET.getPort()));
            while (true) {
                String message = IN.readLine();
                if (message.equalsIgnoreCase("выход")) {
                    logger.log("file.log", String.format("- " + NAME + " отключился.\n"));
                    CONNECT_SOCKET.close();
                    OUT.close();
                    IN.close();
                    threadInterrupt(currentThread());
                    break;
                }
                OUT.println(String.format(NAME + " отправил: %s", message));
                logger.log("file.log", String.format("- " + NAME + " отправил: " + message + "\n"));
            }
        } catch (IOException | NullPointerException e) {
            threadInterrupt(currentThread());
        }
    }

    private void threadInterrupt (Thread thread) {
        thread.interrupt();
        Server.connectionToServer.remove(this);
        System.out.println("Количество подключенных: " + Server.connectionToServer.size());
    }
}
