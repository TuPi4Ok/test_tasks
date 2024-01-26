package org.example.postal_items;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.MailingStatus;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.model.dto.MailingDto;
import org.example.postal_items.repository.MailingRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
class MailingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    @Autowired
    PostOfficeRepository postOfficeRepository;
    @Autowired
    MailingRepository mailingRepository;
    private List<PostOffice> postOfficeList = new ArrayList<>();
    private List<Mailing> mailingList = new ArrayList<>();

    private PostOffice createPostOffice() {
        var postOffice = Instancio.of(PostOffice.class)
                .ignore(Select.field(PostOffice::getId))
                .create();
        return postOffice;
    }

    private Mailing createMailing(int index) {
        var mailing = Instancio.of(Mailing.class)
                .ignore(Select.field(Mailing::getId))
                .supply(Select.field(Mailing::getPostOffices), () -> new ArrayList<>(List.of(postOfficeList.get(index))))
                .supply(Select.field(Mailing::getCurrentPostOffice), () -> postOfficeList.get(index))
                .supply(Select.field(Mailing::isReceived), () -> false)
                .create();
        return mailing;
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

        for (int i = 0; i < 5; i++) {
            var mailing = createMailing(i);
            mailingRepository.save(mailing);
            mailingList.add(mailing);
        }
    }
    @AfterEach
    public void afterEach() {
        mailingRepository.deleteAll();
        postOfficeRepository.deleteAll();
    }
    @Test
    public void createMailinTestPositive() throws Exception {
        MailingDto newMailing = Instancio.of(MailingDto.class)
                .supply(Select.field(MailingDto::getRecipientPostalCode), () -> postOfficeList.get(4).getPostalCode())
                .create();
        var request = post("/post/"+ postOfficeList.get(0).getPostalCode() + "/mailing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newMailing));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();

        assertThat(body).contains(newMailing.getRecipientName(), newMailing.getRecipientAddress());
        assertThat(mailingRepository.findMailingByRecipientName(newMailing.getRecipientName())).isNotNull();
    }

    @Test
    public void departureMailingTestPositive() throws Exception {

        var request = patch("/post/"+ postOfficeList.get(0).getPostalCode() + "/departure/" + mailingList.get(0).getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var resultMailing = om.readValue(result.getContentAsString(), Mailing.class);

        assertThat(resultMailing.getCurrentPostOffice()).isNull();
        assertThat(resultMailing.getStatus()).isEqualTo(MailingStatus.IN_TRANSIT);
        assertThat(resultMailing.getPostOffices()).contains(postOfficeList.get(0));
    }

    @Test
    public void arrivalMailingTestPositive() throws Exception {
        Mailing newMailing = createMailing(0);
        newMailing.setCurrentPostOffice(null);
        mailingRepository.save(newMailing);

        var request = patch("/post/"+ postOfficeList.get(1).getPostalCode() + "/arrival/" + newMailing.getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var resultMailing = om.readValue(result.getContentAsString(), Mailing.class);

        assertThat(resultMailing.getCurrentPostOffice()).isEqualTo(postOfficeList.get(1));
        assertThat(resultMailing.getStatus()).isEqualTo(MailingStatus.AT_INTERMEDIATE_POST_OFFICE);
        assertThat(resultMailing.getPostOffices()).contains(postOfficeList.get(0));
    }

    @Test
    public void getMailingTestPositive() throws Exception {

        var request = get("/mailing/" + mailingList.get(0).getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var resultMailing = om.readValue(result.getContentAsString(), Mailing.class);

        assertThat(resultMailing).isEqualTo(mailingList.get(0));
    }

    @Test
    public void receiptMailingTestPositive() throws Exception {
        Mailing newMailing = createMailing(0);
        newMailing.setCurrentPostOffice(postOfficeList.get(3));
        newMailing.setRecipientPostalCode(postOfficeList.get(3).getPostalCode());
        mailingRepository.save(newMailing);

        var request = patch("/mailing/" + newMailing.getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        var resultMailing = om.readValue(result.getContentAsString(), Mailing.class);

        assertThat(resultMailing.isReceived()).isTrue();
        assertThat(resultMailing.getStatus()).isEqualTo(MailingStatus.IS_RECEIVED);
    }

    @Test
    public void createMailinTestNegative() throws Exception {
        MailingDto newMailing = Instancio.of(MailingDto.class)
                .supply(Select.field(MailingDto::getRecipientPostalCode), () -> postOfficeList.get(4).getPostalCode())
                .create();
        var request = post("/post/-1/mailing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newMailing));

        var result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        var message = result.getErrorMessage();

        assertThat(message).contains("Post office with this id not found");

    }

    @Test
    public void departureMailingTestNegative() throws Exception {
        Mailing newMailing = createMailing(0);
        newMailing.setCurrentPostOffice(null);
        mailingRepository.save(newMailing);
        var request = patch("/post/" + postOfficeList.get(0).getPostalCode() + "/departure/" + newMailing.getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();

        assertThat(body).contains("Mailing in transit");
    }

    @Test
    public void MailingInDifferentPostOfficeTestNegative() throws Exception {
        var request = patch("/post/" + postOfficeList.get(3).getPostalCode() + "/departure/" + mailingList.get(0).getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();

        assertThat(body).contains("This parcel is in a different post office.");
    }

    @Test
    public void arrivalMailingTestNegative() throws Exception {

        var request = patch("/post/"+ postOfficeList.get(0).getPostalCode() + "/arrival/" + mailingList.get(0).getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();

        assertThat(body).contains("Mailing has already been to this post office");
    }

    @Test
    public void getMailingTestNegative() throws Exception {

        var request = get("/mailing/-1");

        var result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        var message = result.getErrorMessage();

        assertThat(message).contains("Mailing with this id not found");
    }

    @Test
    public void receiptMailingTestNegative() throws Exception {
        Mailing newMailing = createMailing(0);
        newMailing.setCurrentPostOffice(postOfficeList.get(3));
        newMailing.setRecipientPostalCode(postOfficeList.get(1).getPostalCode());
        mailingRepository.save(newMailing);

        var request = patch("/mailing/" + newMailing.getMailingCode());

        var result = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        var body = result.getContentAsString();

        assertThat(body).contains("Mailing is at another post office");
    }
}
