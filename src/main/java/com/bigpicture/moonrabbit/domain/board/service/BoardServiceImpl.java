package com.bigpicture.moonrabbit.domain.board.service;

import com.bigpicture.moonrabbit.domain.answer.dto.AnswerResponseDTO;
import com.bigpicture.moonrabbit.domain.answer.entity.Answer;
import com.bigpicture.moonrabbit.domain.answer.repository.AnswerRepository;
import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.item.dto.EquippedItemDTO;
import com.bigpicture.moonrabbit.domain.item.service.UserItemService;
import com.bigpicture.moonrabbit.domain.point.Point;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserItemService userItemService;

    @Override
    public BoardResponseDTO createBoard(BoardRequestDTO boardDTO, Long userId) {
        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setCategory(boardDTO.getCategory());
        board.setAnonymous(boardDTO.isAnonymous());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (boardDTO.isAnonymous()) {
            board.setAnonymous(true);
            board.setAnonymousNickname(userService.generateNickname());
        } else {
            board.setAnonymous(false);
            board.setAnonymousNickname(user.getNickname());
        }
        // 게시글 작성 시 10점 지급
        user.changePoint(Point.CREATE_BOARD.getValue());
        board.setUser(user);
        Board savedBoard = boardRepository.save(board);
        List<EquippedItemDTO> equippedItems = userItemService.getEquippedItems(userId);
        return toDto(savedBoard, userId);
    }
    @Override
    public BoardResponseDTO updateBoard(Long boardId, BoardRequestDTO boardDTO, Long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setCategory(boardDTO.getCategory());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(user.getId() != board.getUser().getId()) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }
        board.setUser(user);
        Board updatedBoard = boardRepository.save(board);
        List<EquippedItemDTO> equippedItems = userItemService.getEquippedItems(userId);
        return toDto(updatedBoard, userId);
    }


    @Override
    public BoardResponseDTO delete(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(user.getId() != board.getUser().getId()) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }
        user.changePoint(Point.DELETE_BOARD.getValue());
        boardRepository.delete(board);
        List<EquippedItemDTO> equippedItems = userItemService.getEquippedItems(userId);
        return toDto(board, userId);
    }

    @Override
    public List<BoardResponseDTO> select() {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long currentUserId = userService.getUserIdByEmail(email);

        List<EquippedItemDTO> equippedItems = userItemService.getEquippedItems(currentUserId);
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(board -> toDto(board, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public BoardResponseDTO selectOne(Long id) {
        Board board = boardRepository.findWithCommentsById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 1. 게시글 작성자의 장착 아이템 조회
        List<EquippedItemDTO> boardAuthorEquippedItems = userItemService.getEquippedItems(board.getUser().getId());

        // 2. 각 댓글을 순회하며 댓글 DTO(AnswerResponseDTO) 리스트를 생성
        List<AnswerResponseDTO> answerDTOs = board.getAnswers().stream()
                .map(answer -> {
                    // 2-1. 각 댓글 작성자의 장착 아이템 조회
                    List<EquippedItemDTO> answerAuthorEquippedItems = userItemService.getEquippedItems(answer.getUser().getId());
                    // 2-2. 수정된 AnswerResponseDTO 생성자 호출
                    return new AnswerResponseDTO(answer, board.getUser().getId(), answerAuthorEquippedItems);
                })
                .collect(Collectors.toList());

        // 3. 모든 정보를 담아 BoardResponseDTO 생성
        return new BoardResponseDTO(board, board.getUser().getId(), boardAuthorEquippedItems, answerDTOs);
    }

    @Override
    public Page<BoardResponseDTO> selectPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardRepository.findAll(pageable).map(board -> toDto(board, null));
    }


    @Override
    public BoardResponseDTO toDto(Board board, Long currentUserId) {
        List<EquippedItemDTO> boardAuthorEquippedItems = userItemService.getEquippedItems(board.getUser().getId());

        List<AnswerResponseDTO> answerDTOs = board.getAnswers().stream()
                .map(answer -> {
                    List<EquippedItemDTO> answerAuthorEquippedItems = userItemService.getEquippedItems(answer.getUser().getId());
                    return new AnswerResponseDTO(answer, currentUserId, answerAuthorEquippedItems);
                })
                .collect(Collectors.toList());

        return new BoardResponseDTO(board, currentUserId, boardAuthorEquippedItems, answerDTOs);
    }

    @Override
    public Page<BoardResponseDTO> selectPagedByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return boardRepository.findByUser_Id(userId, pageable)
                .map(board -> toDto(board, userId));
    }
}

