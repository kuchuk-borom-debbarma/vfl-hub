package dev.kuku.vfl.hub.services.vfl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlock;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlockLog;
import dev.kuku.vfl.hub.model.dtos.pagination.BlockCursor;
import dev.kuku.vfl.hub.model.dtos.pagination.BlockLogCursor;
import dev.kuku.vfl.hub.model.entity.Block;
import dev.kuku.vfl.hub.model.entity.BlockLog;
import dev.kuku.vfl.hub.model.exception.VFLException;
import dev.kuku.vfl.hub.repo.BlockLogRepo;
import dev.kuku.vfl.hub.repo.BlockRepo;
import dev.kuku.vfl.hub.util.VFLUtil;
import dev.kuku.vfl.hub.util.cursorUtil.BlockCursorUtil;
import dev.kuku.vfl.hub.util.cursorUtil.BlockLogCursorUtil;
import jakarta.persistence.EntityManager;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VFLServiceImpl implements VFLService {
    Logger log = LoggerFactory.getLogger(VFLServiceImpl.class);
    //TODO use custom repo with blaze internally being used
    private final BlockRepo blockRepo;
    private final BlockLogRepo logRepo;
    private final EntityManager entityManager;
    private final CriteriaBuilderFactory cbf;

    public VFLServiceImpl(BlockRepo blockRepo, BlockLogRepo logRepo, EntityManager entityManager, CriteriaBuilderFactory cbf) {
        this.blockRepo = blockRepo;
        this.logRepo = logRepo;
        this.entityManager = entityManager;
        this.cbf = cbf;
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
                        VFLUtil.CurrentTime())
                ).toList();
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
                        VFLUtil.CurrentTime()
                ))
                .toList();
        logRepo.saveAll(blockLogs);
    }

    @Override
    public List<ToFetchBlock> getRootBlocks(@Nullable String cursor, int limit) {
        log.trace("getRootBlocks cursor = ${cursor}, limit = ${limit}");

        CriteriaBuilder<Block> cb = cbf.create(entityManager, Block.class)
                .where("parentBlockId").isNull();

        BlockCursor c = null;
        if (cursor != null) {
            try {
                c = BlockCursorUtil.decode(cursor);
            } catch (Exception e) {
                throw new VFLException(HttpStatus.BAD_REQUEST,
                        "Invalid cursor provided for fetching root blocks: " + cursor);
            }
        }

        if (c != null) {
            cb = cb.whereOr()
                    .where("createdAt").lt(c.getTimestamp())
                    .whereAnd()
                    .where("createdAt").eq(c.getTimestamp())
                    .where("id").lt(c.getBlockId())
                    .endAnd()
                    .endOr();
        }
        cb.orderByDesc("createdAt").orderByDesc("id");

        // Apply descending order and limit
        List<Block> blocks = cb.orderByDesc("createdAt")
                .orderByDesc("id")
                .setMaxResults(limit)
                .getResultList();

        // Convert to ToFetchBlock DTOs
        return blocks.stream()
                .map(this::convertToToFetchBlock)
                .collect(Collectors.toList());
    }


    @Override
    public List<ToFetchBlockLog> getLogsByBlockId(String blockId, @Nullable String cursor, int limit) {
        log.trace("getLogsByBlockId blockId = $blockId, $cursor");

        //TODO get from starting point for linear flow but for children show latest first.
        CriteriaBuilder<BlockLog> cb = cbf.create(entityManager, BlockLog.class);
        cb = cb.from(BlockLog.class, "bl")
                .where("bl.blockId").eq(blockId);
        if (cursor != null) {
            try {
                BlockLogCursor c = BlockLogCursorUtil.decode(cursor);
                cb.whereOr()
                        .whereAnd()
                        .where("bl.timestamp").eq(c.timestamp())
                        .where("bl.id").gt(c.id())
                        .endAnd()
                        .where("bl.timestamp").gt(c.timestamp())
                        .endOr();
            } catch (Exception e) {
                log.warn("Failed to parse cursor $cursor");
            }
        }

        List<BlockLog> rawLogs = cb.orderByAsc("bl.timestamp")
                .orderByAsc("bl.id")
                .setMaxResults(limit)
                .getResultList();

        //Collect list of blockIds that needs to be fetched
        Set<String> toFetchBlocks = new HashSet<>();
        rawLogs.forEach(log -> {
            @Nullable String referencedBlockId = log.getReferencedBlockId();
            if (referencedBlockId != null && toFetchBlocks.contains(referencedBlockId) == false) {
                toFetchBlocks.add(referencedBlockId);
            }
        });
        if (toFetchBlocks.isEmpty()) {
            log.debug("No fetch blocks found to fetch. Every log has no referenced block");
            return rawLogs.stream().map(blockLog -> convertToToFetchBlockLog(blockLog, null)).toList();
        }
        Map<String, ToFetchBlock> referencedBlocks = new HashMap<>();
        CriteriaBuilder<Block> cb2 = cbf.create(entityManager, Block.class);
        cb2.distinct()
                .where("id").in(toFetchBlocks)
                .getResultList()
                .forEach(block -> referencedBlocks.put(block.getId(), this.convertToToFetchBlock(block)));
        return rawLogs.stream().map(blockLog -> {
                            @Nullable String refBlock = blockLog.getReferencedBlockId();
                            if (refBlock == null) {
                                return convertToToFetchBlockLog(blockLog, null);
                            }
                            return convertToToFetchBlockLog(blockLog, referencedBlocks.get(refBlock));
                        }
                )
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

    private ToFetchBlockLog convertToToFetchBlockLog(BlockLog blockLog, @Nullable ToFetchBlock referencedBlock) {
        return new ToFetchBlockLog(
                blockLog.getId(),
                blockLog.getBlockId(),
                blockLog.getMessage(),
                referencedBlock,
                blockLog.getTimestamp(),
                blockLog.getLogType()
        );
    }
}
