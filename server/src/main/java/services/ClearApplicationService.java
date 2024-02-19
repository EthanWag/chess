package services;

public class ClearApplicationService extends Service{

    public ClearApplicationService() {}

    // service functions

    public boolean completeJob(){
        return clearApplication();
    }

    private boolean clearApplication(){

        AuthDAO.deleteAll();
        UserDAO.deleteAll();
        GameDAO.deleteAll();
        return true;

    }

}

// complete for now
