package org.example.postal_items;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.model.dto.PostOfficeDto;
import org.example.postal_items.repository.PostOfficeRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
public class PostOfficeControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    private List<PostOffice> postOfficeList = new ArrayList<>();
    @Autowired
    PostOfficeRepository postOfficeRepository;

    private PostOffice createPostOffice() {
        var postOffice = Instancio.of(PostOffice.class)
                .ignore(Select.field(PostOffice::getId))
                .create();
        return postOffice;
    }

    @BeforeAll
    public static void beforeAll() {
        mongoDBContainer.start();
    }
    @BeforeEach
    public void beforeEach() {
        for (int i = 0; i < 5; i++) {
            var postOffice = createPostOffice();
            postOfficeRepository.save(postOffice);
            postOfficeList.add(postOffice);
        }
    }
    @AfterEach
    public void afterEach() {
        postOfficeRepository.deleteAll();
    }

    @Test
    public void createPostOfficeTestPositive() throws Exception {
        PostOfficeDto newPostOffice = Instancio.of(PostOfficeDto.class).create();
        var request = post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newPostOffice));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();

        assertThat(body).contains(newPostOffice.getAddress(), newPostOffice.getName());
        assertThat(postOfficeRepository.findPostOfficeByPostalCode(newPostOffice.getPostalCode())).isNotNull();
    }

    @Test
    public void getPostOfficesTestPositive() throws Exception {
        var request = get("/post");

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();
        assertThat(body).contains(postOfficeList.get(0).getName(),postOfficeList.get(2).getName(),postOfficeList.get(4).getName());
    }

    @Test
    public void createPostOfficeTestNegative() throws Exception {
        PostOfficeDto newPostOffice = Instancio.of(PostOfficeDto.class)
                .supply(Select.field(PostOfficeDto::getPostalCode), () -> postOfficeList.get(0).getPostalCode())
                .create();

        var request = post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newPostOffice));

        var result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();

        assertThat(body).contains("A post office in that postal code already exists.");
    }
}
