package com.example.thymeleaf;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void indexShowsWelcomeMessage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Hello Thymeleaf")));
    }

    @Test
    void personListShowsInitialPersons() throws Exception {
        mvc.perform(get("/personList"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bill")))
                .andExpect(content().string(containsString("Jobs")));
    }

    @Test
    void addPersonRedirectsToList() throws Exception {
        mvc.perform(post("/addPerson").param("firstName", "Linus").param("lastName", "Torvalds"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/personList"));

        mvc.perform(get("/personList"))
                .andExpect(content().string(containsString("Torvalds")));
    }

    @Test
    void addPersonWithEmptyFieldShowsError() throws Exception {
        mvc.perform(post("/addPerson").param("firstName", "").param("lastName", "X"))
                .andExpect(status().isOk())
                .andExpect(view().name("addPerson"))
                .andExpect(content().string(containsString("Last Name is required!")));
    }
}
