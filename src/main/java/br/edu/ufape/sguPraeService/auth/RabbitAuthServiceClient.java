package br.edu.ufape.sguPraeService.auth;

import br.edu.ufape.sguPraeService.auth.eventos.RoleAssignmentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RabbitAuthServiceClient {

    private final RabbitTemplate rabbitTemplate;

    public void assignRoleToUser(String userId, String clientId, String role) {
        RoleAssignmentEvent event = new RoleAssignmentEvent(userId, clientId, role);
        rabbitTemplate.convertAndSend("auth-role-exchange", "auth.role.assign", event);
    }

}