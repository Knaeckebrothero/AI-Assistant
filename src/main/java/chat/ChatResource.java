package chat;


import dto.GenerateMessageDTO;
import dto.MessageDTO;
import dto.MessagePatchDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import message.Message;
import service.StartUpService;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {
    @Inject
    ChatService chatService;
    @Inject
    StartUpService startUpService;


    @GET
    @Path("/conversation/byuserid/{userId}")
    public Response getConversations(@PathParam("userId") Long userId) {
        return chatService.getConversations(userId);
    }

    @GET
    @Path("/conversation/messages/{conversationId}/{timestamp}/{messagesCount}")
    public Response getConversationMessages(
            @PathParam("conversationId") Long conversationId,
            @PathParam("timestamp") Long timestamp,
            @PathParam("messagesCount") int messagesCount) {
        return chatService.getConversationMessages(conversationId, timestamp, messagesCount);
    }

    @POST
    @Path("/message/send")
    public Response sendMessage(MessageDTO messageDTO) {
        return chatService.sendMessage(messageDTO);
    }

    @POST
    @Path("/message/generate")
    public Response generateMessage(GenerateMessageDTO generateMessageDTO) {
        return chatService.generateMessage(generateMessageDTO);
    }

    @PATCH
    @Path("/message/patch")
    public Response patchMessage(MessagePatchDTO messagePatchDTO) {
        return chatService.patchMessage(messagePatchDTO);
    }

    @DELETE
    @Path("/message/delete/{conversationId}/{messageId}")
    public Response deleteMessage(
            @PathParam("conversationId") Long conversationId,
            @PathParam("messageId") Long messageId) {
        return chatService.deleteMessage(conversationId, messageId);
    }

}