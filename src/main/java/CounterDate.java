import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CounterDate {

    private long workingDays;
    private long leftWorkingDays;
    private long leftWorkingHours;

    public CounterDate(int startYear, int endYear) throws SQLException {
        workingDays = getCountWorkingDaysBetweenTwoYears(startYear, endYear);
        leftWorkingDays = getCountLeftWorkingDaysBetweenTwoYears(startYear, endYear);
        leftWorkingHours = getCountLeftWorkingHours();
    }

    private Calendar createStartDate(int startYear)
    {
        Calendar startDate = new GregorianCalendar(startYear, 01, 01);
        return startDate;
    }

    private Calendar createEndDate(int endYear)
    {
        Calendar endDate = new GregorianCalendar(endYear, 01, 01);
        return endDate;
    }

    private long getCountDaysBetweenDates(Calendar start, Calendar end)
    {
        // получаем разницу в миллисекундах
        long differenceDatesInMillis = end.getTimeInMillis() - start.getTimeInMillis();

        // милисекунды перевести в секунды, секунды в минуты, минуты в часы, часы в дни
        long daysBetweenDates = differenceDatesInMillis / 1000 / 60 / 60 / 24;

        return daysBetweenDates;
    }

    private long getCountWeekendDaysBetweenDates(Calendar start, Calendar end) throws SQLException {
        long weekendDaysCount = 0;

        // дата окончания не учитывает выходной, т.е. не ], а ) ... исправляем это следующим способом
        end.set(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH) + 1);

        if(start.equals(end)) return weekendDaysCount;

        while(start.before(end))
        {
            if(Calendar.SATURDAY == start.get(Calendar.DAY_OF_WEEK) || Calendar.SUNDAY == start.get(Calendar.DAY_OF_WEEK))
            {
                weekendDaysCount++;
            }
            // операция инкремента для дня
            start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH) + 1);
        }

        ResultSet resultSet = DatabaseHandler.getDates();
        while(resultSet.next()) weekendDaysCount++;

        return weekendDaysCount;
    }

    private long getCountWorkingDaysBetweenDates(Calendar start, Calendar end) throws SQLException {
        return getCountDaysBetweenDates(start, end) - getCountWeekendDaysBetweenDates(start, end);
    }

    private long getCountWorkingDaysBetweenTwoYears(int startYear, int endYear) throws SQLException {
        Calendar start = createStartDate(startYear);
        Calendar end = createEndDate(endYear);
        return getCountWorkingDaysBetweenDates(start, end);
    }

    private long getCountLeftWorkingDaysBetweenTwoYears(int startYear, int endYear) throws SQLException {
        Calendar start = createStartDate(startYear);
        Calendar end = createEndDate(endYear);
        Calendar now = new GregorianCalendar();
        return getCountWorkingDaysBetweenDates(now, end);
    }

    private long getCountLeftWorkingHours()
    {
        Calendar now = new GregorianCalendar();
        int hourNow = now.get(Calendar.HOUR_OF_DAY);;
        long leftWorkingHours = 0;
        if(leftWorkingDays > 1)
        {
            long leftWorkingHoursAfterToday = (leftWorkingDays - 1) * 8;
            if(hourNow < 13 && hourNow >= 9) leftWorkingHours = ((13 - hourNow) + 4) + leftWorkingHoursAfterToday;
            else if (hourNow < 18 && hourNow >= 14) leftWorkingHours = (18 - hourNow) + leftWorkingHoursAfterToday;
            else leftWorkingHours = 0;
        }
        else if(leftWorkingDays == 1)
        {
            if(hourNow < 13 && hourNow >= 9) leftWorkingHours = ((13 - hourNow) + 4);
            else if (hourNow < 18 && hourNow >= 14) leftWorkingHours = (18 - hourNow);
            else leftWorkingHours = 0;
        }
        else leftWorkingHours = 0;
        return leftWorkingHours;
    }

    public long getWorkingDays() {
        return workingDays;
    }

    public long getLeftWorkingDays() {
        return leftWorkingDays;
    }

    public long getLeftWorkingHours() {
        return leftWorkingHours;
    }
}
