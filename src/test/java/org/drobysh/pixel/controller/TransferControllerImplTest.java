package org.drobysh.pixel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.drobysh.pixel.IntegrationTest.AbstractIT;
import org.drobysh.pixel.TestSecurityConfig;
import org.drobysh.pixel.configuration.SecurityConfig;
import org.drobysh.pixel.dto.request.TransferDto;
import org.drobysh.pixel.enums.AccountNotFoundExceptionMessage;
import org.drobysh.pixel.enums.TransferExceptionMessage;
import org.drobysh.pixel.exceptions.NotFoundException;
import org.drobysh.pixel.exceptions.handler.GlobalExceptionHandler;
import org.drobysh.pixel.model.Account;
import org.drobysh.pixel.model.User;
import org.drobysh.pixel.services.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.drobysh.pixel.TestData.createAccount;
import static org.drobysh.pixel.TestData.createInavalidTransferDto;
import static org.drobysh.pixel.TestData.createTransferDto;
import static org.drobysh.pixel.TestData.createTransferDtoForAmountException;
import static org.drobysh.pixel.TestData.createUser;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Import(value = {
        TestSecurityConfig.class,
        SecurityConfig.class,
        GlobalExceptionHandler.class,
})
class TransferControllerImplTest extends AbstractIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TransferService transferService;

    private static final String TRANSFER_URL = "/api/v1/user-service/transfer";
    private static final Long USER_ID_FROM_HEADER = 2L;
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzQ2MTc0MzYyLCJleHAiOjE3NDYxNzc5NjJ9.MxKb6mBDFkhcIYplcfWrz0d9r9Abhzwh9CnudGHsDC0";

    @Test
    void test_when_transfer_success() throws Exception {
        TransferDto transferDto = createTransferDto();
        User user = createUser();
        Account account = createAccount(user);
        BigDecimal amountAfterTransfer = account.getBalance().add(transferDto.amount());

        when(transferService.transfer(USER_ID_FROM_HEADER, transferDto)).thenReturn(amountAfterTransfer);

        mockMvc.perform(post(TRANSFER_URL)
                .header("Authorization", String.format("Bearer %s", ACCESS_TOKEN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(amountAfterTransfer)));
        verify(transferService).transfer(USER_ID_FROM_HEADER, transferDto);
    }

    @Test
    void test_transfer_when_user_not_authorized() throws Exception {
        TransferDto transferDto = createTransferDto();
        User user = createUser();
        Account account = createAccount(user);
        BigDecimal amountAfterTransfer = account.getBalance().add(transferDto.amount());

        when(transferService.transfer(USER_ID_FROM_HEADER, transferDto)).thenReturn(amountAfterTransfer);

        mockMvc.perform(post(TRANSFER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        verify(transferService, times(0)).transfer(USER_ID_FROM_HEADER, transferDto);
    }

    @Test
    void test_transferMoney_whenUserNotFound_throwsNotFoundException() throws Exception {

        TransferDto transferDto = createInavalidTransferDto();

        when(transferService.transfer(USER_ID_FROM_HEADER, transferDto))
                .thenThrow(new NotFoundException(AccountNotFoundExceptionMessage
                        .ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE.getMessage()));

        mockMvc.perform(post(TRANSFER_URL)
                        .header("Authorization", String.format("Bearer %s", ACCESS_TOKEN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void test_transferMoney_when_not_enough_money_returnsBadRequest() throws Exception {

        TransferDto transferDto = createTransferDtoForAmountException();

        when(transferService.transfer(USER_ID_FROM_HEADER, transferDto))
                .thenThrow(new IllegalArgumentException(TransferExceptionMessage.AMOUNT_FROM_EXCEPTION.getMessage()));

        mockMvc.perform(post(TRANSFER_URL)
                        .header("Authorization", String.format("Bearer %s", ACCESS_TOKEN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
