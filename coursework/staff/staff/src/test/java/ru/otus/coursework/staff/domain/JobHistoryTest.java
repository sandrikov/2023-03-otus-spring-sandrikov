package ru.otus.coursework.staff.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.coursework.staff.misc.TestUtil;


class JobHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobHistory.class);
        JobHistory jobHistory1 = new JobHistory();
        jobHistory1.setJobHistoryId(1L);
        JobHistory jobHistory2 = new JobHistory();
        jobHistory2.setJobHistoryId(jobHistory1.getJobHistoryId());
        assertThat(jobHistory1).isEqualTo(jobHistory2);
        jobHistory2.setJobHistoryId(2L);
        assertThat(jobHistory1).isNotEqualTo(jobHistory2);
        jobHistory1.setJobHistoryId(null);
        assertThat(jobHistory1).isNotEqualTo(jobHistory2);
    }
}
