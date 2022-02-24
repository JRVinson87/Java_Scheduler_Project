package utils;

import javafx.scene.control.Alert;

/** Holds methods for user input validation. */
public class InputValidation {

    /**
     * Checks each textfield to make sure they are not empty and alerts the user if they are.
     * @param name textfield to check.
     * @param address textfield to check.
     * @param phone textfield to check.
     * @param post textfield to check.
     * @return fail is any field is blank.
     */
    public static boolean checkStringCustomer(String name, String address, String phone, String post) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            errorWindow("Name");
            return false;
        }
        if (address == null || address.isBlank() || address.isEmpty()) {
            errorWindow("Address");
            return false;
        }
        if (phone == null || phone.isBlank() || phone.isEmpty()) {
            errorWindow("Phone");
            return false;
        }
        if (post == null || post.isBlank() || post.isEmpty()) {
            errorWindow("Post");
            return false;
        }
        else { return true; }
    }

    /**
     * Checks each textfield to see if it's blank and checks String length of description for database VARCHAR50 limit.
     * @param title textfield to check.
     * @param type textfield to check.
     * @param desc textfield to check.
     * @param location textfield to check.
     * @return fail if a text field is blank or description is > 50 characters.
     */
    public static boolean checkStringApp(String title, String type, String desc, String location) {
        if (title == null || title.isBlank() || title.isEmpty()) {
            errorWindow("Title");
            return false;
        }
        if (type == null || type.isBlank() || type.isEmpty()) {
            errorWindow("Type");
            return false;
        }
        if (desc == null || desc.isBlank() || desc.isEmpty()) {
            errorWindow("Description");
            return false;
        }
        if (location == null || location.isBlank() || location.isEmpty()) {
            errorWindow("Location");
            return false;
        }
        if (desc.length() > 50){
            Alert l = new Alert(Alert.AlertType.ERROR);
            l.setContentText("Your description is too long. Maximum of 50 characters allowed.");
            l.showAndWait();
            return false;
        }
        else { return true; }
    }

    /**
     * Error window template for class methods.
     * @param s textfield that failed the check.
     */
    private static void errorWindow (String s){
        Alert invalid = new Alert(Alert.AlertType.ERROR);
        invalid.setTitle("Invalid Entry.");
        invalid.setContentText(s + " field is blank. Please fill in all fields.");
        invalid.showAndWait();
    }
}
