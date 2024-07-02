package Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Message;
import Model.Account;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import java.util.ArrayList;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private AccountService accountService;
    private MessageService messageService;
    
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this:: getAllMessagesByUserHandler);
        
        return app;
    }
    /*
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.register(account);

        if (createdAccount != null) {
            context.json(createdAccount);  
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account authenticatedAccount = accountService.login(account.getUsername(), account.getPassword());

        if (authenticatedAccount != null) {
            context.json(authenticatedAccount);     
        } else {
            context.status(401);
        }
    }
    private void postMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        
        Message postMessage = messageService.createMessage(message);

        if(postMessage != null){
            context.json(postMessage);
        }else{
            context.status(400);
        }

    }

    private void getAllMessagesHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if(message != null){
            context.json(message);
        }else {
            context.status(200);
        }
        
    }

    private void deleteMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);
        if (deletedMessage != null) {
            context.json(deletedMessage);
        } else {
            context.status(200).result(""); // Return 200 with an empty body if the message didn't exist
        }
    }

    private void updateMessageByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageById(messageId, message);
        
        if (updatedMessage != null) {
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesByUserHandler(Context context){
        int userId = Integer.parseInt(context.pathParam("account_id"));
        System.out.println(userId);
        List<Message> messages = messageService.getAllUserMessages(userId);
        System.out.println(messages);
        context.json(messages);
        // if (messages != null) {
        //     context.json(messages);
        // } else {
        //     context.status(200);
        // }
    }

}