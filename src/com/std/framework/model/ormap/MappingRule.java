package com.std.framework.model.ormap;

/**
 * @author Luox ORMMAPING ������ӳ�����ӿ�
 */
public interface MappingRule {

	public String ObjMapTab(String className) throws Exception;

	public String ObjMapSeq(String className) throws Exception;

	public String ObjMapCol(String fieldName) throws Exception;

}
