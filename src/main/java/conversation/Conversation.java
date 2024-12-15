package conversation;
import java.time.Instant;
import java.util.List;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="conversations", database="chatdb")
public class Conversation extends PanacheMongoEntity {
    public Long id;
    public Long userId;
    public String name;
    public List<String> participants;

    public Instant lastMessageTime;

    public int getHashSum() {
        return hashCode();
    }
}




