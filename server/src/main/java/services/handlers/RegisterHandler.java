package services.handlers;

import services.RegisterService;
import services.RegisterService.RegisterPackage;

import spark.*;

public class RegisterHandler {

    private static RegisterService service = new RegisterService();
    private static ConvertGson GsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionDecider= new ExceptionHandler();

    public RegisterHandler(){}

    public String registerHandler(Request request,Response response){

        try {

            // Simply just creates a register object and casts it to a register object
            var newObj = GsonConverter.RequestToObj(request, register.class);
            register newRegister = (register) newObj;

            // then runs the program through the services and returns the newly created authToken
            RegisterPackage newPackage = service.completeJob(newRegister.username, newRegister.password, newRegister.email);

            // converts that string in to a response object and returns it
            response.status(200);
            return GsonConverter.ObjToJson(newPackage);

        }catch(Exception error){

            // if an error is detected, it will pass it to an exception handler which will serialize it and
            // handle the status of the response

            return exceptionDecider.ExceptionHandler(error,response);
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
