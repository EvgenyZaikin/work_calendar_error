import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class WorkCalendar {
    private static Calendar calendar;
    private static int[] countDaysInMonth;

    public static void fillCalendar(GridPane calendarPane) throws SQLException {
        calendar = Calendar.getInstance();
        countDaysInMonth = new int[12];
        fillArrayCountDaysInMonth(countDaysInMonth, calendar);
        fillWorkCalendarGridPane(calendarPane, calendar, countDaysInMonth);
    }

    private static void fillArrayCountDaysInMonth(int[] array, Calendar calendar)
    {
        for(int i = 0; i < array.length; i++)
        {
            calendar.set(Calendar.MONTH, i);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            array[i] = maxDay;
        }
    }

    private static void fillWorkCalendarGridPane(GridPane calendarPanel, Calendar c, int[] array) throws SQLException {
        for(int i = 0; i < 12; i++)
        {
            for(int j = 1; j <= 31; j++)
            {
                if(j <= array[i])
                {
                    c.set(Calendar.MONTH, i);
                    c.set(Calendar.DAY_OF_MONTH, j);
                    // j - 1 потому что мы дни считаем с единицы, а колонки в панели с 0
                    if(c.get(Calendar.DAY_OF_WEEK) == 1 || c.get(Calendar.DAY_OF_WEEK) == 7)
                    {
                        calendarPanel.add(createLabelForPutInCalendarPanel(Color.GREEN), j - 1, i);
                    }
                    else
                    {
                        calendarPanel.add(createLabelForPutInCalendarPanel(Color.WHITE), j - 1, i);
                    }

                    for(int[] a : DatabaseHandler.getDatesForFillWorkCalendar())
                    {
                        if(a[0] == i - 1 && a[1] == j)
                        {
                            calendarPanel.add(createLabelForPutInCalendarPanel(Color.GREEN), j - 1, i - 2);
                        }
                    }

                    Calendar nowDate = new GregorianCalendar();
                    int nowMonth = nowDate.get(Calendar.MONTH);
                    int nowDay = nowDate.get(Calendar.DAY_OF_MONTH);
                    if(nowMonth == i && nowDay == j)
                    {
                        Label l = new Label();
                        l.setText("C");
                        l.setPadding(new Insets(0, 0, 0, 6));
                        calendarPanel.add(l, j - 1, i);
                    }
                }
                else
                {
                    Label l = new Label();
                    l.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(1), null)));
                    l.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    calendarPanel.add(l, j - 1, i);
                }
            }
        }
    }

    private static Label createLabelForPutInCalendarPanel(Color color)
    {
        Label l = new Label();
        l.setBackground(new Background(new BackgroundFill(color, new CornerRadii(1), null)));
        l.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        l.setStyle("-fx-border-color: grey;");
        return l;
    }
}