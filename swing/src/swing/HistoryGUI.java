package swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Sets up a user interface for the history screen
 * @author sofiiarudenka
 */
public class HistoryGUI extends JFrame {
    private JButton backButton;
    private Image backgroundImage;
    private JPanel contentPane;
    private JLabel history;
    private JScrollPane scrollPane;
    private JTable table;

    /**
     * sets up a history screen with the table of game results
     * including date, time, difficulty and game result
     * and provides a back button to go back to Chess lobby screen
     */
    public HistoryGUI() {
        setTitle("Game History");
        setSize(1440, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources\\button_image.png"));
        //ImageIcon img = new ImageIcon("swing/resources/button_image.png");
        JLabel textLabel1 = new JLabel("BACK");
        textLabel1.setFont(new Font("times", Font.BOLD, 30));
        textLabel1.setHorizontalAlignment(JLabel.CENTER);
        textLabel1.setForeground(Color.white);

        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("\\resources\\background.jpg")).getImage();
        //backgroundImage = new ImageIcon("swing/resources/background.jpg").getImage();

        history = new JLabel("Game History");
        history.setFont(new Font("times", Font.BOLD, 65));
        history.setForeground(Color.WHITE);
        history.setBounds(410, 70, 500, 150);
        history.setHorizontalAlignment(JLabel.CENTER);

        backButton = new JButton();
        backButton.setBounds(450, 600, 350, 100);
        backButton.setIcon(img);
        backButton.setFont(new Font("times", Font.BOLD, 40));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(true);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setLayout(new BorderLayout());
        backButton.add(textLabel1, BorderLayout.CENTER);

        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics k) {
                super.paintComponent(k);
                k.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(null);
        contentPane.add(history);
        contentPane.add(backButton);
        contentPane.setBorder(new EmptyBorder(50, 200, 50, 200));



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChessLobby lobby = new ChessLobby();
                lobby.setVisible(true);
            }
        });



        //get data from the games history file
        Object columns[] = {"Date", "Time", "Difficulty Level", "Result"};
        List<Object[]> rows = new ArrayList<Object[]>();
        try {
            FileReader reader = new FileReader("GameHistory.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                Object[] newRow = line.split(" ");
                rows.add(newRow);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Creates the game history panel and adds it to the content pane.
        GridBagLayout gbl_contentPane = new GridBagLayout();

        gbl_contentPane.columnWidths = new int[]{600, 0, 0};
        gbl_contentPane.rowHeights = new int[] {14, 290, 0, 30, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        // Adds a title to the content pane.
        GridBagConstraints gbc_lblGameHistory = new GridBagConstraints();
        gbc_lblGameHistory.insets = new Insets(150, 300, 0, 300);
        gbc_lblGameHistory.gridx = 0;
        gbc_lblGameHistory.gridy = 0;
        contentPane.add(history, gbc_lblGameHistory);

        scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        contentPane.add(scrollPane, gbc_scrollPane);
        table= new JTable(rows.toArray(new Object[rows.size()][7]), columns);
        scrollPane.setViewportView(table);

        setContentPane(contentPane);

    }

}
