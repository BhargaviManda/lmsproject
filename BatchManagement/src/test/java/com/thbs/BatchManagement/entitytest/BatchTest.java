package com.thbs.BatchManagement.entitytest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thbs.BatchManagement.entity.Batch;

public class BatchTest {

    private Batch batch;

    @BeforeEach
    public void setUp() {
        batch = new Batch();
    }

    @Test
    public void testId() {
        assertNull(batch.getId());
        batch.setId(1L);
        assertEquals(1L, batch.getId());
    }

    @Test
    public void testBatchName() {
        assertNull(batch.getBatchName());
        batch.setBatchName("Test Batch");
        assertEquals("Test Batch", batch.getBatchName());
    }

    @Test
    public void testBatchDescription() {
        assertNull(batch.getBatchDescription());
        batch.setBatchDescription("Test Description");
        assertEquals("Test Description", batch.getBatchDescription());
    }

    @Test
    public void testDuration() {
        assertNull(batch.getDuration());
        batch.setDuration(10);
        assertEquals(10, batch.getDuration());
    }

    @Test
    public void testStartDate() {
        assertNull(batch.getStartDate());
        LocalDate startDate = LocalDate.now();
        batch.setStartDate(startDate);
        assertEquals(startDate, batch.getStartDate());
    }

    @Test
    public void testEndDate() {
        assertNull(batch.getEndDate());
        LocalDate endDate = LocalDate.now().plusDays(30);
        batch.setEndDate(endDate);
        assertEquals(endDate, batch.getEndDate());
    }

    @Test
    public void testEmployeeId() {
        assertNull(batch.getEmployeeId());
        List<Integer> employeeIds = Arrays.asList(1, 2, 3);
        batch.setEmployeeId(employeeIds);
        assertEquals(employeeIds, batch.getEmployeeId());
    }

    @Test
    public void testBatchSize() {
        assertNull(batch.getBatchSize());
        batch.setBatchSize(20);
        assertEquals(20, batch.getBatchSize());
    }
}
