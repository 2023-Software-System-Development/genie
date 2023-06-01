package com.example.genie.domain.pot.repository;

import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.entity.QPot;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class PotRepositoryImpl implements PotRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Pot> findListBySearch(String searchText, Pageable pageable) {
        List<Pot> potList = jpaQueryFactory
                .select(QPot.pot).distinct()
                .from(QPot.pot)
                .where(containWordInPot(searchText))
                .orderBy(QPot.pot.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return getPageImpl(potList, pageable);
    }

    private <T> Page<T> getPageImpl(List<T> list, Pageable pageable) {
        boolean hasNext = false;
        if (list.size() > pageable.getPageSize() + 1) {
            hasNext = true;
            list.remove(list.size() - 1);
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    private BooleanBuilder containWordInPot(String word) {
        if (hasText(word)) {
            return new BooleanBuilder(QPot.pot.potName.contains(word));
        } else {
            return new BooleanBuilder();
        }
    }
}
