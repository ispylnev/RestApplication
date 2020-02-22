package example.rest.service;

import example.rest.domain.Message;
import example.rest.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {


    private final MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Transactional
    public Message save(Message message) {
        return messageRepo.save(message);
    }
}
