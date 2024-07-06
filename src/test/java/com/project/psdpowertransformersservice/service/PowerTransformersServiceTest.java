package com.project.psdpowertransformersservice.service;


import com.project.psdpowertransformersservice.calculation.TransformerSelectionCalculation;
import com.project.psdpowertransformersservice.entity.TransformerSelection;
import com.project.psdpowertransformersservice.repository.PowerTransformerRepository;
import com.project.psdpowertransformersservice.repository.TransformerSelectionRepository;
import com.project.psdpowertransformersservice.rest.FullInformationServiceClient;
import com.project.psdpowertransformersservice.service.PowerTransformersService;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PowerTransformersServiceTest {
    @Mock
    private  FullInformationServiceClient fullInformationServiceClient;
    @Mock
    private  TransformerSelectionRepository transformerSelectionRepository;
    @Mock
    private  PowerTransformerRepository powerTransformerRepository;
    @InjectMocks
    private PowerTransformersService powerTransformersService;

    @Test
    public void powerTransformersService_createPowerTransformerSelectionInformation(){

        TransformerSelectionCalculation transformerSelectionCalculation = new TransformerSelectionCalculation();

        TransformerSelection powerTransformerSelectionInformation = transformerSelectionCalculation
                .createPowerTransformerSelectionInformation((short) 1, 565.8F);

        Assertions.assertEquals(132.17F,powerTransformerSelectionInformation.getRatedPowerForTransformerSelection());
    }


}
