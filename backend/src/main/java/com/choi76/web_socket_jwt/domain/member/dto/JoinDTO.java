package com.choi76.web_socket_jwt.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinDTO {
    private String email;
    private String password;
}
