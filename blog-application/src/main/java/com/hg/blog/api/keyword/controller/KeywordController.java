package com.hg.blog.api.keyword.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.KEYWORD_API;

import com.hg.blog.api.keyword.dto.KeywordDto.GetKeywordList;
import com.hg.blog.api.keyword.service.KeywordService;
import com.hg.blog.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + KEYWORD_API)
@Tag(name = "keyword")
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping("")
    @Operation(description = "많이 검색한 키워드 10개 조회")
    public Response<List<GetKeywordList>> getKeywords() {
        return Response.of(keywordService.getKeywords());
    }
}
