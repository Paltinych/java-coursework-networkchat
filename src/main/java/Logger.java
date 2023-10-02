import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger logger;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private Logger() {
    }

    public void log(String file, String msg) throws IOException {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(LocalDateTime.now().format(dateTimeFormatter) + " " + msg);
            fw.flush();
        }
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }
}