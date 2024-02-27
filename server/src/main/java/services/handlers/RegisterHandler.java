package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.RegisterService;
import services.RegisterService.registerPackage;

import spark.*;

import models.User;

public class RegisterHandler {

    private static RegisterService service = new RegisterService();
    private static ConvertGson GsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public RegisterHandler() {}

    public String registerHandler(Request request,Response response){

        try {

            // Simply just creates a register object and casts it to a register object
            var newObj = GsonConverter.RequestToObj(request, register.class);
            register newRegister = (register) newObj;

            // then runs the program through the services and returns the newly created authToken
            RegisterService.registerPackage newPackage = service.completeJob(newRegister.username, newRegister.password, newRegister.email);

            // converts that string in to a response object and returns it
            response.status(200);
            return GsonConverter.ObjToJson(newPackage);

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.ExceptionHandler(error,response);
        }
    }

    // this is my private class and it can only be used by my RegisterHandler
    private static class register{
        public String username,password,email;
        public register(String username,String password, String email){
            this.username = username;
            this.password = password;
            this.email = email;
        }

    }

}
