package com.wrkspot.assignment.customer_management_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String customerId;

    @NotNull
    private int age;

    @NotNull
    @Digits(integer=12, fraction=6)
    private BigDecimal spendingLimit;

    @NotBlank
    private String mobileNumber;

    @Valid
    private List<AddressDTO> address;

}

