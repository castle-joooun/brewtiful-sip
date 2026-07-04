import { useEffect, useState } from 'react'
import { fetchEventSource } from '@microsoft/fetch-event-source'
import { api, STATUS_LABEL } from '../api.js'

export default function CustomerApp() {
  const [menus, setMenus] = useState([])
  const [beans, setBeans] = useState([])
  const [cart, setCart] = useState([])
  const [order, setOrder] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.getMenus().then(setMenus).catch((e) => setError(e.message))
    api.getBeans().then(setBeans).catch((e) => setError(e.message))
  }, [])

  if (order) {
    return <OrderStatus order={order} onNew={() => setOrder(null)} />
  }

  const addToCart = (menu, beanId) => {
    const bean = beans.find((b) => b.id === Number(beanId))
    setCart((c) => [
      ...c,
      {
        menuId: menu.id,
        menuName: menu.name,
        beanId: bean ? bean.id : null,
        beanName: bean ? bean.name : null,
        quantity: 1,
        price: menu.price,
      },
    ])
  }

  const removeFromCart = (idx) => setCart((c) => c.filter((_, i) => i !== idx))
  const total = cart.reduce((sum, it) => sum + it.price * it.quantity, 0)

  const placeOrder = async () => {
    try {
      const items = cart.map((it) => ({ menuId: it.menuId, beanId: it.beanId, quantity: it.quantity }))
      const res = await api.createOrder(items)
      setOrder({ orderId: res.orderId, orderToken: res.orderToken })
      setCart([])
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <main className="screen">
      {error && <div className="error">{error}</div>}
      <h2>메뉴</h2>
      <ul className="cardList">
        {menus.map((m) => (
          <MenuCard key={m.id} menu={m} beans={beans} onAdd={addToCart} />
        ))}
      </ul>
      <CartBar cart={cart} total={total} onRemove={removeFromCart} onOrder={placeOrder} />
    </main>
  )
}

function MenuCard({ menu, beans, onAdd }) {
  const [beanId, setBeanId] = useState('')
  const canAdd = !menu.requiresBean || beanId !== ''

  const add = () => {
    onAdd(menu, beanId)
    setBeanId('')
  }

  return (
    <li className="card">
      <div className="cardMain">
        <span className="cardName">{menu.name}</span>
        <span className="cardPrice">{menu.price.toLocaleString()}원</span>
      </div>
      {menu.requiresBean && (
        <select value={beanId} onChange={(e) => setBeanId(e.target.value)}>
          <option value="">원두 선택</option>
          {beans.map((b) => (
            <option key={b.id} value={b.id}>
              {b.name}
            </option>
          ))}
        </select>
      )}
      <button className="primary sm" disabled={!canAdd} onClick={add}>
        담기
      </button>
    </li>
  )
}

function CartBar({ cart, total, onRemove, onOrder }) {
  if (cart.length === 0) return null
  return (
    <div className="cartBar">
      <ul className="cartItems">
        {cart.map((it, i) => (
          <li key={i}>
            <span>
              {it.menuName}
              {it.beanName ? ` · ${it.beanName}` : ''}
            </span>
            <button className="link" onClick={() => onRemove(i)}>
              삭제
            </button>
          </li>
        ))}
      </ul>
      <button className="primary block" onClick={onOrder}>
        {total.toLocaleString()}원 · 주문하기
      </button>
    </div>
  )
}

function OrderStatus({ order, onNew }) {
  const [detail, setDetail] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    let aborted = false
    const load = () =>
      api
        .getOrder(order.orderId, order.orderToken)
        .then((d) => !aborted && setDetail(d))
        .catch((e) => !aborted && setError(e.message))
    load()

    const ctrl = new AbortController()
    fetchEventSource(
      `${api.base}/orders/${order.orderId}/stream?token=${encodeURIComponent(order.orderToken)}`,
      {
        signal: ctrl.signal,
        openWhenHidden: true,
        onmessage(ev) {
          if (ev.event === 'order-status') load()
        },
        onerror() {
          /* fetch-event-source가 자동 재시도 */
        },
      },
    ).catch(() => {})

    return () => {
      aborted = true
      ctrl.abort()
    }
  }, [order])

  if (!detail) {
    return (
      <main className="screen">
        <p className="muted">주문 정보를 불러오는 중…</p>
        {error && <div className="error">{error}</div>}
      </main>
    )
  }

  return (
    <main className="screen">
      <div className={`statusBadge ${detail.status}`}>{STATUS_LABEL[detail.status]}</div>
      <p className="muted">주문 #{detail.orderId}</p>
      <ul className="cardList">
        {detail.items.map((it) => (
          <li key={it.orderItemId} className="card">
            <div className="cardMain">
              <span className="cardName">
                {it.menuName}
                {it.beanName ? ` · ${it.beanName}` : ''}
              </span>
              <span className="cardPrice">{it.unitPrice.toLocaleString()}원</span>
            </div>
            {detail.status === 'READY' &&
              (it.reviewed ? (
                <span className="muted">리뷰 완료 ✓</span>
              ) : (
                <ReviewForm
                  orderItemId={it.orderItemId}
                  token={order.orderToken}
                  onDone={() =>
                    api.getOrder(order.orderId, order.orderToken).then(setDetail).catch(() => {})
                  }
                />
              ))}
          </li>
        ))}
      </ul>
      <button className="link" onClick={onNew}>
        ← 새 주문
      </button>
      {error && <div className="error">{error}</div>}
    </main>
  )
}

function ReviewForm({ orderItemId, token, onDone }) {
  const [rating, setRating] = useState(5)
  const [content, setContent] = useState('')
  const [error, setError] = useState(null)

  const submit = async () => {
    try {
      await api.createReview(orderItemId, token, rating, content)
      onDone()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <div className="reviewForm">
      <select value={rating} onChange={(e) => setRating(Number(e.target.value))}>
        {[5, 4, 3, 2, 1].map((n) => (
          <option key={n} value={n}>
            {'★'.repeat(n)}
          </option>
        ))}
      </select>
      <input
        placeholder="후기 (선택)"
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <button className="primary sm" onClick={submit}>
        등록
      </button>
      {error && <div className="error">{error}</div>}
    </div>
  )
}
