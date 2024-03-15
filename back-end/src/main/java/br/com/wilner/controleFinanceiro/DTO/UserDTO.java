package br.com.wilner.controleFinanceiro.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode

public class UserDTO {

    
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "status")
    private Boolean userStatus = true;

}
