package com.example.genie.domain.pot.repository;

import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.entity.QPot;
import com.example.genie.domain.pot.entity.State;
import com.example.genie.domain.pot.form.PotSearchForm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public Page<Pot> findListBySearch(PotSearchForm potSearchForm, Pageable pageable) {
        List<Pot> potList = jpaQueryFactory
                .select(QPot.pot).distinct()
                .from(QPot.pot)
                .leftJoin(QPot.pot.master).fetchJoin() // master를 함께 조회해 매핑 시 N+1 방지
                .where(containWordInPot(potSearchForm.getSearchType(), potSearchForm.getSearchText()),
                        getOttType(potSearchForm.getOttType()),  QPot.pot.state.eq(State.RECRUITING))
                .orderBy(QPot.pot.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = getCount(potSearchForm);
        return new PageImpl<>(potList, pageable, count);
    }

    private Long getCount(PotSearchForm potSearchForm) {
        Long count = jpaQueryFactory
                .select(QPot.pot.count())
                .from(QPot.pot)
                .where(containWordInPot(potSearchForm.getSearchType(), potSearchForm.getSearchText()),
                        getOttType(potSearchForm.getOttType()),  QPot.pot.state.eq(State.RECRUITING))
                .fetchOne();
        return count;
    }
    private BooleanExpression getOttType(String ottType) {
        if (ottType == null || ottType.equals("all")) {
            return null; // all 또는 null이면 전체 데이터를 반환
        }
        return QPot.pot.ottType.eq(ottType);
    }

    private BooleanBuilder containWordInPot(String searchType, String searchText) {
        if (searchType != null) {
            if (searchType.equals("potName"))
                return containWordInPotName(searchText);
            else if (searchType.equals("remain"))
                return containWordInRemain(searchText);
            else
                return containWordInTerm(searchText);
        } else {
            return new BooleanBuilder();
        }
    }

    private BooleanBuilder containWordInPotName(String word) {
        if (hasText(word)) {
            return new BooleanBuilder(QPot.pot.potName.contains(word));
        } else {
            return new BooleanBuilder();
        }
    }

    private BooleanBuilder containWordInRemain(String word) {
        if (hasText(word)) {
            return new BooleanBuilder(QPot.pot.remain.eq(Integer.valueOf(word)));
        } else {
            return new BooleanBuilder();
        }
    }

    private BooleanBuilder containWordInTerm(String word) {
        if (hasText(word)) {
            return new BooleanBuilder(QPot.pot.term.eq(Integer.valueOf(word)));
        } else {
            return new BooleanBuilder();
        }
    }
}
