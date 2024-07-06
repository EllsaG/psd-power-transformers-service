package com.project.psdpowertransformersservice.entity;

import lombok.*;

import jakarta.persistence.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "transformer_selection")
public class TransformerSelection {
    @Id
    @Column(name = "transformer_selection_id")
    private short transformerSelectionId;
    @Column(name = "rated_power_for_transformer_selection")
    private float ratedPowerForTransformerSelection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransformerSelection that = (TransformerSelection) o;
        return transformerSelectionId == that.transformerSelectionId && Float.compare(ratedPowerForTransformerSelection, that.ratedPowerForTransformerSelection) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transformerSelectionId, ratedPowerForTransformerSelection);
    }
}
