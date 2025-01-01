package Service;

import DAO.messageDAO;
import Model.Message;
import java.util.ArrayList;

public class messageService {
    private messageDAO messageDAO;

    public messageService(){
        this.messageDAO = new messageDAO();
    }

    public messageService(messageDAO m){
        this.messageDAO = m;
    }

    public Message createMessage(Message m){
        return this.messageDAO.createMessage(m);
    }

    public ArrayList<Message> allMessages(){
        return this.messageDAO.allMessages();
    }

    public Message messageByID(int id){
        return this.messageDAO.messageByID(id);
    }

    public Message deleteMessageByID(int id){
        return this.messageDAO.deleteMessageByID(id);
    }

    public Message updateMessage(int id, String newText){
        return this.messageDAO.updateMessage(id, newText);
    }

    public ArrayList<Message> accountMessages(int id){
        return this.messageDAO.accountMessages(id);
    }
    
}
