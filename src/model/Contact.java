package model;

/** Class for Contacts. Contains constructor, getters, and setters. */
public class Contact {

    private int id;
    private String name;
    private String email;

    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    /**
     * Override the combobox setItems default of toString in order to get the contact name value.
     * @return value of contact name.
     */
    @Override
    public String toString() { return "[" + id + "]" + " " + name; }
}
