import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {
    @FXML
    private Label leftWorkingDays;
    @FXML
    private Label allWorkingDaysInYear;
    @FXML
    private Label leftWorkingHours;
    @FXML
    private GridPane calendarPanel;
    @FXML
    private GridPane maxCountDaysInMonth;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Label nowYearLabel;
    @FXML
    private Label orderJINR;

    @FXML
    public void initialize() throws SQLException  {
        CounterDate counterDate = new CounterDate(Integer.parseInt(DatabaseHandler.getStartYear()), Integer.parseInt(DatabaseHandler.getStartYear()) + 1);
        allWorkingDaysInYear.setText(String.valueOf(counterDate.getWorkingDays()));
        leftWorkingDays.setText(String.valueOf(counterDate.getLeftWorkingDays()));
        leftWorkingHours.setText(String.valueOf(counterDate.getLeftWorkingHours()));

        fillMaxCountDaysInMonth();
        WorkCalendar.fillCalendar(calendarPanel);

        nowYearLabel.setText(DatabaseHandler.getStartYear());
        orderJINR.setText(DatabaseHandler.getOrderJINR());
    }

    private void fillMaxCountDaysInMonth()
    {
        for(int k = 0; k < 31; k++)
        {
            maxCountDaysInMonth.add(new Label(String.valueOf(k + 1)), k, 0);
        }
    }

    public void showEditWindow(ActionEvent actionEvent)
    {
        try
        {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("edit_window.fxml"));
            stage.setTitle(new String("Редактирование производственного календаря"));
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(menuBar.getScene().getWindow());
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        CounterDate counterDate = new CounterDate(Integer.parseInt(DatabaseHandler.getStartYear()), Integer.parseInt(DatabaseHandler.getStartYear()) + 1);
                        allWorkingDaysInYear.setText(String.valueOf(counterDate.getWorkingDays()));
                        leftWorkingDays.setText(String.valueOf(counterDate.getLeftWorkingDays()));
                        leftWorkingHours.setText(String.valueOf(counterDate.getLeftWorkingHours()));
                        WorkCalendar.fillCalendar(calendarPanel);
                        nowYearLabel.setText(DatabaseHandler.getStartYear());
                        orderJINR.setText(DatabaseHandler.getOrderJINR());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void showAboutWindow(ActionEvent actionEvent) {
        try
        {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("about_window.fxml"));
            stage.setTitle("О разработчике производственного календаря");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(menuBar.getScene().getWindow());
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void closeProgram(ActionEvent actionEvent)
    {
        System.exit(0);
    }
}
