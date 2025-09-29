package com.bigpicture.moonrabbit.domain.admin.service;

import com.bigpicture.moonrabbit.domain.admin.dto.UserAdminResponseDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.board.repository.BoardRepository;
import com.bigpicture.moonrabbit.domain.point.Point;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;


    @Transactional
    public UserAdminResponseDTO updateUserPoint(Long userId, int point) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        int beforePoint = user.getPoint();
        System.out.println("beforePoint = " + beforePoint);
        user.changePoint(point);
        UserAdminResponseDTO userAdminResponseDTO = new UserAdminResponseDTO(user);
        userAdminResponseDTO.setContent("수정 전 포인트 : "+beforePoint+" 수정 후 포인트 : "+user.getPoint());
        return userAdminResponseDTO;
    }

    @Transactional
    public UserAdminResponseDTO updateUserTrust(Long userId, int point) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        int beforeTrust = user.getTrustPoint();
        user.setTrustPoint(user.getTrustPoint() + point);
        UserAdminResponseDTO userAdminResponseDTO = new UserAdminResponseDTO(user);
        userAdminResponseDTO.setContent("수정 전 신뢰도 : "+ beforeTrust +" 수정 후 신뢰도 : "+user.getTrustPoint());
        return userAdminResponseDTO;

    }

    @Transactional
    public BoardResponseDTO updateBoardAsAdmin(Long boardId, BoardRequestDTO boardDTO) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setCategory(boardDTO.getCategory());

        Board updatedBoard = boardRepository.save(board);
        return new BoardResponseDTO(updatedBoard, board.getUser().getId());
    }

    @Transactional
    public void deleteBoardAsAdmin(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 관리자가 삭제했을 경우, 글쓴이 포인트 차감
        User writer = board.getUser();
        writer.changePoint(Point.DELETE_BOARD.getValue());

        boardRepository.delete(board);
    }

    public Page<UserAdminResponseDTO> getUserList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(UserAdminResponseDTO::new);
    }
}
