package com.File;

/**
 * Abstract class to serialize the objects.
 * Will be extended by most of the components.
 */
public abstract class Serialize {
    /**
     * The method which all the components extending this class will have to override.
     * @param tabSize number of tabs to be indented correctly
     * @return  the string to be written into the file
     */
    public abstract String serialize(int tabSize);

    /**
     * Utility method to add tabs into the string.
     * @param tabSize number of tabs to put inside the string
     * @return number of tabs given by tab size
     */
    public String addTabs(int tabSize) {
        return "\t".repeat(tabSize);        //repeat tabs for tabSize
    }

    /**
     * Utility method to add a string property to the string inside the file.
     * @param name name of the property
     * @param value the string
     * @param tabSize number of tabs
     * @param newline true/false if we want newline or not
     * @param comma true/false if we want comma or not
     * @return the string property to be added.
     */
    public String addStringProperty(String name, String value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + "\"" + value + "\"" + addEnding(newline, comma);
    }

    /**
     * Utility method to add a float property to the string inside the file.
     * @param name name of the property
     * @param value the float value
     * @param tabSize number of tabs
     * @param newline true/false if we want newline or not
     * @param comma true/false if we want comma or not
     * @return the float property to be added.
     */
    public String addFloatProperty(String name, float value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + value + "f" + addEnding(newline, comma);
    }

    /**
     * Utility method to add a double property to the string inside the file.
     * @param name name of the property
     * @param value the double value
     * @param tabSize number of tabs
     * @param newline true/false if we want newline or not
     * @param comma true/false if we want comma or not
     * @return the double property to be added.
     */
    public String addDoubleProperty(String name, double value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newline, comma);
    }

    /**
     * Utility method to add a boolean property to the string inside the file.
     * @param name name of the property
     * @param value the boolean value
     * @param tabSize number of tabs
     * @param newline true/false if we want newline or not
     * @param comma true/false if we want comma or not
     * @return the boolean property to be added.
     */
    public String addBooleanProperty(String name, boolean value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newline, comma);
    }

    /**
     * Utility method to add an int property to the string inside the file.
     * @param name name of the property
     * @param value the int value
     * @param tabSize number of tabs
     * @param newline true/false if we want newline or not
     * @param comma true/false if we want comma or not
     * @return the int property to be added.
     */
    public String addIntProperty(String name, int value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newline, comma);
    }

    /**
     * Utility method to add the beginning of the object.
     * @param name name of the object
     * @param tabSize number of tabs so it will be indented properly.
     * @return the beginning of the object as a string
     */
    public String beginObjectProperty(String name, int tabSize) {
        return addTabs(tabSize) + "\"" + name + "\": {" + addEnding(true, false);
    }

    /**
     * Utility method.
     * @param tabSize number of tabs so it will be indented properly.
     * @return the end of the object propertiy as as tring
     */
    public String closeObjectProperty(int tabSize) {
        return addTabs(tabSize) + "}";
    }

    /**
     * Utility method to add the ending of a property.
     * @param newline true/false if we want newline or not
     * @param comma true/false if we want comma or not
     * @return ending for the property
     */
    public String addEnding(boolean newline, boolean comma) {
        String str = "";
        if(comma) str+=",";
        if(newline) str+="\n";
        return str;
    }
}
