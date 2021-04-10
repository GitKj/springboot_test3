package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHistory() throws Exception {

        // user posts the string
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=TextUserInputs")).andReturn();

        // then users clicks history and the string should be there
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL)).andExpect(content().string(containsString("TextUserInputs")));

        // then they go to delete and type in the substring and then the page should contain a string saying it was deleted
        mockMvc.perform(MockMvcRequestBuilders.get("/delete?post_text=TextUserInputs").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("The requested post has been deleted from history.")));

        // then when they click to history and it should not be there
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL)).andExpect(content().string(not(containsString("TextUserInputs"))));

    }


    // Write a unit test to check if the delete functionality is case-sensitive.
    @Test
    void testCaseSensitive() throws Exception {

        // post string and then go to history page to check that the page contains the string
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=TEXTUSERINPUTS")).andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL)).andExpect(content().string(containsString("TEXTUSERINPUTS")));

        // go to delete page and type string wanting to be deleted but in all lowercase (so it should NOT exist)
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=textuserinputs").contentType(MediaType.ALL)).andExpect(content().string(containsString("does not exist")));

        // this should pass because the string DOES exist
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=TEXTUSERINPUTS").contentType(MediaType.ALL)).andExpect(content().string(containsString("The requested post has been deleted from history.")));

        // so it is case sensitive
    }


}