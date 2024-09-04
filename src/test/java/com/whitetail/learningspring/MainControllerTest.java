package com.whitetail.learningspring;

import com.whitetail.learningspring.controller.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("1")
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController controller;

    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/nav/div/div[2]/div").string("1"));
    }

    @Test
    public void messageListTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messagesContainer']/div").nodeCount(4));
    }

    @Test
    public void filterMessageTest() throws Exception {
        this.mockMvc.perform(get("/main").param("tag", "tag"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messagesContainer']/div").nodeCount(2))
                .andExpect(xpath("//div[@id='messagesContainer']//div[@class='card-body']").nodeCount(2))
                .andExpect(xpath("//div[@id='messagesContainer']//div[@data-id='1']").exists())
                .andExpect(xpath("//div[@id='messagesContainer']/div/div/div[@data-id='1']").exists())
                .andExpect(xpath("//div[@id='messagesContainer']//div[@class='card-body' and @data-id='3']").exists());
    }

    @Test
    public void addMessageToListTest() throws Exception {
        this.mockMvc.perform(
                multipart("/main")
                        .file("file", "123".getBytes())
                        .param("text", "new msg")
                        .param("tag", "newTag")
                        .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messagesContainer']/div").nodeCount(5))
                .andExpect(xpath("//div[@id='messagesContainer']//div[@class='card-body' and @data-id='10']").exists())
                .andExpect(xpath("//div[@id='messagesContainer']//div[@data-id='10']/p").string("new msg"))
                .andExpect(xpath("//div[@id='messagesContainer']//div[@data-id='10']/a[@id='msg_tag']").string("newTag"));
    }

}
