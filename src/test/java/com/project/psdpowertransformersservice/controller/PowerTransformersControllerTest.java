package com.project.psdpowertransformersservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.psdpowertransformersservice.config.SpringH2DatabaseConfig;
import com.project.psdpowertransformersservice.PsdPowerTransformersServiceApplication;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformerByIdResponseDTO;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformerSelectionInformationRequestDTO;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformersRequestDTO;
import com.project.psdpowertransformersservice.controller.dto.PowerTransformersResponseDTO;
import com.project.psdpowertransformersservice.entity.PowerTransformers;
import com.project.psdpowertransformersservice.entity.TransformerSelection;
import com.project.psdpowertransformersservice.repository.PowerTransformerRepository;
import com.project.psdpowertransformersservice.repository.TransformerSelectionRepository;
import com.project.psdpowertransformersservice.rest.FullInformationResponseDTO;
import com.project.psdpowertransformersservice.rest.FullInformationServiceClient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {PsdPowerTransformersServiceApplication.class, SpringH2DatabaseConfig.class})
public class PowerTransformersControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TransformerSelectionRepository transformerSelectionRepository;

    @Autowired
    private PowerTransformerRepository powerTransformerRepository;

    @MockBean
    private FullInformationServiceClient fullInformationServiceClient;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @Sql(scripts = {"/sql/clearPowerTransformerSelectionInformationDB.sql"})
    public void createPowerTransformerSelectionInformation() throws Exception {

        String REQUEST = createPowerTransformerSelectionInformationRequestAsString();

        mockMvc.perform(put("/create/selectionInformation")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        TransformerSelection transformerSelection = transformerSelectionRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(132.17F, transformerSelection.getRatedPowerForTransformerSelection());
    }

    @Test
    @Sql(scripts = {"/sql/clearPowerTransformerSelectionInformationDB.sql","/sql/clearPowerTransformersDB.sql",
            "/sql/addPowerTransformerSelectionInformation.sql"})
    public void createPowerTransformer() throws Exception {

        Mockito.when(fullInformationServiceClient.getFullInformationById(ArgumentMatchers.anyShort())).thenReturn(createFullInformationResponseDTO());

        String REQUEST = createPowerTransformersRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/create/powerTransformer")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        PowerTransformersResponseDTO powerTransformersResponseDTO = objectMapper.readValue(body, PowerTransformersResponseDTO.class);
        PowerTransformers powerTransformers = powerTransformersResponseDTO.getPowerTransformersList().get(0);

        PowerTransformers powerTransformersById = powerTransformerRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(powerTransformers.getTransformerModel(), powerTransformersById.getTransformerModel());
        Assertions.assertEquals(powerTransformers.getTransformerFullPower(), powerTransformersById.getTransformerFullPower());
        Assertions.assertEquals(powerTransformers.getTransformerLoadCoef(), powerTransformersById.getTransformerLoadCoef());
        Assertions.assertEquals(powerTransformers.getShortCircuitVoltage(), powerTransformersById.getShortCircuitVoltage());
        Assertions.assertEquals(powerTransformers.getTransformerIdleLosses(), powerTransformersById.getTransformerIdleLosses());
        Assertions.assertEquals(powerTransformers.getHighSideVoltage(), powerTransformersById.getHighSideVoltage());
        Assertions.assertEquals(powerTransformers.getLowSideVoltage(), powerTransformersById.getLowSideVoltage());
        Assertions.assertEquals(powerTransformers.getShortCircuitLosses(), powerTransformersById.getShortCircuitLosses());
        Assertions.assertEquals(powerTransformers.getIdleCurrent(), powerTransformersById.getIdleCurrent());
    }

    @Test
    @Sql(scripts = {"/sql/clearPowerTransformerSelectionInformationDB.sql","/sql/clearPowerTransformersDB.sql",
            "/sql/addPowerTransformerSelectionInformation.sql", "/sql/addPowerTransformersInformation.sql"})
    public void updatePowerTransformer() throws Exception {

        Mockito.when(fullInformationServiceClient.getFullInformationById(ArgumentMatchers.anyShort())).thenReturn(createFullInformationResponseDTO());

        String REQUEST = createPowerTransformersUpdateRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/update/powerTransformer")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        PowerTransformersResponseDTO powerTransformersResponseDTO = objectMapper.readValue(body, PowerTransformersResponseDTO.class);
        PowerTransformers powerTransformersResponse = powerTransformersResponseDTO.getPowerTransformersList().get(0);

        PowerTransformers powerTransformersById = powerTransformerRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(powerTransformersResponse.getTransformerModel(), powerTransformersById.getTransformerModel());
        Assertions.assertEquals(powerTransformersResponse.getTransformerFullPower(), powerTransformersById.getTransformerFullPower());
        Assertions.assertEquals(powerTransformersResponse.getTransformerLoadCoef(), powerTransformersById.getTransformerLoadCoef());
        Assertions.assertEquals(powerTransformersResponse.getShortCircuitVoltage(), powerTransformersById.getShortCircuitVoltage());
        Assertions.assertEquals(powerTransformersResponse.getTransformerIdleLosses(), powerTransformersById.getTransformerIdleLosses());
        Assertions.assertEquals(powerTransformersResponse.getHighSideVoltage(), powerTransformersById.getHighSideVoltage());
        Assertions.assertEquals(powerTransformersResponse.getLowSideVoltage(), powerTransformersById.getLowSideVoltage());
        Assertions.assertEquals(powerTransformersResponse.getShortCircuitLosses(), powerTransformersById.getShortCircuitLosses());
        Assertions.assertEquals(powerTransformersResponse.getIdleCurrent(), powerTransformersById.getIdleCurrent());
    }

    @Test
    @Sql(scripts = {"/sql/clearPowerTransformersDB.sql", "/sql/addPowerTransformersInformation.sql"})
    public void deletePowerTransformer() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/delete/protectiveEquipment/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        PowerTransformersResponseDTO powerTransformersResponseDTO = objectMapper.readValue(body, PowerTransformersResponseDTO.class);
        PowerTransformers powerTransformersResponse = powerTransformersResponseDTO.getPowerTransformersList().get(0);

        List<PowerTransformers> powerTransformerRepositoryAll = powerTransformerRepository.findAll();
        PowerTransformers powerTransformersRepository = powerTransformerRepositoryAll.get(0);

        Assertions.assertEquals(powerTransformersResponse.getPowerTransformersId(), powerTransformersRepository.getPowerTransformersId());
        Assertions.assertEquals(powerTransformersResponse.getTransformerModel(), powerTransformersRepository.getTransformerModel());
        Assertions.assertEquals(powerTransformersResponse.getTransformerFullPower(), powerTransformersRepository.getTransformerFullPower());
        Assertions.assertEquals(powerTransformersResponse.getTransformerLoadCoef(), powerTransformersRepository.getTransformerLoadCoef());
        Assertions.assertEquals(powerTransformersResponse.getShortCircuitVoltage(), powerTransformersRepository.getShortCircuitVoltage());
        Assertions.assertEquals(powerTransformersResponse.getTransformerIdleLosses(), powerTransformersRepository.getTransformerIdleLosses());
        Assertions.assertEquals(powerTransformersResponse.getHighSideVoltage(), powerTransformersRepository.getHighSideVoltage());
        Assertions.assertEquals(powerTransformersResponse.getLowSideVoltage(), powerTransformersRepository.getLowSideVoltage());
        Assertions.assertEquals(powerTransformersResponse.getShortCircuitLosses(), powerTransformersRepository.getShortCircuitLosses());
        Assertions.assertEquals(powerTransformersResponse.getIdleCurrent(), powerTransformersRepository.getIdleCurrent());
    }

    @Test
    @Sql(scripts = {"/sql/clearPowerTransformerSelectionInformationDB.sql",
            "/sql/addPowerTransformerSelectionInformation.sql"})
    public void deletePowerTransformerSelectionInformationById() throws Exception {


        mockMvc.perform(delete("/delete/selectionInformation/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        List<TransformerSelection> transformerSelectionRepositoryAll = transformerSelectionRepository.findAll();
        TransformerSelection transformerSelectionRepository = transformerSelectionRepositoryAll.get(0);

        Assertions.assertEquals(3, transformerSelectionRepository.getTransformerSelectionId());
        Assertions.assertEquals(132.17F, transformerSelectionRepository.getRatedPowerForTransformerSelection());
    }

    @Test
    @Sql(scripts = { "/sql/clearPowerTransformersDB.sql", "/sql/addPowerTransformersInformation.sql"})
    public void getPowerTransformerById() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        PowerTransformerByIdResponseDTO powerTransformerByIdResponseDTO = objectMapper.readValue(body, PowerTransformerByIdResponseDTO.class);
        PowerTransformers powerTransformersResponseOne = powerTransformerByIdResponseDTO.getPowerTransformers();


        PowerTransformers powerTransformerRepositoryById = powerTransformerRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(powerTransformersResponseOne.getTransformerModel(), powerTransformerRepositoryById.getTransformerModel());
        Assertions.assertEquals(powerTransformersResponseOne.getTransformerFullPower(), powerTransformerRepositoryById.getTransformerFullPower());
        Assertions.assertEquals(powerTransformersResponseOne.getTransformerLoadCoef(), powerTransformerRepositoryById.getTransformerLoadCoef());
        Assertions.assertEquals(powerTransformersResponseOne.getShortCircuitVoltage(), powerTransformerRepositoryById.getShortCircuitVoltage());
        Assertions.assertEquals(powerTransformersResponseOne.getTransformerIdleLosses(), powerTransformerRepositoryById.getTransformerIdleLosses());
        Assertions.assertEquals(powerTransformersResponseOne.getHighSideVoltage(), powerTransformerRepositoryById.getHighSideVoltage());
        Assertions.assertEquals(powerTransformersResponseOne.getLowSideVoltage(), powerTransformerRepositoryById.getLowSideVoltage());
        Assertions.assertEquals(powerTransformersResponseOne.getShortCircuitLosses(), powerTransformerRepositoryById.getShortCircuitLosses());
        Assertions.assertEquals(powerTransformersResponseOne.getIdleCurrent(), powerTransformerRepositoryById.getIdleCurrent());

    }


    @Test
    @Sql(scripts = {"/sql/clearPowerTransformerSelectionInformationDB.sql", "/sql/clearPowerTransformersDB.sql",
            "/sql/addPowerTransformerSelectionInformation.sql", "/sql/addPowerTransformersInformation.sql"})
    public void getAllInformation() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/getAllInformation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        PowerTransformersResponseDTO powerTransformersResponseDTO = objectMapper.readValue(body, PowerTransformersResponseDTO.class);
        PowerTransformers powerTransformersResponseOne = powerTransformersResponseDTO.getPowerTransformersList().get(0);
        PowerTransformers powerTransformersResponseTwo = powerTransformersResponseDTO.getPowerTransformersList().get(1);

        TransformerSelection transformerSelectionResponseOne = powerTransformersResponseDTO.getTransformerSelectionList().get(0);
        TransformerSelection transformerSelectionResponseTwo = powerTransformersResponseDTO.getTransformerSelectionList().get(1);

        List<PowerTransformers> powerTransformerRepositoryAll = powerTransformerRepository.findAll();
        PowerTransformers powerTransformersRepositoryOne = powerTransformerRepositoryAll.get(0);
        PowerTransformers powerTransformersRepositoryTwo = powerTransformerRepositoryAll.get(1);

        List<TransformerSelection> transformerSelectionRepositoryAll = transformerSelectionRepository.findAll();
        TransformerSelection transformerSelectionRepositoryOne = transformerSelectionRepositoryAll.get(0);
        TransformerSelection transformerSelectionRepositoryTwo = transformerSelectionRepositoryAll.get(1);

        Assertions.assertEquals(transformerSelectionResponseOne.getTransformerSelectionId(),
                transformerSelectionRepositoryOne.getTransformerSelectionId());
        Assertions.assertEquals(transformerSelectionResponseOne.getRatedPowerForTransformerSelection(),
                transformerSelectionRepositoryOne.getRatedPowerForTransformerSelection());

        Assertions.assertEquals(transformerSelectionResponseTwo.getTransformerSelectionId(),
                transformerSelectionRepositoryTwo.getTransformerSelectionId());
        Assertions.assertEquals(transformerSelectionResponseTwo.getRatedPowerForTransformerSelection(),
                transformerSelectionRepositoryTwo.getRatedPowerForTransformerSelection());

        Assertions.assertEquals(powerTransformersResponseOne.getTransformerModel(), powerTransformersRepositoryOne.getTransformerModel());
        Assertions.assertEquals(powerTransformersResponseOne.getTransformerFullPower(), powerTransformersRepositoryOne.getTransformerFullPower());
        Assertions.assertEquals(powerTransformersResponseOne.getTransformerLoadCoef(), powerTransformersRepositoryOne.getTransformerLoadCoef());
        Assertions.assertEquals(powerTransformersResponseOne.getShortCircuitVoltage(), powerTransformersRepositoryOne.getShortCircuitVoltage());
        Assertions.assertEquals(powerTransformersResponseOne.getTransformerIdleLosses(), powerTransformersRepositoryOne.getTransformerIdleLosses());
        Assertions.assertEquals(powerTransformersResponseOne.getHighSideVoltage(), powerTransformersRepositoryOne.getHighSideVoltage());
        Assertions.assertEquals(powerTransformersResponseOne.getLowSideVoltage(), powerTransformersRepositoryOne.getLowSideVoltage());
        Assertions.assertEquals(powerTransformersResponseOne.getShortCircuitLosses(), powerTransformersRepositoryOne.getShortCircuitLosses());
        Assertions.assertEquals(powerTransformersResponseOne.getIdleCurrent(), powerTransformersRepositoryOne.getIdleCurrent());

        Assertions.assertEquals(powerTransformersResponseTwo.getTransformerModel(), powerTransformersRepositoryTwo.getTransformerModel());
        Assertions.assertEquals(powerTransformersResponseTwo.getTransformerFullPower(), powerTransformersRepositoryTwo.getTransformerFullPower());
        Assertions.assertEquals(powerTransformersResponseTwo.getTransformerLoadCoef(), powerTransformersRepositoryTwo.getTransformerLoadCoef());
        Assertions.assertEquals(powerTransformersResponseTwo.getShortCircuitVoltage(), powerTransformersRepositoryTwo.getShortCircuitVoltage());
        Assertions.assertEquals(powerTransformersResponseTwo.getTransformerIdleLosses(), powerTransformersRepositoryTwo.getTransformerIdleLosses());
        Assertions.assertEquals(powerTransformersResponseTwo.getHighSideVoltage(), powerTransformersRepositoryTwo.getHighSideVoltage());
        Assertions.assertEquals(powerTransformersResponseTwo.getLowSideVoltage(), powerTransformersRepositoryTwo.getLowSideVoltage());
        Assertions.assertEquals(powerTransformersResponseTwo.getShortCircuitLosses(), powerTransformersRepositoryTwo.getShortCircuitLosses());
        Assertions.assertEquals(powerTransformersResponseTwo.getIdleCurrent(), powerTransformersRepositoryTwo.getIdleCurrent());
    }

    @Test
    @Sql(scripts = {"/sql/clearPowerTransformersDB.sql", "/sql/addPowerTransformersInformation.sql"})
    public void checkAvailability() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/check/3"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        boolean fromRepository = powerTransformerRepository.existsById((short) 3);
        String body = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        boolean fromResponse = objectMapper.readValue(body, Boolean.class);

        Assertions.assertEquals(fromRepository, fromResponse);
    }

    private FullInformationResponseDTO createFullInformationResponseDTO() {
        return new FullInformationResponseDTO((short) 3, "ШМА-1", (short) 3, 0.94F,
                1.09F, (short) 3, 3.23F, 3.04F, 1.09F, 3.23F,
                4.91F, 16.5F, 0.65F, 1.16F, 0.06F, 1);
    }

    private String createPowerTransformersRequestAsString() throws JsonProcessingException {
        PowerTransformersRequestDTO request =
                new PowerTransformersRequestDTO((short) 3, "TMG-1000", 250.0F,
                        5.5F, 1400.0F, 10.0F, 0.4F,
                        10.5F, 0.5F);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(request);
    }

    private String createPowerTransformersUpdateRequestAsString() throws JsonProcessingException {
        PowerTransformersRequestDTO request =
                new PowerTransformersRequestDTO((short) 3, "TMG-400", 400.0F,
                        5.5F, 2000.0F, 10.0F, 0.4F,
                        10.5F, 1.0F);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(request);
    }

    private String createPowerTransformerSelectionInformationRequestAsString() throws JsonProcessingException {
        PowerTransformerSelectionInformationRequestDTO request =
                new PowerTransformerSelectionInformationRequestDTO((short) 3, 565.8F);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(request);
    }


}
