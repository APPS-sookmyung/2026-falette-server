package com.falette.backend.web.reading.dto;

import com.falette.backend.domain.reading.ColorLevel;
import com.falette.backend.domain.reading.ColorOption;
import com.falette.backend.domain.reading.ColorReason;
import com.falette.backend.domain.reading.ConcernType;
import jakarta.validation.constraints.NotNull;

public record ReadingCreateRequest(
        @NotNull ConcernType concernType,
        @NotNull ColorOption color,
        @NotNull ColorLevel brightnessLevel,
        @NotNull ColorLevel saturationLevel,
        @NotNull ColorReason colorReason,
        @NotNull Long concernOption1Id,
        @NotNull Long concernOption2Id,
        @NotNull Long tarotCardId
) {
}