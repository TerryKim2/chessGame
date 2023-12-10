package swing;

import javax.swing.*;


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
