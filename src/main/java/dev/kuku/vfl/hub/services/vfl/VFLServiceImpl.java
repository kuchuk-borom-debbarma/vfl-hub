package dev.kuku.vfl.hub.services.vfl;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VFLServiceImpl implements VFLService {
    Logger log = LoggerFactory.getLogger(VFLServiceImpl.class);

    @Override
    public void persistBlocks(List<ToAddBlock> toAddBlocks) {
        log.trace("persistBlocks ${java.util.Arrays.toString(toAddBlocks.toArray())}");
    }

    @Override
    public void persistLogs(List<ToAddBlockLog> logs) {
        log.trace("persistLogs ${java.util.Arrays.toString(logs.toArray())}");
    }
}
