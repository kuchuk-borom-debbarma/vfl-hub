package dev.kuku.vfl.hub.controller;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlock;
import dev.kuku.vfl.hub.services.queue.QueueService;
import dev.kuku.vfl.hub.services.vfl.VFLService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MainController {
    Logger log = LoggerFactory.getLogger(MainController.class);
    private final QueueService queueService;
    private final VFLService vflService;

    public MainController(QueueService queueService, VFLService vflService) {
        this.queueService = queueService;
        this.vflService = vflService;
    }

    @PostMapping("/blocks")
    @ResponseStatus(HttpStatus.OK)
    public void addBlocks(@RequestBody List<ToAddBlock> toAddBlocks) {
        log.trace("addBlocks ${java.util.Arrays.toString(toAddBlocks.toArray())}");
        toAddBlocks.forEach(toAddBlock -> queueService.addBlockToQueue(toAddBlock));
    }

    @PostMapping("/logs")
    public void addLogs(@RequestBody List<ToAddBlockLog> toAddBlockLogs) {
        log.trace("addLogs ${java.util.Arrays.toString(toAddBlockLogs.toArray())}");
        toAddBlockLogs.forEach(toAddBlockLog -> queueService.addBlockLogsToQueue(toAddBlockLog));
    }

    @GetMapping("/blocks")
    public List<ToFetchBlock> getBlocks(@RequestParam(required = false) @Nullable String cursor,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        log.trace("getBlocks cursor = ${java.util.Arrays.toString($cursor, $limit");
        return vflService.getRootBlocks(cursor, limit);
    }
}
