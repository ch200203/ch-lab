CREATE TABLE batch_job_history
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_name      VARCHAR(100) NOT NULL,             -- 배치 작업 종류
    instance_id   VARCHAR(100),                      -- 실행한 인스턴스 ID 또는 호스트명
    start_time    DATETIME     NOT NULL,             -- 실행 시작 시간
    end_time      DATETIME,                          -- 실행 종료 시간
    status        VARCHAR(20)  NOT NULL,             -- SUCCESS, FAIL, SKIPPED 등 상태
    message       VARCHAR(500),                      -- 실패 시 예외 메시지나 상태 메시지
    rows_affected INT,                               -- 삭제된 데이터 개수 등 처리량
    operator_id   VARCHAR(50),                       -- 사번 (Optional) => custId 가 될 수 도 있음.
    type          VARCHAR(50),                       --
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP -- 이력 기록 시각 (INSERT 시간)
);

-- 개인정보 insert, update, 다건 Delete 가 일어날때 개인정보 CUD 를 모두 포괄하는 테이블로 설게해야함.