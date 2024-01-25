package org.example.postal_items;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.model.dto.MailingDto;
import org.example.postal_items.model.dto.PostOfficeDto;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostOfficeControllerTests {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;
    private List<PostOffice> postOfficeList = new ArrayList<>();

    private PostOffice createPostOffice() {
        var postOffice = Instancio.of(PostOffice.class)
                .ignore(Select.field(PostOffice::getId))
                .create();
        return postOffice;
    }

    @BeforeEach
    public void beforeEach() {
        for (int i = 0; i < 5; i++) {
            var postOffice = createPostOffice();
            mongoTemplate.save(postOffice, "postOffice");
            postOfficeList.add(postOffice);
        }
    }
    @AfterEach
    public void afterEach() {
        mongoTemplate.remove(new Query(), "postOffice");
        mongoTemplate.remove(new Query(), "mailing");
        postOfficeList.clear();
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

        Query query = new Query(Criteria.where("postalCode").is(newPostOffice.getPostalCode()));
        assertThat(mongoTemplate.find(query, PostOffice.class)).isNotNull();
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
