package model;
import java.util.List;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

public class Conversation extends PanacheEntityBase {
    public Long id;
    public Long userId;
    public String name;
    public List<String> participants;

    public int getHashSum() {
        return hashCode();
    }
}
