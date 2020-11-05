package net.projectdf.Account;

public class CharacterBuilder {

    private String name = "";
    private String characterClass = "";
    private String gender = "Male";
    private int level = 1;

    public CharacterBuilder() {

    }

    public Character buildCharacter() {
        return new Character(name, characterClass, gender, level);
    }

    public CharacterBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CharacterBuilder characterClass(String characterClass) {
        this.characterClass = characterClass;
        return this;
    }

    public CharacterBuilder gender(String gender) {
        this.gender = gender;
        return this;
    }

    public CharacterBuilder level(int level) {
        this.level = level;
        return this;
    }
}
