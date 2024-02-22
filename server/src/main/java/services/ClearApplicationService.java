package services;

public class ClearApplicationService extends Service{

    public ClearApplicationService() {}

    // service functions

    public void completeJob(){
        clearApplication();
    }

    private void clearApplication(){

        AuthDAO.deleteAll();
        UserDAO.deleteAll();
        GameDAO.deleteAll();

    }

}
