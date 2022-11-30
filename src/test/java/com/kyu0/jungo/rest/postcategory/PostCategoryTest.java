package com.kyu0.jungo.rest.postcategory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.ArrayList;
import java.util.Optional;

import javax.validation.ValidationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyu0.jungo.rest.postcategory.PostCategory.ModifyRequest;
import com.kyu0.jungo.rest.postcategory.PostCategory.SaveRequest;

@WebMvcTest(PostCategoryApiController.class)
public class PostCategoryTest {
    
    private final static String URL = "/api/post-category";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PostCategoryService postCategoryService;

    @Test
    @DisplayName("카테고리 생성 테스트 - 1")
    @WithMockUser(roles = {"ADMIN"})
    public void 카테고리_생성_테스트_1() throws Exception {
        final String TEST_NAME = "삽니다";
        final SaveRequest REQUEST_DTO = new SaveRequest(TEST_NAME);
        
        // given
        when(postCategoryService.save(refEq(REQUEST_DTO)))
            .thenReturn(new PostCategory(1, TEST_NAME, new ArrayList<>()));

        
        // when
        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        /** then
         * 성공, TEST_NAME의 값과 같은지 확인 
         */
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.response.name").value(TEST_NAME))
        .andDo(print());

        verify(postCategoryService).save(refEq(REQUEST_DTO));
    }

    @Test
    @DisplayName("카테고리 생성 테스트 - 2")
    @WithMockUser(roles = {"ADMIN"})
    public void 카테고리_생성_테스트_2() throws Exception {
        final String TEST_NAME = "삽니다123847123";
        final SaveRequest REQUEST_DTO = new SaveRequest(TEST_NAME);

        // given
        when(postCategoryService.save(refEq(REQUEST_DTO)))
            .thenThrow(new ValidationException("카테고리 이름은 10자 이하로 입력해주세요."));
        
        // when
        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        /** then
        * 실패
        */
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.response").doesNotExist())
        .andDo(print());

        verify(postCategoryService).save(refEq(REQUEST_DTO));
    }

    @Test
    @DisplayName("카테고리 생성 테스트 - 3")
    @WithMockUser(roles = {"ADMIN"})
    public void 카테고리_생성_테스트_3() throws Exception {
        final String TEST_NAME = "";
        final SaveRequest REQUEST_DTO = new SaveRequest(TEST_NAME);

        // given
        when(postCategoryService.save(refEq(REQUEST_DTO)))
            .thenThrow(new ValidationException("카테고리의 이름을 입력해주세요."));

        // when
        mockMvc.perform(post(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        
        /** then
         * 실패
         */
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.response").doesNotExist())
        .andDo(print());

        verify(postCategoryService).save(refEq(REQUEST_DTO));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void 카테고리_조회_테스트_1() throws Exception {
        final Integer TEST_ID = 1;

        /** given
         * id : 1, name : test, posts : []
         */
        when(postCategoryService.findById(TEST_ID))
            .thenReturn(Optional.of(new PostCategory(TEST_ID, "test", new ArrayList<>())));
        
        // when
        mockMvc.perform(get(URL + '/' + TEST_ID))
        
        /** then
         * 성공
         */
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.response").exists())
        .andExpect(jsonPath("$.response.id").value(TEST_ID))
        .andExpect(jsonPath("$.response.name").value("test"))
        .andDo(print());

        verify(postCategoryService).findById(TEST_ID);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void 카테고리_조회_테스트_2() throws Exception {
        // given
        when(postCategoryService.findAll())
            .thenReturn(new ArrayList<>());

        // when
        mockMvc.perform(get(URL))
        
        /** then
         * 실패
         */
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.response").isArray())
        .andDo(print());

        verify(postCategoryService).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void 카테고리_수정_테스트_1() throws Exception {
        final int TEST_ID = 1;
        final String MODIFIED_NAME = "수정했어요";
        final ModifyRequest REQUEST_DTO = new ModifyRequest(TEST_ID, MODIFIED_NAME);
        final PostCategory RESULT = new PostCategory(TEST_ID, MODIFIED_NAME, new ArrayList<>());

        // given
        when(postCategoryService.modify(refEq(REQUEST_DTO)))
            .thenReturn(RESULT);

        // when
        mockMvc.perform(put(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        /** then
         * 성공
         */
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.response.id").value(TEST_ID))
        .andExpect(jsonPath("$.response.name").value(MODIFIED_NAME))
        .andDo(print());

        verify(postCategoryService).modify(refEq(REQUEST_DTO));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void 카테고리_수정_테스트_2() throws Exception {
        final int TEST_ID = 1;
        final String MODIFIED_NAME = "수정했어요98273";
        final ModifyRequest REQUEST_DTO = new ModifyRequest(TEST_ID, MODIFIED_NAME);
        
        // given
        when(postCategoryService.modify(refEq(REQUEST_DTO)))
            .thenThrow(new ValidationException("카테고리의 이름은 10자 이하로 입력해주세요."));

        // when
        mockMvc.perform(put(URL)
            .content(mapper.writeValueAsString(REQUEST_DTO))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))

        /** then
         * 
         */
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.response").doesNotExist())
        .andDo(print());

        verify(postCategoryService).modify(refEq(REQUEST_DTO));
    }
}
