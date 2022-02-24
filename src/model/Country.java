package model;

/** Class for Country. Contains constructor, getters, and setters. */
public class Country {
    private int id;
    private String name;

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    /**
     * Override the combobox setItems default of toString in order to get the countryName value.
     * @return value of countryName.
     */
    @Override
    public String toString() { return name; }
}
