package com.hg.blog.api.keyword.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.KEYWORD_API;

import com.hg.blog.api.keyword.dto.KeywordDto.GetKeywordList;
import com.hg.blog.api.keyword.service.KeywordService;
import com.hg.blog.response.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + KEYWORD_API)
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping("")
    public Response<List<GetKeywordList>> getKeywords() {
        return Response.of(keywordService.getKeywords());
    }
}
