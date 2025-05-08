package Helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Brandon Jones
 *
 * - Creates and writes to login_activity.txt
 */
public abstract class Logger {

    /**
     * - Creates a log file called login_activity.txt
     */
    public static void createLog() throws IOException {
        //Check if log exists
        Path path = Paths.get("Logs/login_activity.txt");
        if (Files.exists(path)) {
            System.out.println("Log files found");
        }
        else {
            System.out.println("Log files not found... Creating Log files");
            Path dir = Paths.get("Logs/");
            Path file = Paths.get("Logs/login_activity.txt");
            Files.createDirectories(dir);
            Files.createFile(file);
            System.out.println("Log files created successfully");
        }
    }

    /**
     *
     * @param string - The UTC time of the event being logged
     * @throws IOException
     */
    public static void writeToLog(String string) throws IOException {
        try {
            File dir = new File("Logs");
            File file = new File(dir, "login_activity.txt");
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw =  new PrintWriter(fw);
            pw.println(">> UTC - " + string);
            pw.flush();
            pw.close();
            System.out.println("Attempt Logged");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("writeToLog Failed!");
        }


    }
}
