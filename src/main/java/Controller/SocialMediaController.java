package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.accountService;
import Service.messageService;
import Model.Message;

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
    accountService accountService;
    messageService messageService; 

    public SocialMediaController(){
        this.accountService = new accountService();
        this.messageService = new messageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::register);
        app.post("/login", this::login);
        app.post("/messages", this::messages);
        app.get("/messages", this::allMessages);
        app.get("/messages/{message_id}", this::messageByID);
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::accountMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void register(Context ctx) throws JsonProcessingException{
        
        ObjectMapper m = new ObjectMapper();
        Account a = m.readValue(ctx.body(), Account.class);
        if(a.getPassword().length() >= 4 && a.getUsername() != ""){
            Account b = accountService.addAccount(a);
            if(b != null){
                System.out.println(b.getUsername());
                ctx.json(m.writeValueAsString(b));
                ctx.status(200);
            }
            else{
                ctx.status(400);
            }
        }
        else{
            ctx.status(400);
        }
    }

    private void login(Context ctx) throws JsonProcessingException{
        ObjectMapper m = new ObjectMapper();
        
        Account t = m.readValue(ctx.body(), Account.class);
        Account re = accountService.checkAccount(t);
        if(re != null){
            ctx.json(m.writeValueAsString(re));
            ctx.status(200);
        }
        else{
            ctx.status(401);
        }
    }

    private void messages(Context ctx) throws JsonProcessingException{
        ObjectMapper m = new ObjectMapper();

        Message me = m.readValue(ctx.body(), Message.class);
        //check to see if message is by a valid user
        Account user = accountService.getAccountByID(me.posted_by);
        if(user == null){
            ctx.status(400);
        }
        else{
            Message mes = messageService.createMessage(me);
            if(mes != null){
                ctx.json(m.writeValueAsString(mes));
                ctx.status(200);
            }
            else{
                ctx.status(400);
            }
        }

    }

    private void allMessages(Context ctx) throws JsonProcessingException{
        
        ObjectMapper m = new ObjectMapper();

        ArrayList<Message> allMessage = messageService.allMessages();
        ctx.json(m.writeValueAsString(allMessage));
        ctx.status(200);
    }

    private void messageByID(Context ctx) throws JsonProcessingException{
        ObjectMapper m = new ObjectMapper();

        Message rm = messageService.messageByID(Integer.parseInt(ctx.pathParam("message_id")));
        if(rm != null){
            ctx.json(m.writeValueAsString(rm));
        }
        ctx.status(200);
    }

    private void deleteMessageByID(Context ctx) throws JsonProcessingException{
        ObjectMapper m = new ObjectMapper();

        Message rm = messageService.deleteMessageByID(Integer.parseInt(ctx.pathParam("message_id")));
        if(rm != null){
            ctx.json(m.writeValueAsString(rm));
        }
        ctx.status(200);
    }

    private void updateMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper m = new ObjectMapper();

        //There must be an easier way of reading the value part of the string but I drove my self up a wall trying to figure it out so I just split it up manually
        
        //check to make sure the new message text is valid
        if(ctx.body().length() > 0 && ctx.body().length() < 256){
            String json = ctx.body();
            String[] split1 = json.split(":");

            //System.out.println(split1[1]);
            String[] split2 = split1[1].split("\"");

            //System.out.println(split2[1]);
            String mes = split2[1];
            if(mes.length() > 0){
                Message rm = messageService.updateMessage(Integer.parseInt(ctx.pathParam("message_id")), mes);
                if(rm != null){
                    ctx.json(m.writeValueAsString(rm));
                    ctx.status(200);
                }
                else{
                    ctx.status(400);
                }
            }
            else{
                ctx.status(400);
            }

        }
        else{
            ctx.status(400);
        }
    }

    private void accountMessages(Context ctx) throws JsonProcessingException{
        
        ObjectMapper m = new ObjectMapper();
        System.out.println("start");
        ArrayList<Message> accountMessages = messageService.accountMessages(Integer.parseInt(ctx.pathParam("account_id")));
        ctx.json(m.writeValueAsString(accountMessages));
        ctx.status(200);
        System.out.println("done");
    }


}