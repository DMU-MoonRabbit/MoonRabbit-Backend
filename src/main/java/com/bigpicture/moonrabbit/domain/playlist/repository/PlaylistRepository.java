package com.bigpicture.moonrabbit.domain.playlist.repository;

import com.bigpicture.moonrabbit.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}
