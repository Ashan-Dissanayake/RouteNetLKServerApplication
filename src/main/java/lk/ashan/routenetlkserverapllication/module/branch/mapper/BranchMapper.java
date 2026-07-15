package lk.ashan.routenetlkserverapllication.module.branch.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between Branch entities and their corresponding DTOs.
 * Utilizes MapStruct for automatic mapping and includes additional mappers for related entities.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        BranchTypeMapper.class, BranchStatusMapper.class, RegionalOfficeMapper.class,
})
public interface BranchMapper {

    /**
     * Converts a Branch entity to a BranchDetailResponseDto.
     *
     * @param branch the Branch entity to convert
     * @return the corresponding BranchDetailResponseDto
     */
    BranchDetailResponseDto toDto(Branch branch);

    /**
     * Converts a list of Branch entities to a list of BranchDetailResponseDto.
     *
     * @param branches the list of Branch entities to convert
     * @return the corresponding list of BranchDetailResponseDto
     */
    List<BranchDetailResponseDto> toDtoList(List<Branch> branches);

    /**
     * Converts a Branch entity to a BranchSummaryDto.
     *
     * @param branch the Branch entity to convert
     * @return the corresponding BranchSummaryDto
     */
    @Mapping(target = "regionalOfficeId", source = "regionaloffice.id")
    BranchSummaryDto toSummaryDto(Branch branch);

    /**
     * Converts a list of Branch entities to a list of BranchSummaryDto.
     *
     * @param branches the list of Branch entities to convert
     * @return the corresponding list of BranchSummaryDto
     */
    List<BranchSummaryDto> toSummaryDtolList(List<Branch> branches);

    /**
     * Converts a BranchCreateRequestDto to a Branch entity.
     *
     * @param request the BranchCreateRequestDto containing the data
     * @return the corresponding Branch entity
     */
    Branch toEntity(BranchCreateRequestDto request);

    // Branch toEntity(BranchUpdateRequestDto request);

    /**
     * Updates an existing Branch entity with data from a BranchUpdateRequestDto.
     * Ignores certain fields during the update process.
     *
     * @param dto    the BranchUpdateRequestDto containing the updated data
     * @param entity the existing Branch entity to update
     * @return the updated Branch entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchstatus", ignore = true)
    @Mapping(target = "branchtype", ignore = true)
    @Mapping(target = "regionaloffice", ignore = true)
    Branch updateEntityFromDto(BranchUpdateRequestDto dto, @MappingTarget Branch entity);
}
