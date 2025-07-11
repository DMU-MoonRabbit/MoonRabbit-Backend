package com.bigpicture.moonrabbit.domain.board.service;

import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public BoardResponseDTO createBoard(BoardRequestDTO boardDTO, Long userId) {
        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setCategory(boardDTO.getCategory());
        board.setAnonymous(boardDTO.isAnonymous());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        board.setUser(user);
        Board savedBoard = boardRepository.save(board);
        return new BoardResponseDTO(savedBoard);
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
        return new BoardResponseDTO(updatedBoard);
    }


    @Override
    public BoardResponseDTO delete(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(user.getId() != board.getUser().getId()) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }
        boardRepository.delete(board);

        return new BoardResponseDTO(board);
    }

    @Override
    public List<BoardResponseDTO> select() {
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public BoardResponseDTO selectOne(Long id) {
        Board board = boardRepository.findWithCommentsById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return new BoardResponseDTO(board);
    }

    @Override
    public Page<BoardResponseDTO> selectPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardRepository.findAll(pageable).map(BoardResponseDTO::new);
    }
}
