package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WsController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String receiveMessage(@Payload String string){
        System.out.println("Receive message realtime");
        simpMessagingTemplate.convertAndSend("/topic/messagess", "response: " + string);
        return "data: " + string;
    }
}