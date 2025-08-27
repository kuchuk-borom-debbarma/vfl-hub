package dev.kuku.vfl.hub.repo;

import dev.kuku.vfl.hub.model.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepo extends JpaRepository<Block, String> {
}
