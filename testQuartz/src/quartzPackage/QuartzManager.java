//简单的任务管理类
//QuartzManager.java

package quartzPackage;

import java.text.ParseException;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.JobDetail;

/** *//**
 * @Title:Quartz管理类
 * 
 * @Description:
 * 
 * @Copyright: 
 * @author zz  2008-10-8 14:19:01
 * @version 1.00.000
 *
 */
public class QuartzManager {
   private static SchedulerFactory sf = new StdSchedulerFactory();
   private static String JOB_GROUP_NAME = "group1";
   private static String TRIGGER_GROUP_NAME = "trigger1";
  
   
   /** *//**
    *  添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
    * @param jobName 任务名
    * @param job     任务
    * @param time    时间设置，参考quartz说明文档
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void addJob(String jobName,Job job,String time) 
                               throws SchedulerException, ParseException{
       Scheduler sched = sf.getScheduler();
       
       //任务名，任务组，任务执行类
       JobDetail jobDetail = JobBuilder.newJob(job.getClass())
    	       .withIdentity(new JobKey(jobName, JOB_GROUP_NAME)).build();
       //触发器 触发器名,触发器组
       CronTrigger trigger = TriggerBuilder.newTrigger()
           .withIdentity(new TriggerKey(jobName, TRIGGER_GROUP_NAME))
           .withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression(time))).build();
       
       sched.scheduleJob(jobDetail,trigger);
       //启动
       if(!sched.isShutdown())
          sched.start();
   }
   
   /** *//**
    * 添加一个定时任务
    * @param jobName 任务名
    * @param jobGroupName 任务组名
    * @param triggerName  触发器名
    * @param triggerGroupName 触发器组名
    * @param job     任务
    * @param time    时间设置，参考quartz说明文档
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void addJob(String jobName,String jobGroupName,
                             String triggerName,String triggerGroupName,
                             Job job,String time) 
                               throws SchedulerException, ParseException{
       Scheduler sched = sf.getScheduler();
     //任务名，任务组，任务执行类
       JobDetail jobDetail = JobBuilder.newJob(job.getClass())
    	       .withIdentity(new JobKey(jobName, JOB_GROUP_NAME)).build();
       //触发器 触发器名,触发器组
       CronTrigger trigger = TriggerBuilder.newTrigger()
           .withIdentity(new TriggerKey(triggerName, TRIGGER_GROUP_NAME))
           .withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression(time))).build();
       
       sched.scheduleJob(jobDetail,trigger);
       if(!sched.isShutdown())
          sched.start();
   }
   
   /** *//**
    * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
    * @param jobName
    * @param time
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void modifyJobTime(String jobName,String time) 
                                  throws SchedulerException, ParseException{
       Scheduler sched = sf.getScheduler();
       Trigger trigger =  sched.getTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
       if(trigger != null){
           CronTrigger  ct = (CronTrigger)trigger;
           ct.setCronExpression(time);
           sched.resumeTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
       }
   }
   
   /** *//**
    * 修改一个任务的触发时间
    * @param triggerName
    * @param triggerGroupName
    * @param time
    * @throws SchedulerException
    * @throws ParseException
    */
   public static void modifyJobTime(String triggerName,String triggerGroupName,
                                    String time) 
                                  throws SchedulerException, ParseException{
       Scheduler sched = sf.getScheduler();
       Trigger trigger =  sched.getTrigger(new TriggerKey(triggerName,triggerGroupName));
       if(trigger != null){
           CronTrigger  ct = (CronTrigger)trigger;
           //修改时间
           ct.setCronExpression(time);
           //重启触发器
           sched.resumeTrigger(new TriggerKey(triggerName,triggerGroupName));
       }
   }
   
   /** *//**
    * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
    * @param jobName
    * @throws SchedulerException
    */
   public static void removeJob(String jobName) 
                               throws SchedulerException{
       Scheduler sched = sf.getScheduler();
       sched.pauseTrigger(new TriggerKey(jobName,TRIGGER_GROUP_NAME));//停止触发器
       sched.unscheduleJob(new TriggerKey(jobName,TRIGGER_GROUP_NAME));//移除触发器
       sched.deleteJob(new JobKey(jobName,JOB_GROUP_NAME));//删除任务
   }
   
   /** *//**
    * 移除一个任务
    * @param jobName
    * @param jobGroupName
    * @param triggerName
    * @param triggerGroupName
    * @throws SchedulerException
    */
   public static void removeJob(String jobName,String jobGroupName,
                                String triggerName,String triggerGroupName) 
                               throws SchedulerException{
       Scheduler sched = sf.getScheduler();
       sched.pauseTrigger(new TriggerKey(triggerName,triggerGroupName));//停止触发器
       sched.unscheduleJob(new TriggerKey(triggerName,triggerGroupName));//移除触发器
       sched.deleteJob(new JobKey(jobName,jobGroupName));//删除任务
   }
}

