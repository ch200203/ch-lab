import http from 'k6/http';
import { check, sleep } from 'k6';

// ─── 테스트 설정 ───────────────────────────────────────────────────
export const options = {
    scenarios: {
        clicks_and_get: {
            executor: 'shared-iterations',
            vus: 100,             // 동시 사용자 수
            iterations: 300,      // 총 클릭(＋조회) 반복 횟수
            maxDuration: '5m',     // 테스트 최대 실행 시간
        },
    },
    thresholds: {
        // 95% 요청은 500ms 이내여야
        http_req_duration: ['p(95)<500'],
    },
};

const BASE_URL = 'http://localhost:8080';
const EVENT_ID = '1';      // 테스트할 이벤트 ID (Long 타입)

// ─── VU 당 실행 함수 ───────────────────────────────────────────────────
export default function () {
    // 1) 클릭 API 호출
    const clickUrl = `${BASE_URL}/event/${EVENT_ID}/clicks`;
    const payload = JSON.stringify({
        deviceId: `device-${__VU}-${__ITER}`,  // VU/Iteration 기반 임의값
        gender: __VU % 2 === 0 ? 'M' : 'F', // 남녀 둘중하나
        age: 20 + (__VU % 5) * 10,
    });
    const headers = { 'Content-Type': 'application/json' };

    let res = http.post(clickUrl, payload, { headers });
    check(res, {
        'click: status is 200': (r) => r.status === 200,
        'click: has clickCount': (r) => r.json('clickCount') !== undefined,
    });

    // 2) 조회 API 호출
    const getUrl = `${BASE_URL}/event/${EVENT_ID}`;
    let res2 = http.get(getUrl);
    check(res2, {
        'get: status is 200': (r) => r.status === 200,
        'get: returns number': (r) => typeof r.body === 'string' && !isNaN(Number(r.body)),
    });

    // 아주 짧게 쉬고 다음 반복
    sleep(0.01);
}

// 실행 방법 k6 run {fileName}.js