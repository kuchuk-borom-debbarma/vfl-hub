package dev.kuku.vfl.hub.services.vfl;

import dev.kuku.vfl.hub.model.entity.BlockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockLogRepo extends JpaRepository<BlockLog, String> {
}
