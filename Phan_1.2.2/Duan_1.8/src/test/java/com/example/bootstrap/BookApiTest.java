package com.example.bootstrap;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BookApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void homePageShowsAppNameAndBooks() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bootstrap Spring Boot")))
                .andExpect(content().string(containsString("Clean Code")));
    }

    @Test
    void anyoneCanListBooks() throws Exception {
        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(3)));
    }

    @Test
    void findByTitle() throws Exception {
        mvc.perform(get("/api/books/title/Effective Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Joshua Bloch"));
    }

    @Test
    void unknownIdReturns404() throws Exception {
        mvc.perform(get("/api/books/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithoutLoginIsRejected() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"No Auth\",\"author\":\"X\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void normalUserCannotCreate() throws Exception {
        mvc.perform(post("/api/books").with(httpBasic("user", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"User Book\",\"author\":\"X\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreateUpdateDelete() throws Exception {
        String body = mvc.perform(post("/api/books").with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Domain-Driven Design\",\"author\":\"Eric Evans\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        long id = Long.parseLong(body.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mvc.perform(put("/api/books/" + id).with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":" + id + ",\"title\":\"DDD\",\"author\":\"Eric Evans\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("DDD"));

        mvc.perform(delete("/api/books/" + id).with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk());
        mvc.perform(get("/api/books/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWithMismatchedIdReturns400() throws Exception {
        mvc.perform(put("/api/books/1").with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2,\"title\":\"X\",\"author\":\"Y\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateTitleReturns400() throws Exception {
        mvc.perform(post("/api/books").with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Clean Code\",\"author\":\"Someone\"}"))
                .andExpect(status().isBadRequest());
    }
}
