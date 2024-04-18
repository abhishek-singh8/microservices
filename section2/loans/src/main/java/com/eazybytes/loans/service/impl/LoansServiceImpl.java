package com.eazybytes.loans.service.impl;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;
import com.eazybytes.loans.exceptions.LoanAlreadyExistsException;
import com.eazybytes.loans.exceptions.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoansMapper;
import com.eazybytes.loans.repository.LoansRepository;
import com.eazybytes.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    LoansRepository loansRepository;
    @Override
    public void createLoans(String mobileNumber) {
        Optional<Loans> optionalLoans=loansRepository.findByMobileNumber(mobileNumber);
        if(optionalLoans.isPresent()){
            throw new LoanAlreadyExistsException(mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }
    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;

    }

    @Override
    public LoansDto fetchLoans(String mobileNumber) {
        Optional<Loans> optionalLoans=loansRepository.findByMobileNumber(mobileNumber);
        if(!optionalLoans.isPresent()){
            throw new ResourceNotFoundException("Loans", "mobileNumber", mobileNumber);
        }

        LoansDto loansDto= LoansMapper.mapToLoansDto(optionalLoans.get(),new LoansDto());
        return loansDto;
    }

    @Override
    public boolean updateLoans(LoansDto loansDto) {

        Optional<Loans> optionalLoans=loansRepository.findByLoanNumber(loansDto.getLoanNumber());
        if(!optionalLoans.isPresent()){
           throw new ResourceNotFoundException("Loans", "loan", loansDto.getLoanNumber());
        }
        Loans updatedLoans=LoansMapper.mapToLoans(loansDto,optionalLoans.get());
        loansRepository.save(updatedLoans);

        return true;

    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Optional<Loans> optionalLoans=loansRepository.findByMobileNumber(mobileNumber);
        if(!optionalLoans.isPresent()){
            throw new ResourceNotFoundException("Loans", "mobileNumber", mobileNumber);
        }
        loansRepository.deleteById(optionalLoans.get().getLoanId());
        return true;
    }
}
