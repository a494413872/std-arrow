package com.std.framework.model.ormap;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.std.framework.annotation.PrimaryKey;

/**
 * @author Luox ���ݱ�->����ӳ������
 */
public class Tab2ObjContainer {

	private Map<String, String> tabMap = null;
	private Map<String, String> seqMap = null;
	private Map<String, String> colMap = null;
	private Map<String, String> pkMap = null;

	public Map<String, String> getTabMap() {
		return tabMap;
	}

	public void setTabMap(Map<String, String> tabMap) {
		this.tabMap = tabMap;
	}

	public Map<String, String> getColMap() {
		return colMap;
	}

	public void setColMap(Map<String, String> colMap) {
		this.colMap = colMap;
	}

	public Map<String, String> getSeqMap() {
		return seqMap;
	}

	public void setSeqMap(Map<String, String> seqMap) {
		this.seqMap = seqMap;
	}

	public Map<String, String> getPkMap() {
		return pkMap;
	}

	public void setPkMap(Map<String, String> pkMap) {
		this.pkMap = pkMap;
	}

	public void loadMapping(String className, MappingRule mappingRule) {
		try {
			fillTabMap(className, mappingRule);
			fillSeqMap(className, mappingRule);
			fillColMap(className, mappingRule);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��ı��� Table->Class Mapping
	 */
	public void fillTabMap(String className, MappingRule mappingRule) throws Exception {
		tabMap = new HashMap<String, String>();
		String simpleName = className.substring(className.lastIndexOf(".") + 1);
		tabMap.put(mappingRule.ObjMapTab(simpleName).toUpperCase(), simpleName);
	}

	/**
	 * ��ȡ���������е� Table->Class Mapping
	 */
	public void fillSeqMap(String className, MappingRule mappingRule) throws Exception {
		seqMap = new HashMap<String, String>();
		String simpleName = className.substring(className.lastIndexOf(".") + 1);
		seqMap.put(mappingRule.ObjMapSeq(simpleName).toUpperCase(), simpleName);
	}

	/**
	 * ��ȡ����ֶ�Table->Class Mapping ʹ��LinkedHashMap��ȷ������Iterator������ʱ�򣬱�֤�ͷ�������ʱ��ֵ��˳��һ����
	 */
	public void fillColMap(String className, MappingRule mappingRule) throws Exception {
		colMap = new LinkedHashMap<String, String>();
		pkMap = new LinkedHashMap<String, String>();
		Field[] declaredFields = Class.forName(className).getDeclaredFields();
		for (Field field : declaredFields) {
			String fieldName = field.getName();
			colMap.put(mappingRule.ObjMapCol(fieldName).toUpperCase(), fieldName);
			if (field.getAnnotations() != null && field.getAnnotation(PrimaryKey.class) != null) {
				pkMap.put(mappingRule.ObjMapCol(fieldName).toUpperCase(), fieldName);
			}
		}
	}
}
