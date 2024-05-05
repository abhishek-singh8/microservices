package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsContactInfoDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.ResponseDto;
import com.eazybytes.accounts.service.IAccountsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path="api",produces={MediaType.APPLICATION_JSON_VALUE})

@Validated
public class AccountsController {

    @Autowired
    private IAccountsService iAccountsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
   private Environment environment;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid  @RequestBody CustomerDto customerDto){

        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201,AccountsConstants.MESSAGE_201));
    }


    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchDetails(@RequestParam
                                                        @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                        String mobileNumber){

        CustomerDto customerDto=iAccountsService.fetchAccount(mobileNumber);

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(customerDto);

    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto){
        boolean status=iAccountsService.updateAccount(customerDto);
        if(status){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber){
        boolean status=iAccountsService.deleteAccount(mobileNumber);
        if(status){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.MESSAGE_417_DELETE,AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildVersion(){
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(buildVersion);
    }

    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaEnvironment(){
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<String> getContactInfo(){
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(accountsContactInfoDto.message());
    }



}
