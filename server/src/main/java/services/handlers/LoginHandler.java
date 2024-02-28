package services.handlers;

import services.LoginService;
import services.LoginService.loginPackage;

import spark.Request;
import spark.Response;

public class LoginHandler {

    private static LoginService service = new LoginService();
    private static ConvertGson GsonConverter = new ConvertGson();
    private static ExceptionHandler exceptionHandler = new ExceptionHandler();

    public String loginHandler(Request request,Response response){

        try {

            // Simply just creates a register object and casts it to a register object
            var newObj = GsonConverter.RequestToObj(request, LoginHandler.login.class);
            LoginHandler.login newLogin = (LoginHandler.login) newObj;

            // then runs the program through the services and returns the newly created authToken
            LoginService.loginPackage newPackage = service.completeJob(newLogin.username, newLogin.password);

            // converts that string in to a response object and returns it
            response.status(200);
            return GsonConverter.ObjToJson(newPackage);

        }catch(Exception error) {

            // catches error and returns that
            return exceptionHandler.ExceptionHandler(error,response);
        }
    }

    // this is my private class and it can only be used by my RegisterHandler
    private static class login{
        public String username,password;
        public login(String username,String password){
            this.username = username;
            this.password = password;
        }
    }
}
