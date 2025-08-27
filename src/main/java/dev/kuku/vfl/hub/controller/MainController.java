package dev.kuku.vfl.hub.controller;

import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.services.queue.QueueService;
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

    public MainController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/blocks")
    @ResponseStatus(HttpStatus.OK)
    public void addBlocks(@RequestBody List<ToAddBlock> toAddBlocks) {
        log.trace("addBlocks ${java.util.Arrays.toString(toAddBlocks.toArray())}");
        toAddBlocks.forEach(toAddBlock -> queueService.addBlockToQueue(toAddBlock));
    }
}
