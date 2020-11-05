package net.projectdf.Account;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Account {

    private static Account instance;

    private List<Character> accountCharacters = new ArrayList<>();
//    private static String testjson = "{\"characters\": [{\"name\": \"Mrbagginz\",\"class\": \"Archon\",\"level\": \"210\",\"gender\": \"M\"},{\"name\": \"Riel\",\"class\": \"Werewolf\",\"level\": \"19\",\"gender\": \"M\"},{\"name\": \"Riellz\",\"class\": \"Wizard\",\"level\": \"2\",\"gender\": \"M\"},{\"name\": \"Merpstein\",\"class\": \"Skeleton\",\"level\": \"1\",\"gender\": \"M\"},{\"name\": \"Merfstein\",\"class\": \"Werewolf\",\"level\": \"1\",\"gender\": \"M\"},],\"characterCount\": 5}\n";

    public Account() {

    }

    public static Account getInstance() {
        if (instance == null) {
            instance = new Account();
        }
        return instance;
    }

    public void setAccountData(String json) {
        JSONObject JSON = new JSONObject(json);
        int characterCount = JSON.getInt("characterCount");
        JSONArray characters = JSON.getJSONArray("characters");
        for (int i = 0; i < characterCount; i++) {
            JSONObject c = (JSONObject) characters.get(i);
            String g = c.getString("gender");
            String gender;
            if (g.equals("M")) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            String name = c.getString("name");
            String charClass = c.getString("class");
            int level = c.getInt("level");

            Character character = new CharacterBuilder().name(name).gender(gender).characterClass(charClass)
                    .level(level).buildCharacter();

            accountCharacters.add(character);
        }
    }

    public List<Character> getAccountCharacters() {
        return accountCharacters;
    }
}
