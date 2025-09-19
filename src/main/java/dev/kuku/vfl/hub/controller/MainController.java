package dev.kuku.vfl.hub.controller;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.model.dtos.ToAddBlockLog;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlock;
import dev.kuku.vfl.hub.model.dtos.ToFetchBlockLog;
import dev.kuku.vfl.hub.model.exception.VFLException;
import dev.kuku.vfl.hub.services.vfl.VFLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class MainController {
    //TODO introduce queue service again in future
    Logger log = LoggerFactory.getLogger(MainController.class);
    private final VFLService vflService;

    public MainController(VFLService vflService) {
        this.vflService = vflService;
    }

    @PostMapping("/purge")
    @ResponseStatus(HttpStatus.OK)
    public void purgeDEBUG() {
        log.trace("purgeDEBUG");
        vflService.purge();
    }

    //Add blocks
    @PostMapping("/blocks")
    @ResponseStatus(HttpStatus.OK)
    public void addBlocks(@RequestBody List<ToAddBlock> toAddBlocks) {
        log.trace("addBlocks ${java.util.Arrays.toString(toAddBlocks.toArray())}");
        vflService.persistBlocks(toAddBlocks);
    }

    //Add logs
    @PostMapping("/logs")
    @ResponseStatus(HttpStatus.OK)
    public void addLogs(@RequestBody List<ToAddBlockLog> toAddBlockLogs) {
        log.trace("addLogs ${java.util.Arrays.toString(toAddBlockLogs.toArray())}");
        vflService.persistLogs(toAddBlockLogs);
    }

    //Add block entered data
    @PostMapping("/block-entered")
    @ResponseStatus(HttpStatus.OK)
    public void blockEntered(@RequestBody Map<String, Long> request) {
        //TODO send them to queue and then let queue process them as batch
        log.trace("blockEntered ${String.valueOf(request)}");
        vflService.updateBlockEntered(request);
    }

    //Add block exited data
    @PostMapping("/block-exited")
    @ResponseStatus(HttpStatus.OK)
    public void blockExited(@RequestBody Map<String, Long> request) {
        //TODO send them to queue and then let queue process them as batch
        log.trace("blockExited ${String.valueOf(request)}");
        vflService.updateBlockExited(request);
    }

    //Add block returned data
    @PostMapping("/block-returned")
    @ResponseStatus(HttpStatus.OK)
    public void blockReturned(@RequestBody Map<String, Long> request) {
        log.trace("blockReturned ${String.valueOf(request)}");
        //TODO send them to queue and then let queue process them as batch
        vflService.updateBlockReturned(request);
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

    @DeleteMapping("/blocks/{ids}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBlocksById(@PathVariable("ids") String rawIds) {
        log.trace("deleteBlocksById $rawIds");

        var toDelete = Arrays.stream(rawIds.split(",")).toList();
        vflService.deleteBlocksById(toDelete);
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
