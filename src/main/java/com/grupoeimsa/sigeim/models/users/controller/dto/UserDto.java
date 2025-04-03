package com.grupoeimsa.sigeim.models.users.controller.dto;

import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String email;
    private String role;
    private String status;

    public UserDto(BeanUser user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        switch (user.getRole()) {
            case ADMIN:
                this.role = "Administrador";
                break;
            case RRHH:
                this.role = "Recursos Humanos";
                break;
            case GUESS:
                this.role = "Visualizador";
                break;
            case BETO:
                this.role = "Analista";
                break;
            default:
                this.role = "Desconocido";
        }
        this.status = user.isStatus() ? "Activo" : "Bloqueado";
    }
}
