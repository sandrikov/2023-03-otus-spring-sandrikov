package ru.otus.coursework.staff.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.coursework.staff.misc.TestUtil;

class JobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = new Job();
        job1.setJobId("XX_XXX");
        Job job2 = new Job();
        job2.setJobId(job1.getJobId());
        assertThat(job1).isEqualTo(job2);
        job2.setJobId("YY_YYY");
        assertThat(job1).isNotEqualTo(job2);
        job1.setJobId(null);
        assertThat(job1).isNotEqualTo(job2);
    }
}
