package mes.time.biz;

import kr.co.mesframe.util.DateUtil;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.InitializingBean;

public class SchedulerService implements InitializingBean
{
	private SchedulerFactory	schedulerFactory;
	private Scheduler			scheduler;
	private String				cronExpression;
	private long				repeatInterval	= 0;
	private int					repeatCount		= 0;

	private String				scheduleName	= "scheduleFrame";
	private String				scheduleGroup	= DateUtil.getCurrentTime();
	private Job					jobClass;

	public long getRepeatInterval()
	{
		return repeatInterval;
	}

	public void setRepeatInterval(long repeatInterval)
	{
		this.repeatInterval = repeatInterval;
	}

	public int getRepeatCount()
	{
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount)
	{
		this.repeatCount = repeatCount;
	}

	public SchedulerFactory getSchedulerFactory()
	{
		return schedulerFactory;
	}

	public void setSchedulerFactory(SchedulerFactory schedulerFactory)
	{
		this.schedulerFactory = schedulerFactory;
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public void setCronExpression(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public String getScheduleName()
	{
		return scheduleName;
	}

	public void setScheduleName(String scheduleName)
	{
		this.scheduleName = scheduleName;
	}

	public String getScheduleGroup()
	{
		return scheduleGroup;
	}

	public void setScheduleGroup(String scheduleGroup)
	{
		this.scheduleGroup = scheduleGroup;
	}

	public Job getJobClass()
	{
		return jobClass;
	}

	public void setJobClass(Job jobClass)
	{
		this.jobClass = jobClass;
	}

	public SchedulerService()
	{
		schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
	}

	public void afterPropertiesSet() throws Exception
	{
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		JobDetail jobDetail = new JobDetail(scheduleName, scheduleGroup, jobClass.getClass());
		if (this.repeatInterval == 0 && this.repeatCount == 0)
		{
			CronTrigger trigger = new CronTrigger(scheduleName, scheduleGroup);
			trigger.setCronExpression(cronExpression);
			scheduler.scheduleJob(jobDetail, trigger);
		}
		else
		{
			SimpleTrigger trigger = new SimpleTrigger(scheduleName, scheduleGroup);
			trigger.setRepeatCount(this.repeatCount);
			trigger.setRepeatInterval(this.repeatInterval);
			scheduler.scheduleJob(jobDetail, trigger);
		}
	}

}
