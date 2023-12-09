package swing;

import java.sql.*;

public class DBManager {
    private static final String JDBC_URL = "jdbc:sqlite:chessdb.sqlite";
    private static Connection connection;
    private static Statement statement;

    private DBManager(){}

    public static void initializeDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(JDBC_URL);
            statement = connection.createStatement();

            // Create table if it doesn't exist
            statement.execute("CREATE TABLE IF NOT EXISTS games (id INTEGER PRIMARY KEY AUTOINCREMENT, game_date DATE, game_time TIME, difficulty TEXT NOT NULL, result TEXT NOT NULL)");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ResultSet executeSQLquery(String query) throws SQLException {
        if (connection == null) initializeDB();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }




}
