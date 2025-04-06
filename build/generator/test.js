import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    scenarios: {
      getAll: {
        executor: 'shared-iterations',
        exec: 'all',
        vus: 80,
        iterations: 1000,
      },
      newThing: {
        executor: 'shared-iterations',
        exec: 'newThing',
        vus: 20,
        iterations: 1000,
      },
    },
  };

  export function all() {
    http.get('http://service:8080/things');
    sleep(1)
  }
  
  export function newThing() {
    let res = http.post('http://service:8080/things/ivanov', 
        JSON.stringify({ name: 'iPhone' }), {
        headers: { 'Content-Type': 'application/json' },
    });
    sleep(3)
  }