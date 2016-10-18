package com.std.framework.controller.timer.stepor;

import java.util.Calendar;

public abstract class ClockStepor {

	protected Calendar clock;
	protected int calendarField;
	protected int carryOverLimit;
	protected String unitCronString;


	/**
	 * ����cron���ʽ����ʱ�Ӳ���
	 */
	public void walk(){
		if (unitCronString.equals("*")) {
			 this.walkOnStars();
		}else if (unitCronString.contains("/")) {
			 this.walkOnDiagonal();
		}else if (unitCronString.contains("-")) {
			 this.walkOnLine();
		}else if (unitCronString.contains(",")) {
			 this.walkOnComma();
		}else{
		}
	};

	protected Integer walkOnStars() {
		clock.roll(calendarField, 1);
		Integer start = clock.get(calendarField);
		return start;
	}

	protected Integer walkOnDiagonal() {
		String[] split = unitCronString.split("/");
		int interval = Integer.parseInt(split[1]);
		clock.roll(calendarField, interval);
		Integer start = clock.get(calendarField);
		// ʹ�ü����/������ʱ�������Ƿ������λ���������ʱ��С��ʱ�̼�������ʾ�����˽�λ
		if (start < interval) {
			return null;
		}
		return start;
	}

	protected Integer walkOnLine() {
		String[] split = unitCronString.split("-");
		int begin = Integer.parseInt(split[0]);
		int end = Integer.parseInt(split[1]);
		clock.roll(calendarField, 1);
		Integer start = clock.get(calendarField);
		if (begin <= start && start <= end) {
			return start;
		} else {
			return null;
		}
	}

	protected Integer walkOnComma() {
		String[] split = unitCronString.split(",");
		int oldStart = clock.get(calendarField);
		for (int i = 0; i < split.length; i++) {
			Integer start = Integer.parseInt(split[i]);
			if (start > oldStart) {
				return Integer.parseInt(split[i]);
			}
		}
		return null;
	}
	
	/**
	 * ����Cron���ʽ������ǰʱ�ӵ�λ�ص���ʼ����ʱ��
	 */
	public Integer adjust() {
		if (unitCronString.equals("*")) {
			return this.adjustOnStar();
		}
		if (unitCronString.contains("/")) {
			return this.adjustOnDiagonal();
		}
		if (unitCronString.contains("-")) {
			return this.adjustOnLine();
		}
		if (unitCronString.contains(",")) {
			return this.adjustOnComma();
		}
		return Integer.parseInt(unitCronString);
	}
	

	protected abstract Integer adjustOnStar();
	

	protected Integer adjustOnDiagonal() {
		String[] split = unitCronString.split("/");
		return Integer.parseInt(split[0]);
	}

	protected Integer adjustOnLine() {
		String[] split = unitCronString.split("-");
		return Integer.parseInt(split[0]);
	}

	protected Integer adjustOnComma() {
		String[] split = unitCronString.split(",");
		return Integer.parseInt(split[0]);
	}

	
	
	/**
	 * ����Cron���ʽ�жϵ�ǰʱ�ӵ�λ�Ƿ����Ҫ��
	 */
	public boolean match() {
		if (unitCronString.equals("*")) {
			 return this.matchOnStars();
		}else if (unitCronString.contains("/")) {
			 return this.matchOnDiagonal();
		}else if (unitCronString.contains("-")) {
			 return this.matchOnLine();
		}else if (unitCronString.contains(",")) {
			 return this.matchOnComma();
		}else{
			return false;
		}
	}


	private boolean matchOnStars() {
		// TODO Auto-generated method stub
		return false;
	}
	

	private boolean matchOnDiagonal() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean matchOnLine() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean matchOnComma() {
		// TODO Auto-generated method stub
		return false;
	}

}
