package ru.otus.homework.butterflygarden.services;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.butterflygarden.domain.Butterfly;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Pupa;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Import({LifeCycleServiceImpl.class})
public class LifeCycleServiceImplTest {

    @Autowired
    private LifeCycleService lifeCycleService;

    @Test
    void growPupa() {
        val caterpillar = new Caterpillar("familyX", 1);
        val pupa = lifeCycleService.growPupa(caterpillar);
        assertEquals(caterpillar, pupa.parent());
    }

    @Test
    void growButterfly() {
        Pupa pupa = new Pupa(new Caterpillar("familyZ", 6));
        Butterfly butterfly = lifeCycleService.growButterfly(pupa);
        assertEquals(pupa, butterfly.parent());
    }
}