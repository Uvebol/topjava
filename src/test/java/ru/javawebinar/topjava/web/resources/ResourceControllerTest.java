package ru.javawebinar.topjava.web.resources;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    void getStyle() throws Exception{
        String expectedContentType = "text/css;charset=UTF-8";
        String actualContentType  = perform(get("/resources/css/style.css"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentType();

        Assertions.assertEquals(expectedContentType, actualContentType);
    }
}
