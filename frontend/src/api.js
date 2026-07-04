const BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080'

async function req(path, options = {}) {
  const res = await fetch(BASE + path, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  })
  if (!res.ok) {
    let body
    try {
      body = await res.json()
    } catch {
      body = { message: res.statusText }
    }
    throw new Error(body.message || '요청에 실패했습니다.')
  }
  return res.status === 204 ? null : res.json()
}

export const api = {
  base: BASE,
  getBeans: () => req('/beans'),
  getMenus: () => req('/menus'),
  createOrder: (items) => req('/orders', { method: 'POST', body: JSON.stringify({ items }) }),
  getOrder: (id, token) => req(`/orders/${id}?token=${encodeURIComponent(token)}`),
  changeStatus: (id, status, masterCode) =>
    req(`/orders/${id}/status`, {
      method: 'PATCH',
      headers: { 'X-Master-Code': masterCode },
      body: JSON.stringify({ status }),
    }),
  createReview: (orderItemId, token, rating, content) =>
    req(`/order-items/${orderItemId}/reviews`, {
      method: 'POST',
      headers: { 'X-Order-Token': token },
      body: JSON.stringify({ rating, content }),
    }),
  getReviews: (menuId) => req('/reviews' + (menuId ? `?menuId=${menuId}` : '')),
  getPendingOrders: (masterCode) =>
    req('/orders/pending', { headers: { 'X-Master-Code': masterCode } }),
}

export const STATUS_LABEL = {
  RECEIVED: '접수됨',
  PREPARING: '제조중',
  READY: '준비완료',
}
