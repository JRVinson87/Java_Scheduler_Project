package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.Locale;

/** Class for Appointment. Contains constructor, getters, and setters. */
public class Appointment {

    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private Timestamp createDate;
    private String createBy;
    private Timestamp updateDate;
    private String updateBy;
    private int customerId;
    private int userId;
    private int contactId;

    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                       int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getLocation() { return location; }

    public String getType() { return type; }

    public LocalDateTime getStart() { return start; }

    public LocalDateTime getEnd() { return end; }

    public Timestamp getCreateDate() { return createDate; }

    public String getCreateBy() { return createBy; }

    public Timestamp getUpdateDate() { return updateDate; }

    public String getUpdateBy() { return updateBy; }

    public int getCustomerId() { return customerId; }

    public int getUserId() { return userId; }

    public int getContactId() { return contactId; }

    /**
     * Converts the start LocalDateTime to just the date.
     * @return the start LocalDate.
     */
    public LocalDate getStartDate() { return start.toLocalDate(); }

    /**
     * Converts the end LocalDateTime to just the date.
     * @return the end LocalDate.
     */
    public LocalDate getEndDate() { return end.toLocalDate(); }

    /**
     * Converts the start LocalDateTime to just the time.
     * @return the start LocalTime.
     */
    public LocalTime getStartTime() { return start.toLocalTime(); }

    /**
     * Converts the end LocalDateTime to just the time.
     * @return the end LocalTime.
     */
    public LocalTime getEndTime() { return end.toLocalTime(); }

    /**
     * Obtains the month of an appointment date.
     * @return the month of selected appointment.
     */
    public Month getStartMonth() {
        Month mon = start.getMonth();
        return mon;
    }

    /**
     * Obtains the week of year number for an appointment date.
     * @return week of year number.
     */
    public int getWeek() {
        WeekFields week = WeekFields.of(Locale.getDefault());
        int weekNum = start.get(week.weekOfWeekBasedYear());
        return weekNum;
    }

    /**
     * Overrides what is returned by the toString to the type.
     * @return String of the type.
     */
    @Override
    public String toString(){ return type; }
}
