package com.std.framework.controller.timer.stepor;

import java.util.Calendar;

public class MonthStepor extends ClockStepor {

	public MonthStepor(String monthString, Calendar clock) {
		this.unitCronString = monthString;
		this.calendarField = Calendar.MONTH;
		this.carryOverLimit = 12;
		this.clock = clock;
	}

	@Override
	public Integer walkOnDiagonal() {
		String[] split = unitCronString.split("/");
		int interval = Integer.parseInt(split[1]);
		clock.roll(calendarField, interval);
		Integer start = clock.get(calendarField);
		// ���·ݵļ�������У����������ڵ���ʱ�̣���ô�϶�Ϊ�ڶ��µ����һ�촥��
		if (calendarField == Calendar.DAY_OF_MONTH) {
			int monthDayCounts = getMonthDayCounts(clock.get(Calendar.YEAR), clock.get(Calendar.MONTH));
			if (interval > monthDayCounts) {
				start = monthDayCounts;
			}
		}
		// ʹ�ü����/������ʱ�������Ƿ������λ���������ʱ��С��ʱ�̼�������ʾ�����˽�λ
		if (start < interval) {
			return null;
		}
		return start;
	}

	@Override
	public Integer walkOnComma() {
		String[] split = unitCronString.split(",");
		int oldStart = clock.get(calendarField);
		for (int i = 0; i < split.length; i++) {
			Integer start = Integer.parseInt(split[i]);
			if (start > oldStart) {
				// ���·ݵļ�������У�������ŷָ���趨ʱ�̴��ڵ������ʱ�̣���ô������λ
				if (calendarField == Calendar.DAY_OF_MONTH
						&& start > getMonthDayCounts(clock.get(Calendar.YEAR), clock.get(Calendar.MONTH))) {
					return null;
				}
				return Integer.parseInt(split[i]);
			}
		}
		return null;
	}

	/**
	 * �õ�ָ���µ�����
	 * */
	private int getMonthDayCounts(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// ����������Ϊ���µ�һ��
		a.roll(Calendar.DATE, -1);// ���ڻع�һ�죬Ҳ�������һ��
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}


	@Override
	protected Integer adjustOnStar() {
		// TODO Auto-generated method stub
		return null;
	}

}
