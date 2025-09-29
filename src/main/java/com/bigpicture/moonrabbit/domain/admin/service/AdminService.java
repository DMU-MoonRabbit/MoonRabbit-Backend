package com.bigpicture.moonrabbit.domain.admin.service;

import com.bigpicture.moonrabbit.domain.admin.dto.UserAdminResponseDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import org.springframework.data.domain.Page;

public interface AdminService {
    public UserAdminResponseDTO updateUserPoint(Long userId, int point);

    public UserAdminResponseDTO updateUserTrust(Long userId, int point);

    public BoardResponseDTO updateBoardAsAdmin(Long boardId, BoardRequestDTO boardDTO);

    public void deleteBoardAsAdmin(Long boardId);

    public Page<UserAdminResponseDTO> getUserList(int page, int size);
}

