package models;

public class User {

    private String username;
    private String password;
    private String email;

    public User(String myUsername, String myPassword, String myEmail){

        username = myUsername;
        password = myPassword;
        email = myEmail;

    }

    // getter and setter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail(){
        return email;
    }

}
