package com.std.framework.model.actor;

import java.lang.reflect.Field;
import java.text.ParseException;

import com.std.framework.core.util.ConvertUtil;
import com.std.framework.model.ormap.ORMStore;
import com.std.framework.model.ormap.Obj2TabContainer;
import com.std.framework.model.ormap.Tab2ObjContainer;

public class ColumnAct<T> {

	public static Obj2TabContainer obj2TabContainer = null;
	public static Tab2ObjContainer tab2ObjContainer = null;

	public static Obj2TabContainer getObj2TabMap() {
		return obj2TabContainer;
	}

	public static void setObj2TabMap(Obj2TabContainer obj2TabMap) {
		ColumnAct.obj2TabContainer = obj2TabMap;
	}

	public static Tab2ObjContainer getTab2ObjMap() {
		return tab2ObjContainer;
	}

	public static void setTab2ObjMap(Tab2ObjContainer tab2ObjMap) {
		ColumnAct.tab2ObjContainer = tab2ObjMap;
	}

	@SuppressWarnings("rawtypes")
	public ColumnAct( Class clazz) {
		obj2TabContainer = ORMStore.getO2tContainer(clazz.getName());
		tab2ObjContainer = ORMStore.getT2oContainer(clazz.getName());
	}

	/**
	 * �����ݿ��ѯ����Columnֵ���뵽��Ӧ��ʵ��������
	 * 
	 * @param value2
	 */
	public void setProperty(Object target, String colName, Field field, Object value) throws Exception {
		if (value != null) {
			field.set(target, castToFieldType(value, field.getType()));
		}
	}

	/**
	 * ���ֶ�ֵת������Ӧsql����
	 */
	public static String castToColumnType(Object value, Class<?> targetType) throws ParseException {
		return ConvertUtil.castToSqlType(value, targetType);
	}

	/**
	 * �����ݿ��ѯ���ֶ�ת������Ӧ��ʵ��������Ե����ͣ����ڷ������set����Ϊ�丳ֵ
	 */
	public static Object castToFieldType(Object value, Class<?> targetType) throws ParseException {
		return ConvertUtil.castToFieldType(value, targetType);
	}
}
