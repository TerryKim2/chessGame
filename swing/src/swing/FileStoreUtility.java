package swing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides methods to store the game result to the general filestore
 * Class requires 2 things: difficulty and game result
 * @author sofiiarudenka
 */
public class FileStoreUtility {

    /**
     * stores the result of the game to a file GameHistory.txt
     * @param difficulty
     * @param result
     */
    public static void writeGameResultToFileStore(String difficulty, String result) {
        try {
            FileWriter writer = new FileWriter("GameHistory.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            System.out.println(dateFormat1.format(date));
            bufferedWriter.write(dateFormat1.format(date) + " " + dateFormat2.format(date) + " " + difficulty + " " + result);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
