package com.thbs.BatchManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.thbs.BatchManagement.entity.Batch;
import com.thbs.BatchManagement.entity.EmployeeDTO;
import com.thbs.BatchManagement.exceptionhandler.BatchNotFoundException;
import com.thbs.BatchManagement.exceptionhandler.DuplicateEmployeeException;
import com.thbs.BatchManagement.repository.BatchRepository;
import com.thbs.BatchManagement.service.BatchService;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@RestController
public class BatchController {

	
    @Autowired
    private BatchService batchService;

    @Autowired
    private BatchRepository batchRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    
    // adding trainees with batch creation
    @PostMapping("/createbatch")
    public ResponseEntity<String> createBatch(@RequestBody Batch batch) {
        return batchService.createBatch(batch);
    }

    
    // bulk upload with batch creation
    @PostMapping("/bulkUpload")
    public String bulkUpload(@RequestParam("file") MultipartFile file, @RequestParam("data") String data) throws IOException, ParseException {
        List<EmployeeDTO> Employees = batchService.parseExcel(file);
        batchService.addEmployeesToBatchFromExcel(Employees, data);
        return "Batch created successfully";
    }

    
    // adding employees to existing batch by batchid
    @PostMapping("/addEmployeesToExistingBatch/{batchId}")
    public String addEmployeesToBatch(@PathVariable Long batchId, @RequestBody List<EmployeeDTO> employees) {
        batchService.addEmployeesToExistingBatches(batchId, employees);
        return "Employees added to batch successfully";
    }

    
    // adding employees to existing batch by batchname
    @PostMapping("/addEmployeesToExistingBatches/{batchName}")
    public String addEmployeesToBatch(@PathVariable String batchName, @RequestBody List<EmployeeDTO> employees) {
            batchService.addEmployeesToExistingBatch(batchName, employees);
            return "Employees added to batch successfully";  
    }
    
    
    // bulk upload to existing batch by batchid
    @PostMapping("/addEmployeesToExistingBatch/{batchId}/bulkUpload")
    public String addEmployeesToExistingBatchBulkUpload(@PathVariable("batchId") Long batchId, @RequestParam("file") MultipartFile file) throws BatchNotFoundException, DuplicateEmployeeException, IOException {
        List<EmployeeDTO> employees = batchService.parseExcel(file);
        batchService.addEmployeesToExistingBatchesFromExcel(batchId, employees);
        return "Employees added to batch successfully";  
    }

    
    // bulk upload to existing batch by batchname
    @PostMapping("/addEmployeesToExistingBatches/{batchName}/bulkUpload")
    public String addEmployeesToExistingBatchBulkUpload(@PathVariable("batchName") String batchName, @RequestParam("file") MultipartFile file) throws BatchNotFoundException, DuplicateEmployeeException, IOException {
            List<EmployeeDTO> employees = batchService.parseExcel(file);
            batchService.addEmployeesToExistingBatchFromExcel(batchName, employees);
            return "Employees added to batch successfully";   
    }


    // list of batches by batchid
    @GetMapping("/batches/{batchId}")
    public ResponseEntity<Object> getBatchById(@PathVariable Long batchId) {    
    	return batchService.getBatchById(batchId);
    }

    
    // list of batches by batchnames
    @GetMapping("/viewbatches/{batchName}")
    public ResponseEntity<Object> getBatchByName(@PathVariable String batchName) {
    	return batchService.getBatchByName(batchName);
    }
    

    // list of all batch details
    @GetMapping("/batches")
    public List<Batch> getAllBatches() {
        return batchService.getAllBatches();
    }
    
    
    // list of batchnames
    @GetMapping("/batches/names")
    public List<String> getAllBatchNames() {
        return batchService.getAllBatchNames();
    }

    
    // list of employees using batchid
    @GetMapping("/batches/{batchId}/employees")
    public List<Integer> getEmployeesInBatch(@PathVariable Long batchId) {
        return batchService.getEmployeesInBatch(batchId);
    }
    
    
    // list of employees using batchname
    @GetMapping("/viewbatches/{batchName}/employees")
    public List<Integer> getEmployeesInBatchByName(@PathVariable String batchName) {
        return batchService.getEmployeesInBatchByName(batchName);
    }

    
    // deleting batch with batchid
    @DeleteMapping("/deletebatches/{batchId}")
    public String deleteBatch(@PathVariable Long batchId) {
        batchService.deleteBatchById(batchId);
        return "Batch deleted successfully";
    }

    
    // deleting batch with batchname
    @DeleteMapping("/viewdeletebatches/{batchName}")
    public String deleteBatch(@PathVariable String batchName) {
        batchService.deleteBatchByName(batchName);
        return "Batch deleted successfully";
    }

    
    // deleting employees with batchname
    @DeleteMapping("/viewdeleteEmployee/{batchName}/{employeeId}")
    public String deleteEmployeeFromBatch(@PathVariable String batchName, @PathVariable int employeeId) {
        batchService.deleteEmployeeFromBatch(batchName, employeeId);
        return "Employee deleted from batch successfully";
    }

    
    // deleting employees with batchid
    @DeleteMapping("/deleteEmployee/{batchId}/{employeeId}")
    public String deleteEmployeeFromBatch(@PathVariable Long batchId, @PathVariable int employeeId) {
        batchService.deleteEmployeeFromBatch(batchId, employeeId);
        return "Employee deleted from batch successfully";
    }
    
    
    //updating enddate with id
    @PatchMapping("/endDate/{id}")
    public String updateEndDate(@PathVariable Long id, @RequestBody Batch batch) {
        batchService.updateEndDate(id, batch.getEndDate());
        return "EndDate updated successfully";
    }
   
    
    //renaming batchname with id
    @PatchMapping("/batchName/{id}")
    public String updateBatchName(@PathVariable Long id, @RequestBody Batch batch) {
        batchService.updateBatchName(id, batch.getBatchName());
        return "BatchName updated successfully";
    }
    
}
