package dev.kuku.vfl.hub.controller;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlock;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlockLog;
import dev.kuku.vfl.hub.model.exception.VFLException;
import dev.kuku.vfl.hub.services.queue.QueueService;
import dev.kuku.vfl.hub.services.vfl.VFLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class MainController {
    Logger log = LoggerFactory.getLogger(MainController.class);
    private final QueueService queueService;
    private final VFLService vflService;

    public MainController(QueueService queueService, VFLService vflService) {
        this.queueService = queueService;
        this.vflService = vflService;
    }

    //Add blocks
    @PostMapping("/blocks")
    @ResponseStatus(HttpStatus.OK)
    public void addBlocks(@RequestBody List<ToAddBlock> toAddBlocks) {
        log.trace("addBlocks ${java.util.Arrays.toString(toAddBlocks.toArray())}");
        toAddBlocks.forEach(queueService::addBlockToQueue);
    }

    //Add logs
    @PostMapping("/logs")
    @ResponseStatus(HttpStatus.OK)
    public void addLogs(@RequestBody List<ToAddBlockLog> toAddBlockLogs) {
        log.trace("addLogs ${java.util.Arrays.toString(toAddBlockLogs.toArray())}");
        toAddBlockLogs.forEach(queueService::addBlockLogsToQueue);
    }

    //Get blocks
    @GetMapping("/blocks")
    @ResponseStatus(HttpStatus.OK)
    public List<ToFetchBlock> getBlocks(@RequestParam(required = false) String cursor,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        log.trace("getBlocks cursor = $cursor, $limit");
        return vflService.getRootBlocks(cursor, limit);
    }

    //Get logs of a block
    @GetMapping("/logs/{blockId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ToFetchBlockLog> getLogs(@PathVariable String blockId, @RequestParam(required = false) String cursor, @RequestParam(defaultValue = "2") int limit) {
        log.trace("getLogs blockId = $blockId, cursor = $cursor, limit = $limit");
        var fetched = vflService.getLogsByBlockId(blockId, cursor, limit);
        log.debug("getLogs of $blockId  = $fetched");
        return fetched;
    }

    //get block by id
    @GetMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToFetchBlock getBlock(@PathVariable String id) {
        log.trace("getBlock id = $id");

        var v = vflService.getBlockById(id);
        if (v == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return v;
    }

    @ExceptionHandler(VFLException.class)
    public ResponseEntity<Map<String, Object>> error(VFLException e) {
        log.error("VFL Exception occurred", e);

        Map<String, Object> errorResponse = Map.of(
                "error", "VFL_EXCEPTION",
                "message", e.getMessage() != null ? e.getMessage() : "An error occurred",
                "timestamp", Instant.now()
        );

        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

}
