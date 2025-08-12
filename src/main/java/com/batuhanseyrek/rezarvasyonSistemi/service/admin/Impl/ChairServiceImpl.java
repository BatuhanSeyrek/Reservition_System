package com.batuhanseyrek.rezarvasyonSistemi.service.admin.Impl;

import com.batuhanseyrek.rezarvasyonSistemi.dto.DtoConverter;
import com.batuhanseyrek.rezarvasyonSistemi.dto.response.DtoChair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Admin;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Chair;
import com.batuhanseyrek.rezarvasyonSistemi.entity.adminEntity.Store;
import com.batuhanseyrek.rezarvasyonSistemi.repository.AdminRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.ChairRepository;
import com.batuhanseyrek.rezarvasyonSistemi.repository.StoreRepository;
import com.batuhanseyrek.rezarvasyonSistemi.service.admin.ChairService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChairServiceImpl implements ChairService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ChairRepository chairRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private DtoConverter dtoConverter;


    @Override
    public List<DtoChair> getChairsByAdmin(HttpServletRequest httpServletRequest) {
        Object attr = httpServletRequest.getAttribute("adminId");
        List<Chair> chairs = chairRepository.findByStoreId((Long) attr);
        return chairs.stream()
                .map(DtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DtoChair chairAdd(DtoChair request, HttpServletRequest httpRequest) {
        Object attr = httpRequest.getAttribute("adminId");
        if (attr == null) {
            throw new RuntimeException("Admin kimliği bulunamadı");
        }

        Long adminId = (Long) attr;
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin bulunamadı"));

        Store store = storeRepository.findById((Long) attr)
                .orElseThrow(() -> new RuntimeException("Store bulunamadı"));

        Chair chair = new Chair();
        chair.setChairName(request.getChairName());
        chair.setOpeningTime(request.getOpeningTime());
        chair.setClosingTime(request.getClosingTime());
        chair.setIslemSuresi(request.getIslemSuresi());
        chair.setAdmin(admin);
        chair.setStore(store);

        Chair savedChair = chairRepository.save(chair);

        // Store içindeki sandalye sayısını güncelle
        int chairCount = chairRepository.countByStoreId(store.getId());
        store.setChairCount(chairCount);
        storeRepository.save(store);

        return dtoConverter.toDto(savedChair);
    }

    @Override
    public List<DtoChair> chairList() {
        List<Chair> chairs = chairRepository.findAll();
        return chairs.stream()
                .map(DtoConverter::toDto)   // Class adıyla çağır, instance değil
                .collect(Collectors.toList());
    }

    @Override
    public void chairDelete(Long id) {
        Optional<Chair> chairOpt = chairRepository.findById(id);
        if (chairOpt.isPresent()) {
            Chair chair = chairOpt.get();
            Store store = chair.getStore();
            chairRepository.deleteById(id);

            // Chair silindiğinde sayıyı güncelle
            int chairCount = chairRepository.countByStoreId(store.getId());
            store.setChairCount(chairCount);
            storeRepository.save(store);
        } else {
            throw new RuntimeException("Silinecek sandalye bulunamadı.");
        }
    }

    @Override
    public ResponseEntity<?> chairUpdate(Long id, Chair chairRequest) {
        Optional<Chair> optionalChair = chairRepository.findById(id);
        if (optionalChair.isEmpty()) {
            return ResponseEntity.badRequest().body("Böyle bir sandalye yok");
        }

        Chair existingChair = optionalChair.get();

        if (chairRequest.getChairName() != null)
            existingChair.setChairName(chairRequest.getChairName());

        if (chairRequest.getOpeningTime() != null)
            existingChair.setOpeningTime(chairRequest.getOpeningTime());

        if (chairRequest.getClosingTime() != null)
            existingChair.setClosingTime(chairRequest.getClosingTime());

        if (chairRequest.getIslemSuresi() != null)
            existingChair.setIslemSuresi(chairRequest.getIslemSuresi());

        // NOT: Chair <-> Employee one-to-one ilişkiyi dikkatli yönet!
        if (chairRequest.getEmployee() != null)
            existingChair.setEmployee(chairRequest.getEmployee());

        chairRepository.save(existingChair);
        return ResponseEntity.ok(dtoConverter.toDto(existingChair));
    }


}
