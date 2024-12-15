package message;


import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class MessageRepository implements PanacheMongoRepository<Message> {


    public List<Message> listByConversationId(Long conversationId, Instant timestamp, int limit) {
        return find("conversationId = ?1 and time < ?2 order by time desc", conversationId, timestamp)
                .range(0, limit).list();
    }

    public List<Message> findByConversationId(Long conversationId) {
        return list("conversationId", conversationId);
    }

    public List<Message> findRecentMessages(Long conversationId, Long timestamp, int limit) {
        return find("conversationId = ?1 and time < ?2", conversationId, timestamp)
                .page(0, limit)
                .list();
    }

    public Message findByMessageId(Long messageId) {
        return find("messageId", messageId).firstResult();
    }

    public long deleteByConversationAndMessageId(Long conversationId, Long messageId) {
        return delete("conversationId = ?1 and messageId = ?2", conversationId, messageId);
    }
}