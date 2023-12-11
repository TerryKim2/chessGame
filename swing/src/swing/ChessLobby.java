package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * 
 * @author cubby(Donghwan)
 *
 */

public class ChessLobby extends JFrame {
/**
 * This is the lobby for a chess game. 
 * There is a button to view previously played game records.
 *  Click the buttons to play the game, and then receive the basic information to run the chessboard.
 */
    private JButton startGameButton;
    private JButton historyButton;
    private JButton exitGameButton;
    private JButton easyButton;
    private JButton normalButton;
    private JButton bButton;
    private JButton wButton;
    private Image backgroundImage;
    private JLabel select;
    private JLabel chess;
    public boolean white;
    public boolean easy;
    public ChessLobby() {
        /** Set up the JFrame*/
        setTitle("Chess Lobby");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ImageIcon img = new ImageIcon(System.getProperty("user.dir") + "\\resources\\button_image.png");
        ImageIcon img = new ImageIcon("swing/resources/button_image.png");
        JLabel textLabel1 = new JLabel("PLAY");
        textLabel1.setFont(new Font("times", Font.BOLD, 50));
        textLabel1.setHorizontalAlignment(JLabel.CENTER);
        textLabel1.setForeground(Color.white);

        JLabel textLabel2 = new JLabel("EXIT");
        textLabel2.setFont(new Font("times", Font.BOLD, 50	));
        textLabel2.setHorizontalAlignment(JLabel.CENTER);
        textLabel2.setForeground(Color.white);

        JLabel textLabel3 = new JLabel("EASY");
        textLabel3.setFont(new Font("times", Font.BOLD, 50));
        textLabel3.setHorizontalAlignment(JLabel.CENTER);
        textLabel3.setForeground(Color.white);

        JLabel textLabel4 = new JLabel("NORMAL");
        textLabel4.setFont(new Font("times", Font.BOLD, 50));
        textLabel4.setHorizontalAlignment(JLabel.CENTER);
        textLabel4.setForeground(Color.white);

        JLabel textLabel6 = new JLabel("WHITE");
        textLabel6.setFont(new Font("times", Font.BOLD, 50));
        textLabel6.setHorizontalAlignment(JLabel.CENTER);
        textLabel6.setForeground(Color.white);

        JLabel textLabel7 = new JLabel("BLACK");
        textLabel7.setFont(new Font("times", Font.BOLD, 50));
        textLabel7.setHorizontalAlignment(JLabel.CENTER);
        textLabel7.setForeground(Color.white);

        JLabel textLabel8 = new JLabel("HISTORY");
        textLabel8.setFont(new Font("times", Font.BOLD, 50));
        textLabel8.setHorizontalAlignment(JLabel.CENTER);
        textLabel8.setForeground(Color.white);

        /** Load the background image*/
        //backgroundImage = new ImageIcon(System.getProperty("user.dir")+"\\resources\\background.jpg").getImage();
        backgroundImage = new ImageIcon("swing/resources/background.jpg").getImage();

        /** Create components*/
        startGameButton = new JButton();
        startGameButton.setBounds(470, 206, 500, 150);
        startGameButton.setIcon(img);
        startGameButton.setFont(new Font("times", Font.BOLD, 50));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setBorderPainted(true);
        startGameButton.setContentAreaFilled(false);
        startGameButton.setFocusPainted(false);
        startGameButton.setLayout(new BorderLayout());
        startGameButton.add(textLabel1, BorderLayout.CENTER);

        historyButton = new JButton();
        historyButton.setBounds(470, 406, 500, 150);
        historyButton.setIcon(img);
        historyButton.setFont(new Font("times", Font.BOLD, 50));
        historyButton.setForeground(Color.WHITE);
        historyButton.setBorderPainted(true);
        historyButton.setContentAreaFilled(false);
        historyButton.setFocusPainted(false);
        historyButton.setLayout(new BorderLayout());
        historyButton.add(textLabel8, BorderLayout.CENTER);

        exitGameButton = new JButton();
        exitGameButton.setBounds(470, 606, 500, 150);
        exitGameButton.setIcon(img);
        exitGameButton.setFont(new Font("times", Font.BOLD, 50));
        exitGameButton.setForeground(Color.WHITE);
        exitGameButton.setBorderPainted(true);
        exitGameButton.setContentAreaFilled(false);
        exitGameButton.setFocusPainted(false);
        exitGameButton.setLayout(new BorderLayout());
        exitGameButton.add(textLabel2, BorderLayout.CENTER);
        exitGameButton.setVisible(true);

        easyButton = new JButton();
        easyButton.setBounds(470, 303, 500, 150);
        easyButton.setIcon(img);
        easyButton.setFont(new Font("times", Font.BOLD, 50));
        easyButton.setForeground(Color.WHITE);
        easyButton.setBorderPainted(true);
        easyButton.setContentAreaFilled(false);
        easyButton.setFocusPainted(false);
        easyButton.setLayout(new BorderLayout());
        easyButton.add(textLabel3, BorderLayout.CENTER);
        easyButton.setVisible(false);

        normalButton = new JButton();
        normalButton.setBounds(470, 526, 500, 150);
        normalButton.setIcon(img);
        normalButton.setFont(new Font("times", Font.BOLD, 50));
        normalButton.setForeground(Color.WHITE);
        normalButton.setBorderPainted(true);
        normalButton.setContentAreaFilled(false);
        normalButton.setFocusPainted(false);
        normalButton.setLayout(new BorderLayout());
        normalButton.add(textLabel4, BorderLayout.CENTER);
        normalButton.setVisible(false);

        chess = new JLabel("Chess Game");
        chess.setFont(new Font("times", Font.BOLD, 80));
        chess.setForeground(Color.WHITE);
        chess.setBounds(470, 35, 600, 150);
        chess.setHorizontalAlignment(JLabel.CENTER);

        select = new JLabel("Select Difficulty");
        select.setBounds(40, 410, 500, 150);
        select.setFont(new Font("times", Font.BOLD, 50));
        select.setForeground(Color.getHSBColor(51, 100, 100));
        select.setVisible(false);

        wButton = new JButton();
        wButton.setBounds(470, 303, 500, 150);
        wButton.setIcon(img);
        wButton.setFont(new Font("times", Font.BOLD, 50));
        wButton.setForeground(Color.WHITE);
        wButton.setBorderPainted(true);
        wButton.setContentAreaFilled(false);
        wButton.setFocusPainted(false);
        wButton.setLayout(new BorderLayout());
        wButton.add(textLabel6, BorderLayout.CENTER);
        wButton.setVisible(false);

        bButton = new JButton();
        bButton.setBounds(470, 526, 500, 150);
        bButton.setIcon(img);
        bButton.setFont(new Font("times", Font.BOLD, 50));
        bButton.setForeground(Color.WHITE);
        bButton.setBorderPainted(true);
        bButton.setContentAreaFilled(false);
        bButton.setFocusPainted(false);
        bButton.setLayout(new BorderLayout());
        bButton.add(textLabel7, BorderLayout.CENTER);
        bButton.setVisible(false);

        /** Add components to a custom JPane2*/
        JPanel contentPane1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics k) {
                super.paintComponent(k);
                k.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane1.setLayout(null);
        contentPane1.add(chess);
        contentPane1.add(select);
        contentPane1.add(startGameButton);
        contentPane1.add(historyButton);
        contentPane1.add(exitGameButton);
        contentPane1.add(easyButton);
        contentPane1.add(normalButton);
        contentPane1.add(wButton);
        contentPane1.add(bButton);


        /** Add ActionListener to the Start Game button*/
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGameButton.setVisible(false);
                historyButton.setVisible(false);
                exitGameButton.setVisible(false);
                easyButton.setVisible(true);
                normalButton.setVisible(true);
                select.setVisible(true);
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HistoryGUI historyGUI = new HistoryGUI();
                historyGUI.setVisible(true);
            }
        });
        /**exit button*/
        exitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); /** Exit the program*/
            }
        });
        /**get easy mode information*/
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGameButton.setVisible(false);
                exitGameButton.setVisible(false);
                easyButton.setVisible(false);
                normalButton.setVisible(false);
                select.setVisible(false);
                wButton.setVisible(true);
                bButton.setVisible(true);
                easy = true;
            }
        });
        /**get normal mode information*/
        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGameButton.setVisible(false);
                exitGameButton.setVisible(false);
                easyButton.setVisible(false);
                normalButton.setVisible(false);
                select.setVisible(false);
                wButton.setVisible(true);
                bButton.setVisible(true);
                easy = false;
            }
        });

        /**select player color as black*/
        bButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessBoard board = new ChessBoard(false, easy);
                board.setVisible(true);
            }
        });
        /**select player color as white*/
        wButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessBoard board = new ChessBoard(true, easy);
                board.setVisible(true);
            }
        });


        setContentPane(contentPane1);
    }


}