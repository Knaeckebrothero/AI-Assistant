package user;

import dto.UserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    public Response createUserTest(UserDTO userDTO, Long id){

        if (userDTO.username == null || userDTO.username.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username cannot be empty")
                    .build();
        }


        if (userRepository.findByUsername(userDTO.username).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Username already exists")
                    .build();
        }


        User user = new User();
        user.userId = id;
        user.username = userDTO.username;

        userRepository.persist(user);

        return Response.status(Response.Status.CREATED)
                .entity(user)
                .build();
    }

    public Response createUser(UserDTO userDTO) {

        if (userDTO.username == null || userDTO.username.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username cannot be empty")
                    .build();
        }

        if (userRepository.findByUsername(userDTO.username).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Username already exists")
                    .build();
        }


        User user = new User();
        user.userId = System.currentTimeMillis();
        user.username = userDTO.username;

        userRepository.persist(user);

        return Response.status(Response.Status.CREATED)
                .entity(user)
                .build();
    }

    public Response getUser(Long userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        return user.map(u -> Response.ok(u).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    public Response getAllUsers() {
        List<User> users = userRepository.listAll();
        return Response.ok(users).build();
    }

    public Response deleteUser(Long userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}