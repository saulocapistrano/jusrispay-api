package br.com.jurispay.api.dto.systemconfig;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSystemConfigRequest {

    @Size(max = 120)
    private String brandName;

    @Email
    @Size(max = 180)
    private String contactEmail;

    @Size(max = 40)
    private String contactPhone;

    @Size(max = 30)
    private String cnpj;
}
