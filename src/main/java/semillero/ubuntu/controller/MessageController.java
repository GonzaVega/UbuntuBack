package semillero.ubuntu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semillero.ubuntu.entities.Message;
import semillero.ubuntu.service.contract.MessageService;

@RestController
@RequestMapping("/api/v2/")
public class MessageController {

    @Autowired
    MessageService messageService;

    public ResponseEntity<Message> saveMessage(@RequestBody Message newMessage) {
        return new ResponseEntity<>(messageService.saveMessage(newMessage), HttpStatus.CREATED);
    }


}
