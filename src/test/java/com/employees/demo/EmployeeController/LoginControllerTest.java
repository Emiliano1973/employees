package com.employees.demo.EmployeeController;

import com.employees.demo.controllers.LoginController;
import com.employees.demo.dtos.JjwtResponse;
import com.employees.demo.dtos.LoginRequestDto;
import com.employees.demo.dtos.SignUpDto;
import com.employees.demo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoginController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class LoginControllerTest {

    private static final String TEST_USERNAME="username";
    private static final String TEST_PASSWORD="password";

    private static final String TEST_TOKEN="token";

    private static final String TEST_EMAIL="EMAIL@EMAIL.IE";

    private static final String[] TEST_ROLES=new String[]{"ADMIN", "USER"};
    private static final ObjectMapper objectMapper=new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  UserService userService;


    @Test
    public void whenUserAndPasswordSentTheUserShouldBeAutenthicated() throws Exception{
        LoginRequestDto loginRequest=getLoginRequest();
        JjwtResponse jjwtResponse=getJjwtResponse();
        when(this.userService.authenticateUser(loginRequest)).thenReturn(jjwtResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest)
                )).andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.token").value(TEST_TOKEN))
                .andExpect(jsonPath("$.roles[0]").value(TEST_ROLES[0]))
                .andExpect(jsonPath("$.roles[1]").value(TEST_ROLES[1]));

    }


    @Test
    public void whenUserAndPasswordAreWrongTheUserShouldNotBeAutenthicated() throws Exception{
        LoginRequestDto loginRequest=getLoginRequest();
        JjwtResponse jjwtResponse=getJjwtResponse();
        when(this.userService.authenticateUser(loginRequest)).thenThrow(new BadCredentialsException("Wrong credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest)
                        )).andDo(print()).
                andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Wrong credentials"));


    }



    @Test
    public void aNewSiteUserShouldBeAdded() throws Exception{
        SignUpDto signUpDto=getSignUpDto();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(signUpDto)
                        )).andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User "+TEST_USERNAME+" is added"));

        verify(this.userService).registerUser(signUpDto);

    }


    private LoginRequestDto getLoginRequest(){
      LoginRequestDto loginRequestDto=  new LoginRequestDto(TEST_USERNAME, TEST_PASSWORD);
      return loginRequestDto;
    }

    private SignUpDto getSignUpDto(){
       return new SignUpDto(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD, TEST_ROLES);
    }

    private JjwtResponse getJjwtResponse(){
        return new JjwtResponse(TEST_TOKEN, TEST_USERNAME, TEST_EMAIL, TEST_ROLES);
    }



    public static  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
