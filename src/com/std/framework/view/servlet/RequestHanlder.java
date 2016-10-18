package com.std.framework.view.servlet;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.std.framework.controller.BeanFactory;
import com.std.framework.core.util.ReflectionUtil;
import com.std.framework.view.ViewSetting;
import com.std.framework.view.handle.CoreAction;

public class RequestHanlder {

	private HttpServletRequest request;
	private String invokedClassName;
	private String invokeddMethodName;

	public RequestHanlder(HttpServletRequest request) {
		this.request = request;
		requestSetting();
		requestAnalyze();
	}

	private void requestSetting() {
		try {
			request.setCharacterEncoding(ViewSetting.getReqEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void requestAnalyze() {
		String requestURI = request.getRequestURI();
		String reqUrl = requestURI.substring(requestURI.lastIndexOf("/") + 1, requestURI.lastIndexOf("."));
		invokedClassName = reqUrl.substring(0, reqUrl.lastIndexOf("."));
		invokeddMethodName = reqUrl.substring(reqUrl.lastIndexOf(".") + 1);
	}
	
	public CoreAction getServiceAction() throws Exception {
		Object action = BeanFactory.getMVCBean(invokedClassName);
		if (action == null) {
			action = Class.forName(invokedClassName).newInstance();
		}
		return (CoreAction) action;
	}
	
	public Method getServiceMethod(CoreAction action) throws Exception {
		Method method = null;
		Method[] serviceMethods = action.getClass().getDeclaredMethods();
		for (Method servMethod : serviceMethods) {
			if (servMethod.getName().equals(invokeddMethodName)) {
				method = servMethod;
				Class<?>[] parameterTypes = servMethod.getParameterTypes();
				if (parameterTypes.length > 0) {
					Object[] paramObj = new Object[parameterTypes.length];
					for (int i = 0 ; i < parameterTypes.length ; i ++) {
						Object paramInstance = parameterTypes[i].newInstance();
						/** param������� */
						prepareObjectField(action, paramInstance);
						paramObj[i] = paramInstance;
					}
					action.setParamObj(paramObj);
				} else {
					/** action������� */
					prepareObjectField(action,null);
				}
			}
		}
		return method;
	}
	
	/**
	 * ��ȡrequest�еĲ����Զ���䵽actionͬ��������
	 */
	public static void prepareActionFiled(CoreAction action) throws Exception {
		prepareObjectField(action,null);
	}
	
	/**
	 * ��ȡrequest�еĲ����Զ���䵽action��Ӧ�ķ���������
	 */
	public static void prepareParamFiled(CoreAction action, Object object) throws Exception {
		prepareObjectField(action,object);
	}

	private static void prepareObjectField(CoreAction action, Object object) throws Exception {
		HttpServletRequest request = action.awareRequest();
		Enumeration<?> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String fieldName = (String) e.nextElement(); // ��
			String fieldValue = request.getParameter(fieldName); // ֵ
			if(object != null){
				ReflectionUtil.setFieldForObject(object, fieldName, fieldValue);
			}else{
				ReflectionUtil.setFieldForObject(action, fieldName, fieldValue);
			}
		}
	}

}
