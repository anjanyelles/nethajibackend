package com.nethaji.config;

import com.nethaji.Enums.SubjectType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class LectureDetailsSubjectTypeConstraintUpdater implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public LectureDetailsSubjectTypeConstraintUpdater(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            String regclass = jdbcTemplate.queryForObject("select to_regclass('public.lecture_details')", String.class);
            if (regclass == null) {
                return;
            }

            String allowed = Arrays.stream(SubjectType.values())
                    .map(Enum::name)
                    .map(v -> "'" + v + "'")
                    .collect(Collectors.joining(", "));

            String drop = "ALTER TABLE lecture_details DROP CONSTRAINT IF EXISTS lecture_details_subject_type_check";
            String add = "ALTER TABLE lecture_details ADD CONSTRAINT lecture_details_subject_type_check CHECK (subject_type::text = ANY (ARRAY[" + allowed + "]))";

            jdbcTemplate.execute(drop);
            jdbcTemplate.execute(add);
        } catch (Exception ignored) {
            return;
        }
    }
}
