package semillero.ubuntu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Message;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.entities.UserEntity;
import semillero.ubuntu.enums.Management;
import semillero.ubuntu.repository.MessageRepository;
import semillero.ubuntu.repository.MicroentrepreneurshipRepository;
import semillero.ubuntu.service.contract.MessageService;
import semillero.ubuntu.service.impl.EmailSender;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private MicroentrepreneurshipRepository microentrepreneurshipRepository;

    @Autowired
    private EmailSender emailService;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Message saveMessage(Long microentrepreneurshipId, Message message) {
        logger.info("Save Message");

        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findById(microentrepreneurshipId)
                .orElseThrow(() -> new EntityNotFoundException("The microentrepreneirship with the ID: " + microentrepreneurshipId + " was not found."));

        // Asocia el mensaje con el microemprendimiento
        message.setMicroentrepreneurship(microentrepreneurship);


        // Envia un correo electrónico a todos los usuarios con el rol "admin"
        List<String> adminUsers = userService.getAllAdminEmails();

        String fullName = message.getFullName();
        String microentrepreneurshipName = message.getMicroentrepreneurship().getName();
        String messageContent = "<img src='/Resources/logo.webp'/> El inversionista " + fullName + " quiere invertir en el microemprendimiento: " +
                microentrepreneurshipName + ".\n\n" + "Mensaje: " + message.getMessage();

        for (String adminEmail : adminUsers) {
            //emailService.sendEmail(adminEmail, "Contacto inversionista", messageContent);
            //System.out.println("Email enviado a: " + adminEmail);
        }
        emailService.sendEmail("nodoycorreos@gmail.com", "Contacto inversionista", messageContent);

        // Guarda el mensaje
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long messageId) {
        logger.info("Get Message By ID");
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("The Employee with the ID: " + messageId + " was not found."));
        return message;
    }

    @Override
    public List<Message> getMessagesByMicroentrepreneurshipId(Long microentrepreneurshipId) {
        logger.info("Get Messages By Microentrepreneurship");

        // Verifica si el microemprendimiento existe
        microentrepreneurshipRepository.findById(microentrepreneurshipId)
                .orElseThrow(() -> new EntityNotFoundException("Microemprendimiento no encontrado con ID: " + microentrepreneurshipId));

        // Retorna los mensajes asociados al microemprendimiento
        return messageRepository.findByMicroentrepreneurshipId(microentrepreneurshipId);
    }

    @Override
    public Message changeManagementStatus(Long messageId) {
        logger.info("Change Management Status");

        Message changeStatus = messageRepository.findById(messageId).orElseThrow(() ->
                new ResourceNotFoundException("The Message with ID: " + messageId + "was not found"));

        if(changeStatus.getManagement() == Management.MANAGED) {
            changeStatus.setManagement(Management.UNMANAGED);
            messageRepository.save(changeStatus);
        }else{
            changeStatus.setManagement(Management.MANAGED);
            messageRepository.save(changeStatus);
        }

        return changeStatus;
    }
}
