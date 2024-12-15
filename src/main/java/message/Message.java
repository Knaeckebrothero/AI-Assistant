package message;


import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;

@MongoEntity(collection="messages", database="chatdb")
public class Message extends PanacheMongoEntity {
    public Long messageId;
    public Long conversationId;
    public String roleName;
    public String content;
    public Instant time;
}


