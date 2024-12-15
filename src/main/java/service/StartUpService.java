package service;

import chat.ChatService;
import conversation.Conversation;
import conversation.ConversationRepository;
import dto.GenerateMessageDTO;
import dto.MessageDTO;
import dto.UserDTO;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import message.Message;
import message.MessageRepository;
import user.User;
import user.UserRepository;
import user.UserService;

import java.time.Instant;

@ApplicationScoped
public class StartUpService {
    @Inject
    ChatService chatService;

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent evt) {
        // Check if test user exists
        if (userRepository.findByUserId(1L).isEmpty()) {
            try {
                // Create test user
                UserDTO userDTO = new UserDTO();
                userDTO.username ="Test";
                Long id = 1L;
                userService.createUserTest(userDTO, id);

                // Create initial message from user
                MessageDTO userMsg = new MessageDTO();
                userMsg.conversationId = 1L;
                userMsg.roleName = "user";
                userMsg.content = "Hello AI!";
                userMsg.time = System.currentTimeMillis() / 1000;
                chatService.sendMessage(userMsg);

                // Generate AI response
                GenerateMessageDTO aiMsg = new GenerateMessageDTO();
                aiMsg.conversationId = 1L;
                aiMsg.roleName = "assistant";
                chatService.generateMessage(aiMsg);

            } catch (Exception e) {
                System.err.println("Error creating test data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}