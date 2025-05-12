package com.scheduler.examples;

import com.scheduler.core.InputProvider;
import com.scheduler.core.Task;
import com.scheduler.core.TaskExecutor;
import com.scheduler.core.Task.TaskType;
import com.scheduler.repo.InMemoryTaskRepository;
import com.scheduler.scheduler.TaskScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProcessingExample {
    
    public static void main(String[] args) {
          
        // 1. Word count task
        Task<List<String>, Map<String, Integer>> wordCountTask = new Task.Builder<List<String>, Map<String, Integer>>("wordCount")
            .fn(lines -> {
                Map<String, Integer> wordCounts = new HashMap<>();
                
                for (String line : lines) {
                    Arrays.stream(line.toLowerCase().split("\\W+"))
                        .filter(word -> !word.isEmpty())
                        .forEach(word -> 
                            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1));
                }
                
                return wordCounts;
            })
            .retries(2)
            .build();
        
        // Sample input data - Data input for Task
        List<String> input = Arrays.asList(
            "The quick brown fox jumps over the lazy dog",
            "The dog barks at the fox",
            "The fox runs away"
        );

        // Execute task directly
        Map<String, Integer> output = wordCountTask.execute(input);
        output.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

       System.out.println("--------------------------------------------------");     

       // 1. Task Repo
       InMemoryTaskRepository<List<String>, Map<String, Integer>> repository = new InMemoryTaskRepository<>();
        
        
        // 4. Create an input provider for the word count task
         InputProvider<List<String>> textInputProvider = () -> {
            List<String> list = new ArrayList<>();
            list.add("The quick brown fox jumps over the lazy dog");
            list.add("The dog barks at the fox");
            list.add("The fox runs away");
            return list;
         };

        // 5. Register the input provider with the task
       // Word count task with input provider
       Task<List<String>, Map<String, Integer>> wordCountTaskI = new Task.Builder<List<String>, Map<String, Integer>>("wordCount")
       .fn(lines -> {
           Map<String, Integer> wordCounts = new HashMap<>();
           
           for (String line : lines) {
               Arrays.stream(line.toLowerCase().split("\\W+"))
                   .filter(word -> !word.isEmpty())
                   .forEach(word -> 
                       wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1));
           }
           
           return wordCounts;
       })
       .retries(2)
       .type(TaskType.SIMPLE)
       .inputProvider(textInputProvider)
       .build();

       // 6. Add task to task repo
        repository.registerTask(wordCountTaskI);

        // We should be staring scheduler and executor once task is added to the repo

        // 2. Create task executor
       TaskExecutor<List<String>, Map<String, Integer>> executor = new TaskExecutor<>(3);
       executor.start();

       // 3. Create scheduler
       TaskScheduler<List<String>, Map<String, Integer>> scheduler = new TaskScheduler<>(repository, executor);
       scheduler.start();


      //7. Task scheduler will fetch tasks from Task repo
      // It will add tasks to Task executor queue
      // Task executor will execute tasks - Follow state pattern

      
        //scheduler.stop();
        //executor.stop();
    }
}