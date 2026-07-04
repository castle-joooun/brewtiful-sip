import { useEffect, useRef, useState } from 'react'
import { fetchEventSource } from '@microsoft/fetch-event-source'
import { api } from '../api.js'

export default function OwnerApp() {
  const [code, setCode] = useState('')
  const [connected, setConnected] = useState(false)
  const [orders, setOrders] = useState([])
  const [error, setError] = useState(null)
  const ctrlRef = useRef(null)

  useEffect(() => () => ctrlRef.current?.abort(), [])

  const connect = () => {
    setError(null)
    const ctrl = new AbortController()
    ctrlRef.current = ctrl
    fetchEventSource(`${api.base}/orders/stream`, {
      signal: ctrl.signal,
      openWhenHidden: true,
      headers: { 'X-Master-Code': code },
      async onopen(res) {
        if (res.ok) {
          setConnected(true)
        } else {
          setError(res.status === 401 ? '마스터 코드가 올바르지 않습니다.' : '연결에 실패했습니다.')
          ctrl.abort()
        }
      },
      onmessage(ev) {
        if (ev.event === 'order-created') {
          const d = JSON.parse(ev.data)
          setOrders((o) => [{ orderId: d.orderId, status: 'RECEIVED', itemCount: d.itemCount }, ...o])
        } else if (ev.event === 'order-status') {
          const d = JSON.parse(ev.data)
          setOrders((o) => o.map((x) => (x.orderId === d.orderId ? { ...x, status: d.status } : x)))
        }
      },
      onerror(err) {
        setError('연결 오류가 발생했습니다.')
        throw err // 재시도 중단
      },
    }).catch(() => setConnected(false))
  }

  const markReady = async (orderId) => {
    try {
      await api.changeStatus(orderId, 'READY', code)
    } catch (e) {
      setError(e.message)
    }
  }

  if (!connected) {
    return (
      <main className="screen">
        <h2>운영자 대시보드</h2>
        <p className="muted">마스터 코드를 입력해 실시간 주문을 받습니다.</p>
        <input
          type="password"
          placeholder="마스터 코드"
          value={code}
          onChange={(e) => setCode(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && code && connect()}
        />
        <button className="primary block" onClick={connect} disabled={!code}>
          연결
        </button>
        {error && <div className="error">{error}</div>}
      </main>
    )
  }

  return (
    <main className="screen">
      <h2>
        주문 현황 <span className="live">● LIVE</span>
      </h2>
      {orders.length === 0 && <p className="muted">새 주문을 기다리는 중…</p>}
      <ul className="cardList">
        {orders.map((o) => (
          <li key={o.orderId} className="card">
            <div className="cardMain">
              <span className="cardName">
                주문 #{o.orderId} · {o.itemCount ?? '-'}건
              </span>
              <span className={`statusBadge sm ${o.status}`}>{o.status}</span>
            </div>
            {o.status !== 'READY' && (
              <button className="primary sm" onClick={() => markReady(o.orderId)}>
                준비완료
              </button>
            )}
          </li>
        ))}
      </ul>
      {error && <div className="error">{error}</div>}
    </main>
  )
}
