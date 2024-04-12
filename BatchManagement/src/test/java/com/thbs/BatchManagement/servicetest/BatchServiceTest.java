package com.thbs.BatchManagement.servicetest;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import com.thbs.BatchManagement.entity.Batch;
import com.thbs.BatchManagement.entity.EmployeeDTO;
import com.thbs.BatchManagement.exceptionhandler.BatchEmptyException;
import com.thbs.BatchManagement.exceptionhandler.BatchNotFoundException;
import com.thbs.BatchManagement.exceptionhandler.DuplicateBatchFoundException;
import com.thbs.BatchManagement.exceptionhandler.DuplicateEmployeeException;
import com.thbs.BatchManagement.exceptionhandler.EmployeeNotFoundException;
import com.thbs.BatchManagement.exceptionhandler.EmptyEmployeesListException;
import com.thbs.BatchManagement.exceptionhandler.InvalidDateFormatException;
import com.thbs.BatchManagement.exceptionhandler.ParseException;
import com.thbs.BatchManagement.repository.BatchRepository;
import com.thbs.BatchManagement.service.BatchService;
import static org.junit.jupiter.api.Assertions.assertThrows;



import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatchServiceTest {

	@Mock
	private BatchRepository batchRepository;

	@InjectMocks
	private BatchService batchService;

	@Mock
    private Workbook mockWorkbook;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	

	@Test
	public void testCreateBatch() {
		Batch batch = new Batch();
		batch.setBatchName("Test Batch");

		when(batchRepository.existsByBatchName(batch.getBatchName())).thenReturn(false);
		when(batchRepository.save(batch)).thenReturn(batch);

		assertDoesNotThrow(() -> batchService.createBatch(batch));
	}

	
//	 @Test
//	    public void testParseExcel() throws IOException, InvalidFormatException {
//	        // Mock InputStream for the Excel file
//	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.xlsx");
//	        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);
//
//	        // Mock WorkbookFactory.create to return a Workbook
//	        Workbook mockWorkbook = mock(Workbook.class);
//	        when(WorkbookFactory.create(file.getInputStream())).thenReturn(mockWorkbook);
//
//	        // Mock behavior for the Workbook
//	        Sheet mockSheet = mock(Sheet.class);
//	        when(mockWorkbook.getSheetAt(0)).thenReturn(mockSheet);
//
//	        // Mock behavior for the Sheet
//	        Row mockRow1 = mock(Row.class);
//	        Row mockRow2 = mock(Row.class);
//	        when(mockSheet.iterator()).thenReturn(List.of(mockRow1, mockRow2).iterator());
//
//	        // Mock behavior for the first row
//	        Cell mockCell1 = mock(Cell.class);
//	        when(mockRow1.getCell(0)).thenReturn(mockCell1);
//	        when(mockCell1.getCellType()).thenReturn(CellType.NUMERIC);
//	        when(mockCell1.getNumericCellValue()).thenReturn(1.0);
//
//	        // Mock behavior for the second row
//	        Cell mockCell2 = mock(Cell.class);
//	        when(mockRow2.getCell(0)).thenReturn(mockCell2);
//	        when(mockCell2.getCellType()).thenReturn(CellType.NUMERIC);
//	        when(mockCell2.getNumericCellValue()).thenReturn(2.0);
//
//	        // Create an instance of the service class
//	        BatchService batchService = new BatchService();
//
//	        // Call the method
//	        List<EmployeeDTO> employees = batchService.parseExcel(file);
//
//	        // Assert the result
//	        assertEquals(2, employees.size());
//	        assertEquals(1, employees.get(0).getEmployeeId());
//	        assertEquals(2, employees.get(1).getEmployeeId());
//	    }
//	
 
	
    @Test
    public void testAddEmployeesToBatchFromExcel() throws IOException, ParseException, java.text.ParseException {
        List<EmployeeDTO> employees = new ArrayList<>();
        employees.add(new EmployeeDTO(1)); // Example employee

        String data = "{\"batchName\": \"Test Batch\", \"duration\": 10, \"startDate\": \"2024-04-08\", \"endDate\": \"2024-04-18\", \"batchSize\": 20}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(data);

        // Mocking the behavior of the batchRepository.existsByBatchName method
        when(batchRepository.existsByBatchName("Test Batch")).thenReturn(false);

        // Call the method to be tested
        batchService.addEmployeesToBatchFromExcel(employees, data);

        // Verify that batchRepository.save was called once
        verify(batchRepository, times(1)).save(any());

        // You can add more assertions based on the expected behavior of the method
    }

    @Test
    public void testAddEmployeesToBatchFromExcel_DuplicateBatch() throws IOException, ParseException {
        List<EmployeeDTO> employees = new ArrayList<>();
        employees.add(new EmployeeDTO(1)); // Example employee

        String data = "{\"batchName\": \"Test Batch\", \"duration\": 10, \"startDate\": \"2024-04-08\", \"endDate\": \"2024-04-18\", \"batchSize\": 20}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(data);

        // Mocking the behavior of the batchRepository.existsByBatchName method to return true (indicating a duplicate batch)
        when(batchRepository.existsByBatchName("Test Batch")).thenReturn(true);

        // Verify that the DuplicateBatchFoundException is thrown
        assertThrows(DuplicateBatchFoundException.class, () ->
                batchService.addEmployeesToBatchFromExcel(employees, data));

        // Verify that batchRepository.save was never called
        verify(batchRepository, never()).save(any());
    }

    @Test
    void addEmployeesToExistingBatches_withValidBatchIdAndEmployees_shouldAddEmployeesToBatch() {
        MockitoAnnotations.openMocks(this);

        Long batchId = 1L;
        List<EmployeeDTO> employees = new ArrayList<>();
        employees.add(new EmployeeDTO(1));

        Batch batch = new Batch();
        batch.setEmployeeId(new ArrayList<>());

        when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));

        batchService.addEmployeesToExistingBatches(batchId, employees);

        // Verify that findById was called
        verify(batchRepository, times(1)).findById(batchId);

        // Verify that save was called with the modified batch object
        ArgumentCaptor<Batch> batchCaptor = ArgumentCaptor.forClass(Batch.class);
        verify(batchRepository, times(1)).save(batchCaptor.capture());

        // Check the modified batch object
        Batch modifiedBatch = batchCaptor.getValue();
        assertEquals(employees.size(), modifiedBatch.getEmployeeId().size());
        // Add more assertions as needed for other fields or conditions
    }

//	    @Test
//	    void addEmployeesToExistingBatches_withEmptyEmployeesList_shouldThrowException() {
//	        // Arrange
//	        Long batchId = 1L;
//	        List<EmployeeDTO> employees = new ArrayList<>();
//	        when(batchRepository.findById(batchId)).thenReturn(Optional.empty());
//
//	        // Act and Assert
//	        assertThrows(EmptyEmployeesListException.class, () -> batchService.addEmployeesToExistingBatches(batchId, employees));
//
//	        // Verify
//	        verify(batchRepository, times(1)).findById(batchId);
//	        verify(batchRepository, never()).save(any());
//	    }
//
//   
    
    @Test
    void addEmployeesToExistingBatches_withEmptyEmployeesList_shouldThrowException() {
        // Arrange
        Long batchId = 1L;
        List<EmployeeDTO> employees = new ArrayList<>();
        when(batchRepository.findById(batchId)).thenReturn(Optional.of(new Batch()));

        // Act and Assert
        assertThrows(EmptyEmployeesListException.class, () -> batchService.addEmployeesToExistingBatches(batchId, employees));

        // Verify
        verify(batchRepository, times(1)).findById(batchId);
        verify(batchRepository, never()).save(any());
    }




	    @Test
	    void addEmployeesToExistingBatches_withNonExistingBatchId_shouldThrowException() {
	        MockitoAnnotations.openMocks(this);

	        Long batchId = 1L;
	        List<EmployeeDTO> employees = new ArrayList<>();
	        employees.add(new EmployeeDTO((int) 1L));

	        when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

	        assertThrows(BatchNotFoundException.class, () ->
	                batchService.addEmployeesToExistingBatches(batchId, employees));

	        verify(batchRepository, times(1)).findById(batchId);
	        verify(batchRepository,never()).save(any());
	    }

	    @Test
	    public void testAddEmployeesToExistingBatch() {
	        String batchName = "TestBatch";
	        List<EmployeeDTO> employees = new ArrayList<>();
	        employees.add(new EmployeeDTO(1)); // Example employee

	        Batch batch = new Batch();
	        batch.setBatchName(batchName);
	        batch.setEmployeeId(new ArrayList<>());

	        when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.of(batch));

	        assertDoesNotThrow(() -> batchService.addEmployeesToExistingBatch(batchName, employees));

	        verify(batchRepository, times(1)).findByBatchName(batchName);
	        verify(batchRepository, times(1)).save(batch);
	    }

	    @Test
	    public void testAddEmployeesToExistingBatch_BatchNotFound() {
	        String batchName = "NonExistingBatch";
	        List<EmployeeDTO> employees = new ArrayList<>();
	        employees.add(new EmployeeDTO(1)); // Example employee

	        when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.empty());

	        assertThrows(BatchNotFoundException.class, () -> batchService.addEmployeesToExistingBatch(batchName, employees));

	        verify(batchRepository, times(1)).findByBatchName(batchName);
	        verify(batchRepository, never()).save(any());
	    }
	    
	    
	    @Test
	    public void testAddEmployeesToExistingBatch_EmptyEmployeesList() {
	        String batchName = "TestBatch";
	        List<EmployeeDTO> employees = new ArrayList<>();

	        Batch batch = new Batch();
	        batch.setBatchName(batchName);
	        batch.setEmployeeId(new ArrayList<>());

	        when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.of(batch));

	        assertThrows(EmptyEmployeesListException.class, () -> batchService.addEmployeesToExistingBatch(batchName, employees));

	        verify(batchRepository, never()).save(any());
	    }

	 
	@Test
	public void testAddEmployeesToExistingBatchesFromExcelBatchNotFound() {
		Long batchId = 1L;
		List<EmployeeDTO> employees = new ArrayList<>();
		employees.add(new EmployeeDTO(1));
		employees.add(new EmployeeDTO(2));
		employees.add(new EmployeeDTO(3));

		when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

		assertThrows(BatchNotFoundException.class,
				() -> batchService.addEmployeesToExistingBatchesFromExcel(batchId, employees));

		verify(batchRepository, times(1)).findById(batchId);
		verify(batchRepository, never()).save(any());
	}

	@Test
	public void testAddEmployeesToExistingBatchesFromExcelDuplicateEmployees() {
		Long batchId = 1L;
		List<EmployeeDTO> employees = new ArrayList<>();
		employees.add(new EmployeeDTO(1));
		employees.add(new EmployeeDTO(2));

		Batch batch = new Batch();
		batch.setId(batchId);
		batch.setEmployeeId(List.of(1, 2));

		when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));

		assertThrows(DuplicateEmployeeException.class,
				() -> batchService.addEmployeesToExistingBatchesFromExcel(batchId, employees));

		verify(batchRepository, times(1)).findById(batchId);
		verify(batchRepository, never()).save(any());
	}

//	@Test
//	public void testAddEmployeesToExistingBatchFromExcel() throws BatchNotFoundException, DuplicateEmployeeException {
//	    // Mock batch repository
//	    Batch batch = new Batch();
//	    batch.setBatchName("TestBatch");
//	    batch.setEmployeeId(new ArrayList<>());
//
//	    List<EmployeeDTO> employees = new ArrayList<>();
//	    employees.add(new EmployeeDTO(1));
//	    employees.add(new EmployeeDTO(2));
//
//	    when(batchRepository.findByBatchName("TestBatch")).thenReturn(Optional.of(batch));
//	    doAnswer(invocation -> {
//	        Batch modifiedBatch = invocation.getArgument(0);
//	        assertEquals(2, modifiedBatch.getEmployeeId().size()); // Verify that employees were added
//	        return null;
//	    }).when(batchRepository).save(any());
//
//	    // Test adding new employees
//	    batchService.addEmployeesToExistingBatchFromExcel("TestBatch", employees);
//
//	    // Verify that new employees were added to the batch
//	    verify(batchRepository, times(1)).findByBatchName("TestBatch");
//	    verify(batchRepository, times(1)).save(any());
//
//	    // Test adding duplicate employees
//	    List<EmployeeDTO> duplicateEmployees = new ArrayList<>();
//	    duplicateEmployees.add(new EmployeeDTO(1));
//	    duplicateEmployees.add(new EmployeeDTO(2));
//
//	    assertThrows(DuplicateEmployeeException.class,
//	            () -> batchService.addEmployeesToExistingBatchFromExcel("TestBatch", duplicateEmployees));
//
//	    // Verify that the batch was not saved again
//	    verify(batchRepository, times(1)).findByBatchName("TestBatch");
//	    verify(batchRepository, times(1)).save(any());
//	}

	@Test
	public void testAddEmployeesToExistingBatchFromExcel() throws BatchNotFoundException, DuplicateEmployeeException {
	    String batchName = "TestBatch";

	    List<EmployeeDTO> employees = new ArrayList<>();
	    employees.add(new EmployeeDTO(1));
	    employees.add(new EmployeeDTO(2));

	    Batch existingBatch = new Batch();
	    existingBatch.setBatchName(batchName);
	    existingBatch.setEmployeeId(List.of(1, 2, 3)); // Existing employees in the batch

	    Mockito.when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.of(existingBatch));

	    // Simulate case where all employees are already in the batch
	    DuplicateEmployeeException exception = assertThrows(DuplicateEmployeeException.class,
	            () -> batchService.addEmployeesToExistingBatchFromExcel(batchName, employees));

	    assertEquals("All employees provided are already present in this batch", exception.getMessage());

	    // Verify that the batch was not saved
	    Mockito.verify(batchRepository, Mockito.never()).save(existingBatch);
	}

	@Test
	public void testAddEmployeesToExistingBatchFromExcelBatchNotFound() {
		List<EmployeeDTO> employees = new ArrayList<>();
		employees.add(new EmployeeDTO(1));
		employees.add(new EmployeeDTO(2));

		when(batchRepository.findByBatchName("NonExistentBatch")).thenReturn(Optional.empty());

		assertThrows(BatchNotFoundException.class,
				() -> batchService.addEmployeesToExistingBatchFromExcel("NonExistentBatch", employees));

		verify(batchRepository, times(1)).findByBatchName("NonExistentBatch");
		verify(batchRepository, never()).save(any());
	}

	 @Test
	 public void testGetAllBatchNames() {
	        // Given
	        Batch batch1 = new Batch();
	        batch1.setBatchName("Batch1");

	        Batch batch2 = new Batch();
	        batch2.setBatchName("Batch2");

	        List<Batch> batches = Arrays.asList(batch1, batch2);
	        Mockito.when(batchRepository.findAll()).thenReturn(batches);

	        // When
	        List<String> result = batchService.getAllBatchNames();

	        // Then
	        assertEquals(Arrays.asList("Batch1", "Batch2"), result);
	    }
	    @Test
		public void testGetAllBatchNamesEmpty() {
			List<Batch> batches = new ArrayList<>();

			when(batchRepository.findAll()).thenReturn(batches);

			Exception exception = assertThrows(BatchEmptyException.class, () -> batchService.getAllBatchNames());

			assertEquals("Batches are not created yet", exception.getMessage());

			verify(batchRepository, times(1)).findAll();
		}


	@Test
	public void testGetBatchById() {
		Long batchId = 1L;
		Batch batch = new Batch();
		batch.setId(batchId);

		when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));

		ResponseEntity<Object> responseEntity = batchService.getBatchById(batchId);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(batch, responseEntity.getBody());

		verify(batchRepository, times(1)).findById(batchId);
	}

	@Test
	public void testGetBatchByIdNotFound() {
		Long batchId = 999L;

		when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(BatchNotFoundException.class, () -> batchService.getBatchById(batchId));

		assertEquals("Batch not found with id " + batchId, exception.getMessage());

		verify(batchRepository, times(1)).findById(batchId);
	}

	@Test
	public void testGetBatchByName() {
		String batchName = "TestBatch";
		Batch batch = new Batch();
		batch.setBatchName(batchName);

		when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.of(batch));

		ResponseEntity<Object> responseEntity = batchService.getBatchByName(batchName);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(batch, responseEntity.getBody());

		verify(batchRepository, times(1)).findByBatchName(batchName);
	}

	@Test
	public void testGetBatchByNameNotFound() {
		String batchName = "NonExistentBatch";

		when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.empty());

		Exception exception = assertThrows(BatchNotFoundException.class, () -> batchService.getBatchByName(batchName));

		assertEquals("Batch not found with name " + batchName, exception.getMessage());

		verify(batchRepository, times(1)).findByBatchName(batchName);
	}

	@Test
	public void testGetAllBatches() {
		List<Batch> batches = new ArrayList<>();
		batches.add(new Batch());
		batches.add(new Batch());
		batches.add(new Batch());

		when(batchRepository.findAll()).thenReturn(batches);

		List<Batch> result = batchService.getAllBatches();

		assertEquals(batches.size(), result.size());
		assertEquals(batches.get(0), result.get(0));
		assertEquals(batches.get(1), result.get(1));
		assertEquals(batches.get(2), result.get(2));

		verify(batchRepository, times(1)).findAll();
	}

	@Test
	public void testGetAllBatchesEmpty() {
		List<Batch> batches = new ArrayList<>();

		when(batchRepository.findAll()).thenReturn(batches);

		Exception exception = assertThrows(BatchEmptyException.class, () -> batchService.getAllBatches());

		assertEquals("Batches are not created yet", exception.getMessage());

		verify(batchRepository, times(1)).findAll();
	}

	@Test
	public void testGetEmployeesInBatch() {
		Long batchId = 1L;

		Batch batch = new Batch();
		batch.setId(batchId);
		List<Integer> employeeIds = new ArrayList<>();
		employeeIds.add(1);
		employeeIds.add(2);
		batch.setEmployeeId(employeeIds);

		when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));

		List<Integer> result = batchService.getEmployeesInBatch(batchId);

		assertEquals(employeeIds, result);

		verify(batchRepository, times(1)).findById(batchId);
	}

	@Test
	public void testGetEmployeesInBatchBatchEmpty() {
		Long batchId = 1L;

		Batch batch = new Batch();
		batch.setId(batchId);
		batch.setEmployeeId(new ArrayList<>());

		when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));

		Exception exception = assertThrows(BatchEmptyException.class, () -> batchService.getEmployeesInBatch(batchId));

		assertEquals("No employees found in batch with id " + batchId, exception.getMessage());

		verify(batchRepository, times(1)).findById(batchId);
	}

	@Test
	public void testGetEmployeesInBatchBatchNotFound() {
		Long batchId = 1L;

		when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(BatchNotFoundException.class,
				() -> batchService.getEmployeesInBatch(batchId));

		assertEquals("Batch with id " + batchId + " not found.", exception.getMessage());

		verify(batchRepository, times(1)).findById(batchId);
	}

	@Test
	public void testGetEmployeesInBatchByName() {
		String batchName = "TestBatch";

		Batch batch = new Batch();
		batch.setBatchName(batchName);
		List<Integer> employeeIds = new ArrayList<>();
		employeeIds.add(1);
		employeeIds.add(2);
		batch.setEmployeeId(employeeIds);

		when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.of(batch));

		List<Integer> result = batchService.getEmployeesInBatchByName(batchName);

		assertEquals(employeeIds, result);

		verify(batchRepository, times(1)).findByBatchName(batchName);
	}

	@Test
	public void testGetEmployeesInBatchByNameBatchEmpty() {
		String batchName = "EmptyBatch";

		Batch batch = new Batch();
		batch.setBatchName(batchName);
		batch.setEmployeeId(new ArrayList<>());

		when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.of(batch));

		Exception exception = assertThrows(BatchEmptyException.class,
				() -> batchService.getEmployeesInBatchByName(batchName));

		assertEquals("No employees found in batch with name " + batchName, exception.getMessage());

		verify(batchRepository, times(1)).findByBatchName(batchName);
	}

	@Test
	public void testGetEmployeesInBatchByNameBatchNotFound() {
		String batchName = "NonExistentBatch";

		when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.empty());

		Exception exception = assertThrows(BatchNotFoundException.class,
				() -> batchService.getEmployeesInBatchByName(batchName));

		assertEquals("Batch with name " + batchName + " not found.", exception.getMessage());

		verify(batchRepository, times(1)).findByBatchName(batchName);
	}

	@Test
	public void testDeleteBatchById() {
		Long batchId = 1L;

		Batch batch = new Batch();
		batch.setId(batchId);

		when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
		doNothing().when(batchRepository).delete(batch);

		assertDoesNotThrow(() -> batchService.deleteBatchById(batchId));

		verify(batchRepository, times(1)).findById(batchId);
		verify(batchRepository, times(1)).delete(batch);
	}

	@Test
	public void testDeleteBatchByIdBatchNotFound() {
		Long batchId = 1L;

		when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

		assertThrows(BatchNotFoundException.class, () -> batchService.deleteBatchById(batchId));

		verify(batchRepository, times(1)).findById(batchId);
		verify(batchRepository, never()).delete(any());
	}

	@Test
	public void testDeleteBatchByName() {
		String batchName = "Test Batch";

		Batch batch = new Batch();
		batch.setBatchName(batchName);

		when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.of(batch));
		doNothing().when(batchRepository).delete(batch);

		assertDoesNotThrow(() -> batchService.deleteBatchByName(batchName));

		verify(batchRepository, times(1)).findByBatchName(batchName);
		verify(batchRepository, times(1)).delete(batch);
	}

	@Test
	public void testDeleteBatchByNameBatchNotFound() {
		String batchName = "Test Batch";

		when(batchRepository.findByBatchName(batchName)).thenReturn(Optional.empty());

		assertThrows(BatchNotFoundException.class, () -> batchService.deleteBatchByName(batchName));

		verify(batchRepository, times(1)).findByBatchName(batchName);
		verify(batchRepository, never()).delete(any());
	}

	@Test
	public void testDeleteEmployeeFromBatch() {
		Long batchId = 1L;
		int employeeId = 101;

		Batch batch = new Batch();
		batch.setId(batchId);
		List<Integer> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		batch.setEmployeeId(employeeIds);

		when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
		when(batchRepository.save(batch)).thenReturn(batch);

		assertDoesNotThrow(() -> batchService.deleteEmployeeFromBatch(batchId, employeeId));

		verify(batchRepository, times(1)).findById(batchId);
		verify(batchRepository, times(1)).save(batch);
	}

	
	@Test
	public void testDeleteEmployeeFromBatchEmployeeFound() {
	    // Given
	    Long batchId = 1L;
	    int employeeId = 1001;
	    Batch batch = new Batch();
	    batch.setId(batchId);
	    batch.setEmployeeId(Arrays.asList(1001, 1002, 1003));
	    Mockito.when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));

	    // When
	    batchService.deleteEmployeeFromBatch(batchId, employeeId);

	    // Then
	    assertFalse(batch.getEmployeeId().contains(Integer.valueOf(employeeId)));
	    Mockito.verify(batchRepository, Mockito.times(1)).save(batch);
	}



	@Test
	public void testDeleteEmployeeFromBatchEmployeeNotFound() {
	    // Given
	    Long batchId = 1L;
	    int employeeId = 1004; // Employee ID not in the batch
	    Batch batch = new Batch();
	    batch.setId(batchId);
	    batch.setEmployeeId(Arrays.asList(1001, 1002, 1003));
	    Mockito.when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));

	    // When/Then
	    assertThrows(EmployeeNotFoundException.class, () -> batchService.deleteEmployeeFromBatch(batchId, employeeId));
	}

	@Test
	public void testDeleteEmployeeFromBatchBatchNotFound() {
	    // Given
	    Long batchId = 1L; // Batch ID not found
	    int employeeId = 1001;
	    Mockito.when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

	    // When/Then
	    assertThrows(BatchNotFoundException.class, () -> batchService.deleteEmployeeFromBatch(batchId, employeeId));
	}

    @Test
    public void testUpdateEndDate() {
        // Given
        Long id = 1L;
        LocalDate date = LocalDate.of(2024, 4, 10);

        Batch batch = new Batch();
        batch.setId(id);

        when(batchRepository.findById(id)).thenReturn(Optional.of(batch));

        // When
        batchService.updateEndDate(id, date);

        // Then
        verify(batchRepository, times(1)).findById(id);
        verify(batchRepository, times(1)).save(batch);
        assertEquals(date, batch.getEndDate());
    }

    @Test
    public void testUpdateEndDate_BatchNotFound() {
        // Given
        Long id = 1L;
        LocalDate date = LocalDate.of(2024, 4, 10);

        when(batchRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(BatchNotFoundException.class, () -> batchService.updateEndDate(id, date));
    }


    @Test
    public void testIsValidDateValid() {
        LocalDate validDate = LocalDate.of(2024, 4, 10);
        assertTrue(batchService.isValidDate(validDate));
    }

    @Test
    public void testIsValidDateInvalid() {
        LocalDate invalidDate = LocalDate.of(2024, 2, 30); // Invalid date (February 30th)
        assertThrows(DateTimeException.class, () -> batchService.isValidDate(invalidDate));
    }



	@Test
	public void testAddEmployeesToExistingBatchesEmptyEmployeesList1() {
		Long batchId = 1L;
		List<EmployeeDTO> employees = new ArrayList<>();

		when(batchRepository.findById(batchId)).thenReturn(Optional.empty());

		// Call the method that should throw BatchNotFoundException
		Exception exception = assertThrows(BatchNotFoundException.class,
				() -> batchService.addEmployeesToExistingBatches(batchId, employees));

		assertEquals("Batch not found", exception.getMessage());

		verify(batchRepository, times(1)).findById(batchId);
		verify(batchRepository, never()).save(any());

		// Add another scenario here if needed
	}

	@Test
	public void testUpdateBatchName() {
		Long id = 1L;
		String batchName = "New Batch Name";

		Batch batch = new Batch();
		batch.setId(id);
		batch.setBatchName("Old Batch Name");

		when(batchRepository.findById(id)).thenReturn(Optional.of(batch));
		when(batchRepository.existsByBatchName(batchName)).thenReturn(false);

		batchService.updateBatchName(id, batchName);

		assertEquals(batchName, batch.getBatchName());
		verify(batchRepository, times(1)).save(batch);
	}
}
