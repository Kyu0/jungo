package com.kyu0.jungo.rest.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyu0.jungo.rest.category.Category;
import com.kyu0.jungo.rest.member.Member;
import com.kyu0.jungo.rest.post.Post.DeleteRequest;
import com.kyu0.jungo.rest.post.Post.ModifyRequest;
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
    @DisplayName("게시물 생성 테스트 - 3, 실패, 제목 미입력")
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

    @Test
    @DisplayName("게시물 수정 테스트 - 1, 성공")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 게시물_수정_테스트_1() throws Exception {
        final ModifyRequest REQUEST_DTO = new ModifyRequest(1L, "테스트", "테스트용 게시물입니다.", "kyu0", false, 1);
        
        when(postService.modify(refEq(REQUEST_DTO)))
            .thenReturn(new Post(1L, "테스트", "테스트용 게시물입니다.", 0, false, new Category(1, "테스트용 카테고리", new ArrayList<>()), new Member("kyu0", "URL", null, null), new ArrayList<>(), new ArrayList<>()));

        mockMvc.perform(put(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.response.title").value("테스트"))
            .andDo(print());

        verify(postService).modify(refEq(REQUEST_DTO));
    }

    @Test
    @DisplayName("게시물 수정 테스트 - 2, 실패")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 게시물_수정_테스트_2() throws Exception {
        final ModifyRequest REQUEST_DTO = new ModifyRequest(1L, "테스트", "테스트용 게시물입니다.", "kyu0", false, 1);
        
        when(postService.modify(refEq(REQUEST_DTO)))
            .thenThrow(new AccessDeniedException("작성자는 변경할 수 없습니다."));

        mockMvc.perform(put(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(false))
            .andDo(print());

        verify(postService).modify(refEq(REQUEST_DTO));

    }

    @Test
    @DisplayName("게시물 삭제 테스트 - 1, 성공")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 게시물_삭제_테스트_1() throws Exception {
        final DeleteRequest REQUEST_DTO = new DeleteRequest(1L, "kyu0");

        when(postService.delete(refEq(REQUEST_DTO)))
            .thenReturn(true);

        mockMvc.perform(delete(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andDo(print());

        verify(postService).delete(refEq(REQUEST_DTO));
    }

    @Test
    @DisplayName("게시물 삭제 테스트 -2, 실패")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 게시물_삭졔_테스트_2() throws Exception {
        final DeleteRequest REQUEST_DTO = new DeleteRequest(1L, "kyu0");

        when(postService.delete(refEq(REQUEST_DTO)))
            .thenThrow(new AccessDeniedException("작성자만 삭제할 수 있습니다."));

        mockMvc.perform(delete(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(false))
            .andDo(print());

        verify(postService).delete(refEq(REQUEST_DTO));

    }
}
