package services.handlers;

import services.LoginService;
import services.LoginService.LoginPackage;

import spark.Request;
import spark.Response;

public class LoginHandler {

    private static LoginService service = new LoginService();
    private static ConvertGson gsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public String loginHandler(Request request,Response response){

        try {

            // Simply just creates a register object and casts it to a register object
            var newObj = gsonConverter.requestToObj(request, Login.class);
            Login newLogin = (Login)newObj;

            // then runs the program through the services and returns the newly created authToken
            LoginPackage newPackage = service.completeJob(newLogin.username, newLogin.password);

            // converts that string in to a response object and returns it
            response.status(200);
            return gsonConverter.objToJson(newPackage);

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.handleException(error,response);
        }
    }

    // this is my private class and it can only be used by my RegisterHandler
    private static class Login {
        public String username,password;
        public Login(String username, String password){
            this.username = username;
            this.password = password;
        }
    }
}
