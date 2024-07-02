package com.example.practicaltest.spring.api.service.mail;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.practicaltest.spring.client.mail.MailSendClient;
import com.example.practicaltest.spring.domain.history.mail.MailSendHistory;
import com.example.practicaltest.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailSendClient mailSendClient;
    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;
    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        //given
        // when(
        //         mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
        //     .thenReturn(true);
        given(
            mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
            .willReturn(true);

        // doReturn(true)
        //     .when(mailSendClient)
        //     .sendEmail(any(String.class), any(String.class), any(String.class), any(String.class));

        //when
        boolean result = mailService.sendMail("", "", "", "");

        //then
        Assertions.assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));

    }

}