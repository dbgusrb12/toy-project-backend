package com.hg.blog.domain.keyword.service;

import com.hg.blog.domain.keyword.entity.KeywordCustomRepository;
import com.hg.blog.domain.keyword.projection.KeywordGrouping;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeywordQueryService {

    private final KeywordCustomRepository keywordCustomRepository;

    public List<KeywordGrouping> getKeywordTop10() {
        return keywordCustomRepository.getKeywordGroupingListTop10();
    }
}
