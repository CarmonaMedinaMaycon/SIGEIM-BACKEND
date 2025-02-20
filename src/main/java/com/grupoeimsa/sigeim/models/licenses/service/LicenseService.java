package com.grupoeimsa.sigeim.models.licenses.service;


import com.grupoeimsa.sigeim.models.licenses.controller.dto.ResponseLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.licenses.model.ILicense;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LicenseService {
    public final ILicense licensesRepository;

    public LicenseService(ILicense licenseRepository) {
        this.licensesRepository = licenseRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseLicenseDTO> findAll(String search, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanLicense> licenses = licensesRepository.findAllBySearch(
                search,
                pageable
        );
        if (licenses.isEmpty()){
            throw  new CustomException("No licences were found");
        }
        return licenses.map(ResponseLicenseDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponseLicenseDTO findById(Long id){
        BeanLicense license = licensesRepository.findById(id).orElseThrow(() -> new CustomException("The user was not found"));
        return new ResponseLicenseDTO(license);
    }
}
