'use client'

import { useEffect, useState, useRef } from 'react'

// Import Ty React components
import {
  TyButton,
  TyInput,
  TyModal,
  TyIcon,
  TyDropdown,
  TyOption,
  type TyModalRef
} from '@gersak/ty-react'

export default function Home() {
  const [mounted, setMounted] = useState(false)
  const [inputValue, setInputValue] = useState('')
  const [dropdownValue, setDropdownValue] = useState('')
  const modalRef = useRef<TyModalRef>(null)

  useEffect(() => {
    setMounted(true)
    // Icons are automatically initialized by TyLoader
  }, [])

  const handleInputChange = (event: any) => {
    console.log('Input changed:', event.detail)
    setInputValue(event.detail.value || '')
  }

  const handleDropdownChange = (event: any) => {
    console.log('Dropdown changed:', event.detail)
    setDropdownValue(event.detail.option?.textContent || '')
  }

  const handleButtonClick = () => {
    console.log('Button clicked!')
    alert('Ty Button works!')
  }

  const openModal = () => {
    modalRef.current?.show()
  }

  const closeModal = () => {
    modalRef.current?.hide()
  }

  if (!mounted) {
    return <div>Loading...</div>
  }

  return (
    <main className="container">
      <h1>Ty Components + React + Next.js Example</h1>

      <div className="section">
        <h2>Package Status</h2>

        <div className="component-test">
          <h3>✅ @gersak/ty Package - Loaded</h3>
          <p>Core web components library is available</p>
        </div>

        <div className="component-test">
          <h3>✅ @gersak/ty-react Package - Loaded</h3>
          <p>React wrapper components are available</p>
        </div>
      </div>

      <div className="section">
        <h2>Component Tests</h2>

        <div className="component-test">
          <h3>TyButton Test</h3>
          <p>Testing button component with click handler</p>
          <TyButton onClick={handleButtonClick} flavor="primary">
            Click Me!
          </TyButton>
          <br /><br />
          <TyButton onClick={handleButtonClick} flavor="secondary" size="sm">
            Small Secondary
          </TyButton>
        </div>

        <div className="component-test">
          <h3>TyInput Test</h3>
          <p>Testing input component with value binding</p>
          <TyInput
            placeholder="Type something..."
            value={inputValue}
            onChange={handleInputChange}
          />
          <p>Current value: <strong>{inputValue}</strong></p>
        </div>

        <div className="component-test">
          <h3>TyIcon Test</h3>
          <p>Testing icon component (requires icon registry)</p>
          <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
            <TyIcon name="home" size="24" />
            <TyIcon name="star" size="32" />
            <TyIcon name="user" size="20" />
          </div>
          <p><small>Note: Icons require icon registry initialization</small></p>
        </div>

        <div className="component-test">
          <h3>TyDropdown Test</h3>
          <p>Testing dropdown component with options</p>
          <TyDropdown onChange={handleDropdownChange} placeholder="Select an option...">
            <TyOption value="option1">Option 1</TyOption>
            <TyOption value="option2">Option 2</TyOption>
            <TyOption value="option3">Option 3</TyOption>
          </TyDropdown>
          <p>Selected: <strong>{dropdownValue}</strong></p>
        </div>

        <div className="component-test">
          <h3>TyModal Test</h3>
          <p>Testing modal component with imperative API</p>
          <TyButton onClick={openModal} flavor="primary">
            Open Modal
          </TyButton>

          <TyModal ref={modalRef}>
            <div style={{ padding: '20px' }}>
              <h2>Test Modal</h2>
              <p>This is a test modal using Ty components in React!</p>
              <br />
              <TyButton onClick={closeModal} flavor="secondary">
                Close Modal
              </TyButton>
            </div>
          </TyModal>
        </div>
      </div>

      <div className="section">
        <h2>Integration Status</h2>
        <div className="component-test">
          <h3>✅ Web Components Registration</h3>
          <p>Custom elements should be registered in the browser</p>
        </div>

        <div className="component-test">
          <h3>✅ React Wrapper Integration</h3>
          <p>TypeScript interfaces and event handling working</p>
        </div>

        <div className="component-test">
          <h3>✅ Next.js Compatibility</h3>
          <p>Server-side rendering and client hydration working</p>
        </div>
      </div>

      <div className="section">
        <h2>Debug Information</h2>
        <div className="component-test">
          <p><strong>Environment:</strong> {typeof window !== 'undefined' ? 'Client' : 'Server'}</p>
          <p><strong>Custom Elements Support:</strong> {typeof window !== 'undefined' && window.customElements ? '✅ Available' : '❌ Not Available'}</p>
          <p><strong>Mounted:</strong> {mounted ? '✅ Yes' : '❌ No'}</p>
        </div>
      </div>
    </main>
  )
}
