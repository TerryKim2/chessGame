package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class chessBoard extends JFrame {

    private JButton startGameButton;
    private JButton exitGameButton;
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;
    private Image backgroundImage;
    private JLabel select;
    private JLabel chess;
    
    public chessBoard() {
        // Set up the JFrame
        setTitle("Chess Lobby");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("C:\\Users\\cubby\\git\\chessGame\\swing\\resources\\button_image.png");
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
        
        JLabel textLabel5 = new JLabel("HARD");
        textLabel5.setFont(new Font("times", Font.BOLD, 50));
        textLabel5.setHorizontalAlignment(JLabel.CENTER);
        textLabel5.setForeground(Color.white);
        
        // Load the background image
        backgroundImage = new ImageIcon("C:\\Users\\cubby\\git\\chessGame\\swing\\resources\\background.jpg").getImage();

        // Create components
        startGameButton = new JButton();
        startGameButton.setBounds(710, 403, 500, 150);
        startGameButton.setIcon(img);
        startGameButton.setFont(new Font("times", Font.BOLD, 50));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setBorderPainted(true);
        startGameButton.setContentAreaFilled(false);
        startGameButton.setFocusPainted(false);
        startGameButton.setLayout(new BorderLayout());
        startGameButton.add(textLabel1, BorderLayout.CENTER);
        
        exitGameButton = new JButton();
        exitGameButton.setBounds(710, 726, 500, 150);
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
        easyButton.setBounds(710, 256, 500, 150);
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
        normalButton.setBounds(710, 506, 500, 150);
        normalButton.setIcon(img);
        normalButton.setFont(new Font("times", Font.BOLD, 50));
        normalButton.setForeground(Color.WHITE);
        normalButton.setBorderPainted(true);
        normalButton.setContentAreaFilled(false);
        normalButton.setFocusPainted(false);
        normalButton.setLayout(new BorderLayout());
        normalButton.add(textLabel4, BorderLayout.CENTER);
        normalButton.setVisible(false);
        
        hardButton = new JButton();
        hardButton.setBounds(710, 756, 500, 150);
        hardButton.setIcon(img);
        hardButton.setFont(new Font("times", Font.BOLD, 50));
        hardButton.setForeground(Color.WHITE);
        hardButton.setBorderPainted(true);
        hardButton.setContentAreaFilled(false);
        hardButton.setFocusPainted(false);
        hardButton.setLayout(new BorderLayout());
        hardButton.add(textLabel5, BorderLayout.CENTER);
        hardButton.setVisible(false);
        
        chess = new JLabel("Chess Game");
        chess.setFont(new Font("times", Font.BOLD, 80));
        chess.setForeground(Color.WHITE);
        chess.setBounds(710, 70, 500, 150);
        chess.setHorizontalAlignment(JLabel.CENTER);
        
        select = new JLabel("Select Difficulty");
        select.setBounds(100, 506, 600, 150);
        select.setFont(new Font("times", Font.BOLD, 70));
        select.setForeground(Color.getHSBColor(51, 100, 100));
        select.setVisible(false);

        
        
        // Add components to a custom JPane2
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
        contentPane1.add(exitGameButton);
        contentPane1.add(easyButton);
        contentPane1.add(normalButton);
        contentPane1.add(hardButton);
        

        // Add ActionListener to the Start Game button
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	startGameButton.setVisible(false);
            	exitGameButton.setVisible(false);
                easyButton.setVisible(true);
                normalButton.setVisible(true);
                hardButton.setVisible(true);
                select.setVisible(true);
            }
        });

        exitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program
            }
        });
        
        /*startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(easyButton);
                startGame(normalButton);
                startGame(hardButton);
            }
        });*/
        
        // Set the custom JPanel as the content pane of the JFrame
        setContentPane(contentPane1);
    }

    private void startGame(JButton difficultyButton) {
        String selectedDifficulty = difficultyButton.getText();
        // TODO: Implement game setup with the selected difficulty and transition to the chess game board
        System.out.println("Starting game with difficulty: " + selectedDifficulty);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chessBoard lobby = new chessBoard();
                lobby.setVisible(true);
            }
        });
    }
}