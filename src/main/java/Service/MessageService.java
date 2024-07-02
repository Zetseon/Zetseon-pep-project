package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.ArrayList;
import java.util.List;
import Model.Account;
import DAO.AccountDAO;

public class MessageService {
    private MessageDAO messageDao;
    private AccountDAO accountDao;
    
    public MessageService(){
        messageDao = new MessageDAO();
        accountDao = new AccountDAO();
    }

    public Message createMessage(Message message){
        if(message.getMessage_text() == null || message.getMessage_text().isBlank() ||
            message.getMessage_text().length() > 255){
                return null;
            }
        Account account = accountDao.getUserByAccountId(message.getPosted_by());
        if(account == null){
            return null;
        }
        
        return messageDao.createMessage(message);

    }

    public List<Message> getAllMessages(){
        return messageDao.getAllMessages();
    }
    
    public Message getMessageById(int messageId){
        return messageDao.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        Message message = messageDao.getMessageById(messageId);
        if (message != null) {
            boolean isDeleted = messageDao.deleteMessageById(messageId);
            if (isDeleted) {
                return message;
            }
        }
        return null;
    }
    
    public Message updateMessageById(int messageId, Message message){
        System.out.println("MESSAGE: " + message.getMessage_text());
        String newText = message.getMessage_text();
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }
        Message existingMessage = messageDao.getMessageById(messageId);
        if (existingMessage != null) {
            boolean isUpdated = messageDao.updateMessageById(messageId, newText);
            if (isUpdated) {
                existingMessage.setMessage_text(newText);
                return existingMessage;
            }
        }
        return null;
    }

    public List<Message> getAllUserMessages(int userId){
        return messageDao.getAllMessagesByUser(userId);
    }

}
