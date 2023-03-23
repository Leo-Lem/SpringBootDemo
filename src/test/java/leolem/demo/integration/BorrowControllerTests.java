package leolem.demo.integration;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityNotFoundException;

import leolem.demo.borrow.BorrowController;
import leolem.demo.borrow.BorrowService;

@WebMvcTest(controllers = BorrowController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BorrowControllerTests {

  @Autowired
  private MockMvc mockMVC;

  @MockBean
  private BorrowService borrowService;

  @Test
  void givenExists_whenGetting_thenReturns200() throws Exception {
    when(borrowService.verifyStatus(1, 1))
      .thenReturn(true);

    mockMVC.perform(get("/borrow/1/1"))
        .andExpect(status().isOk());
  }

  @Test
  void givenExistsNot_whenGetting_thenReturns404() throws Exception {
    when(borrowService.verifyStatus(1, 1))
      .thenReturn(false);

    mockMVC.perform(get("/borrow/1/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void whenPosting_thenReturns200() throws Exception {
    mockMVC.perform(post("/borrow/1/1"))
        .andExpect(status().isOk());
  }

  @Test
  void givenUserOrBookExistNot_whenPosting_thenReturns200() throws Exception {
    doThrow(new EntityNotFoundException())
        .when(borrowService)
        .borrowBook(1, 1);

    mockMVC.perform(post("/borrow/1/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void givenIllegalState_whenPosting_thenReturns400() throws Exception {
    doThrow(new IllegalStateException())
        .when(borrowService)
        .borrowBook(1, 1);

    mockMVC.perform(post("/borrow/1/1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void givenExists_whenDeleting_thenReturns200() throws Exception {
    mockMVC.perform(delete("/borrow/1/1"))
        .andExpect(status().isOk());
  }

  @Test
  void givenExistsNot_whenDeleting_thenReturns404() throws Exception {
    doThrow(new EntityNotFoundException())
        .when(borrowService)
        .returnBook(1, 1);

    mockMVC.perform(delete("/borrow/1/1"))
        .andExpect(status().isNotFound());
  }

}
