package com.bigpicture.moonrabbit.domain.user.dto;

import com.bigpicture.moonrabbit.domain.item.dto.EquippedItemDTO;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserRankingDTO {
    private Long userId;
    private String nickname;
    private int level;
    private int totalPoint;
    private int trustPoint;
    private String profileImg;
    private List<EquippedItemDTO> equippedItems;

    public UserRankingDTO(User user, List<EquippedItemDTO> equippedItems) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.level = user.getLevel();
        this.totalPoint = user.getTotalPoint();
        this.trustPoint = user.getTrustPoint();
        this.profileImg = user.getProfileImg();
        this.equippedItems = equippedItems;
    }

}
