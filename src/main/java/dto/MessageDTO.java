package dto;

import message.Message;

public class MessageDTO {
    public Long messageId;
    public Long conversationId;
    public String roleName;
    public String content;
    public Long time;

    public static MessageDTO fromEntity(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.messageId = message.messageId;
        dto.conversationId = message.conversationId;
        dto.roleName = message.roleName;
        dto.content = message.content;
        dto.time = message.time.getEpochSecond();
        return dto;
    }
}