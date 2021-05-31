package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.Test01DTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Test01} and its DTO {@link Test01DTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface Test01Mapper extends EntityMapper<Test01DTO, Test01> {}
