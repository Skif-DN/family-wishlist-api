package com.skif.familywishlist.controller;

import com.skif.familywishlist.domain.Person;
import com.skif.familywishlist.domain.Wish;
import com.skif.familywishlist.dto.wish.WishRequestDTO;
import com.skif.familywishlist.service.PersonService;
import com.skif.familywishlist.service.WishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@WebMvcTest(WishController.class)
@AutoConfigureMockMvc(addFilters = false)
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishService wishService;

    @MockBean
    private PersonService personService;

    @Test
    void createWish_shouldReturnCreatedWish() throws Exception {
        UUID wishId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Wish wish = new Wish();
        wish.setId(wishId);
        wish.setTitle("Bike");
        wish.setDescription("Mountain Bike");

        Person owner = new Person();
        owner.setId(ownerId);
        wish.setOwner(owner);

        when(wishService.addWish(any(UUID.class), anyString(), anyString(), anyString())).thenReturn(wish);

        mockMvc.perform(post("/wishes")
        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "ownerId": "%s",
                                "pin": "1234",
                                "title": "Bike",
                                "description": "Mountain Bike"
                            }
                            """.formatted(ownerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Bike"));
    }

    @Test
    void createWish_shouldReturnForbidden_whenPinInvalid() throws Exception {
        UUID ownerId = UUID.randomUUID();

        when(wishService.addWish(any(UUID.class), anyString(), anyString(), anyString()))
                .thenThrow(new SecurityException("Invalid Pin"));

        mockMvc.perform(post("/wishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "ownerId": "%s",
                                "pin": "wrong-pin",
                                "title": "Bike",
                                "description": "Mountain Bike"
                            }
                            """.formatted(ownerId)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Invalid Pin"));

        verify(wishService).addWish(any(), eq("wrong-pin"), any(), any());
    }

    @Test
    void createWish_shouldReturnNotFound_whenOwnerDoesNotExist() throws Exception {
        UUID ownerId = UUID.randomUUID();

        when(wishService.addWish(any(UUID.class), anyString(), anyString(), anyString()))
        .thenThrow(new IllegalArgumentException("Owner not found"));

        mockMvc.perform(post("/wishes")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
                            {
                                "ownerId": "%s",
                                "pin": "1234",
                                "title": "Bike",
                                "description": "Mountain Bike"
                            }
                            """.formatted(ownerId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Owner not found"));
    }

    @Test
    void deleteWish_shouldDeleteWish() throws Exception {
        UUID wishId = UUID.randomUUID();

        doNothing().when(wishService).deleteWish(wishId, "1234");

        mockMvc.perform(delete("/wishes/{wishId}", wishId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "pin": "1234"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(wishService).deleteWish(wishId, "1234");
    }

    @Test
    void deleteWish_shouldReturnForbidden_whenPinInvalid() throws Exception {
        UUID wishId = UUID.randomUUID();

        doThrow(new SecurityException("Invalid Pin")).when(wishService).deleteWish(wishId, "1111");

        mockMvc.perform(delete("/wishes/{wishId}", wishId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                            "pin": "1111"
                        }   
                       """))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Invalid Pin"));
    }

    @Test
    void deleteWish_shouldReturnNotFound_whenOwnerDoesNotExist() throws Exception {
        UUID wishId = UUID.randomUUID();

        doThrow(new IllegalArgumentException("Wish not found")).when(wishService).deleteWish(wishId, "1234");

        mockMvc.perform(delete("/wishes/{wishId}", wishId)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
                        {
                            "pin": "1234"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wish not found"));
    }

    @Test
    void updateWish_shouldReturnUpdatedWish() throws Exception {
        UUID wishId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Person owner = new Person();
        owner.setId(ownerId);

        Wish updateWish = new Wish();
        updateWish.setId(wishId);
        updateWish.setOwner(owner);
        updateWish.setTitle("Updated title");
        updateWish.setDescription("Updated description");

        when(wishService.updateWish(eq(wishId), any())).thenReturn(updateWish);

        String requestJson = """
            {
                "ownerId": "%s",
                "pin": "1234",
                "title": "Updated title",
                "description": "Updated description"
            }
            """.formatted(ownerId);

        mockMvc.perform(put("/wishes/{wishId}", wishId)
            .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(wishService).updateWish(eq(wishId), any());
    }

    @Test
    void updateWish_shouldReturnForbidden_whenInvalidPin() throws Exception {
        UUID wishId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();



        when(wishService.updateWish(eq(wishId), any())).thenThrow(new SecurityException("Invalid Pin"));

        String requestJson = """
            {
                "ownerId": "%s",
                "pin": "1234",
                "title": "Updated title",
                "description": "Updated description"
            }    
        """.formatted(ownerId);

        mockMvc.perform(put("/wishes/{wishId}", wishId)
        .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Invalid Pin"));

        verify(wishService).updateWish(eq(wishId), any());
    }

    @Test
    void updateWish_shouldReturnNotFound_whenOwnerDoesNotExist() throws Exception {
        UUID wishId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        when(wishService.updateWish(eq(wishId), any())).thenThrow(new IllegalArgumentException("Wish not found"));

        String requestJson = """
            {
                "ownerId": "%s",
                "pin": "1234",
                "title": "Updated title",
                "description": "Updated description"
            }    
        """.formatted(ownerId);

        mockMvc.perform(put("/wishes/{wishId}", wishId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wish not found"));

        verify(wishService).updateWish(eq(wishId), any());
    }

    @Test
    void updateWish_shouldReturnBadRequest_whenTitleIsBlank()  throws Exception {
        UUID wishId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        String requestJson = """
            {
                "ownerId": "%s",
                "pin": "1234",
                "title": "",
                "description": "Updated description"
            }    
        """.formatted(ownerId);

        mockMvc.perform(put("/wishes/{wishId}", wishId)
        .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(wishService);
    }
}
