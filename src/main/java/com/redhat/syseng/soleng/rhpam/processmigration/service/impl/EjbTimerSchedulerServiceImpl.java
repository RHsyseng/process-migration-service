package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Migration;
import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.service.MigrationService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PlanService;
import com.redhat.syseng.soleng.rhpam.processmigration.service.SchedulerService;

@Singleton
@Startup
public class EjbTimerSchedulerServiceImpl implements SchedulerService {

    @Resource
    private TimerService timerService;

    @Inject
    private MigrationService migrationService;

    @Inject
    private PlanService planService;

    @Timeout
    public void doMigration(Timer timer) {
        String timerInfoStr = (String) timer.getInfo();
        String migrationId = timerInfoStr.substring(0, timerInfoStr.indexOf(","));
        String planId = timerInfoStr.substring(timerInfoStr.indexOf(",") + 1);
        Migration migration = migrationService.get(Long.parseLong(migrationId));
        Plan plan = planService.get(Long.parseLong(planId));

        migrationService.migrate(migration, plan);
    }

    @Override
    public void scheduleMigration(Migration migration, Plan plan) {
        String timerInfoStr = migration.getId() + "," + plan.getId();
        timerService.createTimer(migration.getDefinition().getExecution().getScheduledStartTime(), timerInfoStr);
    }

    @PreDestroy
    public void stop() {
        // Stop all timers
        for (Timer timer : timerService.getTimers()) {
            System.out.println("Stopping timer: " + timer.getInfo());
            timer.cancel();
        }
    }
}
