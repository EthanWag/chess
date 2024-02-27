package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.RegisterService;

import spark.*;

import models.User;

public class RegisterHandler {

    private static RegisterService service = new RegisterService();
    private static ConvertGson GsonConverter = new ConvertGson();

    public RegisterHandler() {}

    public String registerHandler(Request request){

        try {

            // Simply just creates a register object and casts it to a register object
            var newObj = GsonConverter.RequestToObj(request, register.class);
            register newRegister = (register) newObj;

            // then runs the program through the services and returns the newly created authToken
            String newAuthToken = service.completeJob(newRegister.username, newRegister.password, newRegister.email);

            // converts that string in to a response object and returns it
            return GsonConverter.ObjToJson(newAuthToken);

        }catch(Exception error){

            // So this code catches the error thrown and prints off a message currently that checks to which kind
            // error was thrown
            // NOTE: you will want to handle these Exception another way
            String errorType = error.getClass().getName();

            switch(errorType){

                case "DataAccessException":
                    System.out.println("This is a DataAccessException");
                    break;
                case "JsonSyntaxException":
                    System.out.println("This is a JsonSyntaxException");
                    break;
                default:
                    System.out.println(errorType);
                    System.out.println("unknown error");
            }

            return null;

        }
    }

    // this is my private class and it can only be used by my RegisterHandler
    private class register{
        public String username,password,email;
        public register(String username,String password, String email){
            this.username = username;
            this.password = password;
            this.email = email;
        }

    }

}
