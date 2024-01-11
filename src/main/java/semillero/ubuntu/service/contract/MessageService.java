package semillero.ubuntu.service.contract;

import semillero.ubuntu.entities.Message;

import java.util.List;

public interface MessageService {

    Message saveMessage(Long microentrepreneurshipId, Message message);

    Message getMessageById(Long messageId);

    List<Message> getMessagesByMicroentrepreneurshipId(Long microentrepreneurshipId);

    Message changeManagementStatus(Long messageId);

    List<Message> getAllMessages();
}
