import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    scenarios: {
      getOwnerInfo: {
        executor: 'shared-iterations',
        exec: 'getOwnerInfo',
        vus: 80,
        iterations: 1000,
      },
      newOwner: {
        executor: 'shared-iterations',
        exec: 'newOwner',
        vus: 20,
        iterations: 1000,
      },
    },
  };

  export function getOwnerInfo() {
    http.get('http://service:8080/owners/ivanov');
    sleep(1)
  }
  
  export function newOwner() {
    let res = http.post('http://service:8080/owners/', 
        JSON.stringify({ nickName: "ivanov", name: 'Ivanov' }), {
        headers: { 'Content-Type': 'application/json' },
    });
    sleep(3)
  }