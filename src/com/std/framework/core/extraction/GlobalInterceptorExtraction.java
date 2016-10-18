package com.std.framework.core.extraction;


import java.util.ArrayList;
import java.util.List;

import com.std.framework.annotation.Global;
import com.std.framework.view.interceptor.CoreInterceptor;

/**
 * @author Luox Interceptor ע�����ȡ�� �ڹ������������ļ���������ָ����jar���е��ࣩ�����У���ȡ���м̳���CoreInterceptor���Ҹ���Interceptorע����ࡣ
 */
public class GlobalInterceptorExtraction implements Extraction {

	private Class<CoreInterceptor> clazz = CoreInterceptor.class;

	public List<Class<?>> extract(List<String> classFileList) throws Exception {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		for (String classFile : classFileList) {
			try {
				Class<?> classInFile = Class.forName(classFile);
				// �Ƿ�̳���CoreInterceptorʵ����  parent.isAssignableFrom(child)
				if (clazz.isAssignableFrom(classInFile)
				// �ų�CoreInterceptor��������
						&& !classInFile.getSimpleName().equals(clazz.getSimpleName())
						// �Ƿ����@Globalע��
						&& classInFile.isAnnotationPresent(Global.class)) {
					classList.add(classInFile);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classList;
	}
}
