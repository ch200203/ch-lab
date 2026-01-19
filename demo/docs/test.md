- 우선 패턴을 담을 실험용 랩을 정합니다. 이벤트/스프링 예제가 섞여 있는 구조이므로 루트에 transactional-outbox-lab 같은 새 디렉터리를 만들어 기존 랩처럼 Gradle Wrapper, build.gradle[.kts], settings.gradle[.kts]를 복사해 두면
  독립적인 빌드·실행이 쉬워집니다.
- 스택은 Spring Boot + Spring Data JPA가 가장 다루기 편합니다. H2(또는 로컬 Postgres) 의존성과 JPA, Web, 필요 시 메시징/스케줄러 스타터를 추가해 트랜잭션 내 DB 쓰기와 폴링 작업을 손쉽게 구성합니다.
- 핵심 도메인 엔티티(예: Order)와 별도 OutboxMessage 엔티티를 같은 DB에 정의합니다. payload, eventType, status, createdAt/processedAt 컬럼을 두고, 패키지는 .../domain, .../outbox 형태로 나누면 구조가 명확합니다.
- 패턴 구현은 두 단계로 나눕니다. 1) 서비스 계층에서 하나의 트랜잭션으로 도메인 엔티티와 아웃박스 레코드를 함께 저장합니다. 2) @Scheduled 잡이나 커스텀 폴러가 status=PENDING 레코드를 조회해 메시지를 브로커(Kafka 템플릿 등)로
  발행하고, 성공하면 status=PROCESSED로 갱신합니다.
- REST 컨트롤러나 CommandLineRunner를 추가해 주문 생성 같은 트랜잭션을 쉽게 호출하도록 하세요. 이렇게 하면 POST 요청 한 번으로 DB 쓰기와 아웃박스 적재 → 폴링 → 발행 흐름을 검증할 수 있습니다.
- 테스트는 src/test/java에서 프로덕션 패키지를 그대로 미러링합니다. @DataJpaTest로 Outbox 저장 로직을 검증하고, @SpringBootTest + H2로 트랜잭션 커밋 시 도메인/아웃박스가 동시에 저장되는지, 폴러가 상태를 변경하는지 확인하세요.
- 마지막으로 새 랩의 README 또는 docs/에 실행 방법을 남깁니다. 예: cd transactional-outbox-lab && ./gradlew bootRun으로 데모 실행, ./gradlew test로 회귀 테스트. 샘플 curl 요청이나 시드 데이터를 첨부하면 다른 기여자가 바로 재
  현할 수 있습니다.


- 데이터베이스/로그 환경 정하기: 트랜잭션 로그를 읽을 수 있는 DB(PostgreSQL, MySQL binlog 등)와 CDC 툴(Debezium, Maxwell, WAL tailer)을 선택합니다. 샌드박스에서는 Docker Compose로 DB+CDC 커넥터를 띄우고, Outbox 테이블이 로그
    에 기록되도록 필요한 플러그인과 권한을 미리 설정합니다.
- Outbox 테이블 스키마 설계: 서비스 DB에 outbox_event 테이블을 만들고 id, aggregate_type/id, event_type, payload, occurred_at, processed 같은 컬럼을 둡니다. 애플리케이션 트랜잭션에서 도메인 데이터와 함께 이 테이블에 INSERT가
  발생해야 로그에 이벤트가 남습니다.
- 애플리케이션 트랜잭션 코드 구현: Spring Data JPA 등으로 도메인 엔티티 저장과 Outbox INSERT를 같은 트랜잭션으로 묶습니다. 이때 Outbox 엔티티에 최소한의 정보만 담고, 필요한 이벤트 메타데이터를 JSON payload로 직렬화해 기록합니
  다. 추가 폴링 로직은 필요 없습니다.
- CDC 커넥터 구성: Debezium 같은 엔진을 실행해 Outbox 테이블의 변경만 구독하도록 설정합니다. database.include.list와 table.include.list를 이용해 불필요한 테이블을 제외하고, 스키마 변화를 추적하려면 schema.history 저장 위치를
  지정합니다. 오프셋 저장 경로도 지정해 재시작 시 마지막 위치부터 이어갈 수 있게 합니다.
- 메시지 브로커/전달 파이프라인 연결: CDC 커넥터의 Sink를 Kafka, Pulsar, HTTP 등 원하는 채널로 연결합니다. 예를 들어 Debezium → Kafka → Spring 소비자 흐름이라면 Kafka Connect에 Outbox 토픽을 지정하고, Downstream 서비스는 해당
  토픽을 구독해 이벤트를 처리합니다.
- 재처리 및 모니터링 전략: CDC가 실패하거나 로그 포지션이 밀렸을 때를 대비해 오프셋 백업, 알람, DLQ 구성을 준비합니다. Outbox 테이블에 processed 플래그를 두어 로그 tailing과 별개로 관리/정리할 수 있고, 주기적으로 오래된 레코
  드를 아카이빙하는 배치를 추가합니다.
- 개발/테스트 플로우 확립: 로컬에서는 Docker Compose로 DB+Debezium+Kafka를 올리고 curl 또는 테스트 코드로 이벤트를 생성해 end-to-end 흐름을 검증합니다. ./gradlew test로 애플리케이션 단의 트랜잭션 로직을 검증하고, 통합 테스트
  는 Testcontainers를 활용해 CDC 파이프라인까지 포함시킬 수 있습니다.
