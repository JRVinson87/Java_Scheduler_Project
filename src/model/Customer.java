package model;

/** Class for Customer. Contains constructor, getters, and setters. */
public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createBy;
    private String updateDate;
    private String updateBy;
    private String division;
    private String country;

    public Customer(int id, String name, String address, String postalCode, String division, String country, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.division =division;
        this.country = country;
        this.phone = phone;
    }

    public void setCreateDate(String createDate) { this.createDate = createDate; }

    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }

    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getAddress() { return address; }

    public String getPostalCode() { return postalCode; }

    public String getDivision() { return division; }

    public String getCountry() { return country; }

    public String getPhone() { return phone; }
}
