package models;




public class AuthData {

    // instance field
    private String authToken;
    private String username;

    public AuthData(String myAuthToken, String myUsername){
        authToken = myAuthToken;
        username = myUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

}
