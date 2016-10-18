package com.std.framework.controller.timer;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

import com.std.framework.controller.timer.quartz.CronExpression;
import com.std.framework.core.log.Log;
import com.std.framework.core.log.LogFactory;

@SuppressWarnings("unused")
public class JobRunnable implements Runnable {

	private static Log logger = LogFactory.getLogger();

	private String jobName;
	private Class<?> jobClass;
	private Object jobInstance;
	private Method jobMethod;
	private CronWrapper wrapper;
	private CronExpression expression;
	private ScheduledExecutorService service;

	private static final Object syncObj = new Object();
	private static final Long SLEEP_MAX_TIME = 86400000l ;
	private static final Long SLEEP_MIN_TIME = 100l ;
	private static final Long ALLOW_DIFFERENCE = 1000l ;

	public JobRunnable(JobDetail job, ScheduledExecutorService service) throws Exception {
		try {
			jobName = job.getJobName();
			jobClass = Class.forName(job.getJobClass());
			jobInstance = jobClass.newInstance();
			jobMethod = jobClass.getDeclaredMethod(job.getJobMethod(), new Class[] {});
//			wrapper = new CronWrapper(job.getJobCron());
			expression = new CronExpression(job.getJobCron());
			this.service = service;
			logger.debug("����" + jobName + "���سɹ���׼��ִ��...");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void run() {
		
		// ��ȡ�����һ��ִ��ʱ��
//		long triggerTime = cronWrapper.getNextTriggerTime();
		long triggerTime = expression.getNextValidTimeAfter(new Date()).getTime();
		
		while (true) {
			if ( Math.abs(triggerTime - System.currentTimeMillis()) < ALLOW_DIFFERENCE) {
				try {
					jobMethod.invoke(jobInstance, new Object[] {});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// ����ִ����Ϻ󣬼�����һ���������ʱ�䣬���½�����ѯ����
//				triggerTime = cronWrapper.getNextTriggerTime();
				triggerTime = expression.getNextValidTimeAfter(new Date()).getTime();

				// -100l ��ʾ���񳹵׽�����������ֹ���������ѯ���߳�
				if (triggerTime == -100l) {
					this.service.shutdown();
					break;
				}else{
					sleepByTriTime(triggerTime);
				}
			}else{
				sleep(SLEEP_MIN_TIME);
			}
		}
	}

	//������һ�����񴥷�ʱ�������ߵ�ǰ�߳�˯��ʱ��
	private void sleepByTriTime(long triggerTime) {
		long sleepTime = triggerTime - System.currentTimeMillis();
		if(sleepTime < SLEEP_MIN_TIME ){
			sleepTime = SLEEP_MIN_TIME ;
		}
		else if(sleepTime > SLEEP_MAX_TIME){
			sleepTime = SLEEP_MAX_TIME  ;
		}
		sleep( sleepTime );
	}
	
	//����ѭ������̽��ѹ��
	private void sleep(long sleepTime){
		synchronized (syncObj) {
			try {
				syncObj.wait(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
