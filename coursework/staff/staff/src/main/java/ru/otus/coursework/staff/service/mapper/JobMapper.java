package ru.otus.coursework.staff.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;

import ru.otus.coursework.staff.domain.Job;
import ru.otus.coursework.staff.service.dto.JobDto;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDto}.
 */
@Mapper(componentModel = SPRING, uses = DictionaryMapper.class)
public interface JobMapper extends EntityMapper<JobDto, Job> {
}
