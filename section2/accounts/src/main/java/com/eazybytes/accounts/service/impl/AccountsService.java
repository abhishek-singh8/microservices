package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.Repository.AccountsRepository;
import com.eazybytes.accounts.Repository.CustomerRepository;
import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entities.Accounts;
import com.eazybytes.accounts.entities.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsService implements IAccountsService {

    // as there is a single constructor with all param created @Autowire is automatically done for both
    AccountsRepository accountsRepository;
    CustomerRepository customerRepository;


    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
            Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
            if(!optionalCustomer.isPresent()) {
                throw new ResourceNotFoundException("Customer","mobileNumber",mobileNumber);
            }

            Accounts accounts= accountsRepository.findByCustomerId(optionalCustomer.get().getCustomerID()).orElseThrow(
                    () -> new ResourceNotFoundException("Accounts","customerId",optionalCustomer.get().getCustomerID().toString())
            );

            CustomerDto customerDto=CustomerMapper.mapToCustomerDto(optionalCustomer.get(),new CustomerDto());
            AccountsDto accountsDto= AccountsMapper.mapToAccountsDto(accounts,new AccountsDto());
            customerDto.setAccountsDto(accountsDto);
            return customerDto;

    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerID());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedBy("Anonymous");
        newAccount.setCreatedAt(LocalDateTime.now());
        return newAccount;
    }
}
