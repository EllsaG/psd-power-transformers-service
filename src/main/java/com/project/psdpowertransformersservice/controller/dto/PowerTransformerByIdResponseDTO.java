package com.project.psdpowertransformersservice.controller.dto;

import com.project.psdpowertransformersservice.entity.PowerTransformers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PowerTransformerByIdResponseDTO {
    PowerTransformers powerTransformers;
}
