package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessageByID);
        app.get("/accounts/{account_id}/messages", this::getALLMessagesByUserID);
        app.get("messages", this::getALLMessages);

        return app;
    }

    private void getALLMessages(Context context) throws  JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        context.json(mapper.writeValueAsString(messageService.getALLMessages()));
    }
    
    private void getALLMessagesByUserID(Context context) throws  JsonProcessingException{
        int input_userId = Integer.parseInt(context.pathParam("account_id"));
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Message> aUserMessages = messageService.getALLMessages_byUserID(input_userId);
        context.json(mapper.writeValueAsString(aUserMessages));
    }

    private void updateMessageByID(Context context) throws  JsonProcessingException{
        int input_id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message input_message = mapper.readValue(context.body(), Message.class);
        Message updatedMessage = messageService.updateMessageByID(input_id, input_message.getMessage_text());
        if(updatedMessage == null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    /**
     * This is an deleteMessageByID handler for an endpoint.
     * If an existing message was deleted, return the message otherwise return no body.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByID(Context context) throws  JsonProcessingException{
        int input_id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.getMessageByID(input_id);
        if(message == null){
            context.status(200);
        }else{
            context.json(mapper.writeValueAsString(message));
        }
    }

    /**
     * This is an getMessageByID handler for an endpoint.
     * The response body should contain a JSON representation of the message identified by the message_id. 
     * It is expected for the response body to simply be empty if there is no such message. 
     * The response status should always be 200, which is the default.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByID(Context context) throws  JsonProcessingException{
        int input_id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.getMessageByID(input_id);
        if(message == null){
            context.status(200);
        }else{
            context.json(mapper.writeValueAsString(message));
        }
    }


     /**
     * This is an createMessage handler for an endpoint.
     * As a user, should be able to submit a new post on the endpoint POST localhost:8080/messages. 
     * The request body will contain a JSON representation of a message, 
     * which should be persisted to the database, but will not contain a message_id.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createMessageHandler(Context context) throws  JsonProcessingException {
        /*
        If successful, the response body should contain a JSON of the message, including its message_id. 
        The response status should be 200, which is the default. 
        The new message should be persisted to the database.
        - If the creation of the message is not successful, the response status should be 400. (Client error)
         */
        
        ObjectMapper mapper = new ObjectMapper();
        Message input_message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.createMessage(input_message);
        if(createdMessage==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(createdMessage));
        }
    }


    /**
     * This is an regsiter handler for an endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws  JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.createAccount(account);
        if(createdAccount==null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(createdAccount));
        }
    }

    /**
     * This is an login handler for an endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void loginHandler(Context context) throws  JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.loginAccount(account);
        if(loginAccount==null){
            context.status(401);
        }else{
            context.json(mapper.writeValueAsString(loginAccount));
        }
    }

}