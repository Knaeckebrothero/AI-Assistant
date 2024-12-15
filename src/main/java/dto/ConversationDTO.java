package dto;

import java.util.List;

public class ConversationDTO {
    public Long id;
    public Long userId;
    public String name;
    public List<String> participants;
    public Long hashsum;

    public ConversationDTO(Long id, Long hashsum) {
        this.id = id;
        this.hashsum = hashsum;
    }
}
