package com.scheduler.core;

import com.scheduler.state.ReadyTaskState;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskExecutor<I,O> {

    private final int numWorkers;
    private final ExecutorService executorService;
    private final BlockingQueue<TaskExecutionContext<I, O>> taskQueue;
    private final Logger logger = Logger.getLogger(TaskExecutor.class.getName());

    public TaskExecutor(int numWorkers) {
        this.numWorkers = numWorkers;
        this.executorService = Executors.newFixedThreadPool(numWorkers);
        this.taskQueue = new LinkedBlockingQueue<>();
    }

    public void start() {
        for(int i = 0; i < numWorkers; i++) {
            executorService.submit(this::workerLoop);
        }
    }

    public void workerLoop() {
        while(true) {
            try {
                System.out.println("Waiting for task...");
                TaskExecutionContext<I, O> context = taskQueue.take();
                if (context.getState() instanceof ReadyTaskState) {
                   System.out.println("Task received: " + context.getTask().getId());
                   System.out.println("Task state: " + context.getState());
                   System.out.println("Task input: " + context.getInput());

                    
                    context.getState().process(context);
                    //context.setState(new RunningTaskState());
                    //context.transitionTo(new RunningTaskState());
                    //context.execute();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in worker loop", e);
            }
        }
    }

    public void submitTask(TaskExecutionContext<I, O> context) {
        taskQueue.add(context);
    }

    public void stop() {
        executorService.shutdown();
    }
    
}
