package com.project.psdpowertransformersservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PowerTransformersRequestDTO {
    @JsonProperty("powerTransformersId")
    private short powerTransformersId;
    @JsonProperty("transformerModel")
    private String transformerModel;
    @JsonProperty("transformerFullPower")
    private float transformerFullPower;
    @JsonProperty("shortCircuitVoltage")
    private float shortCircuitVoltage;
    @JsonProperty("transformerIdleLosses")
    private float transformerIdleLosses;
    @JsonProperty("highSideVoltage")
    private float highSideVoltage;
    @JsonProperty("lowSideVoltage")
    private float lowSideVoltage;
    @JsonProperty("shortCircuitLosses")
    private float shortCircuitLosses;
    @JsonProperty("idleCurrent")
    private float idleCurrent;


}
