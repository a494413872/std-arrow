package com.std.framework.view.handle;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.std.framework.annotation.ActionService;
import com.std.framework.core.log.Log;
import com.std.framework.core.log.LogFactory;
import com.std.framework.view.servlet.Render;
import com.std.framework.view.servlet.RequestHanlder;
import com.std.framework.view.servlet.ResponseHandler;

/**
 * @author Luox ����Servlet������ַ�����Ͷ���action�������
 */
public class CoreServlet extends HttpServlet {

	private Log log = LogFactory.getLogger();
	private static final long serialVersionUID = -5852358908003296657L;

	@Override
	public void init() {
		try {
			super.init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		process(request, response);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	/**
	 * �ַ���ת���Ĵ�����
	 */
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {

		try {
			/** ����action������������ͷ����� */
			RequestHanlder reqHandler = new RequestHanlder(request);
			/** Ԥ���������÷����Action��Method */
			CoreAction action = reqHandler.getServiceAction();
			action.awareServlet(request, response);
			Method method = reqHandler.getServiceMethod(action);
			/** ִ��ҵ������,�������������� */
			if (isActionService(method)) {
				new ResponseHandler().sendResponse(action, method);
			} else {
				WriteNoServiceError(request, response, action);
			}
		} catch (ClassNotFoundException e) {
			log.error(e);
			WriteNoServiceError(request, response, null);
		} catch (NoSuchMethodException e) {
			log.error(e);
			WriteNoServiceError(request, response, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isActionService(Method method) {
		return method.isAnnotationPresent(ActionService.class);
	}

	private void WriteNoServiceError(HttpServletRequest request, HttpServletResponse response, CoreAction action) {
		Render render = new Render(request, response, action);
		render.toText("�����Http���񲻴��ڣ���˶�URL!");
	}

}