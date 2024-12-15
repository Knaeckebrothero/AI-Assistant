package conversation;



import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class ConversationRepository implements PanacheMongoRepository<Conversation> {

    public List<Conversation> listByUserId(Long userId) {
        return list("userId", userId);
    }

    public Optional<Conversation> findByConversationId(Long conversationId) {
        return find("conversationId", conversationId).firstResultOptional();
    }

    public void updateHashsum(Long conversationId, String hashsum) {
        update("hashsum = ?1 where conversationId = ?2", hashsum, conversationId);
    }
}

