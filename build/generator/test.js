import http from 'k6/http';
import { sleep } from 'k6';
import { describe, expect } from 'https://jslib.k6.io/k6chaijs/4.3.4.3/index.js';
import { Trend } from 'k6/metrics';

const things_waiting_time = new Trend('things_waiting_time');

export const options = {
  duration: '4h',
  thresholds: {
    checks: ['rate == 1.00'],
  },
};

export default function () {
  describe('Get things list!', () => {
    const response = http.get('http://dbservice:8080/things');
    things_waiting_time.add(response.timings.waiting);
    expect(response.status, 'response status').to.equal(200);
    expect(response).to.have.validJsonBody();
    // expect(response.json('ratings'), 'ratings list').to.be.an('array');
    sleep(1)
  });
}