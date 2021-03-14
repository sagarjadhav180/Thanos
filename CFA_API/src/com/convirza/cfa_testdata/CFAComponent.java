package com.convirza.cfa_testdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.convirza.constants.Constants;
import com.convirza.constants.TestDataYamlConstants;
import com.convirza.tests.core.io.TestDataYamlReader;
import com.convirza.tests.core.utils.DBComponentUtil;

import common.BaseClass;

public class CFAComponent extends BaseClass implements Components{

	TestDataYamlReader yamlReader = new TestDataYamlReader();
	List<String> removeComponents = new ArrayList<String>();
	List<String> addComponents = new ArrayList<String>();
	String billingId;
	
	public void setUp() {
//		setComponentData();
	}
	
	
	public void componentAction() {
		addComponents.add("WhiteLabelComponent");
		if(addComponents.size()>0) {
			addComponent();			
		}
		if(removeComponents.size()>0){
			removeComponent();
		}
	}
	
	@Override
	public void removeComponent() {
		// TODO Auto-generated method stub
		Map<String, Object> compConfGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String billingId=compConfGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();		
		for(int i=0;i<removeComponents.size();i++) {
			Map queries = ComponentsQueryBuilder.buildQuery(removeComponents.get(i), billingId, "remove");
			DBComponentUtil.removeComponent(queries);
		}		
	}

	@Override
	public void addComponent() {
		// TODO Auto-generated method stub
		Map<String, Object> compConfGroupHierarchyAgency = yamlReader.readGroupInfo(Constants.GroupHierarchy.AGENCY);
		String billingId=compConfGroupHierarchyAgency.get(TestDataYamlConstants.GroupConstants.GROUP_ID).toString();
		for(int i=0;i<addComponents.size();i++) {
			Map queries = ComponentsQueryBuilder.buildQuery(addComponents.get(i), billingId, "add");
			DBComponentUtil.addComponent(queries);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setComponentData() {
		removeComponents = TestDataUtil.getComponentsToAdd("component");
//		addComponents = TestDataUtil.getComponentsToRemove("component");
	}

}
