package com.batuhanseyrek.rezarvasyonSistemi.Service;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoAdmin;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.AdminService;
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
public class AdminServiceTest {
    @InjectMocks
    private AdminService adminService;
    @Mock
    private AdminRepository adminRepository;
    @Test
    void testAdminList_returnsListOfDtoAdmins(){
        Admin admin1=new Admin();
        admin1.setAdminName("Admin1");
        admin1.setId(1L);
        Admin admin2=new Admin();
        admin2.setAdminName("Admin2");
        admin2.setId(2L);
        List<Admin> admins=List.of(admin1,admin2);
        DtoAdmin adminDto1=new DtoAdmin();
        adminDto1.setAdminName("Admin1");
        DtoAdmin adminDto2=new DtoAdmin();
        adminDto2.setAdminName("Admin2");
        Mockito.when(adminRepository.findAll()).thenReturn(admins);
        try (MockedStatic<DtoConverter> mockStatic = Mockito.mockStatic(DtoConverter.class)) {
            mockStatic.when(() -> DtoConverter.toDto(admin1)).thenReturn(adminDto1);
            mockStatic.when(() -> DtoConverter.toDto(admin2)).thenReturn(adminDto2);

            List<DtoAdmin> result=adminService.adminList();

            assertEquals(2, result.size());
            assertEquals("Masa 1",result.get(0).getAdminName());
            assertEquals("Masa 2",result.get(1).getAdminName());

            verify(adminRepository).findAll();

            mockStatic.verify(() -> DtoConverter.toDto(admin1));
            mockStatic.verify(() -> DtoConverter.toDto(admin2));

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
