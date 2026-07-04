# brewtiful-sip — frontend

모바일 우선 React(Vite) 프론트엔드. 손님 주문 화면 + 운영자 실시간 대시보드(SSE).

## 실행

```bash
cd frontend
npm install
npm run dev        # http://localhost:5173
```

백엔드(`http://localhost:8080`)가 떠 있어야 한다. API 주소는 `VITE_API_BASE`로 바꿀 수 있다.

- 손님 화면: `http://localhost:5173/`
- 운영자 대시보드: `http://localhost:5173/#owner` (마스터 코드 입력)

## 구성

- `src/api.js` — REST 클라이언트
- `src/customer/CustomerApp.jsx` — 메뉴/장바구니/주문/상태(SSE)/리뷰
- `src/owner/OwnerApp.jsx` — 마스터코드 연결, 실시간 주문 피드(SSE), 준비완료 처리
- SSE는 `@microsoft/fetch-event-source` 사용(운영자 스트림이 X-Master-Code 헤더를 요구해
  헤더 지원이 필요하기 때문. 브라우저 기본 EventSource는 커스텀 헤더 불가).
