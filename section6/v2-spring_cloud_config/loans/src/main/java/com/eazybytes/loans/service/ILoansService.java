package com.eazybytes.loans.service;

import com.eazybytes.loans.dto.LoansDto;

public interface ILoansService {

    void createLoans(String mobileNumber);

    LoansDto fetchLoans(String mobileNumber);

    boolean updateLoans(LoansDto loansDto);

    boolean deleteLoan(String mobileNumber);
}
