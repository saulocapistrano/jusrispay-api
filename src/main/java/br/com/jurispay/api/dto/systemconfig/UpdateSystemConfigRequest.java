package br.com.jurispay.api.dto.systemconfig;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

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

    @Size(max = 255)
    private String pixKey;

    @Size(max = 80)
    private String notificationTimezone;

    private LocalTime reminderDispatchTime;

    private LocalTime collectionDispatchTime;
}
