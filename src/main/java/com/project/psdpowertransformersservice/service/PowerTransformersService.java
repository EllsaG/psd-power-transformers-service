package com.project.psdpowertransformersservice.service;


import com.project.psdpowertransformersservice.calculation.TransformerSelectionCalculation;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformerByIdResponseDTO;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformersResponseDTO;
import com.project.psdpowertransformersservice.entity.PowerTransformers;
import com.project.psdpowertransformersservice.entity.TransformerSelection;
import com.project.psdpowertransformersservice.exceptions.InformationAlreadyExistsException;
import com.project.psdpowertransformersservice.exceptions.InformationNotFoundException;
import com.project.psdpowertransformersservice.repository.PowerTransformerRepository;
import com.project.psdpowertransformersservice.repository.TransformerSelectionRepository;
import com.project.psdpowertransformersservice.rest.FullInformationResponseDTO;
import com.project.psdpowertransformersservice.rest.FullInformationServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerTransformersService {

    private final FullInformationServiceClient fullInformationServiceClient;
    private final TransformerSelectionRepository transformerSelectionRepository;
    private final PowerTransformerRepository powerTransformerRepository;

    @Autowired
    public PowerTransformersService(FullInformationServiceClient fullInformationServiceClient, TransformerSelectionRepository transformerSelectionRepository,
                                    PowerTransformerRepository powerTransformerRepository) {
        this.fullInformationServiceClient = fullInformationServiceClient;
        this.transformerSelectionRepository = transformerSelectionRepository;
        this.powerTransformerRepository = powerTransformerRepository;
    }


    public PowerTransformersResponseDTO savePowerTransformer(short powerTransformerId, String modelOfTransformer, float fullPowerOfTransformer,
                                                             float shortCircuitVoltage, float idleLossesOfTransformer, float highSideVoltage,
                                                             float lowSideVoltage, float shortCircuitLosses, float idleCurrent) {

        if (powerTransformerRepository.existsById(powerTransformerId)) {
            throw new InformationAlreadyExistsException("Information about equipment with id № " + powerTransformerId + " is already exists");
        }

        TransformerSelectionCalculation transformerSelectionCalculation = new TransformerSelectionCalculation();
        FullInformationResponseDTO fullInformationById = fullInformationServiceClient.getFullInformationById(powerTransformerId);
        PowerTransformers newTransformer = transformerSelectionCalculation.createNewPowerTransformer(powerTransformerId, modelOfTransformer, fullPowerOfTransformer,
                shortCircuitVoltage, idleLossesOfTransformer, highSideVoltage, lowSideVoltage, shortCircuitLosses, idleCurrent,
                fullInformationById, transformerSelectionRepository);

        powerTransformerRepository.save(newTransformer);

        return new PowerTransformersResponseDTO(getAllPowerTransformers(), getAllForChoosePowerTransformers());

    }

    public void savePowerTransformerSelectionInformation(short powerTransformereSelectionId, float maxFullPower) {

        if (transformerSelectionRepository.existsById(powerTransformereSelectionId)) {
            throw new InformationAlreadyExistsException("Information about equipment with id № " + powerTransformereSelectionId + " is already exists");
        }

        TransformerSelectionCalculation transformerSelectionCalculation = new TransformerSelectionCalculation();
        TransformerSelection powerTransformerSelectionInformation = transformerSelectionCalculation
                .createPowerTransformerSelectionInformation(powerTransformereSelectionId, maxFullPower);

        transformerSelectionRepository.save(powerTransformerSelectionInformation);
    }

    public PowerTransformersResponseDTO updatePowerTransformer(short powerTransformerId, String modelOfTransformer, float fullPowerOfTransformer,
                                                               float shortCircuitVoltage, float idleLossesOfTransformer, float highSideVoltage,
                                                               float lowSideVoltage, float shortCircuitLosses, float idleCurrent) {
        deletePowerTransformerById(powerTransformerId);
        return savePowerTransformer(powerTransformerId, modelOfTransformer, fullPowerOfTransformer, shortCircuitVoltage,
                idleLossesOfTransformer, highSideVoltage, lowSideVoltage, shortCircuitLosses, idleCurrent);
    }

    public PowerTransformersResponseDTO deletePowerTransformerById(short powerTransformerId) {
        if (powerTransformerRepository.existsById(powerTransformerId)) {
            powerTransformerRepository.deleteById(powerTransformerId);
        } else {
            throw new InformationNotFoundException("Unable to find information about the power transformer. Check the availability of this equipment.");
        }

        return new PowerTransformersResponseDTO(getAllPowerTransformers(), getAllForChoosePowerTransformers());
    }

    public void deletePowerTransformerSelectionInformationById(short powerTransformerId) {
        if (transformerSelectionRepository.existsById(powerTransformerId)) {
            transformerSelectionRepository.deleteById(powerTransformerId);
        }
    }

    public PowerTransformerByIdResponseDTO getPowerTransformerInformationById(short powerTransformerId) {
        PowerTransformers powerTransformers = powerTransformerRepository.findById(powerTransformerId)
                .orElseThrow(() -> new InformationNotFoundException("Unable to find information about power transformer with id № " + powerTransformerId));
        return new PowerTransformerByIdResponseDTO(powerTransformers);
    }

    public List<PowerTransformers> getAllPowerTransformers() {
        return powerTransformerRepository.findAll();
    }

    public List<TransformerSelection> getAllForChoosePowerTransformers() {
        return transformerSelectionRepository.findAll();
    }

    public Boolean isAvailable(short powerTransformersSelectionId) {
        return powerTransformerRepository.existsById(powerTransformersSelectionId);
    }

}
