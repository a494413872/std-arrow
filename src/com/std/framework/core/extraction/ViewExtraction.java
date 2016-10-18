package com.std.framework.core.extraction;


import java.util.ArrayList;
import java.util.List;

import com.std.framework.view.handle.CoreAction;

/**
 * @author Luox CoreAction �̳����ȡ�� �ڹ������������ļ���������ָ����jar���е��ࣩ�����У���ȡ���м̳���CoreAction���ࡣ
 */
public class ViewExtraction implements Extraction {

	private Class<CoreAction> clazz = CoreAction.class;

	public List<Class<?>> extract(List<String> classFileList) throws Exception {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		for (String classFile : classFileList) {
			try {
				Class<?> classInFile = Class.forName(classFile);
				// �Ƿ�̳���CoreAction������  parent.isAssignableFrom(child)
				if (clazz.isAssignableFrom(classInFile)
				// �ų�CoreAction��������
						&& !classInFile.getSimpleName().equals(clazz.getSimpleName())) {
					classList.add(classInFile);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classList;
	}

}
