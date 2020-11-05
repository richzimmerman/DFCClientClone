package net.projectdf.Account;

public class Character {

    private String name;
    private String characterClass;
    private String gender;
    private int level;

    public Character(String name, String characterClass, String gender, int level) {
        this.name = name;
        this.characterClass = characterClass;
        this.gender = gender;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public String getGender() {
        return gender;
    }
}
