package com.std.framework.model.ormap;

/**
 * @author Luox �շ������->�� ������ӳ��
 */
public class MappingByClass implements MappingRule {

	/**
	 * ����ӳ�����
	 */
	public String ObjMapTab(String table) {
		return "T_" + handleHumpString(table);
	}

	/**
	 * ����ӳ�����
	 */
	public String ObjMapSeq(String sequence) {
		return "S_" + handleHumpString(sequence);
	}

	/**
	 * �ֶ�ӳ�����
	 */
	public String ObjMapCol(String column) {
		return handleHumpString(column);
	}

	private String handleHumpString(String string) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			if (Character.isUpperCase(string.charAt(i)) && i > 0) {
				sb.append("_").append(string.charAt(i));
			} else {
				sb.append(string.charAt(i));
			}
		}
		return sb.toString();
	}

}
