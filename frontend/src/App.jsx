import { useEffect, useState } from 'react'
import CustomerApp from './customer/CustomerApp.jsx'
import OwnerApp from './owner/OwnerApp.jsx'

export default function App() {
  const [hash, setHash] = useState(window.location.hash)

  useEffect(() => {
    const onHash = () => setHash(window.location.hash)
    window.addEventListener('hashchange', onHash)
    return () => window.removeEventListener('hashchange', onHash)
  }, [])

  const isOwner = hash.startsWith('#owner')

  return (
    <div className="app">
      <header className="topbar">
        <span className="brand">☕ brewtiful sip</span>
        <a className="modeToggle" href={isOwner ? '#' : '#owner'}>
          {isOwner ? '손님 화면' : '운영자'}
        </a>
      </header>
      {isOwner ? <OwnerApp /> : <CustomerApp />}
    </div>
  )
}
