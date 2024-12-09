package repository;
import java.time.Instant;
import java.util.List;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.Message;


@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {
    public List<Message> listByConversationId(Long conversationId, Instant timestamp, int limit) {
        return find("conversationId = ?1 and time < ?2 order by time desc", conversationId, timestamp)
                .range(0, limit).list();
    }
}
