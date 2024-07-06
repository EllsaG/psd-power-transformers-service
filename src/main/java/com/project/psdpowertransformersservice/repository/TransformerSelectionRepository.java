package com.project.psdpowertransformersservice.repository;

import com.project.psdpowertransformersservice.entity.TransformerSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransformerSelectionRepository extends JpaRepository<TransformerSelection,Short> {
}
