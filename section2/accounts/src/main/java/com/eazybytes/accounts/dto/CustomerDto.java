package com.eazybytes.accounts.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class CustomerDto {



    private String name;

    private String email;

    private String mobileNumber;
}
