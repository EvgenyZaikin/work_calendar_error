import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static void Connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:workCalendarDatabase.s3db");
        statement = connection.createStatement();
    }

    public static ResultSet getDates() throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM holidays");
        return resultSet;
    }

    public static ArrayList<int[]> getDatesForFillWorkCalendar() throws SQLException {
        ArrayList<int[]> a = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT * FROM holidays");
        while(resultSet.next())
        {
            String date = resultSet.getDate("date").toString();
            String[] splitDate = date.split("-");
            int[] currentDate = new int[2];
            currentDate[0] = Integer.parseInt(splitDate[1]);
            currentDate[1] = Integer.parseInt(splitDate[2]);
            a.add(currentDate);
        }
        return a;
    }

    public static void putDate(String formatDate) throws SQLException {
        statement.execute("INSERT INTO holidays VALUES(NULL, '" + formatDate + "')");
    }

    public static void deleteDate(String formatDate) throws SQLException {
        statement.execute("DELETE FROM holidays WHERE date = '" + formatDate + "'");
    }

    public static String getStartYear() throws SQLException {
        String s;
        resultSet = statement.executeQuery("SELECT value FROM helpInformation WHERE title = 'startYear'");
        if(resultSet.next()) return s = resultSet.getString("value");
        else return s = "";
    }

    public static String getOrderJINR() throws SQLException {
        resultSet = statement.executeQuery("SELECT value FROM helpInformation WHERE title = 'orderJINR'");
        String s;
        if(resultSet.next()) return s = resultSet.getString("value");
        else return s = "";
    }

    public static void setStartYear(String startYear) throws SQLException {
        statement.executeUpdate("UPDATE helpInformation SET value = '" + startYear + "' WHERE title = 'startYear'");
    }

    public static void setOrderJINR(String orderJINR) throws SQLException{
        statement.executeUpdate("UPDATE helpInformation SET value = '" + orderJINR + "' WHERE title = 'orderJINR'");
    }
}
