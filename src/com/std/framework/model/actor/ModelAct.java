package com.std.framework.model.actor;


import java.util.ArrayList;
import java.util.List;

import com.std.framework.annotation.Entity;
import com.std.framework.container.m.ModelException;

public class ModelAct<T> {

	ExcutionAct<T> excutor = null;
	T entity = null;

	public ModelAct(T entity) {
		if (isEntityAnnotation(entity)) {
			this.entity = entity;
		}
		excutor = new ExcutionAct<T>(entity.getClass());
	}

	private boolean isEntityAnnotation(Object entity) {
		if (entity.getClass().isAnnotationPresent(Entity.class))
			return true;
		throw new ModelException("��ǰ��û�б�עEntiyע�⣬�޷�ʹ��session������й���!");
	}

	/** ����������ݿ� */
	public String save() {
		String pk = "";
		try {
			pk = excutor.excuteSave(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pk;
	}

	/** ���ݶ���������ɾ�����ݿ�����Ӧ��¼ */
	public boolean delete() {
		boolean isDeleteSuccess = false;
		try {
			isDeleteSuccess = excutor.excuteDelete(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDeleteSuccess;
	}

	/** ���ݶ����������޸����ݿ�����Ӧ��¼ */
	public boolean update() {
		boolean isUpdateSuccess = false;
		try {
			isUpdateSuccess = excutor.excuteUpdate(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isUpdateSuccess;
	}

	/** ���ݶ������������Ҳ��������ݿ��м�¼��Ӧ����ʵ�� */
	public T findByPK() {
		T obj = null;
		try {
			obj = excutor.excuteFindByPK(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/** ���ݶ��������ݿ��ӳ�䣬���Ҳ��������ݿ��Ӧ�������м�¼��Ӧ����ʵ�� */
	public List<T> findAll() {
		List<T> list = new ArrayList<T>();
		try {
			list = excutor.excuteFindAll(entity.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<T> findAll(OrderAct orderAct) {
		List<T> list = new ArrayList<T>();
		return list;
	}

	/** ���ݶ��������ݿ��ӳ�䣬���յ�ǰ���������ֵ�����Ҳ��������ݿ��Ӧ�������м�¼��Ӧ����ʵ�� */
	public List<T> findByEntity() {
		List<T> list = new ArrayList<T>();
		try {
			list = excutor.excuteFindAll(entity.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<T> findByEntity(OrderAct orderAct) {
		List<T> list = new ArrayList<T>();
		return list;
	}
}
