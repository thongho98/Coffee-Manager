package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(this.getClass().getResource("/View/Login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}

//	private void runThread() throws SchedulerException {
//		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerName", "group1")
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();
//
//		JobDetail job = JobBuilder.newJob(HomeOrderTableController.class).withIdentity("jobName", "group1").build();
//		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//		scheduler.start();
//		scheduler.scheduleJob(job, trigger);

//		Trigger triggerHomeStaff = TriggerBuilder.newTrigger().withIdentity("triggerName", "group1")
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();
//
//		JobDetail jobHomeStaff = JobBuilder.newJob(HomeStaffController.class).withIdentity("jobName", "group1").build();
//		Scheduler schedulerHomeStaff = new StdSchedulerFactory().getScheduler();
//		schedulerHomeStaff.start();
//		schedulerHomeStaff.scheduleJob(jobHomeStaff, triggerHomeStaff);
//	}
}
