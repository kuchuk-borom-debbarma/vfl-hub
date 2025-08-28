package dev.kuku.vfl.hub.services.vfl;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlock;
import dev.kuku.vfl.hub.model.entity.Block;
import dev.kuku.vfl.hub.model.entity.BlockLog;
import dev.kuku.vfl.hub.model.exception.VFLException;
import dev.kuku.vfl.hub.repo.BlockRepo;
import jakarta.persistence.EntityManager;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VFLServiceImpl implements VFLService {
    Logger log = LoggerFactory.getLogger(VFLServiceImpl.class);
    private final BlockRepo blockRepo;
    private final BlockLogRepo logRepo;
    private final EntityManager entityManager;

    public VFLServiceImpl(BlockRepo blockRepo, BlockLogRepo logRepo, EntityManager entityManager) {
        this.blockRepo = blockRepo;
        this.logRepo = logRepo;
        this.entityManager = entityManager;
    }

    @Override
    public void persistBlocks(List<ToAddBlock> toAddBlocks) {
        log.trace("persistBlocks ${java.util.Arrays.toString(toAddBlocks.toArray())}");

        List<Block> blocks = toAddBlocks.stream()
                .map(toAddBlock -> new Block(
                        toAddBlock.getId(),
                        toAddBlock.getName(),
                        toAddBlock.getParentBlockId(),
                        toAddBlock.getCreatedAt(),
                        null,
                        null,
                        null,
                        null,
                        Instant.now().toEpochMilli())).toList();
        blockRepo.saveAll(blocks);
    }

    @Override
    public void persistLogs(List<ToAddBlockLog> logs) {
        log.trace("persistLogs ${java.util.Arrays.toString(logs.toArray())}");

        List<BlockLog> blockLogs = logs.stream()
                .map(l -> new BlockLog(
                        l.getId(),
                        l.getBlockId(),
                        l.getMessage(),
                        l.getParentLogId(),
                        l.getReferencedBlockId(),
                        l.getTimestamp(),
                        l.getLogType(),
                        Instant.now().toEpochMilli()
                ))
                .toList();
        logRepo.saveAll(blockLogs);
    }

    @Override
    public List<ToFetchBlock> getRootBlocks(@Nullable String cursor, int limit) {
        var cb = entityManager.getCriteriaBuilder();
        var cr = cb.createQuery(Block.class);
        var root = cr.from(Block.class);

        cr = cr.where(cb.isNull(root.get("parentBlockId")));

        if (cursor != null) {
            String cursorBlockId;
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(cursor);
                cursorBlockId = new String(decodedBytes);
            } catch (IllegalArgumentException e) {
                throw new VFLException(HttpStatus.BAD_REQUEST, "Invalid cursor provided for fetching root blocks :- $cursor");
            }
            // Assuming cursor contains the last ID from previous page
            cr = cr.where(
                    cb.and(
                            cb.isNull(root.get("parentBlockId")),
                            cb.lessThan(root.get("id"), cursorBlockId)
                    )
            );
        }

        cr = cr.orderBy(
                cb.desc(root.get("persistedTime")),
                cb.desc(root.get("id"))
        );

        var query = entityManager.createQuery(cr);
        query.setMaxResults(limit);

        List<Block> blocks = query.getResultList();

        return blocks.stream()
                .map(this::convertToToFetchBlock)
                .collect(Collectors.toList());
    }

    private ToFetchBlock convertToToFetchBlock(Block block) {
        return new ToFetchBlock(
                block.getId(),
                block.getName(),
                block.getCreatedAt(),
                block.getEnteredAt(),
                block.getExitedAt(),
                block.getReturnedAt(),
                block.getExitMessage()
        );
    }
}
