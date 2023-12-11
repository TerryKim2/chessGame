package swing;

import javax.swing.*;

/**
 * This class loads the application and is the main entry point to the game
 * @author Group 15
 */
public class ApplicationRunner {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChessLobby lobby = new ChessLobby();
                lobby.setVisible(true);
            }
        });
    }

}
