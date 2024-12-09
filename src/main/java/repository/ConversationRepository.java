package repository;
import java.util.List;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.Conversation;


@ApplicationScoped
public class ConversationRepository implements PanacheRepository<Conversation> {
    public List<Conversation> listByUserId(Long userId) {
        return list("userId", userId);
    }
}
