package com.project.psdpowertransformersservice.controller;


import com.project.psdpowertransformersservice.controller.dto.PowerTransformerByIdResponseDTO;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformerSelectionInformationRequestDTO;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformersRequestDTO;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformersResponseDTO;
import com.project.psdpowertransformersservice.service.PowerTransformersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PowerTransformersController {

    private final PowerTransformersService powerTransformersService;

    @Autowired
    public PowerTransformersController(PowerTransformersService powerTransformersService) {
        this.powerTransformersService = powerTransformersService;
    }


    @GetMapping("/getAllInformation")
    public PowerTransformersResponseDTO getAllInformation() {
        return new PowerTransformersResponseDTO(powerTransformersService.getAllPowerTransformers(),
                powerTransformersService.getAllForChoosePowerTransformers());
    }

    @GetMapping("/{powerTransformerId}")
    public PowerTransformerByIdResponseDTO getPowerTransformerById(@PathVariable short powerTransformerId) {
        return powerTransformersService.getPowerTransformerInformationById(powerTransformerId);
    }

    @PutMapping("/create/powerTransformer")
    public PowerTransformersResponseDTO createPowerTransformer(@RequestBody PowerTransformersRequestDTO powerTransformersRequestDTO) {
        return powerTransformersService.savePowerTransformer(powerTransformersRequestDTO.getPowerTransformersId(),
                powerTransformersRequestDTO.getTransformerModel(), powerTransformersRequestDTO.getTransformerFullPower(),
                powerTransformersRequestDTO.getShortCircuitVoltage(), powerTransformersRequestDTO.getTransformerIdleLosses(),
                powerTransformersRequestDTO.getHighSideVoltage(), powerTransformersRequestDTO.getLowSideVoltage(),
                powerTransformersRequestDTO.getShortCircuitLosses(), powerTransformersRequestDTO.getIdleCurrent());
    }

    @PutMapping("/create/selectionInformation")
    public void createPowerTransformerSelectionInformation(
            @RequestBody PowerTransformerSelectionInformationRequestDTO powerTransformersRequestDTO) {
        powerTransformersService.savePowerTransformerSelectionInformation(
                powerTransformersRequestDTO.getPowerTransformerSelectionId(),
                powerTransformersRequestDTO.getRatedPowerForTransformerSelection());
    }

    @PutMapping("/update/powerTransformer")
    public PowerTransformersResponseDTO updatePowerTransformer(@RequestBody PowerTransformersRequestDTO powerTransformersRequestDTO) {
        return powerTransformersService.updatePowerTransformer(powerTransformersRequestDTO.getPowerTransformersId(), powerTransformersRequestDTO.getTransformerModel(),
                powerTransformersRequestDTO.getTransformerFullPower(), powerTransformersRequestDTO.getShortCircuitVoltage(),
                powerTransformersRequestDTO.getTransformerIdleLosses(), powerTransformersRequestDTO.getHighSideVoltage(),
                powerTransformersRequestDTO.getLowSideVoltage(), powerTransformersRequestDTO.getShortCircuitLosses(),
                powerTransformersRequestDTO.getIdleCurrent());
    }

    @DeleteMapping("/delete/protectiveEquipment/{powerTransformerId}")
    public PowerTransformersResponseDTO deletePowerTransformer(@PathVariable short powerTransformerId) {
        return powerTransformersService.deletePowerTransformerById(powerTransformerId);
    }

    @DeleteMapping("/delete/selectionInformation/{powerTransformerSelectionId}")
    public void deletePowerTransformerSelectionInformationById(@PathVariable short powerTransformerSelectionId) {
        powerTransformersService.deletePowerTransformerSelectionInformationById(powerTransformerSelectionId);
    }

    @GetMapping("/check/{powerTransformerSelectionId}")
    public Boolean checkAvailability(@PathVariable short powerTransformerSelectionId) {
        return powerTransformersService.isAvailable(powerTransformerSelectionId);
    }


}
