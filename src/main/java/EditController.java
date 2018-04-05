import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EditController {
    @FXML
    private DatePicker chooseHolidayDate;
    @FXML
    private VBox holidaysDeleteBlock;
    @FXML
    private Button deleteDateButton;
    @FXML
    private TextField chooseYearLabel;
    @FXML
    private TextField chooseOrderJINRLabel;

    public static ArrayList<int[]> repositoryHoliday = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        ResultSet resultSet = DatabaseHandler.getDates();
        while(resultSet.next())
        {
            final CheckBox date = new CheckBox(resultSet.getDate("date").toString());
            holidaysDeleteBlock.getChildren().add(date);
        }
    }

    public void addHolidayDay(ActionEvent actionEvent) {
        LocalDate currentDate;
        int month;
        int day;
        int year;

        currentDate = chooseHolidayDate.getValue();
        day = currentDate.getDayOfMonth();
        month = currentDate.getMonthValue();
        year = currentDate.getYear();
        int[] currentDateRepository = {month, day};
        repositoryHoliday.add(currentDateRepository);
        String formatDate = getFormatDateStringFromDate(day, month, year);
        System.out.println(formatDate);
        try {
            DatabaseHandler.putDate(formatDate);
        }
        catch (SQLException e) {
            System.out.println("Не удалось занести дату в базу данных!");
        }

        holidaysDeleteBlock.getChildren().add(new CheckBox(year + "-" + month + "-" + day));
    }

    public void deleteCheckedHolidayDays(ActionEvent actionEvent) throws SQLException {
        for(int i = 0; i < holidaysDeleteBlock.getChildren().size(); i++)
        {
            CheckBox currentCheckBox = (CheckBox) holidaysDeleteBlock.getChildren().get(i);
            if(currentCheckBox.isSelected())
            {
                String date = holidaysDeleteBlock.getChildren().get(i).toString();
                int x = date.indexOf("\'");
                date = date.substring(x + 1, date.length() - 1);
                System.out.println(date);
                String splitDate[] = date.split("-");
                date = getFormatDateStringFromDate(Integer.parseInt(splitDate[2]),
                        Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[0]));
                DatabaseHandler.deleteDate(date);
                holidaysDeleteBlock.getChildren().remove(i--);
            }
        }
    }

    public void changeStartYear(ActionEvent actionEvent) throws SQLException {
        String s = chooseYearLabel.getText();
        if(s.length() > 0) DatabaseHandler.setStartYear(s);
    }

    public void changeOrderJINR(ActionEvent actionEvent) throws SQLException {
        String s = chooseOrderJINRLabel.getText();
        if(s.length() > 0) DatabaseHandler.setOrderJINR(s);
    }

    private String getFormatDateStringFromDate(int day, int month, int year)
    {
        String rightDay = (day < 10) ? "0" + String.valueOf(day) : String.valueOf(day);
        String rightMonth = (month < 10) ? "0" + String.valueOf(month) : String.valueOf(month);
        return year + "-" + rightMonth + "-" + rightDay + " 00:00:00.000";
    }
}
