package models;




public class Authtoken {

    // instance field
    private String authToken;
    private String username;

    public Authtoken(String myAuthToken, String myUsername){
        authToken = myAuthToken;
        username = myUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
}
