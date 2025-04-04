package com.grupoeimsa.sigeim.models.cellphones.service;


import com.grupoeimsa.sigeim.models.cellphones.controller.dto.ResponseCellphoneDTO;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.ResponseRegisterCellphone;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.cellphones.model.ICellphone;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Transactional
public class CellphoneService {
    public final ICellphone cellphoneRepository;
    public final IPerson personRepository;

    public CellphoneService(ICellphone cellphoneRepository, IPerson personRepository) {
        this.cellphoneRepository = cellphoneRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseCellphoneDTO> findAll(String search, int page, int size, Boolean status, String enterprise, String departament) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanCellphone> cellphone = cellphoneRepository.findAllBySearch(
                search,
                departament,
                enterprise,
                status,
                pageable
        );
        if (cellphone.isEmpty()){
            throw new CustomException("No cellphone were found");
        }
        return cellphone.map(ResponseCellphoneDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponseCellphoneDTO findById(long id) {
        BeanCellphone cellphone = cellphoneRepository.findById(id).orElseThrow(() -> new CustomException("The cellphone was not found"));
        return new ResponseCellphoneDTO(cellphone);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void registerCellphone(ResponseRegisterCellphone registerCellphone) {
        BeanCellphone cellphone = new BeanCellphone();
        cellphone.setLegalName(registerCellphone.getLegalName());
        cellphone.setEquipamentName(registerCellphone.getEquipamentName());
        cellphone.setCompany(registerCellphone.getCompany());
        cellphone.setShortDialing(registerCellphone.getShortDialing());
        cellphone.setDateRenovation(registerCellphone.getDateRenovation());
        cellphone.setImei(registerCellphone.getImei());
        cellphone.setComments(registerCellphone.getComments());
        cellphone.setStatus(true);
        cellphone.setWhatsappBussiness(registerCellphone.getWhatsappBussiness());
        cellphone.setPerson(registerCellphone.getPerson());
        cellphoneRepository.save(cellphone);
    }

    @Transactional
    public void enableDisable(Long id){
        BeanCellphone cellphone = cellphoneRepository.findById(id).orElseThrow(() -> new CustomException("Person not found"));
        cellphone.setStatus(!cellphone.getStatus());
        BeanPerson defaultPerson = personRepository.findById(1L)
                .orElseThrow(() -> new CustomException("Default person not found"));

        cellphone.setPerson(defaultPerson);
        cellphoneRepository.save(cellphone);
    }

    @Transactional
    public void update(ResponseRegisterCellphone registerCellphone) {
        BeanCellphone cellphone = cellphoneRepository.findById(registerCellphone.getCellphoneId()).orElseThrow(() -> new CustomException("The cellphone was not found"));
        cellphone.setLegalName(registerCellphone.getLegalName());
        cellphone.setEquipamentName(registerCellphone.getEquipamentName());
        cellphone.setCompany(registerCellphone.getCompany());
        cellphone.setShortDialing(registerCellphone.getShortDialing());
        cellphone.setDateRenovation(registerCellphone.getDateRenovation());
        cellphone.setImei(registerCellphone.getImei());
        cellphone.setComments(registerCellphone.getComments());
        cellphone.setStatus(true);
        cellphone.setWhatsappBussiness(registerCellphone.getWhatsappBussiness());
        cellphoneRepository.save(cellphone);
    }


}
