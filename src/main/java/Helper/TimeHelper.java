package Helper;

import DB_Model.DBAppointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author Brandon Jones
 * - This class is made up of methods related to time and date validation and conversions.
 */
public abstract class TimeHelper {

    /**
     *
     * @return - The current Date and Time in UTC as a string
     */
    public static String getUTC() {
        LocalDateTime ldt = LocalDateTime.now();
        int offsetHours = -(((TimeZone.getDefault().getRawOffset() / 1000) / 60) / 60);
        LocalDateTime ldtUTC = ldt.plusHours(offsetHours);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ldtUTC.format(dtf);
    }

    /**
     *
     * @param localDate - The localDate object to be converted
     * @param localTime - The localTime object to be converted
     * @return - a LocalDateTime object in UTC
     */
    public static String getUTC(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        int offsetHours = -(((TimeZone.getDefault().getRawOffset() / 1000) / 60) / 60);
        LocalDateTime localDateTimeUTC = localDateTime.plusHours(offsetHours);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTimeUTC.format(dtf);
    }

    /**
     *
     * @return - An observableList of all Months.
     */
    public static ObservableList<String> getMonths() {
        ObservableList<String> allMonths = FXCollections.observableArrayList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
        return allMonths;
    }

    /**
     *
     * @param UTCtime - a dateTime string in UTC
     * @return - a dateTime string in the user's local time.
     */
    public static String getLocal(String UTCtime) {
        //Get Offset
        int offsetHours = (((TimeZone.getDefault().getRawOffset() / 1000) / 60) / 60);

        //Format time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localTime = LocalDateTime.parse(UTCtime, dtf);

        //Apply Offset
        localTime = localTime.plusHours(offsetHours);

        String minute;
        String second;

        //Format the minute and second so it displays as 00 instead of 0 when set to 0
        if (localTime.getMinute() < 10) {
            minute = ("0" + localTime.getMinute());
        }
        else {
            minute = String.valueOf(localTime.getMinute());
        }

        if (localTime.getSecond() < 10) {
            second = ("0" + localTime.getSecond());
        }
        else {
            second = String.valueOf(localTime.getSecond());
        }

        //Format the month so it always displays as the two digit month -- 01, 02...
        String month = String.valueOf(localTime.getMonth().getValue());
        if (month.length() == 1) {
            month = "0" + month;
        }

        //Format the hour so it always displays as the two digit hour
        String hour = String.valueOf(localTime.getHour());
        if (hour.length() == 1) {
            hour = "0" + hour;
        }

        String date = (localTime.getYear() + "-" + month + "-" + localTime.getDayOfMonth());
        String time = (hour + ":" + minute + ":" + second);

        return (date + " " + time);
    }

    /**
     *
     * @param UTCtime A LocalDateTime in UTC
     * @return A LocalDateTime in user's local time
     */
    public static LocalDateTime getLocal(LocalDateTime UTCtime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int offsetHours = (((TimeZone.getDefault().getRawOffset() / 1000) / 60) / 60);
        return UTCtime.plusHours(offsetHours);
    }

    /**
     *
      * @param time - The time to be broken down
     * @param result - The part of the time to be returned, (year, month, day, hour, minute)
     * @return - The selected part of the passed time.
     */
    public static String breakDownTime(String time, String result) {
        //result can be year, month, day, hour, minute
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldtTimeUtc = LocalDateTime.parse(time, yearFormatter);
        int offsetHours = (((TimeZone.getDefault().getRawOffset() / 1000) / 60) / 60);
        LocalDateTime ldtTime = ldtTimeUtc.plusHours(offsetHours);


        if (result.equals("year")) {return String.valueOf(ldtTime.getYear());}
        else if (result.equals("month")) {return String.valueOf(ldtTime.getMonth());}
        else if (result.equals("day")) {return String.valueOf(ldtTime.getDayOfMonth());}
        else if (result.equals("hour")) {return String.valueOf(ldtTime.getHour());}
        else if (result.equals("minute")) {return String.valueOf(ldtTime.getMinute());}
        else { return "Time not found";}
    }

    /**
     *
     * @param time - the time to be checked
     * @return - true if the time is within business hours, false if it is not.
     */
    public static boolean withinBusinessHours(String time) {
        boolean duringBusinessHours = false;
        //Convert time String into LocalDateTime
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(time, dtf);

        //Convert LocalDateTime ldt into Eastern Standard Time
        int offset = (((TimeZone.getTimeZone("America/New_York").getRawOffset() / 1000) / 60) / 60); //Eastern Standard Time is 5 hours behind UTC
        LocalDateTime ldtET = ldt.plusHours(offset);

        //check if this falls between business hours
        int hour = ldtET.getHour();
        if((hour >= 8) && (hour <= 18)) {
            duringBusinessHours = true;
        }
        return duringBusinessHours;
    }

    /**
     *
     * @param appointment the DBAppointments Object that the start time is being extracted from
     * @return true if the appointment starts within 15 minutes, false if the appointment does not.
     */
    public static boolean upcomingAppointment(DBAppointments appointment) {
        //Ensure the start time extracted from appointment is in a standard format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime appointmentStart = LocalDateTime.parse(appointment.getStart(), formatter);

        //get the current time in UTC
        String utcNowString = getUTC();
        LocalDateTime utcNow = LocalDateTime.parse(utcNowString, formatter);


        //If the current time plus 15 minutes is after the start time, return true. (The appointment starts within 15 minutes)
        //Also make sure the appointment is not in the past
        if (utcNow.plusMinutes(15).isAfter(appointmentStart) && (appointmentStart.isAfter(utcNow))) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param time A string holding the appointment time
     * @return - true if the appointment is in the future, false if it is in the past
     */
    public static boolean inPast(String time) {
        boolean inPast = false;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime appointmentStart = LocalDateTime.parse(time, dtf);
        LocalDateTime now = LocalDateTime.parse(TimeHelper.getUTC(), dtf);

        if (appointmentStart.isBefore(now)) {
            inPast = true;
            return inPast;
        }
        else {
            return inPast;
        }
    }
}
