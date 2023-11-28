package semillero.ubuntu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Message;
import semillero.ubuntu.repository.MessageRepository;
import semillero.ubuntu.service.contract.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message saveMessage(Message message) {
        logger.info("Save Message");
        return messageRepository.save(message);
    }
}
