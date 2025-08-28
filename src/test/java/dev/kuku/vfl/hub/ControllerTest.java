package dev.kuku.vfl.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kuku.vfl.hub.model.dtos.ToAddBlock;
import dev.kuku.vfl.hub.services.queue.QueueService;
import dev.kuku.vfl.hub.services.vfl.VFLService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("initialization.field.uninitialized")
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QueueService queueService;

    @MockitoBean
    private VFLService vflService;

    @Test
    void testAddBlocks() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        List<ToAddBlock> blocks = List.of(
                new ToAddBlock("1", "Block 1", null, 1000L),
                new ToAddBlock("2", "Block 2", "1", 2000L)
        );

        String json = objectMapper.writeValueAsString(blocks);

        mockMvc.perform(post("/api/v1/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}