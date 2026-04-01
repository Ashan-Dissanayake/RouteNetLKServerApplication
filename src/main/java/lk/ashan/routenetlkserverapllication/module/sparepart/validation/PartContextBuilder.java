package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartContextBuilder {

    public PartContext buildForCreate(
            PartCreateRequestDto dto) {

        return PartContext.builder()
                .qoh(dto.getQoh())
                .rop(dto.getRop())
                .maxlevel(dto.getMaxlevel())
                .build();
    }

    public PartContext buildForUpdate(
            PartUpdateRequestDto dto,
            Part existingPart) {

        return PartContext.builder()
                .partId(existingPart.getId())
                .qoh(dto.getQoh())
                .rop(dto.getRop())
                .maxlevel(dto.getMaxlevel())
                .existingQoh(existingPart.getQoh())
                .build();
    }
}
