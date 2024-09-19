package Service;

import java.util.ArrayList;

import DAO.MessageDAO;
import Model.Message;
import Model.Account;

public class MessageService {
    
    MessageDAO messageDAO;
    AccountService accountService;

    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message){        
        String message_text = message.getMessage_text();
        int posted_by = message.getPosted_by();
        /*
         * The creation of the message will be successful if and only if 
            1. the message_text is not blank, 
            2. is not over 255 characters, and 
         */
        if(message_text.isEmpty() || message_text == null || message_text.length() > 255){
            System.out.println("MessageService.createMessage: The input message is over 255 or blank.");
            return null;
        }
        //3. posted_by refers to a real, existing user. 
        Account posted_by_account = accountService.get_account_byID(posted_by);
        if(posted_by_account == null){
            System.out.println("MessageService.createMessage: The input message withou valid a user.");
            return null;
        }
        
        return this.messageDAO.createMessage(message);
    }

   public Message getMessageByID(int id){
        Message getMessage = messageDAO.getMessage_byID(id);

        if(getMessage == null){
            return null;
        }else{
            return getMessage;
        }
   }

   public ArrayList<Message> getALLMessages(){
        return messageDAO.getALLMessages();
   }

    public ArrayList<Message> getALLMessages_byUserID(int userId){
        Account theAccount = accountService.get_account_byID(userId);
        if(theAccount == null){
            return new ArrayList<>();
        }
        return messageDAO.getALLMessages_byUserID(userId);
    }

    public Message deleteMessageByID(int id){
        Message getMessage = messageDAO.deleteMessage_byID(id);

        if(getMessage == null){
            return null;
        }else{
            return getMessage;
        }
    }

    public Message updateMessageByID(int id, String newMessage){
        if(id<0 || newMessage.length()<=0 || newMessage == null || newMessage.length()>255){
            return null;
        }
        Message updatedMessage = messageDAO.updateMessage_byID(id, newMessage);
        if(updatedMessage == null){
            return null;
        }else{
            return updatedMessage;
        }
    }
}
