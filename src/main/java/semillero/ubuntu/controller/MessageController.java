package semillero.ubuntu.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import semillero.ubuntu.entities.Message;
import semillero.ubuntu.service.contract.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.url}/message")
@CrossOrigin(origins= "http://localhost:5173")
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("save/{microentrepreneurshipId}")
    public ResponseEntity<?> saveMessage(@Valid @PathVariable Long microentrepreneurshipId, @RequestBody Message newMessage, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();

            for (FieldError err : result.getFieldErrors()) {
                String s = err.getDefaultMessage();
                errors.add(s);
            }

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Message message = messageService.saveMessage(microentrepreneurshipId, newMessage);
            response.put("message", "Mensaje creado con éxito.");
            response.put("created", message);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getMessage/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long messageId) {
        Message message = messageService.getMessageById(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("getMessagesByMicroentrepreneurshipId/{microentrepreneurshipId}")
    public ResponseEntity<List<Message>> getMessagesByMicroentrepreneurshipId(@PathVariable Long microentrepreneurshipId) {
        return ResponseEntity.ok(messageService.getMessagesByMicroentrepreneurshipId(microentrepreneurshipId));
    }

    @PutMapping("{messageId}/change")
    public ResponseEntity<?> changeManagementStatus(@PathVariable Long messageId) {
        Map<String, Object> response = new HashMap<>();

        try {
            messageService.changeManagementStatus(messageId);
            response.put("message", "Estado del mensaje cambiado con éxito.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllMessages() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Message> messages = messageService.getAllMessages();
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
