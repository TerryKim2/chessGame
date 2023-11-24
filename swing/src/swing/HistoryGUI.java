package swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryGUI extends JFrame {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JTable table;

    public HistoryGUI() throws SQLException {
        setTitle("Game History");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        //get data from the games table
        ResultSet rs = DBManager.executeSQLquery("SELECT * FROM games");
        List<Object[]> rows = new ArrayList<Object[]>();
        Object columns[] = {"Date", "Time", "Difficulty Level", "Result"};

        while(rs.next()) {
            Object[] newRow = new Object[4];
            newRow[0] = rs.getString("game_date");
            newRow[1]  = rs.getString("game_time");
            newRow[3] = rs.getString("difficulty");
            newRow[4] = rs.getString("result");
            rows.add(newRow);
        }

        // Creates the game history panel and adds it to the content pane.
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{463, 0, 0};
        gbl_contentPane.rowHeights = new int[] {14, 290, 0, 30, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        // Adds a title to the content pane.
        JLabel lblGameHistory = new JLabel("Game History");
        GridBagConstraints gbc_lblGameHistory = new GridBagConstraints();
        gbc_lblGameHistory.insets = new Insets(0, 0, 5, 0);
        gbc_lblGameHistory.gridx = 0;
        gbc_lblGameHistory.gridy = 0;
        contentPane.add(lblGameHistory, gbc_lblGameHistory);

        scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        contentPane.add(scrollPane, gbc_scrollPane);
        table= new JTable(rows.toArray(new Object[rows.size()][7]), columns);
        scrollPane.setViewportView(table);
    }

}
