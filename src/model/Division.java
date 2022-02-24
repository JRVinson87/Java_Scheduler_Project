package model;

import java.time.LocalDateTime;

/** Class for Division. Contains constructor, getters, and setters. */
public class Division {

    private int id;
    private String division;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int countryId;

    public Division(int id, String division, LocalDateTime created, LocalDateTime updated, int countryId) {
        this.id = id;
        this.division = division;
        this.created = created;
        this.updated = updated;
        this.countryId = countryId;
    }

    public int getId() { return id; }

    public String getDivision() {
        return division;
    }

    public LocalDateTime getUpdated() { return updated; }

    public int getCountryId() { return countryId; }

    public LocalDateTime getCreated() { return created; }

    /**
     * Override the combobox setItems default of toString in order to get the countryName value.
     * @return value of countryName.
     */
    @Override
    public String toString() { return division; }
}
