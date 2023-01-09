package com.kyu0.jungo.rest.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyu0.jungo.rest.post.Post.SaveRequest;
import com.kyu0.jungo.utils.WithCustomMockUser;

@WebMvcTest(PostApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PostControllerTest {
    
    private final static String URL = "/api/posts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("게시물 생성 테스트 - 1, 성공")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 게시물_생성_테스트_1() throws Exception {
        final SaveRequest REQUEST_DTO = new SaveRequest("kyu0", "테스트", "테스트용 게시물입니다.", 1);

        when(postService.save(refEq(REQUEST_DTO)))
            .thenReturn(1L);

        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.response").value(1L))
        .andDo(print());

        verify(postService).save(refEq(REQUEST_DTO));
    }

    @Test
    @DisplayName("게시물 생성 테스트 - 2, 실패, 카테고리 미입력")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 게시물_생성_테스트_2() throws Exception {
        final SaveRequest REQUEST_DTO = new SaveRequest("kyu0", "테스트", "테스트용 게시물입니다.", null);

        when(postService.save(refEq(REQUEST_DTO)))
            .thenThrow(new EntityNotFoundException("카테고리를 입력해주세요."));

        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(false))
        .andDo(print());

        verify(postService).save(refEq(REQUEST_DTO));
    }

    @Test
    @DisplayName("게시물 생성 테스트 - 3")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 게시물_생성_테스트_3() throws Exception {
        final SaveRequest REQUEST_DTO = new SaveRequest("kyu0", null, "테스트용 게시물입니다.", 1);

        when(postService.save(refEq(REQUEST_DTO)))
            .thenThrow(new ValidationException("제목을 입력해주세요."));

        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(false))
        .andDo(print());

        verify(postService).save(refEq(REQUEST_DTO));
    }
}
