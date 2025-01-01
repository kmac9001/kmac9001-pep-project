package Service;

import DAO.accountDAO;
import Model.Account;

public class accountService {
    private accountDAO accountDAO;

    public accountService(){
        this.accountDAO = new accountDAO();
    }

    public accountService(accountDAO a){
        this.accountDAO = a;
    }

    public Account addAccount(Account a){
        return this.accountDAO.insertAccount(a);
    }

    public Account checkAccount(Account a){
        return this.accountDAO.checkAccount(a);
    }

    public Account getAccountByID(int id){
        return this.accountDAO.getAccountByID(id);
    }
}
