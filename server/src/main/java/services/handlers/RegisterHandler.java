package services.handlers;

import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import services.RegisterService;
import services.RegisterService.RegisterPackage;

import spark.*;

public class RegisterHandler {

    private static RegisterService service = new RegisterService();
    private static ConvertGson gsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public RegisterHandler(){}

    public String registerHandler(Request request,Response response){

        try {

            // Simply just creates a register object and casts it to a register object

            System.out.println(request.body());

            var newObj = gsonConverter.requestToObj(request, Register.class);
            Register newRegister = (Register) newObj;

            // checks to see if the user gave valid input


            // then runs the program through the services and returns the newly created authToken
            RegisterPackage newPackage = service.completeJob(newRegister.username, newRegister.password, newRegister.email);

            // converts that string in to a response object and returns it
            response.status(200);
            return gsonConverter.objToJson(newPackage);

        }catch(JsonSyntaxException err){
            return exceptionHandler.jsonException(response);
        }catch(DataAccessException err) {
            return exceptionHandler.handleException(err,response);
        }
    }

    // this is my private class and it can only be used by my RegisterHandler
    private static class Register {
        public String username,password,email;
        public Register(String username, String password, String email){
            this.username = username;
            this.password = password;
            this.email = email;
        }
    }
}
