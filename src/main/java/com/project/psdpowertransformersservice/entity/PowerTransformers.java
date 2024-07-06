package com.project.psdpowertransformersservice.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "power_transformers")
public class PowerTransformers {

    @Id
    @Column(name = "power_transformers_id")
    private short powerTransformersId;
    @Column(name = "transformer_model")
    private String transformerModel;
    @Column(name = "transformer_full_power")
    private float transformerFullPower;
    @Column(name = "transformer_load_coef")
    private float transformerLoadCoef;
    @Column(name = "short_circuit_voltage")
    private float shortCircuitVoltage;
    @Column(name = "transformer_idle_losses")
    private float transformerIdleLosses;
    @Column(name = "high_side_voltage")
    private float highSideVoltage;
    @Column(name = "low_side_voltage")
    private float lowSideVoltage;
    @Column(name = "short_circuit_losses")
    private float shortCircuitLosses;
    @Column(name = "idle_current")
    private float idleCurrent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerTransformers that = (PowerTransformers) o;
        return powerTransformersId == that.powerTransformersId && Float.compare(transformerFullPower, that.transformerFullPower) == 0 && Float.compare(transformerLoadCoef, that.transformerLoadCoef) == 0 && Float.compare(shortCircuitVoltage, that.shortCircuitVoltage) == 0 && Float.compare(transformerIdleLosses, that.transformerIdleLosses) == 0 && Float.compare(highSideVoltage, that.highSideVoltage) == 0 && Float.compare(lowSideVoltage, that.lowSideVoltage) == 0 && Float.compare(shortCircuitLosses, that.shortCircuitLosses) == 0 && Float.compare(idleCurrent, that.idleCurrent) == 0 && Objects.equals(transformerModel, that.transformerModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(powerTransformersId, transformerModel, transformerFullPower, transformerLoadCoef, shortCircuitVoltage, transformerIdleLosses, highSideVoltage, lowSideVoltage, shortCircuitLosses, idleCurrent);
    }
}
