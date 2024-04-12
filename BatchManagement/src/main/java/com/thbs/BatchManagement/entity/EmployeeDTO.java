package com.thbs.BatchManagement.entity;

public class EmployeeDTO {


	    private Integer employeeId;
	    
	    // Constructors, getters, and setters...


		public Integer getEmployeeId() {
			return  employeeId;
		}

		public void setEmployeeId(Integer  employeeId) {
			this. employeeId =  employeeId;
		}

		public EmployeeDTO(Integer employeeId) {
			super();
			this.employeeId = employeeId;
		}

		public EmployeeDTO() {
			super();
		}

		
}

