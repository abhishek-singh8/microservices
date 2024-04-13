package com.eazybytes.accounts.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AccountsDto {


    private Long accountNumber;

    private String accountType;

    private String branchAddress;
}
