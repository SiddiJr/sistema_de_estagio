package sistemadeestagio.Model;

public class userNameSingleton {
    private String username;
    private final static userNameSingleton INSTANCE = new userNameSingleton();

    public static userNameSingleton getInstance() {
        return INSTANCE;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
