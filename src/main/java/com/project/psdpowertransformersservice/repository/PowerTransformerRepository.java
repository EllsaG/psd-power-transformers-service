package com.project.psdpowertransformersservice.repository;

import com.project.psdpowertransformersservice.entity.PowerTransformers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerTransformerRepository extends JpaRepository<PowerTransformers,Short> {
}
