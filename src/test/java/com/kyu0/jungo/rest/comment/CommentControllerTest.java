package com.kyu0.jungo.rest.comment;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityNotFoundException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyu0.jungo.rest.comment.Comment.SaveRequest;
import com.kyu0.jungo.rest.member.Member;
import com.kyu0.jungo.rest.post.Post;
import com.kyu0.jungo.utils.WithCustomMockUser;

@WebMvcTest(CommentApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class CommentControllerTest {
        
    private final static String URL = "/api/comments";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성 테스트 - 1, 성공")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 댓글_생성_테스트_1() throws Exception {
        final SaveRequest REQUEST_DTO = new SaveRequest("테스트 댓글입니다.", "kyu0", 1L);

        when(commentService.save(refEq(REQUEST_DTO)))
            .thenReturn(new Comment(1L, "테스트 댓글입니다.", new Member("kyu0", null, null, null), new Post(1L, null, null, 0, false, null, null, null, null)));


        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.response").value(1L))
        .andDo(print());

        verify(commentService).save(refEq(REQUEST_DTO));
    }

    @Test
    @DisplayName("댓글 생성 테스트 - 2, 실패")
    @WithCustomMockUser(username = "kyu0", roles = {"USER"})
    public void 댓글_생성_테스트_2() throws Exception {
        final SaveRequest REQUEST_DTO = new SaveRequest("테스트 댓글입니다.", "kyu0", 1L);

        when(commentService.save(refEq(REQUEST_DTO)))
            .thenThrow(new EntityNotFoundException("해당 게시물을 찾을 수 없습니다."));

        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(false))
        .andDo(print());

        verify(commentService).save(refEq(REQUEST_DTO));
    }
}