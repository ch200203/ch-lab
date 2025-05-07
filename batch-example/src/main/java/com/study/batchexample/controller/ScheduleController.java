package com.study.batchexample.controller;

import com.study.batchexample.schedule.ScheduleDeleteTask;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleDeleteTask scheduleDeleteTask;

    @DeleteMapping("/batch-test") // POST
    public ResponseEntity<String> deleteBatch() {
        scheduleDeleteTask.deleteData();
        return ResponseEntity.ok().body("배치 성공!");
    }

}
