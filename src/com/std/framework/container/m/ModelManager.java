package com.std.framework.container.m;


import org.w3c.dom.Node;

import com.std.framework.container.ContainerManager;
import com.std.framework.core.log.Log;
import com.std.framework.core.log.LogFactory;
import com.std.framework.model.connection.ConnectionsPool;
import com.std.framework.model.ormap.ORMStore;

/**
 * @author Luox Modelģ�������ĳ�ʼ�����
 */
public class ModelManager extends ContainerManager {

	private static Log logger = LogFactory.getLogger();
	private static final ModelXMLParser modelXMLParser = new ModelXMLParser();

	public static void loadModelContext() throws Exception {
		loadConnectionPool();
		loadORM();
	}

	private static void loadConnectionPool() throws Exception {
		logger.debug(">>>>>Stupideer ���װ��... >>>>>>>��ʼ�����ݿ����ӳ�...");
		Node dataSourceNode = modelXMLParser.getDataSourceNode();
		ConnectionsPool.instance().loadDataSource(dataSourceNode);
	}

	private static void loadORM() throws Exception {
		logger.debug(">>>>>Stupideer ���װ��... >>>>>>>��ʼ��Ormmaping����...");
		ORMStore.instance().loadORM();
	}
}
