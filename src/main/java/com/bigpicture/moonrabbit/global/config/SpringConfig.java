package com.bigpicture.moonrabbit.global.config;

import com.bigpicture.moonrabbit.domain.admin.service.AdminService;
import com.bigpicture.moonrabbit.domain.admin.service.AdminServiceImpl;
import com.bigpicture.moonrabbit.domain.answer.repository.AnswerRepository;
import com.bigpicture.moonrabbit.domain.answer.service.AnswerService;
import com.bigpicture.moonrabbit.domain.answer.service.AnswerServiceImpl;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.board.service.BoardService;
import com.bigpicture.moonrabbit.domain.board.service.BoardServiceImpl;
import com.bigpicture.moonrabbit.domain.boardLike.repository.BoardLikeRepository;
import com.bigpicture.moonrabbit.domain.boardLike.service.BoardLikeService;
import com.bigpicture.moonrabbit.domain.boardLike.service.BoardLikeServiceImpl;
import com.bigpicture.moonrabbit.domain.dailyquestion.repository.DailyAnswerRepository;
import com.bigpicture.moonrabbit.domain.dailyquestion.repository.DailyQuestionRepository;
import com.bigpicture.moonrabbit.domain.dailyquestion.service.DailyAnswerServiceImpl;
import com.bigpicture.moonrabbit.domain.dailyquestion.service.DailyQuestionServiceImpl;
import com.bigpicture.moonrabbit.domain.example.aiservice.repository.AssistantReplyRepository;
import com.bigpicture.moonrabbit.domain.example.aiservice.service.AssistantReplyService;
import com.bigpicture.moonrabbit.domain.example.aiservice.service.AssistantReplyServiceImpl;
import com.bigpicture.moonrabbit.domain.item.repository.ItemRepository;
import com.bigpicture.moonrabbit.domain.item.repository.UserItemRepository;
import com.bigpicture.moonrabbit.domain.item.service.UserItemService;
import com.bigpicture.moonrabbit.domain.item.service.UserItemServiceImpl;
import com.bigpicture.moonrabbit.domain.like.repository.LikesRepository;
import com.bigpicture.moonrabbit.domain.like.service.LikeService;
import com.bigpicture.moonrabbit.domain.like.service.LikeServiceImpl;
import com.bigpicture.moonrabbit.domain.playlist.repository.PlaylistRepository;
import com.bigpicture.moonrabbit.domain.playlist.service.PlaylistService;
import com.bigpicture.moonrabbit.domain.playlist.service.PlaylistServiceImpl;
import com.bigpicture.moonrabbit.domain.report.repository.ReportRepository;
import com.bigpicture.moonrabbit.domain.report.service.ReportService;
import com.bigpicture.moonrabbit.domain.report.service.ReportServiceImpl;
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
    private final LikesRepository likesRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final ReportRepository reportRepository;
    private final UserItemRepository userItemRepository;
    private final ItemRepository itemRepository;
    private final DailyQuestionRepository dailyQuestionRepository;
    private final DailyAnswerRepository dailyAnswerRepository;

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
        return new BoardServiceImpl(boardRepository, userRepository, userService());
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

    @Bean
    public LikeService likeService() {
        return new LikeServiceImpl(answerRepository, likesRepository, userRepository);
    }

    @Bean
    public BoardLikeService boardLikeService() {
        return new BoardLikeServiceImpl(boardLikeRepository, boardRepository, userRepository, userService());
    }

    @Bean
    public ReportService reportService() {
        return new ReportServiceImpl(reportRepository, boardRepository, answerRepository);
    }

    @Bean
    public AdminService adminService() {
        return new AdminServiceImpl(userRepository, boardRepository, userService());
    }

    @Bean
    public UserItemService userItemService() {
        return new UserItemServiceImpl(userItemRepository, userRepository, itemRepository, userService());
    }

    @Bean
    public DailyQuestionServiceImpl dailyQuestionService() {
        return new DailyQuestionServiceImpl(dailyQuestionRepository);
    }

    @Bean
    public DailyAnswerServiceImpl dailyAnswerService() {
        return new DailyAnswerServiceImpl(dailyAnswerRepository, dailyQuestionRepository, userService());
    }
}
