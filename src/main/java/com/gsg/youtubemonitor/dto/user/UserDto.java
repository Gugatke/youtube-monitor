package com.gsg.youtubemonitor.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int id;

    @NotBlank(message = "username should not be blank")
    private String username;

    @NotBlank(message = "password should not be blank")
    private String password;

    @Length(min = 2, max = 2, message = "countryCode length should be 2")
    private String countryCode;

    @Min(value = 1, message = "jobRunMinute should be more than 0")
    @Max(value = 60, message = "jobRunMinute should be less or equal to 60")
    private int jobRunMinute;
}
