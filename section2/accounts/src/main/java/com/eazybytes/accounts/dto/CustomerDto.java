package com.eazybytes.accounts.dto;

import com.eazybytes.accounts.entities.Accounts;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CustomerDto {



    private String name;

    private String email;

    private String mobileNumber;

    private AccountsDto accountsDto;
}
