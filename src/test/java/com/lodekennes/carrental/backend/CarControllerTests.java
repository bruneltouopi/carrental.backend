package com.lodekennes.carrental.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.ContentResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTests {

    private static final String PATH = "/api/v1/cars";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getEmpty() throws Exception {
        //mockMvc.
    }

    @Test
    public void postOne() throws Exception {
        MockHttpServletRequestBuilder p = post(PATH).content("{ 'name': 'Toyota V2','pricePerDay': 20}").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(p).andExpect(status().isOk());
        ContentResultMatchers c = content();
        String s = c.toString();
        //s.
    }
}
