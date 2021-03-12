package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }
    
    public void insertNewRecord() {
        
        classicModelsRepository.insertNewRecord();
        classicModelsRepository.insertRecordReturnAllFields();
    }    
    
    public void updateRecord() {
        
        classicModelsRepository.updateRecord();
        classicModelsRepository.updateRecordReturnAllFields();
    }
    
    public void deleteRecord() {
        
        classicModelsRepository.deleteRecord();       
    }
    
    public void mergeRecord() {
        
        classicModelsRepository.mergeRecord();
    }
    
    public void storeRecord() {
        
        classicModelsRepository.storeRecord();
    }
    
    public void resetOrginalChangedRefresh() {
        
        classicModelsRepository.resetOrginalChangedRefresh();
    }
    
    public void storeRecordAfterUpdatePrimaryKey() {
        
        classicModelsRepository.storeRecordAfterUpdatePrimaryKeyViaInsert();
        classicModelsRepository.storeRecordAfterUpdatePrimaryKeyViaUpdate();
    }
}
