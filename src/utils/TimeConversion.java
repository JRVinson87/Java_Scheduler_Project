package utils;

import java.time.*;

/** Contains method for time conversion. */
public class TimeConversion {

    /**
     * Converts business American/New_York start/end time to users local time.<br>
     * Accepts system default zoneID. Creates local time variable of business time which is in EST.
     * Creates an American/New_York ZonedDateTime of the business time.
     * Business zoned time is converted to users local date time.
     * @param dzd Users zoneID based on system default.
     * @return Converted local time from business time.
     */
    public static LocalDateTime userLocalTime(ZoneId dzd, int i){

        //setting the business zoneid
        ZoneId zoneEST = ZoneId.of("America/New_York");

        // Company Business operating hours
        LocalDate localDate = LocalDate.now();
        LocalTime localTimeStart = LocalTime.of(i,00);

        //zonedatetime of business in EST
        ZonedDateTime businessZonedDateTime = ZonedDateTime.of(localDate, localTimeStart, zoneEST);

        // zonedatetime of user system default based off business est i.e. est to local
        ZonedDateTime userZonedDateTime = businessZonedDateTime.withZoneSameInstant(dzd);
        //convert user zonedatetime to localtime
        LocalDateTime userLocalDateTime = userZonedDateTime.toLocalDateTime();

        return userLocalDateTime;
    }
}
