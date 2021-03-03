package com.convirza.cfa_testdata;

import java.util.HashMap;
import java.util.Map;

public class ComponentsQueryBuilder {
	
	
	@SuppressWarnings("unchecked")
	public static Map buildQuery(String compoenent, String org_unit_id, String action) {
		Map<String,String> map = new HashMap<String,String>();
		
		switch(compoenent) {
		case "CallActionComponent":
			map = addCallActionComponent(org_unit_id, action);
			break;
		case "CAComponent":
			map = addCAComponent(org_unit_id, action);
			break;	
		case "RefferalComponent":
			map = addRefferalComponent(org_unit_id, action);
			break;
		case "HIPPAComponent":
			map = addHIPPAComponent(org_unit_id, action);
			break;	
		case "SpamGuardComponent":
			map = addSpamGuardComponent(org_unit_id, action);
			break;
		case "WhiteLabelComponent":
			map = addWhiteLabelComponent(org_unit_id, action);
			break;	
			
		}
		return map;
	}
	
	
	public static Map addCallActionComponent(String org_unit_id, String action) {
		Map<String,String> map = new HashMap<String,String>();
		
		String org_account = null;
		String org_component_count = null;
		
		if(action.equals("add")) {
			org_account = "insert into org_account (component_id , org_unit_id ) values (10, "+org_unit_id+")";
			org_component_count = "insert into org_component_count (component_id , org_unit_id ) values (10, "+org_unit_id+")";			
		}else if(action.equals("remove")){
			org_account = "delete from org_component_count where org_unit_id="+org_unit_id+" and component_id=25";
			org_component_count = "delete from org_account where org_unit_id="+org_unit_id+" and component_id=25";
		}
		
		map.put("org_account", org_account);
		map.put("org_component_count", org_component_count);
		return map;
	}
	
	
	public static Map addCAComponent(String org_unit_id, String action) {
		Map<String,String> map = new HashMap<String,String>();
		
		String org_account = null;
		String org_component_count = null;
		
		if(action.equals("add")) {
			org_account = "insert into org_account (component_id , org_unit_id ) values (19,"+org_unit_id+")";
			org_component_count = "insert into org_component_count (component_id , org_unit_id ) values (19,"+org_unit_id+")";			
		}else if(action.equals("remove")){
			org_account = "delete from org_component_count where org_unit_id="+org_unit_id+" and component_id=19";
			org_component_count = "delete from org_account where org_unit_id="+org_unit_id+" and component_id=19";
		}
		
		map.put("org_account", org_account);
		map.put("org_component_count", org_component_count);
		return map;
	}
	
	
	public static Map addRefferalComponent(String org_unit_id, String action) {
		Map<String,String> map = new HashMap<String,String>();
		
		String org_account = null;
		String org_component_count = null;
		
		if(action.equals("add")) {
			org_account = "insert into org_account (component_id , org_unit_id ) values (28,"+org_unit_id+")";
			org_component_count = "insert into org_component_count (component_id , org_unit_id ) values (28,"+org_unit_id+")";			
		}else if(action.equals("remove")){
			org_account = "delete from org_component_count where org_unit_id="+org_unit_id+" and component_id=28";
			org_component_count = "delete from org_account where org_unit_id="+org_unit_id+" and component_id=28";
		}
		
		map.put("org_account", org_account);
		map.put("org_component_count", org_component_count);
		return map;
	}

	
	public static Map addHIPPAComponent(String org_unit_id, String action) {
		Map<String,String> map = new HashMap<String,String>();
		
		String org_account = null;
		String org_component_count = null;
		
		if(action.equals("add")) {
			org_account = "insert into org_account (component_id , org_unit_id ) values (78,"+org_unit_id+")";
			org_component_count = "insert into org_component_count (component_id , org_unit_id ) values (78,"+org_unit_id+")";			
		}else if(action.equals("remove")){
			org_account = "delete from org_component_count where org_unit_id="+org_unit_id+" and component_id=78";
			org_component_count = "delete from org_account where org_unit_id="+org_unit_id+" and component_id=78";
		}
		
		map.put("org_account", org_account);
		map.put("org_component_count", org_component_count);
		return map;
	}
	
	
	public static Map addSpamGuardComponent(String org_unit_id, String action) {
		Map<String,String> map = new HashMap<String,String>();
		
		String org_account = null;
		String org_component_count = null;
		
		if(action.equals("add")) {
			org_account = "insert into org_account (component_id , org_unit_id ) values (396,"+org_unit_id+")";
			org_component_count = "insert into org_component_count (component_id , org_unit_id ) values (396,"+org_unit_id+")";			
		}else if(action.equals("remove")){
			org_account = "delete from org_component_count where org_unit_id="+org_unit_id+" and component_id=396";
			org_component_count = "delete from org_account where org_unit_id="+org_unit_id+" and component_id=396";
		}
		
		map.put("org_account", org_account);
		map.put("org_component_count", org_component_count);
		return map;
	}
	
	
	public static Map addWhiteLabelComponent(String org_unit_id, String action) {
		Map<String,String> map = new HashMap<String,String>();
		
		String org_account = null;
		String org_component_count = null;
		
		if(action.equals("add")) {
			org_account = "insert into org_account (component_id , org_unit_id ) values (25,"+org_unit_id+")";
			org_component_count = "insert into org_component_count (component_id , org_unit_id ) values (25,"+org_unit_id+")";			
		}else if(action.equals("remove")){
			org_account = "delete from org_component_count where org_unit_id="+org_unit_id+" and component_id=25";
			org_component_count = "delete from org_account where org_unit_id="+org_unit_id+" and component_id=25";
		}
		
		map.put("org_account", org_account);
		map.put("org_component_count", org_component_count);
		return map;
	}
	
}
