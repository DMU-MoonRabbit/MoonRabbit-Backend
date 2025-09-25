package com.bigpicture.moonrabbit.global.config;

import com.bigpicture.moonrabbit.domain.answer.repository.AnswerRepository;
import com.bigpicture.moonrabbit.domain.answer.service.AnswerService;
import com.bigpicture.moonrabbit.domain.answer.service.AnswerServiceImpl;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.board.service.BoardService;
import com.bigpicture.moonrabbit.domain.board.service.BoardServiceImpl;
import com.bigpicture.moonrabbit.domain.example.aiservice.repository.AssistantReplyRepository;
import com.bigpicture.moonrabbit.domain.example.aiservice.service.AssistantReplyService;
import com.bigpicture.moonrabbit.domain.example.aiservice.service.AssistantReplyServiceImpl;
import com.bigpicture.moonrabbit.domain.playlist.repository.PlaylistRepository;
import com.bigpicture.moonrabbit.domain.playlist.service.PlaylistService;
import com.bigpicture.moonrabbit.domain.playlist.service.PlaylistServiceImpl;
import com.bigpicture.moonrabbit.domain.sms.repository.SmsRepository;
import com.bigpicture.moonrabbit.domain.sms.service.SmsService;
import com.bigpicture.moonrabbit.domain.sms.service.SmsServiceImpl;
import com.bigpicture.moonrabbit.domain.sms.util.SmsCertificationUtil;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.domain.user.service.UserServiceImpl;
import com.bigpicture.moonrabbit.global.auth.jwt.generator.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final SmsRepository smsRepository;
    private final UserRepository userRepository;
    private final SmsCertificationUtil smsCertificationUtil;
    private final BoardRepository boardRepository;
    private final AnswerRepository answerRepository;
    private final AssistantReplyRepository replyRepository;
    private final PlaylistRepository playlistRepository;

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository, passwordEncoder, jwtGenerator, smsRepository);
    }

    @Bean
    public SmsService smsService() {
        return new SmsServiceImpl(smsCertificationUtil, smsRepository);
    }

    @Bean
    public BoardService boardService(UserService userService) {
        return new BoardServiceImpl(boardRepository, userRepository, userService(), answerRepository);
    }

    @Bean
    public AnswerService answerService() {
        return new AnswerServiceImpl(answerRepository, boardRepository, userRepository, userService());
    }

    @Bean
    public PlaylistService playlistService() {
        return new PlaylistServiceImpl(playlistRepository);
    }

    @Bean
    public AssistantReplyService assistantReplyService() {
        return new AssistantReplyServiceImpl(replyRepository);
    }

}
