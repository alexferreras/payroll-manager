package com.payroll.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PayrollUploadRequest {
    @NotNull
    private MultipartFile file;
    @Getter(AccessLevel.NONE)
    private String country;
    @NotBlank(message = "credential is mandatory")
    private String credentials;
    @NotBlank(message = "Company is required")
    private String company;
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private String username;
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private String password;

    @JsonIgnore
    @AssertTrue(message = "Full fill the credentials")
    public boolean isDataFullFill() {
        if (credentials != null) {
            String[] data = credentials.split("\\+");
            username = data[0];
            password = data[1];
            return true;
        }
        return false;
    }

    public String getCountry() {
        //DO as default country value
        return isValid(this.country) ? this.country : Country.DO.toString();
    }

    private boolean isValid(String country) {
        return country != null &&
            Arrays.stream(Country.values()).anyMatch(c -> c.name().equals(country));
    }
}
