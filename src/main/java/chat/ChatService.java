package chat;

import conversation.Conversation;
import conversation.ConversationRepository;
import dto.ConversationDTO;
import dto.GenerateMessageDTO;
import dto.MessageDTO;
import dto.MessagePatchDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import message.Message;
import message.MessageRepository;
import user.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ChatService {
    @Inject
    MessageRepository messageRepository;

    @Inject
    ConversationRepository conversationRepository;

    @Inject
    UserRepository userRepository;

    public Response getConversations(Long userId) {
        if (userRepository.findByUserId(userId).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Conversation> conversations = conversationRepository.listByUserId(userId);
        if (conversations.isEmpty()) {
            System.out.println("HSLLLOOOO");
            return Response.noContent().build();

        }
        List<ConversationDTO> conversationDTOs = conversations.stream()
                .map(conv -> {
                    List<Message> messages = messageRepository.findByConversationId(conv.id);
                    Long hashSum = Long.valueOf(calculateHashsum(messages));
                    return new ConversationDTO(conv.id, hashSum);
                })
                .collect(Collectors.toList());
        return Response.ok(conversationDTOs).build();
    }

    public Response getConversationMessages(Long conversationId, Long timestamp, int messagesCount) {
        if (conversationId == null || timestamp == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (conversationRepository.findByConversationId(conversationId).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        int limit = Math.min(messagesCount, 30);
        List<Message> messages = messageRepository.findRecentMessages(conversationId, timestamp, limit);

        if (messages.isEmpty()) {
            return Response.noContent().build();
        }

        List<MessageDTO> messageDTOs = messages.stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList());

        if (messagesCount > 30 && messages.size() == 30) {
            return Response.status(Response.Status.PARTIAL_CONTENT)
                    .entity(messageDTOs)
                    .build();
        }

        return Response.ok(messageDTOs).build();
    }

    public Response sendMessage(MessageDTO messageDTO) {
        if (messageDTO.content == null || messageDTO.content.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Message message = new Message();
        message.messageId = System.currentTimeMillis();
        message.conversationId = messageDTO.conversationId;
        message.roleName = messageDTO.roleName;
        message.content = messageDTO.content;
        message.time = Instant.ofEpochSecond(messageDTO.time);

        messageRepository.persist(message);

        return Response.status(Response.Status.CREATED)
                .entity(MessageDTO.fromEntity(message))
                .build();
    }

    public Response generateMessage(GenerateMessageDTO generateMessageDTO) {
        if (generateMessageDTO.conversationId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Optional<Conversation> conversation = conversationRepository.findByConversationId(generateMessageDTO.conversationId);
        if (conversation.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Conversation not found")
                    .build();
        }

        List<Message> context = messageRepository.findByConversationId(generateMessageDTO.conversationId);
        String aiResponse = generateAIResponse(context);

        Message message = new Message();
        message.messageId = System.currentTimeMillis();
        message.conversationId = generateMessageDTO.conversationId;
        message.roleName = generateMessageDTO.roleName;
        message.content = aiResponse;
        message.time = Instant.ofEpochSecond(generateMessageDTO.time);

        messageRepository.persist(message);

        return Response.status(Response.Status.CREATED)
                .entity(MessageDTO.fromEntity(message))
                .build();
    }

    public Response patchMessage(MessagePatchDTO messagePatchDTO) {
        Message existingMessage = messageRepository.findByMessageId(messagePatchDTO.id);
        if (existingMessage == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Conversation> conversation = conversationRepository.findByConversationId(existingMessage.conversationId);
        if (conversation.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Conversation not found")
                    .build();
        }

        existingMessage.content = messagePatchDTO.content;
        messageRepository.update(existingMessage);

        return Response.ok().build();
    }

    public Response deleteMessage(Long conversationId, Long messageId) {
        Optional<Conversation> conversation = conversationRepository.findByConversationId(conversationId);
        if (conversation.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Conversation not found")
                    .build();
        }

        long deleted = messageRepository.deleteByConversationAndMessageId(conversationId, messageId);
        if (deleted == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        List<Message> remainingMessages = messageRepository.findByConversationId(conversationId);
        if (!remainingMessages.isEmpty()) {
            Conversation conv = conversation.get();
            conv.lastMessageTime = remainingMessages.get(remainingMessages.size() - 1).time;
            conversationRepository.update(conv);
        }

        return Response.ok().build();
    }

    private long calculateHashsum(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return 0L;
        }

        StringBuilder sb = new StringBuilder();
        for (Message message : messages) {
            sb.append(message.messageId)
                    .append(message.content)
                    .append(message.time);
        }

        long hash = 7L;
        String str = sb.toString();
        for (int i = 0; i < str.length(); i++) {
            hash = hash * 31L + str.charAt(i);
        }

        return Math.abs(hash);
    }

    private String generateAIResponse(List<Message> context) {
        if (context == null || context.isEmpty()) {
            return "I don't have enough context to generate a response.";
        }

        Message lastMessage = context.get(context.size() - 1);

        return "Response to: " + lastMessage.content +
                "\nThis is a placeholder AI response. In a real implementation, " +
                "this would be generated by an AI model based on the full conversation context.";
    }
}