package services;

public class ClearApplicationService extends Service{

    public ClearApplicationService() {}

    // service functions

    public void completeJob(){
        clearApplication();
    }

    private void clearApplication(){

        authDAO.deleteAll();
        userDAO.deleteAll();
        gameDAO.deleteAll();

    }

}
