package com.batuhanseyrek.rezarvasyonSistemi.Service;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ChairRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.ChairService;
import org.assertj.core.internal.ErrorMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChairServiceTest {
    @InjectMocks
    private ChairService chairService;
    @Mock
    private ChairRepository chairRepository;
    @Test
    void testChairList_returnsListOfDtoChairs(){
        Chair chair1=new Chair();
        chair1.setChairName("Masa 1");
        chair1.setId(1L);

        Chair chair2= new Chair();
        chair2.setId(2L);
        chair2.setChairName("Masa 2");

        List<Chair> chairs= List.of(chair1,chair2);

        DtoChair dto1=new DtoChair();
        dto1.setChairName("Masa 1");

        DtoChair dto2=new DtoChair();
        dto2.setChairName("Masa 2");

        Mockito.when(chairRepository.findAll()).thenReturn(chairs);

        try (MockedStatic<DtoConverter> mockStatic = Mockito.mockStatic(DtoConverter.class)) {
            mockStatic.when(() -> DtoConverter.toDto(chair1)).thenReturn(dto1);
            mockStatic.when(() -> DtoConverter.toDto(chair2)).thenReturn(dto2);

            List<DtoChair> result=chairService.chairList();

            assertEquals(2, result.size());
            assertEquals("Masa 1",result.get(0).getChairName());
            assertEquals("Masa 2",result.get(1).getChairName());

            verify(chairRepository).findAll();

            mockStatic.verify(() -> DtoConverter.toDto(chair1));
            mockStatic.verify(() -> DtoConverter.toDto(chair2));

        }
catch (Exception e){
            throw new RuntimeException(e);
        }


    }

}
