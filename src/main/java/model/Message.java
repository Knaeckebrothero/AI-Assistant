package model;
import java.time.Instant;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


public class Message extends PanacheEntityBase {
    public Long id;
    public Long conversationId;
    public String roleName;
    public String content;
    public Instant time;
}
