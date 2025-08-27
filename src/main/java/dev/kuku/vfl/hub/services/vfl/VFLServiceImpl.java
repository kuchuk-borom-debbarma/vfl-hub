package dev.kuku.vfl.hub.services.vfl;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.entity.Block;
import dev.kuku.vfl.hub.model.entity.BlockLog;
import dev.kuku.vfl.hub.repo.BlockRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class VFLServiceImpl implements VFLService {
    Logger log = LoggerFactory.getLogger(VFLServiceImpl.class);
    private final BlockRepo blockRepo;
    private final BlockLogRepo logRepo;

    public VFLServiceImpl(BlockRepo blockRepo, BlockLogRepo logRepo) {
        this.blockRepo = blockRepo;
        this.logRepo = logRepo;
    }

    @Override
    public void persistBlocks(List<ToAddBlock> toAddBlocks) {
        log.trace("persistBlocks ${java.util.Arrays.toString(toAddBlocks.toArray())}");

        List<Block> blocks = toAddBlocks.stream()
                .map(toAddBlock -> new Block(
                        toAddBlock.getId(),
                        toAddBlock.getName(),
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
}
