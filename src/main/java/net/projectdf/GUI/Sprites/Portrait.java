package net.projectdf.GUI.Sprites;

public class Portrait {

    private int[] male;
    private int[] female;

    public Portrait(int[] male, int[] female) {
        this.male = male;
        this.female = female;
    }

    public int[] get(String gender) {
        if (gender.toUpperCase().equals("MALE")) {
            return male;
        } else if (gender.toUpperCase().equals("FEMALE")) {
            return female;
        } else {
            throw new IllegalArgumentException("Gender not valid: " + gender);
        }
    }
}
